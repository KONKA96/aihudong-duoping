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
import com.model.Subject;
import com.service.FacultyService;
import com.service.SubjectService;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/subject")
public class SubjectController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private SubjectService subjectService;
	@Autowired
	private FacultyService facultyService;
	
	/**
	 * 修改或添加科目
	 * @author KONKA
	 * @param subject
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInfo")
	public String updateInfo(Subject subject,HttpSession session){
		Faculty faculty = new Faculty();
		faculty.setId(subject.getFacultyId());
		List<Faculty> facultyList = facultyService.selectAllFaculty(faculty);
		List<Subject> subjectList = facultyList.get(0).getSubjectList();
		//判断该院系下是否有科目
		if(subjectList.size()!=0) {
			int count = 0;
			//遍历科目比对，如果有相同科目则添加失败
			for (Subject sub : subjectList) {
				if(sub.getSubjectName().equals(subject.getSubjectName())) {
					return "exist";
				}else {
					count++;
				}
				
				if(count==subjectList.size()) {
					if(subject.getId()!=null) {
						if(subjectService.updateByPrimaryKeySelective(subject)>0) {
							return "success";
						}
					}else {
						if(subjectService.insertSelective(subject)>0) {
							return "success";
						}
					}

				}
			}
		}else {
			if(subject.getId()!=null) {
				if(subjectService.updateByPrimaryKeySelective(subject)>0) {
					return "success";
				}
			}else {
				if(subjectService.insertSelective(subject)>0) {
					return "success";
				}
			}
		}
		
		
		return "";
	}
	
	/**
	 * 删除科目
	 * @author KONKA
	 * @param subject
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteSubject")
	public String deleteSubject(Subject subject) {
		if(subjectService.deleteByPrimaryKey(subject)>0) {
			return "success";
		}
		return "";
	}
	
	/**
	 * 添加或修改科目负责人
	 * @param subject
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateSubjectAdmin")
	public String updateSubjectAdmin(Subject subject,HttpSession session){
		Admin SjAdmin=(Admin) session.getAttribute("admin");
		
		List<Subject> subjectList = subjectService.selectSubjectByName(subject);
		Subject oldSubject=null;
//		数据库内匹配不到输入的字符串，输入有误
		if(subjectList.size()<=0){
			logger.info(SjAdmin.getUsername()+"修改科目失败!");
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
			logger.info(SjAdmin.getUsername()+"修改科目:"+subject.getSubjectName());
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
	public String deleteSubejctAdmin(Subject subject,HttpSession session){
		Admin SjAdmin=(Admin) session.getAttribute("admin");
		
		List<Subject> subjectList = subjectService.selectSubjectByName(subject);
//		subjectList.get(0).setAdminId(0);
		if(subjectService.updateByPrimaryKeySelective(subjectList.get(0))>0){
			logger.info(SjAdmin.getUsername()+"删除科目:"+subject.getSubjectName());
			return "success";
		}
		return "";
	}
}
