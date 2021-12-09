package gr7.discexchange.model;

import java.util.ArrayList;
import java.util.List;

public class MessageRoom {
    private String roomUid;
    private List<String> usersUid;
    private List<User> users;
    private List<Message> messages;
    private Message lastMessage;
    private User fromUser;

    public MessageRoom() {
        usersUid = new ArrayList<>();
        users = new ArrayList<>();
        messages = new ArrayList<>();
        lastMessage = new Message();
    }

    public String getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(String roomUid) {
        this.roomUid = roomUid;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getUsersUid() {
        return usersUid;
    }

    public void setUsersUid(List<String> usersUid) {
        this.usersUid = usersUid;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }


    public void addUser(User user) {
        users.add(user);
    }
}
