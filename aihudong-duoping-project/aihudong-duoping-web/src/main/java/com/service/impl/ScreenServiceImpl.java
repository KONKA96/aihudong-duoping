package com.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.ScreenMapper;
import com.model.Admin;
import com.model.Screen;
import com.service.ScreenService;

@Service
public class ScreenServiceImpl implements ScreenService {
	@Autowired
	private ScreenMapper screenMapper;

	@Override
	public List<Screen> selectAllScreen(Screen screen) {
		// TODO Auto-generated method stub
		return screenMapper.selectAllScreen(screen);
	}

	@Override
	public int insertSelective(Screen screen) {
		// TODO Auto-generated method stub
		List<Screen> screenList = screenMapper.selectAllScreen(null);
		for (Screen scr : screenList) {
			if(scr.getUsername().equals(screen.getUsername())){
				return 0;
			}
		}
		if(screen.getPassword()!=null) {
			screen.setPassword(new Md5Hash(screen.getPassword(), screen.getUsername() ,2).toString());
		}
		if(screen.getUsername()==null||screen.getPassword()==null||screen.getRoomId()==null){
			return 0;
		}
		screen.setDuration("00:00:00");
		screen.setNumber(0);
		
		return screenMapper.insertSelective(screen);
	}

	@Override
	public Screen selectByPrimaryKey(Screen screen) {
		// TODO Auto-generated method stub
		return screenMapper.selectByPrimaryKey(screen.getId());
	}

	@Override
	public int updateByPrimaryKeySelective(Screen screen) {
		List<Screen> screenList = selectAllScreen(screen);
		// TODO Auto-generated method stub
		if(screen.getPassword()!=null) {
			screen.setPassword(new Md5Hash(screen.getPassword(), screenList.get(0).getUsername() ,2).toString());
		}
		return screenMapper.updateByPrimaryKeySelective(screen);
	}

	@Override
	public int deleteByPrimaryKey(Screen screen) {
		// TODO Auto-generated method stub
		return screenMapper.deleteByPrimaryKey(screen.getId());
	}

	@Override
	public Integer selectAdminLastScreen(Admin admin) {
		// TODO Auto-generated method stub
		return screenMapper.selectAdminLastScreen(admin);
	}

	@Override
	public List<String> selectAllId() {
		// TODO Auto-generated method stub
		return screenMapper.selectAllId();
	}

	@Override
	public List<String> selectAllUsername() {
		// TODO Auto-generated method stub
		return screenMapper.selectAllUsername();
	}

	@Override
	public List<Screen> selectScreenByZoneOrBuilding(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return screenMapper.selectScreenByZoneOrBuilding(map);
	}

	@Override
	public Integer selectScreenCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return screenMapper.selectScreenCount(map);
	}

}
