package com.dao;

import java.util.List;

import com.model.Building;

public interface BuildingMapper {
	List<Building> selectAllBuilding(Building building);
	
    int deleteByPrimaryKey(Integer id);

    int insert(Building record);

    int insertSelective(Building record);

    Building selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Building record);

    int updateByPrimaryKey(Building record);
}