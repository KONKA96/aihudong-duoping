package com.model;

import java.util.List;

public class Admin {
    private Integer id;

    private String username;

    private String password;

    private String truename;

    private Integer sex;

    private String telephone;

    private String email;

    private Integer power;

    private Integer higherId;

    private Integer screenNum;

    private Integer screenRemain;
	
	private List<Building> buildingList;
	
	private List<Faculty> facultyList;
	
	private List<Admin> adminList;
	
	public List<Admin> getAdminList() {
		return adminList;
	}

	public void setAdminList(List<Admin> adminList) {
		this.adminList = adminList;
	}

	public List<Building> getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(List<Building> buildingList) {
		this.buildingList = buildingList;
	}

	public List<Faculty> getFacultyList() {
		return facultyList;
	}

	public void setFacultyList(List<Faculty> facultyList) {
		this.facultyList = facultyList;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename == null ? null : truename.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getHigherId() {
        return higherId;
    }

    public void setHigherId(Integer higherId) {
        this.higherId = higherId;
    }

    public Integer getScreenNum() {
        return screenNum;
    }

    public void setScreenNum(Integer screenNum) {
        this.screenNum = screenNum;
    }

    public Integer getScreenRemain() {
        return screenRemain;
    }

    public void setScreenRemain(Integer screenRemain) {
        this.screenRemain = screenRemain;
    }

	@Override
	public String toString() {
		return "Admin [id=" + id + ", username=" + username + ", password=" + password + ", truename=" + truename
				+ ", sex=" + sex + ", telephone=" + telephone + ", email=" + email + ", power=" + power + ", higherId="
				+ higherId + ", screenNum=" + screenNum + ", screenRemain=" + screenRemain + ", buildingList="
				+ buildingList + ", facultyList=" + facultyList + ", adminList=" + adminList + "]";
	}
    
    
}