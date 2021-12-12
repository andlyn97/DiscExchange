package gr7.discexchange.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Ad;

public class StoreViewModel extends ViewModel {
    private FirebaseFirestore firestore;
    private MutableLiveData<List<Ad>> ads;
    private MutableLiveData<List<Ad>> shoppingcart;

    public StoreViewModel() {
        ads = new MutableLiveData<>();
        shoppingcart = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
    }

    private void getStoreAdsFromFirebase() {
        firestore.collection("adStore").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()) {
                    return;
                }
                List<Ad> fetchedStoreAds = new ArrayList<>();
                List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                    if (documentSnapshot != null) {
                        Ad ad = documentSnapshot.toObject(Ad.class);
                        ad.setUid(documentSnapshot.getId());
                        fetchedStoreAds.add(ad);
                    }
                }
                ads.postValue(fetchedStoreAds);
            }
        });
    }
}
