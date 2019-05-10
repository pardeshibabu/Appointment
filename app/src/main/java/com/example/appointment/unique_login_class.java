package com.example.appointment;

public class unique_login_class {
    String usertype,email,flag;

    public unique_login_class() {
    }

    public unique_login_class(String usertype) {
        this.usertype = usertype;
    }

    public unique_login_class(String email, String flag) {
        this.email = email;
        this.flag = flag;
    }

    public unique_login_class(String usertype, String email, String flag) {
        this.usertype = usertype;
        this.email = email;
        this.flag = flag;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
