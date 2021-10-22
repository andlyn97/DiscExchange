package gr7.discexchange;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import gr7.discexchange.model.Ad;


public class CreateAdFragment extends Fragment {

    private Uri currentUri;
    private ImageView currentImage;
    private TextInputEditText textInputName;
    private TextInputEditText textInputBrand;
    private TextInputEditText textInputCondition;
    private TextInputEditText textInputFlight;
    private TextInputEditText textInputColor;
    private TextInputEditText textInputInk;
    private TextInputEditText textInputDescription;
    private TextInputEditText textInputWish;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_ad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentImage = view.findViewById(R.id.createImageView);

        Button takeImageBtn = view.findViewById(R.id.createTakeImage);
        Button selectImageBtn = view.findViewById(R.id.createSelectImage);
        Button createBtnCreate = view.findViewById(R.id.createBtnCreate);
        textInputName = view.findViewById(R.id.createName);
        textInputBrand = view.findViewById(R.id.createBrand);
        textInputCondition = view.findViewById(R.id.createCondition);
        textInputFlight = view.findViewById(R.id.createFlight);
        textInputColor = view.findViewById(R.id.createColor);
        textInputInk = view.findViewById(R.id.createInk);
        textInputDescription = view.findViewById(R.id.createDescription);
        textInputWish = view.findViewById(R.id.createWish);

        ActivityResultLauncher<String> handleGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
                    currentUri = result;
                    currentImage.setImageURI(currentUri);
                });

        ActivityResultLauncher<Uri> handleTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> currentImage.setImageURI(currentUri));

        setOnClickListeners(view, takeImageBtn, selectImageBtn, createBtnCreate, handleGetContent, handleTakePicture);

        File file = new File(getContext().getFilesDir(), "picFromCamera");
        currentUri = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", file );
    }

    private void setOnClickListeners(@NonNull View view, Button takeImageBtn, Button selectImageBtn, Button createBtnCreate, ActivityResultLauncher<String> handleGetContent, ActivityResultLauncher<Uri> handleTakePicture) {
        takeImageBtn.setOnClickListener(view1 -> {
            handleTakePicture.launch(currentUri);
        });

        selectImageBtn.setOnClickListener(view12 -> {
            handleGetContent.launch("image/*");
        });

        createBtnCreate.setOnClickListener(view1 -> {
            handleForm(view);
        });
    }

    private void handleForm(@NonNull View view) {
        String name = textInputName.getEditableText().toString();
        String brand = textInputBrand.getEditableText().toString();
        int condition = Integer.parseInt(textInputCondition.getEditableText().toString());
        String flight = textInputFlight.getEditableText().toString();
        String color = textInputColor.getEditableText().toString();
        String ink = textInputInk.getEditableText().toString();
        String description = textInputDescription.getEditableText().toString();
        String wish = textInputWish.getEditableText().toString();
        String published = String.valueOf(System.currentTimeMillis());

        Ad ad = new Ad(name, brand, condition, flight, color, ink, description, wish, published, FirebaseAuth.getInstance().getCurrentUser().getUid());

        uploadToFirestore(view, published, ad);
    }

    private void uploadToFirestore(@NonNull View view, String published, Ad ad) {
        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("ad-images").child(published);
        firebaseStorage.putFile(currentUri).addOnCompleteListener(task -> {
            firebaseStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                ad.setImageUrl(uri.toString());
                FirebaseFirestore.getInstance().collection("ad").add(ad).addOnCompleteListener(task1 -> Navigation.findNavController(view).popBackStack());
            });
        });
    }


}

