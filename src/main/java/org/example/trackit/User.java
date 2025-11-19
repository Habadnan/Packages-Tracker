package org.example.trackit;

public class User {
    public String name;
    public String email;
    public String password;
    public String address;
    public String town;
    public String state;
    public String zipcode;
    public String phoneNum;

    public User(String name, String email, String password, String address, String town, String state, String zipcode, String phoneNum) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.town = town;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNum = phoneNum;
    }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getAddress(){
        return address;
    }
    public String getTown(){
        return town;
    }
    public String getState(){
        return state;
    }
    public String getZipcode(){
        return zipcode;
    }
    public String getPhoneNum(){
        return phoneNum;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setTown(String town){
        this.town = town;
    }
    public void setState(String state){
        this.state = state;
    }
    public void setZipcode(String zipcode){
        this.zipcode = zipcode;
    }
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }

}
