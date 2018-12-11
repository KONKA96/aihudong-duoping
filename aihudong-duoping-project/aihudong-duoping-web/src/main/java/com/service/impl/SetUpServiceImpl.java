package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.SetUpMapper;
import com.model.SetUp;
import com.service.SetUpService;

@Service
public class SetUpServiceImpl implements SetUpService {

	@Autowired
	private SetUpMapper setUpMapper;
	
	@Override
	public int insertSelective(SetUp setUp) {
		// TODO Auto-generated method stub
		return setUpMapper.insertSelective(setUp);
	}

	@Override
	public SetUp selectByPrimaryKey(SetUp setUp) {
		// TODO Auto-generated method stub
		return setUpMapper.selectByPrimaryKey(setUp.getId());
	}

	@Override
	public int updateByPrimaryKeySelective(SetUp setUp) {
		// TODO Auto-generated method stub
		return setUpMapper.updateByPrimaryKeySelective(setUp);
	}

	@Override
	public int deleteByPrimaryKey(SetUp setUp) {
		// TODO Auto-generated method stub
		return setUpMapper.deleteByPrimaryKey(setUp.getId());
	}

}
