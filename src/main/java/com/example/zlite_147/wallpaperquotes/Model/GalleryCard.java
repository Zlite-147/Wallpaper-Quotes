package com.example.zlite_147.wallpaperquotes.Model;

public class GalleryCard {

    public String imagelink;
    public String categoryId;

    public GalleryCard(String imagelink, String categoryId) {
        this.imagelink = imagelink;
        this.categoryId = categoryId;
    }

    public GalleryCard() {
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
