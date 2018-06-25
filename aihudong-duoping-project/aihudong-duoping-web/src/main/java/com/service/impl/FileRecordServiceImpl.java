package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.FileRecordMapper;
import com.model.FileRecord;
import com.service.FileRecordService;

@Service
public class FileRecordServiceImpl implements FileRecordService {

	@Autowired
	private FileRecordMapper fileRecordMapper;
	
	@Override
	public int deleteByPrimaryKey(FileRecord file) {
		// TODO Auto-generated method stub
		return fileRecordMapper.deleteByPrimaryKey(file.getId());
	}

	@Override
	public int insertSelective(FileRecord file) {
		// TODO Auto-generated method stub
		return fileRecordMapper.insertSelective(file);
	}

	@Override
	public FileRecord selectByPrimaryKey(FileRecord file) {
		// TODO Auto-generated method stub
		return fileRecordMapper.selectByPrimaryKey(file.getId());
	}

	@Override
	public int updateByPrimaryKeySelective(FileRecord file) {
		// TODO Auto-generated method stub
		return fileRecordMapper.updateByPrimaryKeySelective(file);
	}

}
