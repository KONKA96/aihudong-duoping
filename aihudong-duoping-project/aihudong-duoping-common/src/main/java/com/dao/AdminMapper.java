package com.dao;

import java.util.List;
import java.util.Map;

import com.model.Admin;
import com.model.Zone;

public interface AdminMapper {
	Admin selectGenJiAdmin();
	
	Admin selectStudentAdmin(Map<String,Object> map);
	
	Admin selectAllBuilding(Admin admin);
	
	Admin selectAllFaculty(Admin admin);
	
	List<Admin> selectYijiAdmin(Map<String,Object> map);
	
	List<Admin> selectAllAdmin(Admin admin);
	
	Admin adminLogin(Admin admin);
	
    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
}