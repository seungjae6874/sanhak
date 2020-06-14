package com.example.chm;

public class ResponseClass {
    String feedback;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ResponseClass(String feedback) {
        this.feedback = feedback;
    }

    public ResponseClass() {
    }
}
