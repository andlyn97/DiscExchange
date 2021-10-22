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

    private ImageView currentImage;
    Bitmap currentBitmap;
    Uri currentUri;
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


        setOnClickListeners(takeImageBtn, selectImageBtn);

        // Handle form
        TextInputEditText textInputName =  view.findViewById(R.id.createName);
        TextInputEditText textInputBrand = view.findViewById(R.id.createBrand);
        TextInputEditText textInputCondition = view.findViewById(R.id.createCondition);
        TextInputEditText textInputFlight = view.findViewById(R.id.createFlight);
        TextInputEditText textInputColor = view.findViewById(R.id.createColor);
        TextInputEditText textInputInk = view.findViewById(R.id.createInk);
        TextInputEditText textInputDescription = view.findViewById(R.id.createDescription);
        TextInputEditText textInputWish = view.findViewById(R.id.createWish);


        createBtnCreate.setOnClickListener(view1 -> {
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

            StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("ad-images").child(published);
            firebaseStorage.putFile(currentUri).addOnCompleteListener(task -> {
                firebaseStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    ad.setImageUrl(uri.toString());
                    FirebaseFirestore.getInstance().collection("ad").add(ad).addOnCompleteListener(task1 -> Navigation.findNavController(view).popBackStack());
                });
            });
        });

        File file = new File(getContext().getFilesDir(), "picFromCamera");
        currentUri = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", file );
    }

    ActivityResultLauncher<String> handleGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    currentUri = result;
                    currentImage.setImageURI(currentUri);
                }
            });

    ActivityResultLauncher<Uri> handleTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            currentImage.setImageURI(currentUri);
        }
    });

    private void setOnClickListeners(Button takeImageBtn, Button selectImageBtn) {
        takeImageBtn.setOnClickListener(view1 -> {
            handleTakePicture.launch(currentUri);
        });

        selectImageBtn.setOnClickListener(view12 -> {
            handleGetContent.launch("image/*");
        });
    }

}

