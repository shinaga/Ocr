package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class LoginDTO {

    @SerializedName("user_id")
    public String userid;
    @SerializedName("user_pw")
    public String userpw;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpw() {
        return userpw;
    }

    public void setUserpw(String userpw) {
        this.userpw = userpw;
    }

    public LoginDTO(String userid, String userpw) {
        this.userid = userid;
        this.userpw = userpw;
    }
}
