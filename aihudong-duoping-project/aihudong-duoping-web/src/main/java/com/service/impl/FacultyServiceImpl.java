package com.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.FacultyMapper;
import com.model.Faculty;
import com.model.Subject;
import com.service.FacultyService;
import com.service.SubjectService;
@Service
public class FacultyServiceImpl implements FacultyService {
	
	@Autowired
	private FacultyMapper facultyMapper;
	@Autowired
	private SubjectService subjectService;

	public List<Faculty> selectAllFaculty(Faculty faculty) {
		// TODO Auto-generated method stub
		return facultyMapper.selectAll(faculty);
	}

	public Faculty selectFacultyById(Faculty faculty) {
		// TODO Auto-generated method stub
		return facultyMapper.selectByPrimaryKey(faculty.getId());
	}

	public int updateByPrimaryKeySelective(Faculty faculty) {
		// TODO Auto-generated method stub
		Faculty temp = new Faculty();
		temp.setId(faculty.getId());
		List<Faculty> facultyList = selectAllFaculty(temp);
		
		List<Subject> subjectList2 = facultyList.get(0).getSubjectList();
		
		List<Subject> subjectList = facultyList.get(0).getSubjectList();
		for (Subject subject : subjectList) {
			if(subject.getId()==null && subject.getSubjectName()!=null && !"".equals(subject.getSubjectName())) {
				subject.setFacultyId(faculty.getId());
				subjectService.insertSelective(subject);
				continue;
			}
			
			for (Subject sub : subjectList2) {
				if(sub.getId()==subject.getId()) {
					//该科目是被修改的
					if(!sub.getSubjectName().equals(subject.getSubjectName())) {
						subjectService.updateByPrimaryKeySelective(subject);
					}
					break;
				}
			}
		}
		
		for(Subject sub: subjectList2) {
			int count = 0; 
			for (Subject subject : subjectList) {
				if(sub.getId()!=subject.getId()) {
					count++;
				}else {
					break;
				}
				
				if(count==subjectList.size()) {
					//该科目已被删除
					subjectService.deleteByPrimaryKey(sub);
				}
			}
		}
		
		return facultyMapper.updateByPrimaryKeySelective(faculty);
	}

	@Override
	public int deleteByPrimaryKey(Faculty faculty) {
		// TODO Auto-generated method stub
		List<Faculty> facultyList = selectAllFaculty(faculty);
		List<Subject> subjectList = facultyList.get(0).getSubjectList();
		for (Subject subject : subjectList) {
			subjectService.deleteByPrimaryKey(subject);
		}
		return facultyMapper.deleteByPrimaryKey(faculty.getId());
	}

	@Override
	public List<Faculty> showAllFaculty(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return facultyMapper.selectAllFaculty(map);
	}

	@Override
	public int insertSelective(Faculty faculty) {
		// TODO Auto-generated method stub
		
		return facultyMapper.insertSelective(faculty);
	}

}
