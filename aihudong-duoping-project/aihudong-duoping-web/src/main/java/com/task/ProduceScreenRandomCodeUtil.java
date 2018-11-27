package com.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.model.Screen;
import com.service.ScreenService;

@Component
public class ProduceScreenRandomCodeUtil {
	
	@Autowired 
	private ScreenService screenService;
	
	@Scheduled(cron="0 0/5 8-22 * * ?") //每天早8时至晚22时，每5分钟执行一次  
	public void produceScreenRandomCodeTask() {
		//存放当前随机数
		List<String> codeList1 = new ArrayList<>();
		//存放上一次过期的随机数
		List<String> codeList2 = new ArrayList<>();
		//存放所有的随机数
		List<String> codeListAll = new ArrayList<>();
		
		List<Screen> screenList = screenService.selectAllScreen(null);
		for (Screen screen : screenList) {
			if(screen.getRandomCode1()!=null) {
				codeList1.add(screen.getRandomCode1());
			}
			if(screen.getRandomCode2()!=null) {
				codeList2.add(screen.getRandomCode2());
			}
		}
		
		codeListAll.addAll(codeList1);
		codeListAll.addAll(codeList2);
		
		for (int i = 0; i < screenList.size(); i++) {
			while(true) {
				String code = produceRandomCode(3);
				if(codeListAll.indexOf(code)==0) {
					continue;
				}else {
					Screen screen = screenList.get(i);
					//将原有的随机数放置在过期的随机数位置
					screen.setRandomCode2(screen.getRandomCode1());
					screen.setRandomCode1(code);
					//防止修改密码
					screen.setPassword(null);
					screenService.updateByPrimaryKeySelective(screen);
					break;
				}
			}
		}
	}
	
	public String produceRandomCode(int weishu) {
		Random rand = new Random();
		char[] data =  {'1','2','3','4','5','6','7','8','9','0'
				,'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		
		String code = "";
		for (int i = 0; i < weishu; i++) {
			code = code+data[rand.nextInt(36)];
		}
		return code;
	}
}
