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

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import gr7.discexchange.model.Ad;
import gr7.discexchange.viewmodel.AdsViewModel;

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
    private Button takeImageBtn;
    private Button selectImageBtn;
    private Button createBtnCreate;
    private AdsViewModel adsViewModel;
    private Ad ad;
    private int count;
    private static final String TAG = CreateAdFragment.class.getName();

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
        adsViewModel = new ViewModelProvider(requireActivity()).get(AdsViewModel.class);

        textInputName = view.findViewById(R.id.createName);
        textInputBrand = view.findViewById(R.id.createBrand);
        textInputCondition = view.findViewById(R.id.createCondition);
        textInputFlight = view.findViewById(R.id.createFlight);
        textInputColor = view.findViewById(R.id.createColor);
        textInputInk = view.findViewById(R.id.createInk);
        textInputDescription = view.findViewById(R.id.createDescription);
        textInputWish = view.findViewById(R.id.createWish);

        count = 0;

        ActivityResultLauncher<String> handleGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            currentUri = result;
            currentImage.setImageURI(currentUri);
            count++;
        });

        ActivityResultLauncher<Uri> handleTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            currentImage.setImageURI(currentUri);
            count++;
        });

        setOnClickListeners(view, handleGetContent, handleTakePicture);

        // Inspiration: https://stackoverflow.com/questions/61941959/activityresultcontracts-takepicture/65526167
        File file = new File(getContext().getFilesDir(), "picFromCamera");
        currentUri = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", file );

        String from;
        int pos;

        pos = getArguments().getInt("pos");
        from = getArguments().getString("from");

        if (from.equals("Settings")) {
            createBtnCreate = view.findViewById(R.id.createBtnCreate);
            TextInputLayout createWishLayout = view.findViewById(R.id.createWishLayout);
            createBtnCreate.setText("Opprett annonse i butikk");

            textInputWish.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            createWishLayout.setHint("Pris");

            createBtnCreate.setOnClickListener(view2 -> {
                ad = handleCreateAd();

                StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("ad-images").child(ad.getPublished());
                firebaseStorage.putFile(currentUri).addOnCompleteListener(task -> {
                    firebaseStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                        ad.setImageUrl(uri.toString());
                        FirebaseFirestore
                                .getInstance()
                                .collection("adStore")
                                .add(ad)
                                .addOnCompleteListener(task1 -> Navigation.findNavController(view).popBackStack());
                    });
                });

            });
        }

        if (from.equals("Edit")) {
            ad = adsViewModel.getUserAds().getValue().get(pos);
            createBtnCreate = view.findViewById(R.id.createBtnCreate);

            Glide.with(getContext()).load(Uri.parse(ad.getImageUrl())).into(currentImage);
            textInputName.setText(ad.getName());
            textInputBrand.setText(ad.getBrand());
            textInputCondition.setText(String.valueOf(ad.getCondition()));
            textInputFlight.setText(ad.getFlight());
            textInputColor.setText(ad.getColor());
            textInputInk.setText(ad.getInk());
            textInputDescription.setText(ad.getDescription());
            textInputWish.setText(ad.getWish());

            createBtnCreate.setText("Endre annonse");
            createBtnCreate.setOnClickListener(view1 -> {
                String name = textInputName.getEditableText().toString();
                String brand = textInputBrand.getEditableText().toString();
                int condition = Integer.parseInt(textInputCondition.getEditableText().toString());
                String flight = textInputFlight.getEditableText().toString();
                String color = textInputColor.getEditableText().toString();
                String ink = textInputInk.getEditableText().toString();
                String description = textInputDescription.getEditableText().toString();
                String wish = textInputWish.getEditableText().toString();

                ad.setName(name);
                ad.setBrand(brand);
                ad.setCondition(condition);
                ad.setFlight(flight);
                ad.setColor(color);
                ad.setInk(ink);
                ad.setDescription(description);
                ad.setWish(wish);

                if (count == 0) {
                    currentUri = Uri.parse(ad.getImageUrl());
                }

                updateAd(view, ad);
            });
        }
    }

    private void setOnClickListeners(@NonNull View view, ActivityResultLauncher<String> handleGetContent, ActivityResultLauncher<Uri> handleTakePicture) {
        takeImageBtn = view.findViewById(R.id.createTakeImage);
        selectImageBtn = view.findViewById(R.id.createSelectImage);
        createBtnCreate = view.findViewById(R.id.createBtnCreate);

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
                FirebaseFirestore
                        .getInstance()
                        .collection("ad")
                        .add(ad)
                        .addOnCompleteListener(task1 -> Navigation.findNavController(view).popBackStack());
            });
        });
    }

    private void updateAd(@NonNull View view, Ad ad) {
        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("ad-images");
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("ad");

        String imageRef = ad.getPublished();

        if (!ad.getImageUrl().equals(currentUri.toString())) {
            firebaseStorage.child(imageRef).delete().addOnSuccessListener(del -> {
                firebaseStorage.child(imageRef).putFile(currentUri).addOnSuccessListener(task -> {
                    firebaseStorage.child(imageRef).getDownloadUrl().addOnSuccessListener(uri -> {
                        ad.setImageUrl(uri.toString());
                        collectionRef.document(ad.getUid()).update("imageUrl", uri.toString());
                    }).addOnFailureListener( err -> Log.d(TAG, err.getMessage()));
                }).addOnFailureListener(e -> Log.d(TAG, e.getMessage()));
            });
        }

        collectionRef.document(ad.getUid()).set(ad).addOnSuccessListener(result -> Navigation.findNavController(view).popBackStack());
    }

    private Ad handleCreateAd() {
        String name = textInputName.getEditableText().toString();
        String brand = textInputBrand.getEditableText().toString();
        int condition = Integer.parseInt(textInputCondition.getEditableText().toString());
        String flight = textInputFlight.getEditableText().toString();
        String color = textInputColor.getEditableText().toString();
        String ink = textInputInk.getEditableText().toString();
        String description = textInputDescription.getEditableText().toString();
        double price = Double.valueOf(textInputWish.getEditableText().toString());
        String published = String.valueOf(System.currentTimeMillis());

        return new Ad(name, brand, condition, flight, color, ink, description, price, published, "ADMIN");
    }


}

