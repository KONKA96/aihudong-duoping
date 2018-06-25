package com.model;

public class License {
    private Integer id;

    private String remainingTime;

    private Integer screenNum;

    private Integer peopleNum;

    private Integer screenNumSametime;

    private String licenseId;

    private String licenseSize;

    private String licenseType;

    private Integer isCurrent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime == null ? null : remainingTime.trim();
    }

    public Integer getScreenNum() {
        return screenNum;
    }

    public void setScreenNum(Integer screenNum) {
        this.screenNum = screenNum;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Integer getScreenNumSametime() {
        return screenNumSametime;
    }

    public void setScreenNumSametime(Integer screenNumSametime) {
        this.screenNumSametime = screenNumSametime;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId == null ? null : licenseId.trim();
    }

    public String getLicenseSize() {
        return licenseSize;
    }

    public void setLicenseSize(String licenseSize) {
        this.licenseSize = licenseSize == null ? null : licenseSize.trim();
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType == null ? null : licenseType.trim();
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
    }
}