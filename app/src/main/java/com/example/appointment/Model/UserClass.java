package com.example.appointment.Model;

public class UserClass {
       private String collegeid,password;

    public UserClass() {
    }

    public UserClass(String collegeid, String password) {
        this.collegeid = collegeid;
        this.password = password;
    }

    public String getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(String collegeid) {
        this.collegeid = collegeid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
