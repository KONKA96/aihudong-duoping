package com.dao;

import java.util.List;
import java.util.Map;

import com.model.Record;

public interface RecordMapper {
	List<Record> selectAllScreenRecord(Record record);
	
	List<Map<String,Object>> selectTeacherOrderByTime(Map<String,Object> map);
	
	List<Map<String,Object>> selectStudentOrderByTime(Map<String,Object> map);
	
	Integer selectBingfa(Map<String,Object> map);
	
	Integer selectRecord(Map<String,Object> map);
	
    int deleteByPrimaryKey(int id);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(int id);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);
}