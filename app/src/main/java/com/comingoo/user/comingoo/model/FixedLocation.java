package com.comingoo.user.comingoo.model;

public class FixedLocation {

    private String start, dest, price;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public FixedLocation(String start, String dest, String price) {
        this.start = start;
        this.dest = dest;
        this.price = price;
    }
}
