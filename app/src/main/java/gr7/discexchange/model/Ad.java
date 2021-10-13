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

    public Ad(String name, String brand, String color, int image) {
        this.name = name;
        this.brand = brand;
        this.color = color;
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

    public void setImage(int image) {
        this.image = image;
    }

    public static ArrayList<Ad> getData() {
        ArrayList<Ad> adList = new ArrayList<>();

        int imgId = R.drawable.firebird;


        Ad one = new Ad("Firebird", "P2", "Green", imgId);
        Ad two = new Ad("Firebird", "P2", "Green", imgId);
        Ad three = new Ad("Firebird", "P2", "Green", imgId);
        Ad four = new Ad("Firebird", "P2", "Green", imgId);

        adList.add(one);
        adList.add(two);
        adList.add(three);
        adList.add(four);

        return adList;
    }

}
