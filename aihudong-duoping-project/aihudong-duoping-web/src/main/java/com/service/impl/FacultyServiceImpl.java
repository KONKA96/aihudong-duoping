package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.FacultyMapper;
import com.model.Faculty;
import com.service.FacultyService;
@Service
public class FacultyServiceImpl implements FacultyService {
	
	@Autowired
	private FacultyMapper facultyMapper;

	public List<Faculty> selectAllFaculty(Faculty faculty) {
		// TODO Auto-generated method stub
		return facultyMapper.selectAll(faculty);
	}

	public Faculty selectFacultyById(Faculty faculty) {
		// TODO Auto-generated method stub
		return facultyMapper.selectByPrimaryKey(faculty.getId());
	}

	@Override
	public int updateByPrimaryKeySelective(Faculty faculty) {
		// TODO Auto-generated method stub
		return facultyMapper.updateByPrimaryKeySelective(faculty);
	}

	@Override
	public int deleteByPrimaryKey(Faculty faculty) {
		// TODO Auto-generated method stub
		return facultyMapper.deleteByPrimaryKey(faculty.getId());
	}

}
