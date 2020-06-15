package com.example.android.myapplogin.model;

public class FbLoginModel {
    private String email;
    private String fbid;

    public FbLoginModel( String email, String fbid) {
        this.email = email;
        this.fbid = fbid;
    }


    public String getfbId(){
        return fbid;
    }
    public String getEmail(){
        return email;
    }
}
