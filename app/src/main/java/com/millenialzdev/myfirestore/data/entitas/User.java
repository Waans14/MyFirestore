package com.millenialzdev.myfirestore.data.entitas;

import java.io.Serializable;

public class User implements Serializable {

    private String id, email, nama, password;

    public User() {

    }

    public User(String id, String email, String nama, String password) {
        this.id = id;
        this.email = email;
        this.nama = nama;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
