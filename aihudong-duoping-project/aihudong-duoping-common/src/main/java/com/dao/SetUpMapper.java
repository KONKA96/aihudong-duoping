package com.dao;

import com.model.SetUp;

public interface SetUpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SetUp record);

    int insertSelective(SetUp record);

    SetUp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SetUp record);

    int updateByPrimaryKey(SetUp record);
}