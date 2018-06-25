package com.service;

import java.util.List;
import java.util.Map;

import com.model.Admin;

public interface AdminService {
	Admin selectGenJiAdmin();
	
	Admin selectStudentAdmin(Map<String,Object> map);
	
	Admin selectAllBuilding(Admin admin);
	
	Admin selectAllFaculty(Admin admin);
	
	List<Admin> selectYijiAdmin(Map<String,Object> map);
	
	Admin adminLogin(Admin admin);
	
	List<Admin> selectAllAdmin(Admin admin);
	
	int insertSelective(Admin admin);

	Admin selectByPrimaryKey(Admin admin);

	int updateByPrimaryKeySelective(Admin admin);
	
	int deleteByPrimaryKey(Admin admin);
}
