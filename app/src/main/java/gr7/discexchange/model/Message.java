package gr7.discexchange.model;

public class Message {

    private String fromUserUid;
    private User fromUser;
    private String message;
    private String sentAt;

    public Message() {
    }

    public String getFromUserUid() {
        return fromUserUid;
    }

    public void setFromUserUid(String fromUserUid) {
        this.fromUserUid = fromUserUid;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
}
