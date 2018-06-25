package com.service;

import java.util.List;
import java.util.Map;

import com.model.Student;

public interface StudentService {
	Student selectStudentByUsername(Student student);
	
	List<Student> selectStudentByFaculty(Map<String,Object> map);
	
	List<String> selectAllId();
	
	List<Student> selectAllStudent(Student student);
	
	int deleteByPrimaryKey(Student student);
	 
	int insertSelective(Student student);

	Student selectByPrimaryKey(Student student);

	int updateByPrimaryKeySelective(Student student);
	
	Student studentLogin(Student student);
}
