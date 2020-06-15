package com.example.chm;

public class RequestClass {
    String checkdate;
    String ksum;
    public String getCheckdate() {
        return checkdate;
    }
    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }

    public String getKsum(){
        return ksum;
    }

    public void setKsum(String ksum){this.ksum = ksum;}


    public RequestClass(String checkdate, String ksum) {
        this.checkdate = checkdate;
        this.ksum = ksum;
    }

    public RequestClass() {
    }
}
