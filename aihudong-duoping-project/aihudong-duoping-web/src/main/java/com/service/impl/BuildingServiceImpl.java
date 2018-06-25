package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.BuildingMapper;
import com.model.Building;
import com.service.BuildingService;
@Service
public class BuildingServiceImpl implements BuildingService {
	
	@Autowired
	private BuildingMapper buildingMapper;

	@Override
	public List<Building> selectAllBuilding(Building building) {
		// TODO Auto-generated method stub
		return buildingMapper.selectAllBuilding(building);
	}

	@Override
	public int deleteByPrimaryKey(Building building) {
		// TODO Auto-generated method stub
		return buildingMapper.deleteByPrimaryKey(building.getId());
	}

	@Override
	public int updateByPrimaryKeySelective(Building building) {
		// TODO Auto-generated method stub
		return buildingMapper.updateByPrimaryKey(building);
	}

	@Override
	public int insertSelective(Building building) {
		// TODO Auto-generated method stub
		return buildingMapper.insertSelective(building);
	}

}
