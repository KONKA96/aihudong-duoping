package com.service;

import java.util.List;
import java.util.Map;

import com.model.Faculty;

public interface FacultyService {
	List<Faculty> showAllFaculty(Map<String,Object> map);
	
	List<Faculty> selectAllFaculty(Faculty faculty);
	
	Faculty selectFacultyById(Faculty faculty);
	
	int insertSelective(Faculty faculty);
	
	int updateByPrimaryKeySelective(Faculty faculty);
	 
	int deleteByPrimaryKey(Faculty faculty);

}
