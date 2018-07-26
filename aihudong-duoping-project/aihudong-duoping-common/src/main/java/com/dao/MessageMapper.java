package com.dao;

import java.util.List;
import java.util.Map;

import com.model.Message;

public interface MessageMapper {
	List<Message> selectAllMessage(Map<String,Object> map);
	
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);
}