package com.cookandroid.haje;

import java.io.Serializable;

public class Breakdown implements Serializable {
    String id;
    String date;
    String startTime;
    String endTime;
    String user_email;
    String departure;
    String destination;
    int price;
    boolean safe_message;

    public Breakdown(String id, String date, String startTime,
                          String endTime, String user_email,
                          String departure, String destination,
                          int price, boolean safe_message){
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user_email = user_email;
        this.departure = departure;
        this.destination = destination;
        this.price = price;
        this.safe_message = safe_message;
    }

    public String getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public String getDeparture() {
        return departure;
    }
    public String getDestination() {
        return destination;
    }
    public int getPrice() {
        return price;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public String getUser_email() {
        return user_email;
    }
    public boolean isSafe_message(){
        return safe_message;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setSafe_message(boolean safe_message) {
        this.safe_message = safe_message;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
