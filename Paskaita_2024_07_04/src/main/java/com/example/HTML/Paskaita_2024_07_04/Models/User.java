package com.example.HTML.Paskaita_2024_07_04.Models;

public class User {
    private int id;
    private String user;
    private String password;

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
