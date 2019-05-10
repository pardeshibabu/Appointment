package com.example.appointment;

public class RegisterSaveData {
    private String register_email,register_password,
            register_cnf_password,register_name,register_collegeid,token;

    public RegisterSaveData() {
    }

    public RegisterSaveData(String register_email, String register_password,
                            String register_cnf_password, String register_name,String register_collegeid,String token) {
        this.register_email = register_email;
        this.register_password = register_password;
        this.register_cnf_password = register_cnf_password;
        this.register_name = register_name;
        this.register_collegeid = register_collegeid;
        this.token =token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegister_collegeid() {
        return register_collegeid;
    }

    public void setRegister_collegeid(String register_collegeid) {
        this.register_collegeid = register_collegeid;
    }

    public String getRegister_email() {
        return register_email;
    }

    public void setRegister_email(String register_email) {
        this.register_email = register_email;
    }

    public String getRegister_password() {
        return register_password;
    }

    public void setRegister_password(String register_password) {
        this.register_password = register_password;
    }

    public String getRegister_cnf_password() {
        return register_cnf_password;
    }

    public void setRegister_cnf_password(String register_cnf_password) {
        this.register_cnf_password = register_cnf_password;
    }

    public String getRegister_name() {
        return register_name;
    }

    public void setRegister_name(String register_name) {
        this.register_name = register_name;
    }
}
