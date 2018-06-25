package com.service;

import java.util.List;

import com.model.Building;

public interface BuildingService {
	List<Building> selectAllBuilding(Building building);
	
	int deleteByPrimaryKey(Building building);
	
	int updateByPrimaryKeySelective(Building building);
	 
	int insertSelective(Building building);
}
