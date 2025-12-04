//done by Alya Jaad
package com.mycompany.hudoor;

public class User {
    private String userId;
    private String name;
    private String password;

    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean login(String email, String password) {
        return this.userId.equals(email) && this.password.equals(password);
    }

    public void logout() {
        
    }
}

