package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Subject;
import com.service.SubjectService;

@Controller
@RequestMapping("/subject")
public class SubjectController {

	@Autowired
	private SubjectService subjectService;
	
	/**
	 * 添加或修改科目负责人
	 * @param subject
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSubjectAdmin")
	public String updateSubjectAdmin(Subject subject){
		List<Subject> subjectList = subjectService.selectSubjectByName(subject);
		Subject oldSubject=null;
//		数据库内匹配不到输入的字符串，输入有误
		if(subjectList.size()<=0){
			return "none";
		}else{
			oldSubject=subjectList.get(0);
			subject.setId(oldSubject.getId());
		}
//		该科目下已有负责人
		/*if(oldSubject.getAdminId()!=subject.getAdminId() && oldSubject.getAdminId()!=null && oldSubject.getAdminId()!=0){
			return "error";
		}*/
		if(subjectService.updateByPrimaryKeySelective(subject)>0){
			return "success";
		}
		return "";
	}
	/**
	 * 删除科目负责人
	 * @param subject
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteSubejctAdmin")
	public String deleteSubejctAdmin(Subject subject){
		List<Subject> subjectList = subjectService.selectSubjectByName(subject);
//		subjectList.get(0).setAdminId(0);
		if(subjectService.updateByPrimaryKeySelective(subjectList.get(0))>0){
			return "success";
		}
		return "";
	}
}
