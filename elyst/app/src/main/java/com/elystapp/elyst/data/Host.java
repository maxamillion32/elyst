package com.elystapp.elyst.data;

/**
 * Created by roxanagheorghe on 5/20/16.
 *
 */
public class Host {

    private String name;
    private String email;
    private Long phone;
    private String user_ID;
    private String password;
    private Integer image;


    public  Host(){
        this.name="";
        this.email="";
        this.phone=-1L;
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

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
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
