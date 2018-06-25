package com.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.model.Admin;
import com.model.Record;
import com.model.Screen;
import com.service.AdminService;
import com.service.RecordService;
import com.service.ScreenService;

@Component
public class deleteScreenUseless {

	@Autowired 
	private ScreenService screenService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private AdminService adminService;
	
	 @Scheduled(cron="59 59 23 * * ? ") //每天24时执行  
	 public void deleteScreenByDay(){  
	    List<Screen> screenList = screenService.selectAllScreen(null);
	    List<Record> recordList = recordService.selectAllScreenRecord(null);
	    for (Screen screen : screenList) {
	    	if(screen.getType().equals("5")) {
	    		screenService.deleteByPrimaryKey(screen);
//	    		将屏幕数量还给其分配的操作管理员	    		
	    		Admin admin=new Admin();
	        	screen=screenService.selectByPrimaryKey(screen);
	        	admin.setId(screen.getAdminId());
	        	admin.setScreenRemain(admin.getScreenRemain()+1);
        		adminService.updateByPrimaryKeySelective(admin);
	    		continue;
	    	}
	    	int count=0;
	    	for (Record record : recordList) {
				if(!record.getUserId().equals(screen.getId())){
					count++;
				}else{
					break;
				}
			}
	    	if(count==recordList.size()){
	    		Admin admin=new Admin();
	        	screen=screenService.selectByPrimaryKey(screen);
	        	admin.setId(screen.getAdminId());
	        	admin=adminService.selectByPrimaryKey(admin);
	        	
	        	System.out.println("用户名为："+screen.getUsername()+"的屏幕在一天内没有登录记录，该屏幕被删除");
	    		if(screenService.deleteByPrimaryKey(screen)>0) {
//		    		将屏幕数量还给其分配的操作管理员	        	
		        	admin.setScreenRemain(admin.getScreenRemain()+1);
	        		adminService.updateByPrimaryKeySelective(admin);
	    		}
	    	}
		}
	 }  
}
