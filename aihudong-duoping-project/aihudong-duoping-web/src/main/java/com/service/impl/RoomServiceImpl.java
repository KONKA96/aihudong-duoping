package com.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.RoomMapper;
import com.model.Room;
import com.service.RoomService;
import com.util.ProduceVirtualRoomIdUtil;

import sun.misc.BASE64Encoder;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomMapper roomMapper;
	@Override
	public Room selectScreenByRoom(Room room) {
		// TODO Auto-generated method stub
		return roomMapper.selectScreenByRoom(room);
	}
	@Override
	public Room selectByPrimaryKey(Room room) {
		// TODO Auto-generated method stub
		return roomMapper.selectByPrimaryKey(room.getId());
	}
	@Override
	public int insertSelective(Room room) {
		// TODO Auto-generated method stub
		return roomMapper.insertSelective(room);
	}
	@Override
	public int updateByPrimaryKeySelective(Room room) {
		// TODO Auto-generated method stub
		return roomMapper.updateByPrimaryKeySelective(room);
	}
	@Override
	public List<String> selectAllId() {
		// TODO Auto-generated method stub
		return roomMapper.selectAllId();
	}
	@Override
	public Room selectRoomBuildZone(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return roomMapper.selectRoomBuildZone(map);
	}
	@Override
	public List<Room> selectVirtualRoom(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return roomMapper.selectVirtualRoom(map);
	}
	@Override
	public int insertVirtualRoom(List<String> idList,String num,String password,String userId) {
		// TODO Auto-generated method stub
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		Room room = new Room();
		ProduceVirtualRoomIdUtil pvUtil = new ProduceVirtualRoomIdUtil();
		String id = pvUtil.ProduceVirtualRoomId(idList);
		room.setId(id);
		room.setNum(num);
		//密码转码
		password = new String(encoder.encode(password.getBytes()));
		password = new String(encoder.encode(password.getBytes()));
		room.setPassword(password);
		room.setUserId(userId);
		return roomMapper.insertSelective(room);
	}

}
