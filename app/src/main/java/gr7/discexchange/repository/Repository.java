package gr7.discexchange.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import gr7.discexchange.model.Message;
import gr7.discexchange.model.MessageRoom;
import gr7.discexchange.model.User;

public class Repository implements IRepository {

    private FirebaseFirestore firebaseFirestore;
    private String userUid;

    private List<Ad> ads;
    private List<Ad> userAds;
    private MutableLiveData<User> user;

    private List<User> users;
    private List<MessageRoom> rooms;
    private List<Message> messages;

    public Repository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        ads = new ArrayList<>();
        userAds = new ArrayList<>();
        userUid = FirebaseAuth.getInstance().getUid();
        users = new ArrayList<>();
        rooms = new ArrayList<>();
        messages = new ArrayList<>();
        user = new MutableLiveData<>();
    }

    public List<Ad> getAds() {

        firebaseFirestore.collection("ad").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot document : value) {
                    if(error != null) {
                        return;
                    }

                    if(document != null) {
                        ads.add(document.toObject(Ad.class));
                    }
                }
            }
        });

        return ads;
    }

    public MutableLiveData<User> getUser() {
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

    public List<Ad> getUserAds() {
        String userUid = FirebaseAuth.getInstance().getUid();
        firebaseFirestore.collection("ad").whereEqualTo("userUid", userUid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot document : value) {
                    if(error != null) {
                        return;
                    }

                    if(document != null) {
                        userAds.add(document.toObject(Ad.class));
                    }
                }

            }
        });
        return userAds;
    }

    public List<User> getUsers() {
        firebaseFirestore.collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null) {
                    return;
                }

                for (DocumentChange userDoc : value.getDocumentChanges()) {
                    users.add(userDoc.getDocument().toObject(User.class));
                }

            }
        });
        return users;
    }

    public List<MessageRoom> getRooms() {
        firebaseFirestore
                .collection("messageRoom")
                .whereArrayContains("usersUid", userUid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null) {
                            Log.d("TAG", "onEvent: " + error.getMessage());
                        }

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            if(documentSnapshot != null) {
                                MessageRoom room = documentSnapshot.toObject(MessageRoom.class);
                                rooms.add(room);
                            }
                        }
                    }
                });
        return rooms;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
