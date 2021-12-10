package gr7.discexchange;

import static android.content.ContentValues.TAG;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

        ActivityResultLauncher<String> handleGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
                    currentUri = result;
                    currentImage.setImageURI(currentUri);
                });

        ActivityResultLauncher<Uri> handleTakePicture = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> currentImage.setImageURI(currentUri));

        setOnClickListeners(view, handleGetContent, handleTakePicture);

        File file = new File(getContext().getFilesDir(), "picFromCamera");
        currentUri = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID + ".provider", file );

        String from;
        int pos;

        pos = getArguments().getInt("pos");
        from = getArguments().getString("from");

        ad = adsViewModel.getUserAds().getValue().get(pos);

        String adUserUid = ad.getUserUid();
        String userUid = FirebaseAuth.getInstance().getUid();

        if (from.equals("Edit")) {
            createBtnCreate = view.findViewById(R.id.createBtnCreate);

            currentImage.setImageURI(currentUri);
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
                String published = String.valueOf(System.currentTimeMillis());

                ad.setName(name);
                ad.setBrand(brand);
                ad.setCondition(condition);
                ad.setFlight(flight);
                ad.setColor(color);
                ad.setInk(ink);
                ad.setDescription(description);
                ad.setWish(wish);
                ad.setPublished(published);

                Log.d("Maome", "name: " + name);
                updateAd(view, ad, pos);
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

    private void updateAd(@NonNull View view, Ad ad, int pos) {
        String uid = adsViewModel.getUserAds().getValue().get(pos).getUid();
        Log.d("Maome", "pos: " + pos + ", uid" + uid);
        String createdAt = String.valueOf(System.currentTimeMillis());
        StorageReference firebaseStorage = FirebaseStorage.getInstance().getReference().child("ad-images");
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("ad");

        // Kombinere denne metoden med handleForm?
        // if sjekk på bundle, sende med parameter inn i felles metode

        if (!ad.getImageUrl().equals(currentUri.toString())) {
            firebaseStorage.child(createdAt).putFile(currentUri).addOnCompleteListener(task -> {
                firebaseStorage.child(createdAt).getDownloadUrl().addOnSuccessListener(uri -> {
                    String oldPublished = ad.getImageUrl();
                    ad.setImageUrl(createdAt);
                    ad.setImageUrl(uri.toString());

                    if(!oldPublished.equals("") && !oldPublished.equals(ad.getImageUrl())) {
                        firebaseStorage.child(oldPublished).delete();
                    }

                    collectionRef
                            .document(ad.getUid())
                            .set(ad)
                            .addOnCompleteListener(task1 -> {
                                Log.d("Maome", "Success");
                            });
                });
            });
        }

        DocumentReference adRef = FirebaseFirestore.getInstance().collection("ad").document(uid);
        adRef
                .update("name", ad.getName(),
                "brand", ad.getBrand(),
                        "condition", ad.getCondition(),
                        "flight", ad.getFlight(),
                        "color", ad.getColor(),
                        "ink", ad.getInk(),
                        "description", ad.getDescription(),
                        "wish", ad.getWish(),
                        "published", ad.getPublished())
                .addOnSuccessListener(unused -> Log.d("Maome", "Ad updated successfully"))
                .addOnFailureListener(e -> Log.d("Maome", "Error updating", e));
    }


}

