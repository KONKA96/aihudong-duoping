package com.service;

import java.util.List;
import java.util.Map;

import com.model.Message;

public interface MessageService {
	List<Message> selectAllMessage(Map<String,Object> map);
	
    int deleteByPrimaryKey(Message message);

    int insertSelective(Message message);

    Message selectByPrimaryKey(Message message);

    int updateByPrimaryKeySelective(Message message);
}
