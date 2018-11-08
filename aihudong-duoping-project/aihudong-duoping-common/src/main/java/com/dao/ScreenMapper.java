package com.dao;

import java.util.List;
import java.util.Map;

import com.model.Admin;
import com.model.Screen;

public interface ScreenMapper {
	Integer selectScreenCount(Map<String,Object> map);
	
	List<String> selectAllUsername();
	
	List<String> selectAllId();
	
	Integer selectAdminLastScreen(Admin admin);
	
	List<Screen> selectAllScreen(Screen screen);
	
	List<Screen> selectScreenByZoneOrBuilding(Map<String,Object> map);
	
    int deleteByPrimaryKey(String id);

    int insert(Screen record);

    int insertSelective(Screen record);

    Screen selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Screen record);

    int updateByPrimaryKey(Screen record);
}