package com.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dao.TeacherMapper;
import com.model.Teacher;
import com.service.RoomService;
import com.service.TeacherService;

import sun.misc.BASE64Encoder;

@Service
public class TeacherServiceImpl implements TeacherService {
	
	@Value("${defaultPwd}")
	private String defaultPwd;
	/*@Value("${virtualRoomSwitch}")
	private boolean virtualRoomSwitch;*/
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private TeacherMapper teacherMapper;

	public List<Teacher> selectAllTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		return teacherMapper.selectAll(teacher);
	}
	public List<Teacher> selectTeacherFromAdmin(Map<String,Object> map){
		return teacherMapper.selectTeacherFromAdmin(map);
	}

	public Teacher selectTeacherById(Teacher teacher) {
		// TODO Auto-generated method stub
		List<Teacher> teacherList = teacherMapper.selectAll(teacher);
		teacher=teacherList.get(0);
		return teacher;
	}

	public int updateTeacherSelected(Teacher teacher) {
		// TODO Auto-generated method stub
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		if(teacher.getPassword()!=null) {
			String password = new String(encoder.encode(teacher.getPassword().getBytes()));
			password = new String(encoder.encode(password.getBytes()));
			teacher.setPassword(password);
		}
		return teacherMapper.updateByPrimaryKeySelective(teacher);
	}

	public int insertTeacherSelected(Teacher teacher) {
		// TODO Auto-generated method stub
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		List<Teacher> teacherList = teacherMapper.selectAll(null);
		for (Teacher tea : teacherList) {
			if(teacher.getUsername().equals(tea.getUsername())&&teacher.getTruename().equals(tea.getTruename())){
				return 0;
			}
		}
		//通过开关控制，新增教师新开一间虚拟教室
		/*if(virtualRoomSwitch) {
			List<String> roomIdList = roomService.selectAllId();
			
			roomService.insertVirtualRoom(roomIdList, teacher.getUsername()+"'s Virtual Room", defaultPwd, teacher.getId());
		}*/
		
		if(teacher.getPassword()!=null) {
			String password = new String(encoder.encode(teacher.getPassword().getBytes()));
			password = new String(encoder.encode(password.getBytes()));
			teacher.setPassword(password);
		}
		if(teacher.getUsername()==null||teacher.getPassword()==null||teacher.getTruename()==null){
			return 0;
		}
		if(teacher.getDuration()==null){
			teacher.setDuration("00:00:00");
		}
		if(teacher.getScreenNumSametime()==null){
			teacher.setScreenNumSametime(0);
		}
		teacher.setTimes(0);
		
		return teacherMapper.insertSelective(teacher);
	}

	public int deleteTeacherById(Teacher teacher) {
		// TODO Auto-generated method stub
		return teacherMapper.deleteByPrimaryKey(teacher.getId());
	}
	@Override
	public List<Teacher> selectTeacherByAdmin(Map<String, Integer> map) {
		// TODO Auto-generated method stub
		return teacherMapper.selectTeacherByAdmin(map);
	}
	@Override
	public Teacher teacherLogin(Teacher teacher) {
		// TODO Auto-generated method stub
		return teacherMapper.teacherLogin(teacher);
	}
	@Override
	public List<String> selectAllId() {
		// TODO Auto-generated method stub
		return teacherMapper.selectAllId();
	}
	@Override
	public List<Teacher> selectTeacherByFaculty(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return teacherMapper.selectTeacherByFaculty(map);
	}

}
