package com.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Admin;
import com.model.Faculty;
import com.model.Logger;
import com.service.FacultyService;
import com.util.JsonUtils;

@Controller
@RequestMapping("/faculty")
public class FacultyController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private FacultyService facultyService;
	
	/**
	 * 通过系查询其所有的科目
	 * @param faculty
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getSubjectFromFaculty")
	public String getSubjectFromFaculty(Faculty faculty){
		List<Faculty> facultyList = facultyService.selectAllFaculty(faculty);
		faculty=facultyList.get(0);
		return JsonUtils.objectToJson(faculty.getSubjectList());
	}
	
	/**
	 * 添加或修改系负责人
	 * @param faculty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateFaculty",produces = "text/json;charset=UTF-8")
	public String updateFaculty(Faculty faculty,HttpSession session){
		Admin Sjadmin=(Admin) session.getAttribute("admin");
		
		List<Faculty> facultyList = facultyService.selectAllFaculty(faculty);
		Faculty facultyOld=null;
//		输入的字符串不匹配，查询不到该系
		if(facultyList.size()<=0){
			logger.info(Sjadmin.getUsername()+"修改院系失败!");
			return "none";
		}else{
			facultyOld=facultyList.get(0);
			faculty.setId(facultyOld.getId());
		}
//		该系已有负责人
		if(facultyOld.getAdminId()!=faculty.getAdminId() && facultyOld.getAdminId()!=null && facultyOld.getAdminId()!=0){
			return "error";
		}
//		添加或修改成功
		if(facultyService.updateByPrimaryKeySelective(faculty)>0){
			logger.info(Sjadmin.getUsername()+"修改了院系:"+faculty.getFacultyName());
			return "success";
		}
		return "";
	}
	/**
	 * 删除系负责人
	 */
	@ResponseBody
	@RequestMapping("/deleteFacultyAdmin")
	public String deleteFacultyAdmin(Faculty faculty,HttpSession session){
		Admin Sjadmin=(Admin) session.getAttribute("admin");
		
		Faculty facultyOld = facultyService.selectFacultyById(faculty);
		facultyOld.setAdminId(0);
		if(facultyService.updateByPrimaryKeySelective(facultyOld)>0){
			logger.info(Sjadmin.getUsername()+"删除了院系:"+faculty.getFacultyName()+"的负责人");
			return "success";
		}
		return "";
	}
}
