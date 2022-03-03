package com.cookandroid.haje;

public class User {

    String number;
    String email;
    String name;
    String password;
    boolean auto_or_manual;
    String car;
    String guardian_number;

    public User(String number,
                String email, String name,
                String password, boolean auto_or_manual,
                String car, String guardian_number){
        this.number = number;
        this.email = email;
        this.name = name;
        this.password = password;
        this.auto_or_manual = auto_or_manual;
        this.car = car;
        this.guardian_number = guardian_number;
    }

    public String getCar() {
        return car;
    }
    public String getEmail() {
        return email;
    }
    public String getGuardian_number() {
        return guardian_number;
    }
    public String getName() {
        return name;
    }
    public String getNumber() {
        return number;
    }
    public String getPassword() {
        return password;
    }
    public boolean getAuto_or_manual(){
        return auto_or_manual;
    }

    public void setAuto_or_manual(boolean auto_or_manual) {
        this.auto_or_manual = auto_or_manual;
    }
    public void setCar(String car) {
        this.car = car;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGuardian_number(String guardian_number) {
        this.guardian_number = guardian_number;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
