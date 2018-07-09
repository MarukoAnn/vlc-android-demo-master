package com.nmbb.vlc.modle;

/**
 * Created by moonshine on 2018/4/21.
 */

public class UserpassData {
    private String id;
    private String user;
    private String password;

    public UserpassData(String id, String user, String password){
        super();
        this.id = id;
        this.user = user;
        this.password =password;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
