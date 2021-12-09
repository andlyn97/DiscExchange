package gr7.discexchange.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


    private String userUid;
    private FirebaseFirestore firestore;
    private MutableLiveData<User> user;
    private MutableLiveData<List<MessageRoom>> rooms;
    private MutableLiveData<List<Message>> messages;
    private List<User> users;

    public ChatViewModel() {
        firestore = FirebaseFirestore.getInstance();
        userUid = FirebaseAuth.getInstance().getUid();
        user = new MutableLiveData<>();
        rooms = new MutableLiveData<>();
        messages = new MutableLiveData<>();
        users = new ArrayList<>();


        getUsersFromFirestore();
        getRoomsFromFirestore();
        getMessagesFromFirestore("");

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

    public String getFromUsername() {
        return getRooms().getValue().get(0).getFromUser().getName();
    }



    // Firestore

    private void getUsersFromFirestore() {

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
    }

    private void getRoomsFromFirestore() {
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
                                room.setRoomUid(documentSnapshot.getId());


                                for (User u : users) {
                                    for (String uUid : room.getUsersUid()) {
                                        if(u.getUid().equals(uUid)) {
                                            room.addUser(u);
                                        }
                                    }
                                }

                                for (User user : room.getUsers()) {
                                    if(!user.getUid().equals(userUid)) {
                                        room.setFromUser(user);
                                    }
                                }
                                room = getLastMessagesFromFirestore(room);
                                fetchedRooms.add(room);
                            }

                        }
                        rooms.postValue(fetchedRooms);

                    }
                });


    }

    private MessageRoom getLastMessagesFromFirestore(MessageRoom room) {

            firestore
                    .collection("messageRoom")
                    .document(room.getRoomUid())
                    .collection("messages")
                    .orderBy("sentAt")
                    .limitToLast(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            DocumentSnapshot lastMessageDocSnap = task.getResult().getDocuments().get(0);
                            Message lastMessage = lastMessageDocSnap.toObject(Message.class);

                            room.setLastMessage(lastMessage);

                        }
                    });

            return room;
    }


    private void getMessagesFromFirestore(String roomUid) {
        // TODO: Fikse denne så den henter dynamisk onClick.
        roomUid = "PjAQkTfsYyCThLsS3eoC";

        firestore
                .collection("messageRoom")
                .document(roomUid)
                .collection("messages")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Message> messageList = new ArrayList<>();
                List<DocumentSnapshot> documentMessages = task.getResult().getDocuments();

                for (DocumentSnapshot doc : documentMessages) {
                    Message message = doc.toObject(Message.class);
                    for (User user : users) {
                        if(message.getFromUserUid().equals(user.getUid())) {
                            message.setFromUser(user);
                        }
                    }
                    messageList.add(message);

                }


                messages.postValue(messageList);
            }
        });


    }






}
