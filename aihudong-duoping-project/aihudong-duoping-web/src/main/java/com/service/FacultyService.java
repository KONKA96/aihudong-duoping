package com.service;

import java.util.List;

import com.model.Faculty;

public interface FacultyService {
	List<Faculty> selectAllFaculty(Faculty faculty);
	
	Faculty selectFacultyById(Faculty faculty);
	
	 int updateByPrimaryKeySelective(Faculty record);
	 
	 int deleteByPrimaryKey(Faculty faculty);

}
