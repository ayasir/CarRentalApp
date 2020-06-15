package com.kashsoftwares.dashboardsplashscreen;

public class UserHelper {

    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phoneno;

    public UserHelper() {
    }

    public UserHelper(String fullName, String username, String password, String email, String phoneno) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneno = phoneno;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
