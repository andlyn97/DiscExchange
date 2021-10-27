package gr7.discexchange.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Ad;
import gr7.discexchange.model.User;

public class Repository implements IRepository {

    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private List<Ad> ads;
    private User user;

    public Repository() {
        loadAds();
        loadUser();
    }

    private void loadAds() {
        ads = new ArrayList<>();
        firebaseFirestore
                .collection("ad")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot adDocumentSnapshot: task.getResult()) {
                                Ad ad = adDocumentSnapshot.toObject(Ad.class);
                                ad.setUid(adDocumentSnapshot.getId());
                                ads.add(ad);
                            }

                        }
                    }
                });
    }

    private void loadUser() {
        String userUid = FirebaseAuth.getInstance().getUid();
        firebaseFirestore
                .collection("user")
                .whereEqualTo("uid", userUid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() && task.getResult().size() == 1) {
                            DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);
                            user = userDoc.toObject(User.class);
                        }
                    }
                });
    }



    @Override
    public List<Ad> getAds() {
        return ads;
    }

    @Override
    public User getUser() {
        return user;
    }
}
