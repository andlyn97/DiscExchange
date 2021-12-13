package gr7.discexchange;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import gr7.discexchange.model.User;
import gr7.discexchange.viewmodel.UserViewModel;

public class EditProfileFragment extends Fragment {
    private User currentUser;
    private ImageView editProfilePictureView;
    private Button editProfileTakeImage;
    private Button editProfileSelectImage;
    private TextInputEditText profileEditName;
    private TextInputEditText profileEditAddress;
    private Button editProfileSubmit;
    private Button findAddress;
    private Uri currentUri;
    private UserViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        currentUser = viewModel.getUser().getValue();
        editProfilePictureView = view.findViewById(R.id.editProfilePictureView);
        editProfileTakeImage = view.findViewById(R.id.editProfileTakeImage);
        editProfileSelectImage = view.findViewById(R.id.editProfileSelectImage);
        profileEditName = view.findViewById(R.id.profileEditName);
        profileEditAddress = view.findViewById(R.id.profileEditAddress);
        editProfileSubmit = view.findViewById(R.id.editProfileSubmit);
        findAddress = view.findViewById(R.id.editProfileFindAdress);


        ActivityResultLauncher<String> handleGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            currentUri = result;
            editProfilePictureView.setImageURI(currentUri);
        });

        ActivityResultLauncher<Uri> handleTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {

            editProfilePictureView.setImageURI(currentUri);
        });

        if (!currentUser.getImageUrl().equals("")) {
            currentUri = Uri.parse(currentUser.getImageUrl());
        }

        profileEditName.setText(currentUser.getName());
        profileEditAddress.setText(currentUser.getAddress());

        editProfileTakeImage.setOnClickListener(view1 -> handleTakePicture.launch(currentUri));
        editProfileSelectImage.setOnClickListener(view2 -> handleGetContent.launch("image/*"));

        if (currentUri != null) {
            Glide.with(getContext())
                    .load(currentUri)
                    .into(editProfilePictureView);
        }

        CollectionReference collectionRef = FirebaseFirestore
                .getInstance()
                .collection("user");

        File file = new File(getContext().getFilesDir(), "picFromCamera");
        currentUri = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", file);

        editProfileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.setAddress(profileEditAddress.getText().toString());
                currentUser.setName(profileEditName.getText().toString());

                String createdAt = String.valueOf(System.currentTimeMillis());
                StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("user-images");

                // If userimage doesnt exist
                if (currentUser.getImageUrl().equals("")) {
                    firebaseStorage.child(createdAt).putFile(currentUri).addOnCompleteListener(task -> {
                        firebaseStorage.child(createdAt).getDownloadUrl().addOnSuccessListener(uri -> {
                            currentUser.setImageStorageRef(createdAt);
                            currentUser.setImageUrl(uri.toString());
                            collectionRef
                                    .document(currentUser.getUid())
                                    .set(currentUser)
                                    .addOnCompleteListener(task1 -> {
                                        popBackStack();
                                    });
                        });
                    });
                    return;
                }

                if (!currentUser.getImageUrl().equals(currentUri.toString())) {
                    firebaseStorage.child(createdAt).putFile(currentUri).addOnCompleteListener(task -> {
                        firebaseStorage.child(createdAt).getDownloadUrl().addOnSuccessListener(uri -> {
                            String oldCreatedAt = currentUser.getImageStorageRef();
                            currentUser.setImageStorageRef(createdAt);
                            currentUser.setImageUrl(uri.toString());

                            if (!oldCreatedAt.equals("") && !oldCreatedAt.equals(currentUser.getImageStorageRef())) {
                                firebaseStorage.child(oldCreatedAt).delete();
                            }

                            collectionRef
                                    .document(currentUser.getUid())
                                    .set(currentUser)
                                    .addOnCompleteListener(task1 -> {
                                        popBackStack();
                                    });
                        });
                    });
                    return;
                }

                collectionRef
                        .document(currentUser.getUid())
                        .set(currentUser)
                        .addOnCompleteListener(task1 -> {
                            popBackStack();
                        });
            }

            private void popBackStack() {
                Navigation.findNavController(view).popBackStack();
            }
        });


        // Not working in our code, but works for Firebase Test Lab
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        findAddress.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location == null) {
                    Log.d("TAG", "LOCATION IS NULL");
                    return;
                }
                Log.d("TAG", "SUCCESS");
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!addresses.isEmpty()) {
                        StringBuilder addressString = new StringBuilder();
                        Address address = addresses.get(0);
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            if (i != 0) {
                                addressString.append(", ");
                            }
                            addressString.append(address.getAddressLine(i));
                        }

                        profileEditAddress.setText(addressString.toString());
                    } else {
                        profileEditAddress.setText("BRA Veien 6D, 1783 Halden");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}