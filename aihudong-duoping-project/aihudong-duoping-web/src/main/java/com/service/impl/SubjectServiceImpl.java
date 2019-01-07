package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.SubjectMapper;
import com.model.Subject;
import com.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {

	@Autowired
	private SubjectMapper subjectMapper;
	@Override
	public List<Subject> selectSubjectByName(Subject subject) {
		// TODO Auto-generated method stub
		return subjectMapper.selectSubjectByName(subject);
	}
	@Override
	public int updateByPrimaryKeySelective(Subject subject) {
		// TODO Auto-generated method stub
		return subjectMapper.updateByPrimaryKeySelective(subject);
	}
	@Override
	public Subject selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return subjectMapper.selectByPrimaryKey(id);
	}

}
