package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class EmailResponseDTO {

@SerializedName("suc")
    public boolean suc;
@SerializedName("code")
    public String code;

    public boolean isSuc() {
        return suc;
    }

    public void setSuc(boolean suc) {
        this.suc = suc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



}
