package com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.ZoneMapper;
import com.model.Building;
import com.model.Zone;
import com.service.ZoneService;
@Service
public class ZoneServiceImpl implements ZoneService {

	@Autowired
	private ZoneMapper zoneMapper;
	
	@Override
	public List<Zone> selectAllZone(Zone zone) {
		// TODO Auto-generated method stub
		return zoneMapper.selectAllZone(zone);
	}

	@Override
	public int insertSelective(Zone zone) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Zone selectByPrimaryKey(Zone zone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(Zone zone) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByPrimaryKey(Zone zone) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Zone selectAllRoomByBuilding(Building building) {
		// TODO Auto-generated method stub
		return zoneMapper.selectAllRoomByBuilding(building);
	}

}
