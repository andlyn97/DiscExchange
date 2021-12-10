package gr7.discexchange.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Ad;
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
    private String currentRoomUid;

    public ChatViewModel() {
        firestore = FirebaseFirestore.getInstance();
        userUid = FirebaseAuth.getInstance().getUid();
        user = new MutableLiveData<>();
        rooms = new MutableLiveData<>();
        messages = new MutableLiveData<>();
        users = new ArrayList<>();


        getUsersFromFirestore();
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

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getCurrentRoomUid() {
        return currentRoomUid;
    }

    public void setCurrentRoomUid(String currentRoomUid) {
        this.currentRoomUid = currentRoomUid;
    }

    public String getFromUsername() {
        return getRooms().getValue().get(0).getFromUser().getName();
    }



    // Firestore

    public void addMessageRoomToFirestore(Ad ad) {

        List<String> usersUid = new ArrayList<>();
        usersUid.add(ad.getUserUid());
        usersUid.add(FirebaseAuth.getInstance().getUid());
        MessageRoom room = new MessageRoom();
        room.setUsersUid(usersUid);
        room.setAdUid(ad.getUid());
        room.setMessages(new ArrayList<>());
        firestore
                .collection("messageRoom")
                .document()
                .set(room);
        firestore
                .collection("messageRoom")
                .whereEqualTo("adUid", ad.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.isSuccessful()) {
                            return;
                        }

                        String roomUid = task.getResult().getDocuments().get(0).getId();
                        setCurrentRoomUid(roomUid);
                    }
                });



    }

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
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null || value.getDocuments().size() == 0) {
                                return;
                            }
                            DocumentSnapshot lastMessageDocSnap = value.getDocuments().get(0);
                            Message lastMessage = lastMessageDocSnap.toObject(Message.class);

                            room.setLastMessage(lastMessage);
                        }
                    });


            return room;
    }

    public void getMessagesFromFirestore(String roomUid) {
        currentRoomUid = roomUid;
        firestore
                .collection("messageRoom")
                .document(roomUid)
                .collection("messages")
                .orderBy("sentAt")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<Message> messageList = new ArrayList<>();
                        List<DocumentSnapshot> documentMessages = value.getDocuments();

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


    public void addMessage(Message message) {
        CollectionReference ref = firestore.collection("messageRoom").document(currentRoomUid).collection("messages");
        ref.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()) {
                    return;
                }


                if(task.getResult().size() == 0){
                    ref.add(message);
                }
            }
        });

        ref.add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    return;
                }
                DocumentReference docRef = task.getResult();
                Message message = docRef.get().getResult().toObject(Message.class);

                List<Message> tempMessagesList = messages.getValue();
                tempMessagesList.add(message);

                messages.postValue(tempMessagesList);
            }
        });


    }
}
