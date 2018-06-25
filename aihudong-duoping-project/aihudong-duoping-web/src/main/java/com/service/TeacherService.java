package com.service;

import java.util.List;
import java.util.Map;

import com.model.Teacher;

public interface TeacherService {
	List<String> selectAllId();
	
	Teacher teacherLogin(Teacher teacher);
	
	List<Teacher> selectTeacherByAdmin(Map<String,Integer> map);
	
	List<Teacher> selectTeacherByFaculty(Map<String,Object> map);
	
	List<Teacher> selectAllTeacher(Teacher teacher);
	
	List<Teacher> selectTeacherFromAdmin(Map<String,Object> map);
	
	Teacher selectTeacherById(Teacher teacher);
	
	int updateTeacherSelected(Teacher teacher);
	
	int insertTeacherSelected(Teacher teacher);
	
	int deleteTeacherById(Teacher teacher);
}
