package com.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.AdminMapper;
import com.model.Admin;
import com.service.AdminService;

import sun.misc.BASE64Encoder;

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
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		List<Admin> adminList = adminMapper.selectAllAdmin(null);
		for (Admin ad : adminList) {
			if(ad.getUsername().equals(admin)){
				return 0;
			}
		}
		
		if(admin.getPassword()!=null) {
			String password = new String(encoder.encode(admin.getPassword().getBytes()));
			password = new String(encoder.encode(password.getBytes()));
			admin.setPassword(password);
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
