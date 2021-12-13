package gr7.discexchange.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Ad;

public class AdsViewModel extends ViewModel {
    private FirebaseFirestore firestore;
    private MutableLiveData<List<Ad>> ads;
    private MutableLiveData<List<Ad>> userAds;
    private MutableLiveData<List<Ad>> feed;

    public AdsViewModel() {
        firestore = FirebaseFirestore.getInstance();
        ads = new MutableLiveData<>();
        userAds = new MutableLiveData<>();
        feed = new MutableLiveData<>();
        getAdsFromFirestore();
        getUserAdsFromFirestore(false);
        getFeedFromFirestore();
    }

    public MutableLiveData<List<Ad>> getAds() {
        return ads;
    }
    public MutableLiveData<List<Ad>> getUserAds() {
        return userAds;
    }
    public MutableLiveData<List<Ad>> getFeed() { return feed; }

    private void getFeedFromFirestore() {
        firestore.collection("ad").orderBy("published", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            List<Ad> adList = new ArrayList<>();
            String userUid = FirebaseAuth.getInstance().getUid();

            firestore
                    .collection("ad")
                    .whereNotEqualTo("userUid", userUid)
                    .addSnapshotListener((value1, error1) -> {
                        if (error1 != null) {
                            return;
                        }
                        for (QueryDocumentSnapshot document : value1) {
                            if (error1 != null) {
                                return;
                            }

                            if (document != null) {
                                Ad ad = document.toObject(Ad.class);
                                ad.setUid(document.getId());
                                adList.add(ad);
                            }
                        }
                        feed.postValue(adList);
                    });
        });
    }

    private void getAdsFromFirestore() {
        firestore.collection("ad").orderBy("published", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            List<Ad> adList = new ArrayList<>();
            for(QueryDocumentSnapshot document : value) {
                if(error != null) {
                    return;
                }

                if(document != null) {
                    Ad ad = document.toObject(Ad.class);
                    ad.setUid(document.getId());
                    adList.add(ad);
                }
            }
            ads.postValue(adList);
        });
    }

    public void getUserAdsFromFirestore(boolean isArchived) {
        String userUid = FirebaseAuth.getInstance().getUid();
        if (!isArchived) {
            firestore
                    .collection("ad")
                    .whereEqualTo("userUid", userUid)
                    .whereEqualTo("archived", null)
                    .addSnapshotListener((value, error) -> {
                        List<Ad> adList = new ArrayList<>();
                        if (error != null) {
                            return;
                        }
                        for (QueryDocumentSnapshot document : value) {
                            if (error != null) {
                                return;
                            }

                            if (document != null) {
                                Ad ad = document.toObject(Ad.class);
                                ad.setUid(document.getId());
                                adList.add(ad);
                            }
                        }
                        userAds.postValue(adList);
                    });
        } else {
            firestore
                    .collection("ad")
                    .whereEqualTo("userUid", userUid)
                    .whereNotEqualTo("archived", null)
                    .addSnapshotListener((value, error) -> {
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
                    });
        }
    }

}
