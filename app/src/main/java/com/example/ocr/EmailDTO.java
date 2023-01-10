package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class EmailDTO {


    @SerializedName("user_email")
    public String useremail;

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public EmailDTO(String useremail) {
        this.useremail = useremail;
    }
}
