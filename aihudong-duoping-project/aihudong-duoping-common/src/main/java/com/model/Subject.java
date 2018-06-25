package com.model;

import java.util.List;

public class Subject {
    private Integer id;

    private String subjectName;

    private Integer facultyId;

    private String desc;
	
	private Faculty faculty;
    
    private List<Teacher> teacherList;
    
    private List<Screen> screenList;
    
    private List<Student> studentList;
    
    public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public List<Teacher> getTeacherList() {
		return teacherList;
	}

	public void setTeacherList(List<Teacher> teacherList) {
		this.teacherList = teacherList;
	}

	public List<Screen> getScreenList() {
		return screenList;
	}

	public void setScreenList(List<Screen> screenList) {
		this.screenList = screenList;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName == null ? null : subjectName.trim();
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

	@Override
	public String toString() {
		return "Subject [id=" + id + ", subjectName=" + subjectName + ", facultyId=" + facultyId + ", desc=" + desc
				+ ", faculty=" + faculty + ", teacherList=" + teacherList + ", screenList=" + screenList
				+ ", studentList=" + studentList + "]";
	}
    
}