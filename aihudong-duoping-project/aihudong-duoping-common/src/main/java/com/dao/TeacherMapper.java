package com.dao;

import java.util.List;
import java.util.Map;

import com.model.Teacher;

public interface TeacherMapper {
	List<String> selectAllId();
	
	Teacher teacherLogin(Teacher teacher);
	
	List<Teacher> selectTeacherByAdmin(Map<String,Integer> map);
	
	List<Teacher> selectTeacherByFaculty(Map<String,Object> map);
	
	List<Teacher> selectAll(Teacher teacher);
	
	List<Teacher> selectTeacherFromAdmin(Map<String,Object> map);
	
    int deleteByPrimaryKey(String id);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    Teacher selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);
}