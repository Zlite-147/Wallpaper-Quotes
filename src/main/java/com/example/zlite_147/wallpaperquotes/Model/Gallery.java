package com.example.zlite_147.wallpaperquotes.Model;

public class Gallery {

    public String imagelink;
    public String name;
    public String number;

    public Gallery(String imagelink, String name, String number) {
        this.imagelink = imagelink;
        this.name = name;
        this.number = number;
    }

    public Gallery() {
    }

    public String getimagelink() {
        return imagelink;
    }

    public void setimagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }
}
