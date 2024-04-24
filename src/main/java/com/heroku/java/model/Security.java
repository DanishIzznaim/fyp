package com.heroku.java.model;

public class Security {
    private int id ;
    private String name;
    private String address;
    private String email;
    private int phone;
    private String username;
    private String password;
    private String icnumber;

    public Security() {
    }

    public Security(int id, String name, String address, String email, int phone, String username, String password,
            String icnumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.icnumber = icnumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcnumber() {
        return icnumber;
    }

    public void setIcnumber(String icnumber) {
        this.icnumber = icnumber;
    }

}
