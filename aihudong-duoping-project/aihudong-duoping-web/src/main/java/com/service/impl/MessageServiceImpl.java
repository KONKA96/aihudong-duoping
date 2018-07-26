package com.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.MessageMapper;
import com.model.Message;
import com.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMapper messageMapper;

	@Override
	public List<Message> selectAllMessage(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return messageMapper.selectAllMessage(map);
	}

	@Override
	public int deleteByPrimaryKey(Message message) {
		// TODO Auto-generated method stub
		return messageMapper.deleteByPrimaryKey(message.getId());
	}

	@Override
	public int insertSelective(Message message) {
		// TODO Auto-generated method stub
		return messageMapper.insertSelective(message);
	}

	@Override
	public Message selectByPrimaryKey(Message message) {
		// TODO Auto-generated method stub
		return messageMapper.selectByPrimaryKey(message.getId());
	}

	@Override
	public int updateByPrimaryKeySelective(Message message) {
		// TODO Auto-generated method stub
		return messageMapper.updateByPrimaryKeySelective(message);
	}
	
	
}
