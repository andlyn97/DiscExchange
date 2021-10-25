package gr7.discexchange;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import gr7.discexchange.model.User;

public class EditProfileFragment extends Fragment {


    private MainActivity mainActivity;
    private User currentUser;
    private ImageView editProfilePictureView;
    private Button editProfileTakeImage;
    private Button editProfileSelectImage;
    private TextInputEditText profileEditName;
    private TextInputEditText profileEditAddress;
    private Button editProfileSubmit;
    private Uri currentUri;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        currentUser = mainActivity.currentUserViewModel.getUser().getValue();
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


    }
}