package com.dao;

import java.util.List;

import com.model.Faculty;

public interface FacultyMapper {
	List<Faculty> selectAll(Faculty faculty);
	
    int deleteByPrimaryKey(Integer id);

    int insert(Faculty record);

    int insertSelective(Faculty record);

    Faculty selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Faculty record);

    int updateByPrimaryKey(Faculty record);
}