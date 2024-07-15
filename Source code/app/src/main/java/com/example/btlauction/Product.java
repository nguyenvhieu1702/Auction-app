package com.example.btlauction;

import java.util.List;


public class Product {
    private String mID;
    private String mName;

    private String mDescribe;
    private String mPrice;
    private String mBidPrice;
    private String mTime;

    private List<String> mImageUrls;


    private String DayStart;

    private String monthStart;

    private String yearStart;

    private String hourStart;

    private String minuteStart;


    private String DayEnd;

    private String monthEnd;

    private String yearEnd;

    private String hourEnd;

    private String minuteEnd;
    private  String mNameStudio;

    private  String mRatio;

    private  String mMaterial;

    private  String mCondition;

    private  String mWeight;

    private  String mDimension;

    public Product() {

    }

    public Product(String id, String name,String describe, String price, String bidprice, String time,List<String> imageUrls,String daystart, String monthstart, String yearstart
            ,String hourstart,String minutestart,String dayend, String monthend, String yearend, String hourend, String minuteend,  String nameStudio, String ratio, String material, String condition,
                   String weight, String dimension) {
        this.mID = id;
        this.mName = name;
        this.mDescribe = describe;
        this.mPrice = price;
        this.mBidPrice = bidprice;
        this.mTime = time;
        this.mImageUrls = imageUrls;
        this.DayStart = daystart;
        this.monthStart = monthstart;
        this.yearStart = yearstart;
        this.hourStart = hourstart;
        this.minuteStart = minutestart;

        this.DayEnd = dayend;
        this.monthEnd = monthend;
        this.yearEnd = yearend;
        this.hourEnd = hourend;
        this.minuteEnd = minuteend;

        this.mNameStudio = nameStudio;
        this.mRatio = ratio;
        this.mMaterial = material;
        this.mCondition = condition;
        this.mWeight = weight;
        this.mDimension = dimension;
    }


    public String getIDpd() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getBidPrice() {
        return mBidPrice;
    }

    public String getTime() {
        return mTime;
    }


    public String getDayStart() {
        return DayStart;
    }

    public String getDayEnd() {
        return DayEnd;
    }

    public String getMonthEnd() {
        return monthEnd;
    }

    public String getMonthStart() {
        return monthStart;
    }

    public String getYearStart() {
        return yearStart;
    }

    public String getYearEnd() {
        return yearEnd;
    }

    public String getHourStart() {
        return hourStart;
    }

    public String getHourEnd() {
        return hourEnd;
    }

    public String getMinuteStart() {
        return minuteStart;
    }

    public String getMinuteEnd() {
        return minuteEnd;
    }

    public String getNameStudio() {
        return mNameStudio;
    }

    public String getCondition() {
        return mCondition;
    }

    public String getDimension() {
        return mDimension;
    }

    public String getMaterial() {
        return mMaterial;
    }

    public String getRatio() {
        return mRatio;
    }

    public String getWeight() {
        return mWeight;
    }


    public void setDayEnd(String dayEnd) {
        DayEnd = dayEnd;
    }

    public void setDayStart(String dayStart) {
        DayStart = dayStart;
    }

    public void setMinuteStart(String minuteStart) {
        this.minuteStart = minuteStart;
    }

    public void setMonthEnd(String monthEnd) {
        this.monthEnd = monthEnd;
    }

    public void setYearStart(String yearStart) {
        this.yearStart = yearStart;
    }

    public void setYearEnd(String yearEnd) {
        this.yearEnd = yearEnd;
    }

    public void setHourEnd(String hourEnd) {
        this.hourEnd = hourEnd;
    }

    public void setHourStart(String hourStart) {
        this.hourStart = hourStart;
    }

    public void setMinuteEnd(String minuteEnd) {
        this.minuteEnd = minuteEnd;
    }

    public void setIDpd(String mID) {
        this.mID = mID;
    }

    public void setNameStudio(String mNameStudio) {
        this.mNameStudio = mNameStudio;
    }

    public void setCondition(String mCondition) {
        this.mCondition = mCondition;
    }

    public void setMaterial(String mMaterial) {
        this.mMaterial = mMaterial;
    }

    public void setRatio(String mRatio) {
        this.mRatio = mRatio;
    }

    public void setWeight(String mWeight) {
        this.mWeight = mWeight;
    }

    public void setDimension(String mDimension) {
        this.mDimension = mDimension;
    }

    public void setDescribe(String mDescribe) {
        this.mDescribe = mDescribe;
    }

    public  List<String> getImageUrls() {
        return mImageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.mImageUrls = imageUrls;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setPrice(String price) {
        this.mPrice = price;
    }


    public void setBidPrice(String bidprice) {
        this.mBidPrice = bidprice;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

}