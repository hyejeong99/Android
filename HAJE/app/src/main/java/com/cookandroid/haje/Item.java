package com.cookandroid.haje;

import android.widget.TextView;

public class Item {

    String ridePoint;
    String arrivePoint;
    String rideDate;
    String rideTime, arriveTime;

    Item(String rideDate, String rideTime, String ridePoint, String arriveTime, String arrivePoint) {

        this.rideDate = rideDate;
        this.rideTime = rideTime;
        this.ridePoint = ridePoint;
        this.arriveTime = arriveTime;
        this.arrivePoint = arrivePoint;
    }

    //getter
    public String getRideDate() {
        return rideDate;
    }

    public String getRideTime() {
        return rideTime;
    }

    public String getRidePoint() {
        return ridePoint;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public String getArrivePoint() {
        return arrivePoint;
    }

    //setter
    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public void setRidePoint(String ridePoint) {
        this.ridePoint = ridePoint;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public void setArrivePoint(String arrivePoint) {
        this.arrivePoint = arrivePoint;
    }

}
