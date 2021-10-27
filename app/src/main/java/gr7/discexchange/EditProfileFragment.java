package gr7.discexchange;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import gr7.discexchange.model.User;
import gr7.discexchange.viewmodel.DEViewModel;

public class EditProfileFragment extends Fragment {


    private User currentUser;
    private ImageView editProfilePictureView;
    private Button editProfileTakeImage;
    private Button editProfileSelectImage;
    private TextInputEditText profileEditName;
    private TextInputEditText profileEditAddress;
    private Button editProfileSubmit;
    private Uri currentUri;

    private DEViewModel viewModel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel = new ViewModelProvider(requireActivity()).get(DEViewModel.class);

        currentUser = viewModel.getUser().getValue();
        editProfilePictureView = view.findViewById(R.id.editProfilePictureView);
        editProfileTakeImage = view.findViewById(R.id.editProfileTakeImage);
        editProfileSelectImage = view.findViewById(R.id.editProfileSelectImage);
        profileEditName = view.findViewById(R.id.profileEditName);
        profileEditAddress = view.findViewById(R.id.profileEditAddress);
        editProfileSubmit = view.findViewById(R.id.editProfileSubmit);


        ActivityResultLauncher<String> handleGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            currentUri = result;
            editProfilePictureView.setImageURI(currentUri);
        });

        ActivityResultLauncher<Uri> handleTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> editProfilePictureView.setImageURI(currentUri));

        currentUri = Uri.parse(currentUser.getImageUrl());

        if(currentUri != null) {
            Glide.with(getContext())
                    .load(currentUri)
                    .into(editProfilePictureView);
        }

        profileEditName.setText(currentUser.getName());
        profileEditAddress.setText(currentUser.getAddress());


        editProfileTakeImage.setOnClickListener(view1 -> handleTakePicture.launch(currentUri));
        editProfileSelectImage.setOnClickListener(view2 -> handleGetContent.launch("image/*"));

        File file = new File(getContext().getFilesDir(), "picFromCamera");
        currentUri = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", file );

        CollectionReference collectionRef = FirebaseFirestore
                .getInstance()
                .collection("user");

        editProfileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.setAddress(profileEditAddress.getText().toString());
                currentUser.setName(profileEditName.getText().toString());

                StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("user-images").child(String.valueOf(System.currentTimeMillis()));
                firebaseStorage.putFile(currentUri).addOnCompleteListener(task -> {
                    firebaseStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                        currentUser.setImageUrl(uri.toString());
                        collectionRef
                                .document(currentUser.getUid())
                                .set(currentUser)
                                .addOnCompleteListener(task1 -> Navigation.findNavController(view).popBackStack());
                    });
                });
            }
        });
    }
}