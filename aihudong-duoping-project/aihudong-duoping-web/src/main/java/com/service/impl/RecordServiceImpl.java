package com.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.RecordMapper;
import com.model.Admin;
import com.model.Faculty;
import com.model.Record;
import com.model.Subject;
import com.model.Teacher;
import com.service.RecordService;
import com.service.TeacherService;

@Service
public class RecordServiceImpl implements RecordService {

	@Autowired
	private RecordMapper recordMapper;
	@Autowired
	private TeacherService teacherService;
	
	/**
	 * 通过不同的条件查询教师集合
	 */
	public List<Teacher> produceTeaList(Teacher teacher,Admin admin){
		List<Teacher> selectAllTeacher=null;
//		不同级别的管理员查询不同范围的对象
		if(admin.getPower()==0){
			selectAllTeacher = teacherService.selectAllTeacher(teacher);
		}else if(admin.getPower()==1){
			Map<String,Integer> adminMap=new HashMap<>();
			adminMap.put("adminId1", admin.getId());
			selectAllTeacher = teacherService.selectTeacherByAdmin(adminMap);
		}else if(admin.getPower()==2){
			Map<String,Integer> adminMap=new HashMap<>();
			adminMap.put("adminId2", admin.getId());
			selectAllTeacher = teacherService.selectTeacherByAdmin(adminMap);
		}
			
		return selectAllTeacher;
	}
	
	public Map<String,Integer> selectRecord(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		  
        Calendar cd = Calendar.getInstance();
        String time=(String) map.get("time");
        Map<String,Integer> record=new LinkedHashMap<>();
        for (int i = 0; i < 6; i++) {
        	if(time!=null && time!=""){
        		try {
					cd.setTime(sdf.parse(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}else{
        		cd.setTime(new Date());
        	}
        	cd.add(Calendar.DATE, -(Integer)map.get("interval")*i+1);
        	Date startTime=cd.getTime();
        	cd.add(Calendar.DATE, -(Integer)map.get("interval"));
        	Date endTime=cd.getTime();
        	map.put("startTime", sdf.format(startTime));
        	map.put("endTime", sdf.format(endTime));
        	
        	SimpleDateFormat sdf1 = new SimpleDateFormat("MM.dd");  
        	record.put(sdf1.format(endTime)+"-"+sdf1.format(startTime), recordMapper.selectRecord(map));
		}
		return record;
	}
	
	public Map<String,Integer> selectBingfa(Map<String, Object> map){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		  
        Calendar cd = Calendar.getInstance();
        String time=(String) map.get("time");
        Map<String,Integer> record=new LinkedHashMap<>();
        if(time!=null){
    		try {
    			 time+=" 23:00:00";
				cd.setTime(sdf.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else{
    		cd.setTime(new Date());
    	}
        for (int i = 0; i < 16; i++) {
        	
        	cd.add(Calendar.HOUR, -(Integer)map.get("interval"));
        	Date startTime=cd.getTime();
        	map.put("startTime", sdf.format(startTime));
        	
        	SimpleDateFormat sdf1 = new SimpleDateFormat("HH");  
        	record.put(sdf1.format(startTime)+"时", recordMapper.selectBingfa(map));
		}
		return record;
	}

	@Override
	public List<Map<String, Object>> selectTeacherOrderByTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return recordMapper.selectTeacherOrderByTime(map);
	}

	@Override
	public List<Map<String, Object>> selectStudentOrderByTime(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return recordMapper.selectStudentOrderByTime(map);
	}

	@Override
	public int insertSelective(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKeySelective(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Record> selectAllScreenRecord(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.selectAllScreenRecord(record);
	}

	@Override
	public Record selectByPrimaryKey(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.selectByPrimaryKey(record.getId());
	}

	@Override
	public List<Record> selectTeacherRecord(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.selectTeacherRecord(record);
	}

	@Override
	public List<Record> selectStudentRecord(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.selectStudentRecord(record);
	}

	@Override
	public List<Record> selectScreenRecord(Record record) {
		// TODO Auto-generated method stub
		return recordMapper.selectScreenRecord(record);
	}

}
