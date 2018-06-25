package com.service;

import java.util.List;

import com.model.Room;

public interface RoomService {
	List<String> selectAllId();
	
	Room selectScreenByRoom(Room room);
	
	Room selectByPrimaryKey(Room room);
	 
	int insertSelective(Room room);

	int updateByPrimaryKeySelective(Room room);
}
