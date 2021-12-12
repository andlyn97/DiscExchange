package gr7.discexchange.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
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

        getStoreAdsFromFirebase();
    }

    public MutableLiveData<List<Ad>> getStoreAds() { return ads; }

    public MutableLiveData<List<Ad>> getShoppingcart() {
        return shoppingcart;
    }

    // shoppingcart har fortsatt count/size 0 etter setShoppingcart har kjørt

    public void setShoppingcart(List<Ad> shoppingcartList) {
        shoppingcart.postValue(shoppingcartList);
    }
    public void addToShoppingcart(Ad newCartItem) {
        List<Ad> tempCart = new ArrayList<>();
        if (shoppingcart.getValue() != null) {
            for (int i = 0; i < shoppingcart.getValue().size(); i++) {
                tempCart.add(shoppingcart.getValue().get(i));
            }
        }
        tempCart.add(newCartItem);
        setShoppingcart(tempCart);

    }

    private void getStoreAdsFromFirebase() {
        firestore
                .collection("adStore")
                .whereEqualTo("archived", null)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            return;
                        }
                        List<Ad> fetchedStoreAds = new ArrayList<>();
                        List<DocumentSnapshot> documentSnapshots = value.getDocuments();
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

    public void setCartToArchived(List<Ad> cartItems) {
        String currentTime = String.valueOf(System.currentTimeMillis());
        for (Ad ad : cartItems) {
            firestore.collection("adStore").document(ad.getUid()).update("archived", currentTime);
        }

    }
}
