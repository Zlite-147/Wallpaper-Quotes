package com.example.zlite_147.wallpaperquotes.Model;

public class QuotesCard {
    public String quotes;
    public String categoryId;
    public String color;


    public QuotesCard() {
    }

    public QuotesCard(String quotes, String categoryId,String color) {
        this.quotes = quotes;
        this.categoryId = categoryId;
        this.color=color;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }


    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
