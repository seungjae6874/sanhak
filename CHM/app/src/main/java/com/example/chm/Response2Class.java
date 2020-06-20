package com.example.chm;

import java.util.List;

public class Response2Class {
    String food1,food2,food3,food4;
    String meal1,meal2,meal3,meal4;

    public String getfood1() {
        return food1;
    }
    public String getfood2() {
        return food2;
    }
    public String getfood3() {
        return food3;
    }
    public String getfood4() {
        return food4;
    }

    public String getMeal1(){
        return meal1;
    }

    public String getMeal2(){
        return meal2;
    }

    public String getMeal3(){
        return meal3;
    }
    public String getMeal4(){
        return meal4;
    }

    public void setFood1(String food1) {
        this.food1 = food1;
    }

    public void setFood2(String food2) {
        this.food2 = food2;
    }

    public void setFood3(String food3) {
        this.food3 = food3;
    }
    public void setFood4(String food4) {
        this.food4 = food4;
    }

    public void setMeal1(String meal1) {
        this.meal1 = meal1;
    }

    public void setMeal2(String meal2) {
        this.meal1 = meal2;
    }
    public void setMeal3(String meal3) {
        this.meal1 = meal3;
    }
    public void setMeal4(String meal4) {
        this.meal1 = meal4;
    }

    public Response2Class(String food1,String food2,String food3,String food4, String meal1,String meal2,String meal3,String meal4) {
        this.food1 = food1;
        this.food2 = food2;
        this.food3 = food3;
        this.food4 = food4;
        this.meal1 = meal1;
        this.meal2 = meal2;
        this.meal3 = meal3;
        this.meal4 = meal4;
    }

    public Response2Class() {
    }
}
