package com.example.btlauction;

public class Bid {
    private String mName;
    private String mBidPrice;

    private String mTime;

    private String mDay;

    public Bid() {
    }

    public Bid(String mName, String mbidPrice, String mtime, String mday) {
        this.mName = mName;
        this.mBidPrice = mbidPrice;
        this.mTime = mtime;
        this.mDay = mday;

    }

    public String getDay() {
        return mDay;
    }

    public String getTime() {
        return mTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getBidPrice() {
        return mBidPrice;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public void setDay(String mDay) {
        this.mDay = mDay;
    }

    public void setBidPrice(String mBidPrice) {
        this.mBidPrice = mBidPrice;
    }
}