package com.service;

import com.model.FileRecord;

public interface FileRecordService {
	int deleteByPrimaryKey(FileRecord file);

    int insertSelective(FileRecord file);

    FileRecord selectByPrimaryKey(FileRecord file);

    int updateByPrimaryKeySelective(FileRecord file);
}
