package gr7.discexchange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import gr7.discexchange.model.Ad;


public class CreateAdFragment extends Fragment {

    private ImageView currentImage;
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

                    Uri currentUri = result.getData().getData();
                    Bitmap currentBitmap;
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
        TextInputEditText textInputFlight = view.findViewById(R.id.createFlight);
        TextInputEditText textInputColor = view.findViewById(R.id.createColor);
        TextInputEditText textInputInk = view.findViewById(R.id.createInk);
        TextInputEditText textInputDescription = view.findViewById(R.id.createDescription);
        TextInputEditText textInputWish = view.findViewById(R.id.createWish);





        createBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = textInputName.getEditableText().toString();
                String brand = textInputBrand.getEditableText().toString();
                String flight = textInputFlight.getEditableText().toString();
                String color = textInputColor.getEditableText().toString();
                String ink = textInputInk.getEditableText().toString();
                String description = textInputDescription.getEditableText().toString();
                String wish = textInputWish.getEditableText().toString();


                Log.d("Debug12", "Variabler:" + name + brand + flight + color + ink + description + wish);
            }
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