package gr7.discexchange.viewmodel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Message;
import gr7.discexchange.model.MessageRoom;
import gr7.discexchange.model.User;

public class ChatViewModel extends ViewModel {


    String userUid;
    private FirebaseFirestore firestore;
    private MutableLiveData<User> user;
    private MutableLiveData<List<MessageRoom>> rooms;
    private MutableLiveData<List<Message>> messages;

    public ChatViewModel() {
        firestore = FirebaseFirestore.getInstance();
        userUid = FirebaseAuth.getInstance().getUid();
        user = new MutableLiveData<>();
        rooms = new MutableLiveData<>();
        messages = new MutableLiveData<>();

        getRoomsFromFirestore();

    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public MutableLiveData<List<MessageRoom>> getRooms() {
        return rooms;
    }

    public void setRooms(MutableLiveData<List<MessageRoom>> rooms) {
        this.rooms = rooms;
    }

    public MutableLiveData<List<Message>> getMessages() {
        return messages;
    }

    public void setMessages(MutableLiveData<List<Message>> messages) {
        this.messages = messages;
    }


    // Firestore


    private void getRoomsFromFirestore() {
        List<User> users = new ArrayList<>();
        firestore.collection("user").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<User> fetchedUsers = new ArrayList<>();
                if(error != null) {
                    return;
                }

                for (DocumentChange userDoc : value.getDocumentChanges()) {
                    fetchedUsers.add(userDoc.getDocument().toObject(User.class));
                }
                users.addAll(fetchedUsers);
            }
        });

        firestore
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
                                for (String uUid : room.getUsersUid()) {
                                    for (User user : users) {
                                        if(user.getUid().equals(uUid)) {
                                            room.addUser(user);
                                        }
                                    }
                                }

                                fetchedRooms.add(room);
                            }


                        }
                        rooms.postValue(fetchedRooms);
                    }
                });
    }
}
