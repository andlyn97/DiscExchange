package gr7.discexchange.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public void setUsers(MutableLiveData<List<User>> users) {
        this.users = users;
    }

    public void getUserFromFirestore() {
        String userUid = FirebaseAuth.getInstance().getUid();
        createUserIfNotInFirestore(userUid);
        fireStore.collection("user").document(userUid).addSnapshotListener((value, error) -> {
            if(error != null) {
                return;
            }

            if(value != null) {
                User fetchedUser = value.toObject(User.class);
                User userAfterTrim = new User();
                if(fetchedUser != null) {
                    userAfterTrim.setName(fetchedUser.getName() != null ? fetchedUser.getName().trim() : "");
                    userAfterTrim.setAddress(fetchedUser.getAddress() != null ? fetchedUser.getAddress().trim() : "");
                    userAfterTrim.setFeedback(fetchedUser.getFeedback());
                    userAfterTrim.setStoreCredit(fetchedUser.getStoreCredit());
                    userAfterTrim.setUid(fetchedUser.getUid().trim());
                    userAfterTrim.setImageUrl(fetchedUser.getImageUrl() != null ? fetchedUser.getImageUrl().trim() : "");
                    userAfterTrim.setImageStorageRef(fetchedUser.getImageStorageRef() != null ? fetchedUser.getImageStorageRef().trim() : "");
                    user.postValue(userAfterTrim);
                }

            }
        });
    }

    private void createUserIfNotInFirestore(String userUid) {
        fireStore.collection("user").document(userUid).get().addOnCompleteListener(task -> {
            DocumentSnapshot doc = task.getResult();
            User newUser = new User();
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            if(!doc.exists()) {
                newUser.setName(fUser.getDisplayName());
                newUser.setUid(userUid);
                fireStore.collection("user").document(userUid).set(newUser);
            }
        });
    }

    public void getUsersFromFirestore() {
        fireStore.collection("user").addSnapshotListener((value, error) -> {
            List<User> fetchedUsers = new ArrayList<>();
            if(error != null) {
                return;
            }

            for (DocumentSnapshot userDoc : value.getDocuments()) {
                fetchedUsers.add(userDoc.toObject(User.class));
            }
            users.postValue(fetchedUsers);
        });
    }

    public void payForCart(double credit) {
        String userUid = FirebaseAuth.getInstance().getUid();
        fireStore.collection("user").document(userUid).update("storeCredit", credit);
    }
}
