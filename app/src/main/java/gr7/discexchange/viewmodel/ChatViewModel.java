package gr7.discexchange.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import gr7.discexchange.model.Message;
import gr7.discexchange.model.MessageRoom;
import gr7.discexchange.model.User;
import gr7.discexchange.repository.Repository;

public class ChatViewModel extends ViewModel {

    private Repository repository;
    private MutableLiveData<User> user;
    private MutableLiveData<List<User>> users;
    private MutableLiveData<List<MessageRoom>> rooms;
    private MutableLiveData<List<Message>> messages;

    public ChatViewModel() {
        repository = new Repository();
        user = new MutableLiveData<>();
        users = new MutableLiveData<>();
        rooms = new MutableLiveData<>();
        messages = new MutableLiveData<>();

        user = repository.getUser();
        users = repository.getUsers();
        rooms = repository.getRooms();
        messages = repository.getMessages();
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(MutableLiveData<User> user) {
        this.user = user;
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public void setUsers(MutableLiveData<List<User>> users) {
        this.users = users;
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
}
