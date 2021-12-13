package gr7.discexchange.model;

public class Ad {
    private String uid;
    private String name;
    private String imageUrl;
    private String brand;
    private int condition;
    private String flight;
    private String color;
    private String ink;
    private String description;
    private String wish;
    private double price;
    private String published;
    private String archived;
    private String userUid;

    public Ad(String uid, String name, String brand, int condition, String flight, String color, String ink, String description, String wish, double price, String published, String archived, String userUid) {
        this.uid = uid;
        this.name = name;
        this.brand = brand;
        this.condition = condition;
        this.flight = flight;
        this.color = color;
        this.ink = ink;
        this.description = description;
        this.wish = wish;
        this.price = price;
        this.published = published;
        this.archived = archived;
        this.userUid = userUid;
    }

    public Ad(String name, String brand, int condition, String flight, String color, String ink, String description, String wish, String published, String userUid) {
        this.name = name;
        this.brand = brand;
        this.condition = condition;
        this.flight = flight;
        this.color = color;
        this.ink = ink;
        this.description = description;
        this.wish = wish;
        this.published = published;
        this.userUid = userUid;
    }

    public Ad(String name, String brand, int condition, String flight, String color, String ink, String description, String wish, double price, String published, String archived, String userUid) {
        this.name = name;
        this.brand = brand;
        this.condition = condition;
        this.flight = flight;
        this.color = color;
        this.ink = ink;
        this.description = description;
        this.wish = wish;
        this.price = price;
        this.published = published;
        this.archived = archived;
        this.userUid = userUid;
    }

    public Ad(String name, int condition, String color, String ink, String wish) {
        this.name = name;
        this.condition = condition;
        this.color = color;
        this.ink = ink;
        this.wish = wish;
    }

    public Ad(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }



    public Ad(String name, String brand, int condition, String flight, String color, String ink, String description, double price, String published, String userUid) {
        this.name = name;
        this.brand = brand;
        this.condition = condition;
        this.flight = flight;
        this.color = color;
        this.ink = ink;
        this.description = description;
        this.price = price;
        this.published = published;
        this.userUid = userUid;
    }

    public Ad() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getInk() {
        return ink;
    }

    public void setInk(String ink) {
        this.ink = ink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getArchived() {
        return archived;
    }

    public void setArchived(String archived) {
        this.archived = archived;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
