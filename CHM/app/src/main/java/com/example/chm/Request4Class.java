package com.example.chm;

public class Request4Class {

    String checkdate;
    String meal;
    String food;

    public String getCheckdate() {
        return checkdate;
    }
    public void setCheckdate(String checkdate) {
        this.checkdate = checkdate;
    }

    public String getMeal() {
        return meal;
    }
    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getFood() {
        return food;
    }
    public void setFood(String checkdate) {
        this.food = food;
    }



    public Request4Class(String checkdate, String meal, String food) {
        this.checkdate = checkdate;
        this.meal = meal;
        this.food = food;

    }

    public Request4Class() {
    }
}
