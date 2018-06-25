package com.dao;

import java.util.List;

import com.model.License;

public interface LicenseMapper {
	List<License> selectAllLicense();
	
    int deleteByPrimaryKey(Integer id);

    int insert(License record);

    int insertSelective(License record);

    License selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(License record);

    int updateByPrimaryKey(License record);
}