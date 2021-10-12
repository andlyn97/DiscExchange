package gr7.discexchange.model;

import java.util.Date;

public class Ad {

    private String name;
    // private Img picture;
    private String brand;
    private int condition;
    private String flight;
    private String color;
    private boolean ink;
    private String description;
    private String wish;
    private double price;
    private Date published;
    private Date archived;
    private User user;

    public Ad(String name, String brand, int condition, String flight, String color, boolean ink, String description, String wish, double price, Date published, Date archived, User user) {
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
        this.user = user;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isInk() {
        return ink;
    }

    public void setInk(boolean ink) {
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

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getArchived() {
        return archived;
    }

    public void setArchived(Date archived) {
        this.archived = archived;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
