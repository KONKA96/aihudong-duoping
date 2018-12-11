package com.task;


import org.springframework.stereotype.Component;

import com.model.Screen;
import com.service.ScreenService;
@Component
public class DeleteTemporaryScreen implements Runnable {

	private String id;
	private ScreenService screenService;
	
	public ScreenService getScreenService() {
		return screenService;
	}


	public void setScreenService(ScreenService screenService) {
		this.screenService = screenService;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Screen screen = new Screen();
		screen.setId(id);
		try {
			Thread.sleep(1000*60*2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		screenService.deleteByPrimaryKey(screen);
	}

}
