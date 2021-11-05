package gr7.discexchange.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Ad;

public class AdsViewModel extends ViewModel {
    private FirebaseFirestore firestore;
    private MutableLiveData<List<Ad>> ads;
    private MutableLiveData<List<Ad>> userAds;

    public AdsViewModel() {
        firestore = FirebaseFirestore.getInstance();
        ads = new MutableLiveData<>();
        userAds = new MutableLiveData<>();
        getAdsFromFirestore();
        getUserAdsFromFirestore(false);
    }

    public MutableLiveData<List<Ad>> getAds() {
        return ads;
    }
    public MutableLiveData<List<Ad>> getUserAds() {
        return userAds;
    }

    private void getAdsFromFirestore() {
        firestore.collection("ad").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Ad> adList = new ArrayList<>();
                for(QueryDocumentSnapshot document : value) {
                    if(error != null) {
                        return;
                    }

                    if(document != null) {
                        adList.add(document.toObject(Ad.class));
                    }
                }
                ads.postValue(adList);
            }
        });
    }
    public void getUserAdsFromFirestore(boolean isArchived) {
        String userUid = FirebaseAuth.getInstance().getUid();
        if (!isArchived) {
            firestore
                    .collection("ad")
                    .whereEqualTo("userUid", userUid)
                    .whereEqualTo("archived", null)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            List<Ad> adList = new ArrayList<>();
                            if (error != null) {
                                return;
                            }
                            for (QueryDocumentSnapshot document : value) {
                                if (error != null) {
                                    return;
                                }

                                if (document != null) {
                                    adList.add(document.toObject(Ad.class));
                                }
                            }
                            userAds.postValue(adList);

                        }
                    });
        } else {
            firestore
                    .collection("ad")
                    .whereEqualTo("userUid", userUid)
                    .whereNotEqualTo("archived", null)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            List<Ad> adList = new ArrayList<>();
                            if (error != null) {
                                return;
                            }
                            for (QueryDocumentSnapshot document : value) {
                                if (error != null) {
                                    return;
                                }

                                if (document != null) {
                                    adList.add(document.toObject(Ad.class));
                                }
                            }
                            userAds.postValue(adList);
                        }
                    });
        }
    }

}
