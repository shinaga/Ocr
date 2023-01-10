package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class RegisterRes {
    @SerializedName("suc")
    public boolean suc;

    public boolean isSuc() {
        return suc;
    }

    public void setSuc(boolean suc) {
        this.suc = suc;
    }
}
