package com.model;

public class SetUp {
    private Integer id;

    private String waterMark;

    private String logo;
    
    private Double teacherMaxResourceSize;

    private Double studentMaxResourceSize;
    
    private String enterpriseName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(String waterMark) {
        this.waterMark = waterMark == null ? null : waterMark.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

	public Double getTeacherMaxResourceSize() {
		return teacherMaxResourceSize;
	}

	public void setTeacherMaxResourceSize(Double teacherMaxResourceSize) {
		this.teacherMaxResourceSize = teacherMaxResourceSize;
	}

	public Double getStudentMaxResourceSize() {
		return studentMaxResourceSize;
	}

	public void setStudentMaxResourceSize(Double studentMaxResourceSize) {
		this.studentMaxResourceSize = studentMaxResourceSize;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
    
}