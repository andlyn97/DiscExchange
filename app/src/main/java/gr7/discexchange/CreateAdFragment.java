package gr7.discexchange;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


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