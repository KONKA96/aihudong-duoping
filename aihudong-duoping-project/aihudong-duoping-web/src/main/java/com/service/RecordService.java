package com.service;

import java.util.List;
import java.util.Map;

import com.model.Admin;
import com.model.Faculty;
import com.model.Record;
import com.model.Teacher;

public interface RecordService {
	List<Teacher> produceTeaList(Teacher teacher,Admin admin);
	
	List<Record> selectTeacherRecord(Record record);
	
	List<Record> selectStudentRecord(Record record);
	
	List<Record> selectScreenRecord(Record record);
	
	List<Record> selectAllScreenRecord(Record record);
	
	Map<String,Integer> selectRecord(Map<String,Object> map);
	
	public Map<String,Integer> selectBingfa(Map<String, Object> map);
	
	List<Map<String,Object>> selectTeacherOrderByTime(Map<String, Object> map);
	
	List<Map<String,Object>> selectStudentOrderByTime(Map<String,Object> map);
	
	Record selectByPrimaryKey(Record record);
	
	int insertSelective(Record record);
	
	int updateByPrimaryKeySelective(Record record);
}
