package com.comingoo.user.comingoo.model;

public class Place {
    public String name, address, lat, lng;
    public int image;

    public Place(String name, String address, String lat, String lng, int image) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.image = image;
    }

    public Place() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
