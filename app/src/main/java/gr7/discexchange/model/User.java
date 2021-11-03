package gr7.discexchange.model;

public class User {

    private String uid;
    private String name;
    private String address;
    private int feedback;
    private double storeCredit;
    private String imageUrl;
    private String imageStorageRef;



    public User(String name, String address, int feedback, double storeCredit) {
        this.name = name;
        this.address = address;
        this.feedback = feedback;
        this.storeCredit = storeCredit;
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public double getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(double storeCredit) {
        this.storeCredit = storeCredit;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageStorageRef() {
        return imageStorageRef;
    }

    public void setImageStorageRef(String imageStorageRef) {
        this.imageStorageRef = imageStorageRef;
    }

}
