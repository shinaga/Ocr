package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class findpwDTO {

    @SerializedName("user_email")
    public String useremail;
    @SerializedName("user_pw")
    public String userpw;

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpw() {
        return userpw;
    }

    public void setUserpw(String userpw) {
        this.userpw = userpw;
    }

    public findpwDTO(String useremail, String userpw) {
        this.useremail = useremail;
        this.userpw = userpw;
    }
}
