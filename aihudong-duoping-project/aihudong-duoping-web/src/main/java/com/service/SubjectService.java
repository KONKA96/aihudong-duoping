package com.service;

import java.util.List;

import com.model.Subject;

public interface SubjectService {
	int insertSelective(Subject subject);
	
	int deleteByPrimaryKey(Subject subject);
	
	List<Subject> selectSubjectByName(Subject subject);
	
	int updateByPrimaryKeySelective(Subject subject);
	
	Subject selectByPrimaryKey(Integer id);
}
