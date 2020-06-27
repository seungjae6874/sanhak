package com.example.chm;

public class Request2Class {

    String checkdate;
    String mealtime;
    String food;

    public String getCheckdate() {
        return checkdate;
    }
    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }



    public Request2Class(String checkdate) {
        this.checkdate = checkdate;

    }

    public Request2Class() {
    }
}
