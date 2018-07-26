package com.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AdminMapper;
import com.model.Admin;
import com.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminMapper adminMapper;

	@Override
	public List<Admin> selectAllAdmin(Admin admin) {
		// TODO Auto-generated method stub
		return adminMapper.selectAllAdmin(admin);
	}

	@Override
	public int insertSelective(Admin admin) {
		// TODO Auto-generated method stub
		List<Admin> adminList = adminMapper.selectAllAdmin(null);
		for (Admin ad : adminList) {
			if(ad.getUsername().equals(admin)){
				return 0;
			}
		}
		
		if(admin.getPassword()!=null) {
			admin.setPassword(new Md5Hash(admin.getPassword(), admin.getUsername() ,2).toString());
		}
		if(admin.getScreenNum()==null){
			admin.setScreenNum(0);
		}
		if(admin.getScreenRemain()==null){
			admin.setScreenRemain(0);
		}
		
		return adminMapper.insertSelective(admin);
	}

	@Override
	public Admin selectByPrimaryKey(Admin admin) {
		// TODO Auto-generated method stub
		return adminMapper.selectByPrimaryKey(admin.getId());
	}

	@Override
	public int updateByPrimaryKeySelective(Admin admin) {
		// TODO Auto-generated method stub
		if(admin.getPassword()!=null) {
			Admin oldAdmin = adminMapper.selectByPrimaryKey(admin.getId());
			admin.setPassword(new Md5Hash(admin.getPassword(), oldAdmin.getUsername() ,2).toString());
		}
		return adminMapper.updateByPrimaryKeySelective(admin);
	}

	@Override
	public int deleteByPrimaryKey(Admin admin) {
		// TODO Auto-generated method stub
		return adminMapper.deleteByPrimaryKey(admin.getId());
	}

	@Override
	public Admin adminLogin(Admin admin) {
		// TODO Auto-generated method stub
		return adminMapper.adminLogin(admin);
	}

	@Override
	public List<Admin> selectYijiAdmin(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return adminMapper.selectYijiAdmin(map);
	}

	@Override
	public Admin selectAllFaculty(Admin admin) {
		// TODO Auto-generated method stub
		return adminMapper.selectAllFaculty(admin);
	}

	@Override
	public Admin selectAllBuilding(Admin admin) {
		// TODO Auto-generated method stub
		return adminMapper.selectAllBuilding(admin);
	}

	@Override
	public Admin selectStudentAdmin(Map<String,Object> map) {
		// TODO Auto-generated method stub
		return adminMapper.selectStudentAdmin(map);
	}

	@Override
	public Admin selectGenJiAdmin() {
		// TODO Auto-generated method stub
		return adminMapper.selectGenJiAdmin();
	}

}
