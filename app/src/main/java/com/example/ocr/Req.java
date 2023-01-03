package com.example.ocr;

import com.google.gson.annotations.SerializedName;

public class Req {

    @SerializedName("tool_id")
    public String tollid;


    public String getTollid(){
        return tollid;
    }
    public void setTollid(String tollid){
        this.tollid = tollid;
    }

    @SerializedName("tool_state")
    public String toolstate;

    public String getToolstate(){
        return toolstate;
    }
    public void setToolstate(String toolstate){
        this.toolstate = toolstate;
    }


    public Req(String tollid, String toolstate){
        this.tollid = tollid;
        this.toolstate = toolstate;
    }

}

