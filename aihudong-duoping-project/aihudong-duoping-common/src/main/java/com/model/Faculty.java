package com.model;

import java.util.List;

public class Faculty {
    private Integer id;

    private String facultyName;

    private Integer adminId;

    private String desc;
	
	private List<Subject> subjectList;
    
    private Admin admin;
    
    private List<Room> roomList;
    
    public List<Room> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<Room> roomList) {
		this.roomList = roomList;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<Subject> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName == null ? null : facultyName.trim();
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

	@Override
	public String toString() {
		return "Faculty [id=" + id + ", facultyName=" + facultyName + ", adminId=" + adminId + ", desc=" + desc
				+ ", subjectList=" + subjectList + ", admin=" + admin + ", roomList=" + roomList + "]";
	}
    
}