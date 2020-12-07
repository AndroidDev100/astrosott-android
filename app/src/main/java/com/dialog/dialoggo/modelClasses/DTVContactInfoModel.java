package com.dialog.dialoggo.modelClasses;

public class DTVContactInfoModel {
    private String smsNotifyNo;
    private String mobileNo;
    private String contactNo;

    public String getSmsNotifyNo() {
        return smsNotifyNo;
    }

    public void setSmsNotifyNo(String smsNotifyNo) {
        this.smsNotifyNo = smsNotifyNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    private String resultCode;
    private String resultDesc;
}
