package gr7.discexchange;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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


    ActivityResultLauncher<Intent> handleImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() == null) {
                        return;
                    }

                    Log.d("Debug12", "" + result.getData().getData());

                    currentUri = result.getData().getData();

                    if(result.getData().getExtras() != null) {
                        currentBitmap = (Bitmap)result.getData().getExtras().get("data");

                        if(currentBitmap != null) {
                            currentImage.setImageBitmap(currentBitmap);
                            Log.d("Debug12 Bitmap",currentBitmap.toString());
                        }

                    }

                    if(currentUri != null) {
                        currentImage.setImageURI(currentUri);
                        Log.d("Debug12 Uri",currentUri.toString());
                    }

                }
            }
    );

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

            // TODO: Legge inn at man kan laste opp bilde som er hentet fra "Ta bilde".


            Ad ad = new Ad(name, brand, condition, flight, color, ink, description, wish, published, FirebaseAuth.getInstance().getCurrentUser().getUid());


            StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("ad-images").child(published);
            firebaseStorage.putFile(currentUri).addOnCompleteListener(task -> {
                Log.d("Debug12", "Uploaded");
                firebaseStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    ad.setImageUrl(uri.toString());
                    Log.d("Debug12", "Downloadurl = " + uri.toString());
                    FirebaseFirestore.getInstance().collection("ad").add(ad).addOnCompleteListener(task1 -> Navigation.findNavController(view).popBackStack());
                });
            });
        });
    }

    private void setOnClickListeners(Button takeImageBtn, Button selectImageBtn) {
        takeImageBtn.setOnClickListener(view1 -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            handleImageActivityResultLauncher.launch(cameraIntent);
        });

        selectImageBtn.setOnClickListener(view12 -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            handleImageActivityResultLauncher.launch(galleryIntent);
        });
    }
}