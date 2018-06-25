package com.dao;

import java.util.List;

import com.model.Building;
import com.model.Zone;

public interface ZoneMapper {
	List<Zone> selectAllZone(Zone zone);
	
	Zone selectAllRoomByBuilding(Building building);
	
    int deleteByPrimaryKey(Integer id);

    int insert(Zone record);

    int insertSelective(Zone record);

    Zone selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Zone record);

    int updateByPrimaryKey(Zone record);
}