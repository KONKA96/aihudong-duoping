package com.service;

import com.model.SetUp;

public interface SetUpService {
	int insertSelective(SetUp setUp);

	SetUp selectByPrimaryKey(SetUp setUp);

	int updateByPrimaryKeySelective(SetUp setUp);
	
	int deleteByPrimaryKey(SetUp setUp);
}
