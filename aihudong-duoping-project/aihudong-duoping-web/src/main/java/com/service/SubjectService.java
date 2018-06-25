package com.service;

import java.util.List;

import com.model.Subject;

public interface SubjectService {
	List<Subject> selectSubjectByName(Subject subject);
	
	int updateByPrimaryKeySelective(Subject subject);
}
