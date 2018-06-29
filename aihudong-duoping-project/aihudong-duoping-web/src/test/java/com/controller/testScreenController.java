package com.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.BaseTest;
import com.exception.screenException;
import com.model.Logger;
import com.model.Screen;
import com.service.ScreenService;

public class testScreenController extends BaseTest{

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ScreenService screenService;
	
	@Transactional
	@Test
	public void testInsertScreen() {
		try {
			Screen screen =new Screen();
			screen.setId("sc00001");
			screen.setUsername("test0001");
			screen.setPassword("123");
			int result=screenService.insertSelective(screen);
			if(result<0) {
				throw new screenException("操作失败");
			}else {
				logger.info("操作成功");
			}
		} catch (screenException e) {
			// TODO Auto-generated catch block
			logger.info("操作失败");
			//e.printStackTrace();
		}
	}
}
