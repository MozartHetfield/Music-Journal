package com.soare.musicjournal;

import android.graphics.Bitmap;
import com.google.firebase.firestore.Exclude;

public class Concert {

    private String singerName;
    private String location;
    private String genre;
    private String price;
    private String date;
    private float rating;
    private Bitmap image;

    public Concert() {

    }

    public Concert(String singerName, String location, String genre, String price, String date, float rating) {
        this.singerName = singerName;
        this.location = location;
        this.genre = genre;
        this.price = price;
        this.date = date;
        this.rating = rating;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getSingerName() {
        return singerName;
    }

    public String getLocation() {
        return location;
    }

    public String getGenre() {
        return genre;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public float getRating() {
        return rating;
    }

    @Exclude
    public Bitmap getImage() {
        return image;
    }

}
