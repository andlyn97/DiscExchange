package gr7.discexchange.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
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

public class UserViewModel extends ViewModel {
    private FirebaseFirestore fireStore;
    private MutableLiveData<User> user;

    private MutableLiveData<List<User>> users;


    public UserViewModel() {
        fireStore = FirebaseFirestore.getInstance();

        user = new MutableLiveData<>();
        users = new MutableLiveData<>();
        getUserFromFirestore();
        getUsersFromFirestore();
    }





    public void setAds(List<Ad> ads) {

    }

    public MutableLiveData<User> getUser () {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }



    // Firestore





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

    public void getUsersFromFirestore() {
        fireStore.collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<User> fetchedUsers = new ArrayList<>();
                if(error != null) {
                    return;
                }

                for (DocumentChange userDoc : value.getDocumentChanges()) {
                    fetchedUsers.add(userDoc.getDocument().toObject(User.class));
                }
                users.postValue(fetchedUsers);
            }
        });
    }

}
