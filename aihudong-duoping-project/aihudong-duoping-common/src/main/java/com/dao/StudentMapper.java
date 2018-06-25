package com.dao;

import java.util.List;
import java.util.Map;

import com.model.Student;

public interface StudentMapper {
	Student selectStudentByUsername(Student student);
	
	List<String> selectAllId();
	
	List<Student> selectAllStudent(Student student);
	
	List<Student> selectStudentByFaculty(Map<String,Object> map);
	
	Student studentLogin(Student student);
	
	int deleteByPrimaryKey(String id);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);
}