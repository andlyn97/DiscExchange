package gr7.discexchange.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.transform.Source;

import gr7.discexchange.R;

public class Ad {

    private String name;
     private int image;
    private String brand;
    private String condition; // int
    private String flight;
    private String color;
    private String ink; // boolean?
    private String description;
    private String wish;
    private double price;
    private Date published;
    private Date archived;
    private User user;

    public Ad(String name, String condition, String color, String ink, String wish, int image) {
        this.name = name;
        this.condition = condition;
        this.color = color;
        this.ink = ink;
        this.wish = wish;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
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

    public void setImage(int image) {
        this.image = image;
    }

    public static ArrayList<Ad> getData() {
        ArrayList<Ad> adList = new ArrayList<>();

        int imgId = R.drawable.firebird;


        Ad one = new Ad("Navn: Firebird", "Tilstand: 7/10", "Farge: Rød", "Ink:Ingen", "Ønsker: P2 C-line", imgId);
        Ad two = new Ad("Navn: Firebird", "Tilstand: 7/10", "Farge: Rød", "Ink:Ingen", "Ønsker: P2 C-line", imgId);
        Ad three = new Ad("Navn: Firebird", "Tilstand: 7/10", "Farge: Rød", "Ink:Ingen", "Ønsker: P2 C-line", imgId);
        Ad four = new Ad("Navn: Firebird", "Tilstand: 7/10", "Farge: Rød", "Ink:Ingen", "Ønsker: P2 C-line", imgId);

        adList.add(one);
        adList.add(two);
        adList.add(three);
        adList.add(four);

        return adList;
    }

}
