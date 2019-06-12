package com.service;

import java.util.List;
import java.util.Map;

import com.model.Teacher;
import com.model.Room;

public interface TeacherService {
	List<String> selectAllId();
	
	Teacher teacherLogin(Teacher teacher);
	
	List<Teacher> selectTeacherByAdmin(Map<String,Integer> map);
	
	List<Teacher> selectTeacherByFaculty(Map<String,Object> map);
	
	List<Teacher> selectAllTeacher(Teacher teacher);
	
	List<Teacher> selectTeacherFromAdmin(Map<String,Object> map);
	
	Teacher selectTeacherById(Teacher teacher);
	
	int updateTeacherSelected(Teacher teacher);
	
	int insertTeacherSelected(Teacher teacher ,boolean virtualRoomSwitch);
	
	int deleteTeacherById(Teacher teacher);
	
	 int untyingUnionId(Teacher teacher);
	 
	 List<Map<String, Object>> selectVirtualRoom(Map<String, Object> map);
}
