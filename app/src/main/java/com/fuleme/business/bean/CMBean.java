package com.fuleme.business.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/20.
 */

public class CMBean implements Serializable{

    private String id="";
    private String name="";
    private String type="";
    private String phone="";
    private String password="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
