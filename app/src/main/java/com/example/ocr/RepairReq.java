package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class RepairReq {

    @SerializedName("repair_reason")
    public String repairreason;

    @SerializedName("tool_id")
    public String toolid;


    @SerializedName("user_id")
    public String user_id;

    public String getRepairreason() {
        return repairreason;
    }

    public void setRepairreason(String repairreason) {
        this.repairreason = repairreason;
    }

    public String getToolid() {
        return toolid;
    }

    public void setToolid(String toolid) {
        this.toolid = toolid;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public RepairReq(String repairreason, String toolid, String user_id) {
        this.repairreason = repairreason;
        this.toolid = toolid;
        this.user_id = user_id;
    }
}

