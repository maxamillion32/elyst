package com.elystapp.elyst.data;

/**
 * Created by Roxana Gheorghe on 5/20/16.
 *
 */
public class Host {

    private String name;
    private String email;
    private String user_ID;
    private String password;
    private Integer image;


    public  Host(){
        this.name="";
        this.email="";
        this.user_ID="";
        this.password="";
        this.image= -1;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
