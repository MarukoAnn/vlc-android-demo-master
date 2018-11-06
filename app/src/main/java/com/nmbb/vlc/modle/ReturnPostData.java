package com.nmbb.vlc.modle;

/**
 * Created by moonshine on 2018/2/5.
 */

public class ReturnPostData {
    private String id;
    private String userCode;
    private String idCode;
    private String realName;
    private String userName;
    private String homeAddress;
    private String homeTelephone;
    private String organizationId;
    private String password;
    private String phone;
    private String email;
    private String birthday;
    private String gender;
    private String idt;
    private String udt;
    private String sysids;

    public ReturnPostData(String id, String userCode, String idCode, String realName,
                          String userName, String homeAddress, String homeTelephone,
                          String organizationId, String password, String phone,
                          String email, String birthday, String gender, String idt, String udt,String sysids){
        super();
        this.id = id;
        this.userCode = userCode;
        this.homeAddress = homeAddress;
        this.realName = realName;
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
        this.homeTelephone = homeTelephone;
        this.gender = gender;
        this.email = email;
        this.idCode = idCode;
        this.idt = idt;
        this.organizationId = organizationId;
        this.phone = phone;
        this.udt = udt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHomeTelephone() {
        return homeTelephone;
    }

    public void setHomeTelephone(String homeTelephone) {
        this.homeTelephone = homeTelephone;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getIdt() {
        return idt;
    }

    public void setIdt(String idt) {
        this.idt = idt;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUdt() {
        return udt;
    }

    public void setUdt(String udt) {
        this.udt = udt;
    }
}
