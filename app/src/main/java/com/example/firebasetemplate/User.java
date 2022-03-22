package com.example.firebasetemplate;


public class User {
    private String idUser;
    private String username;
    private String email;
    private String photoUser;

    public User(String idUser, String username, String email, String photoUser) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.photoUser = photoUser;
    }

    public User() {
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
    }
}