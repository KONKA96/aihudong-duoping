package com.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.SetUp;
import com.service.SetUpService;
import com.util.JsonUtils;

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
	
	/**
	 * 查询企业名称接口（对接前端）
	 * @author KONKA
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/showEnterpriseName", produces = { "text/json;charset=UTF-8" })
	public String showEnterpriseName() {
		SetUp setUp = new SetUp();
		setUp.setId(1);
		setUp = setUpService.selectByPrimaryKey(setUp);
		
		Map<String, Object> argMap = new HashMap<>();

		argMap.put("code", "200");
		argMap.put("EnterpriseName", setUp.getEnterpriseName());
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 查询水印开关接口（对接前端）
	 * @author KONKA
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/showWaterMark", produces = { "text/json;charset=UTF-8" })
	public String showWaterMark() {
		SetUp setUp = new SetUp();
		setUp.setId(1);
		setUp = setUpService.selectByPrimaryKey(setUp);
		
		Map<String, Object> argMap = new HashMap<>();

		argMap.put("code", "200");
		argMap.put("WaterMark", setUp.getWaterMark());
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 查询logo开关接口（对接前端）
	 * @author KONKA
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/showLogo", produces = { "text/json;charset=UTF-8" })
	public String showLogo() {
		SetUp setUp = new SetUp();
		setUp.setId(1);
		setUp = setUpService.selectByPrimaryKey(setUp);
		
		Map<String, Object> argMap = new HashMap<>();

		argMap.put("code", "200");
		argMap.put("Logo", setUp.getLogo());
		return JsonUtils.objectToJson(argMap);
	}

}
