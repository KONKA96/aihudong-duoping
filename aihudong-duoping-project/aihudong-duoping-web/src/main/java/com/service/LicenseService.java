package com.service;

import java.util.List;

import com.model.License;

public interface LicenseService {
	List<License> selectAllLicense();
	
	License selectByPrimaryKey(License license);
}
