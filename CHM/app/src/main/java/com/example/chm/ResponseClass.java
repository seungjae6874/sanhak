package com.example.chm;

public class ResponseClass {
    String feedback;
    String ksum;

    public String getFeedback() {
        return feedback;
    }
    public String getEatsum(){
        return ksum;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setEatsum(String ksum) {
        this.ksum =ksum;
    }

    public ResponseClass(String feedback, String ksum) {
        this.feedback = feedback;
        this.ksum = ksum;
    }

    public ResponseClass() {
    }
}
