package gr7.discexchange.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Ad;
import gr7.discexchange.model.User;

public class DEViewModel extends ViewModel {
    private FirebaseFirestore fireStore;
    private MutableLiveData<User> user;
    private MutableLiveData<List<Ad>> ads;
    private MutableLiveData<List<Ad>> userAds;


    public DEViewModel() {
        fireStore = FirebaseFirestore.getInstance();
        ads = new MutableLiveData<>();
        userAds = new MutableLiveData<>();
        user = new MutableLiveData<>();
        getUserFromFirestore();
        getAdsFromFirestore();
        getUserAdsFromFirestore(false);
    }



    public MutableLiveData<List<Ad>> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {

    }

    public MutableLiveData<User> getUser () {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    public MutableLiveData<List<Ad>> getUserAds() {
        return userAds;
    }




    // Firestore

    public void getUserAdsFromFirestore(boolean isArchived) {
        String userUid = FirebaseAuth.getInstance().getUid();
        if (!isArchived) {
            fireStore
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
            fireStore
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

    public void getAdsFromFirestore() {
        fireStore.collection("ad").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void getUserFromFirestore() {
        String userUid = FirebaseAuth.getInstance().getUid();
        fireStore.collection("user").document(userUid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }

                if(value != null) {
                    User fetchedUser = value.toObject(User.class);
                    User userAfterTrim = new User();
                    userAfterTrim.setName(fetchedUser.getName().trim());
                    userAfterTrim.setAddress(fetchedUser.getAddress().trim());
                    userAfterTrim.setFeedback(fetchedUser.getFeedback());
                    userAfterTrim.setStoreCredit(fetchedUser.getStoreCredit());
                    userAfterTrim.setUid(fetchedUser.getUid().trim());
                    userAfterTrim.setImageUrl(fetchedUser.getImageUrl().trim());
                    userAfterTrim.setImageStorageRef(fetchedUser.getImageStorageRef().trim());
                    user.postValue(userAfterTrim);


                }
            }
        });
    }
}
