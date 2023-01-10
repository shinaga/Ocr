package com.example.ocr;


import com.google.gson.annotations.SerializedName;

public class RegisterReq {

@SerializedName("user_id")
    public String userid;
@SerializedName("user_pw")
    public String userpw;
@SerializedName("user_email")
    public String useremail;
@SerializedName("user_student_number")
    public String userstudentnumber;
@SerializedName("user_name")
    public String username;
@SerializedName("user_phone_number")
    public String userphonenumber;

@SerializedName("user_license")
public Integer userlicense;
@SerializedName("department_id")
public Integer departmentid;



    public Integer getUserlicense() {
        return userlicense;
    }

    public void setUserlicense(Integer userlicense) {
        this.userlicense = userlicense;
    }

    public Integer getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(Integer departmentid) {
        this.departmentid = departmentid;
    }

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

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserstudentnumber() {
        return userstudentnumber;
    }

    public void setUserstudentnumber(String userstudentnumber) {
        this.userstudentnumber = userstudentnumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphonenumber() {
        return userphonenumber;
    }

    public void setUserphonenumber(String userphonenumber) {
        this.userphonenumber = userphonenumber;
    }

    public RegisterReq(String userid, String userpw, String useremail, String userstudentnumber, String username, String userphonenumber, Integer userlicense, Integer departmentid) {
        this.userid = userid;
        this.userpw = userpw;
        this.useremail = useremail;
        this.userstudentnumber = userstudentnumber;
        this.username = username;
        this.userphonenumber = userphonenumber;
        this.userlicense = userlicense;
        this.departmentid = departmentid;
    }
}
