package com.service;

import java.util.List;
import java.util.Map;

import com.model.Room;

public interface RoomService {
	Room selectRoomBuildZone(Map<String,Object> map);
	
	List<String> selectAllId();
	
	Room selectScreenByRoom(Room room);
	
	Room selectByPrimaryKey(Room room);
	 
	int insertSelective(Room room);

	int updateByPrimaryKeySelective(Room room);
}
