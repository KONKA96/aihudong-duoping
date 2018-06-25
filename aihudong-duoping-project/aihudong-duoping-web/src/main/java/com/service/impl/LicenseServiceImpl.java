package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.LicenseMapper;
import com.model.License;
import com.service.LicenseService;

@Service
public class LicenseServiceImpl implements LicenseService {

	@Autowired
	private LicenseMapper licenseMapper;
	@Override
	public List<License> selectAllLicense() {
		// TODO Auto-generated method stub
		return licenseMapper.selectAllLicense();
	}

	@Override
	public License selectByPrimaryKey(License license) {
		// TODO Auto-generated method stub
		return licenseMapper.selectByPrimaryKey(license.getId());
	}

}
