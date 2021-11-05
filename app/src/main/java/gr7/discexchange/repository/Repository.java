package gr7.discexchange.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private MutableLiveData<List<User>> users;
    private MutableLiveData<List<MessageRoom>> rooms;
    private MutableLiveData<List<Message>> messages;

    public Repository() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        ads = new ArrayList<>();
        userUid = FirebaseAuth.getInstance().getUid();
        users = new MutableLiveData<>();
        rooms = new MutableLiveData<>();
        messages = new MutableLiveData<>();
        user = new MutableLiveData<>();
        userAds = new ArrayList<>();
    }

    public List<Ad> getAds() {

        firebaseFirestore.collection("ad").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //List<Ad> adList = new ArrayList<>();
                for(QueryDocumentSnapshot document : value) {
                    if(error != null) {
                        return;
                    }

                    if(document != null) {
                        ads.add(document.toObject(Ad.class));
                    }
                }
                //ads.postValue(adList);
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

    public List<Ad> getUserAds(boolean isArchived) {
        String userUid = FirebaseAuth.getInstance().getUid();

        if (!isArchived) {
            firebaseFirestore
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
                            //userAds.postValue(adList);
                            userAds = adList;
                        }
                    });
        } else {
            firebaseFirestore
                    .collection("ad")
                    .whereEqualTo("userUid", userUid)
                    .whereEqualTo("archived", null)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            List<Ad> adList = new ArrayList<>();
                            if (error != null) {
                                int i = 1 + 1;
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
                            //userAds.postValue(adList);
                            userAds = adList;
                        }
                    });
        }
        return userAds;
    }

    public MutableLiveData<List<User>> getUsers() {
        firebaseFirestore.collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        return users;
    }

    public MutableLiveData<List<MessageRoom>> getRooms() {
        firebaseFirestore
                .collection("messageRoom")
                .whereArrayContains("usersUid", userUid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<MessageRoom> fetchedRooms = new ArrayList<>();
                        if(error != null) {
                            Log.d("TAG", "onEvent: " + error.getMessage());
                        }

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            if(documentSnapshot != null) {
                                MessageRoom room = documentSnapshot.toObject(MessageRoom.class);
                                fetchedRooms.add(room);
                            }
                        }
                        rooms.postValue(fetchedRooms);
                    }
                });
        return rooms;
    }

    public MutableLiveData<List<Message>> getMessages() {
        return messages;
    }
}
