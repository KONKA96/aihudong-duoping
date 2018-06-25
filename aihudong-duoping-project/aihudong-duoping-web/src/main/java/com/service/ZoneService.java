package com.service;

import java.util.List;

import com.model.Building;
import com.model.Zone;

public interface ZoneService {
	Zone selectAllRoomByBuilding(Building building);
	
	List<Zone> selectAllZone(Zone zone);
	
	 int insertSelective(Zone zone);

	 Zone selectByPrimaryKey(Zone zone);

	 int updateByPrimaryKeySelective(Zone zone);
	 
	 int deleteByPrimaryKey(Zone zone);
}
