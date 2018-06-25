package com.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.StudentMapper;
import com.model.Student;
import com.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentMapper studentMapper;
	@Override
	public Student studentLogin(Student student){
		// TODO Auto-generated method stub
		return studentMapper.studentLogin(student);
	}
	@Override
	public List<Student> selectAllStudent(Student student) {
		// TODO Auto-generated method stub
		return studentMapper.selectAllStudent(student);
	}
	@Override
	public int deleteByPrimaryKey(Student student) {
		// TODO Auto-generated method stub
		return studentMapper.deleteByPrimaryKey(student.getId());
	}
	@Override
	public int insertSelective(Student student) {
		// TODO Auto-generated method stub
		List<Student> studentList = studentMapper.selectAllStudent(null);
		for (Student stu : studentList) {
			if(stu.getUsername().equals(student.getUsername())&&stu.getTruename().equals(student.getTruename())){
				return 0;
			}
		}
		if(student.getUsername()==null||student.getPassword()==null||student.getTruename()==null){
			return 0;
		}
		student.setDuration("00:00:00");
		student.setTime(0);
		
		return studentMapper.insertSelective(student);
	}
	@Override
	public Student selectByPrimaryKey(Student student) {
		// TODO Auto-generated method stub
		return studentMapper.selectByPrimaryKey(student.getId());
	}
	@Override
	public int updateByPrimaryKeySelective(Student student) {
		// TODO Auto-generated method stub
		return studentMapper.updateByPrimaryKeySelective(student);
	}
	@Override
	public List<String> selectAllId() {
		// TODO Auto-generated method stub
		return studentMapper.selectAllId();
	}
	@Override
	public Student selectStudentByUsername(Student student) {
		// TODO Auto-generated method stub
		return studentMapper.selectStudentByUsername(student);
	}
	@Override
	public List<Student> selectStudentByFaculty(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return studentMapper.selectStudentByFaculty(map);
	}

}
