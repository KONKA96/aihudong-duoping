package com.dao;

import java.util.List;

import com.model.Subject;

public interface SubjectMapper {
	List<Subject> selectSubjectByName(Subject subject);
	
    int deleteByPrimaryKey(Integer id);

    int insert(Subject record);

    int insertSelective(Subject record);

    Subject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Subject record);

    int updateByPrimaryKey(Subject record);
}