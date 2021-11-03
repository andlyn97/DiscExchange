package gr7.discexchange.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

public class Repository implements IRepository {

    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<List<Ad>> ads;
    private MutableLiveData<List<Ad>> userAds;
    private MutableLiveData<User> user;

    public Repository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        ads = new MutableLiveData<>();
        user = new MutableLiveData<>();
        userAds = new MutableLiveData<>();
    }

    public MutableLiveData<List<Ad>> getAds() {

        firebaseFirestore.collection("ad").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

        return ads;
    }

    public MutableLiveData<User> getUser() {
        String userUid = FirebaseAuth.getInstance().getUid();
        firebaseFirestore.collection("user").document(userUid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
        return user;
    }

    public MutableLiveData<List<Ad>> getUserAds() {
        String userUid = FirebaseAuth.getInstance().getUid();
        firebaseFirestore.collection("ad").whereEqualTo("userUid", userUid).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                userAds.postValue(adList);
            }
        });
        return userAds;
    }

}
