package com.service;

import java.util.List;
import java.util.Map;

import com.model.Admin;
import com.model.Screen;

public interface ScreenService {
	List<String> selectAllUsername();
	
	List<String> selectAllId();
	
	Integer selectAdminLastScreen(Admin admin);
	
	List<Screen> selectAllScreen(Screen screen);
	
	List<Screen> selectScreenByZoneOrBuilding(Map<String,Object> map);
	
	int insertSelective(Screen record);

    Screen selectByPrimaryKey(Screen screen);

    int updateByPrimaryKeySelective(Screen record);
    
    int deleteByPrimaryKey(Screen screen);
}
