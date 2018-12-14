package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.SetUp;
import com.service.SetUpService;

@Controller
@RequestMapping("/setup")
public class SetUpController {
	
	@Autowired
	private SetUpService setUpService;
	
	/**
	 * 跳转设置首页
	 * @author KONKA
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toIndex")
	public String toIndex(ModelMap modelMap) {
		SetUp setUp = new SetUp();
		setUp.setId(1);
		setUp = setUpService.selectByPrimaryKey(setUp);
		
		modelMap.put("setUp", setUp);
		return "/other/set-up";
	}
	
	/**
	 * 更改设置
	 * @author KONKA
	 * @param setUp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeSetUp")
	public String changeSetUp(SetUp setUp) {
		if(setUp.getLogo()==null) {
			setUp.setLogo("off");
		}
		if(setUp.getWaterMark()==null) {
			setUp.setWaterMark("off");
		}
		setUpService.updateByPrimaryKeySelective(setUp);
		return "success";
	}

}
