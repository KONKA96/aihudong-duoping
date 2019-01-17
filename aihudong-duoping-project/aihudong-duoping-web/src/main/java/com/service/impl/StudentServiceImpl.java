package com.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dao.StudentMapper;
import com.model.Student;
import com.service.RoomService;
import com.service.StudentService;

import sun.misc.BASE64Encoder;

@Service
public class StudentServiceImpl implements StudentService {

	@Value("${defaultPwd}")
	private String defaultPwd;
	/*@Value("${virtualRoomSwitch}")
	private boolean virtualRoomSwitch;*/
	
	@Autowired
	private RoomService roomService;
	
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
	public int insertSelective(Student student,boolean virtualRoomSwitch) {
		// TODO Auto-generated method stub
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		List<Student> studentList = studentMapper.selectAllStudent(null);
		for (Student stu : studentList) {
			if(stu.getUsername().equals(student.getUsername())&&stu.getTruename().equals(student.getTruename())){
				return 0;
			}
		}
		
		//通过开关控制，新开一间虚拟教室 (临时账户除外)
		if(!"5".equals(student.getType()) && virtualRoomSwitch) {
			List<String> roomIdList = roomService.selectAllId();
			
			roomService.insertVirtualRoom(roomIdList, student.getUsername()+"'s Virtual Room", defaultPwd, student.getId());
		}
		
		if(student.getPassword()!=null) {
			String password = new String(encoder.encode(student.getPassword().getBytes()));
			password = new String(encoder.encode(password.getBytes()));
			student.setPassword(password);
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
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		if(student.getPassword()!=null) {
			String password = new String(encoder.encode(student.getPassword().getBytes()));
			password = new String(encoder.encode(password.getBytes()));
			student.setPassword(password);
		}
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
	@Override
	public int untyingUnionId(Student student) {
		// TODO Auto-generated method stub
		return studentMapper.untyingUnionId(student);
	}

}
