package com.example.csit228f2_2;

import java.net.MalformedURLException;
import java.net.URL;

public class User {
    int id;
    String username;
    String password;
    String css;

    public User(int id, String username, String password) throws MalformedURLException {
        this.id = id;
        this.username = username;
        this.password = password;
        css = username + ".css";
    }
}
