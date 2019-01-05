package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Faculty;
import com.model.Logger;
import com.model.Subject;
import com.service.FacultyService;
import com.service.SubjectService;
import com.util.JsonUtils;
import com.util.PageUtil;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/faculty")
public class FacultyController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private FacultyService facultyService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private PageUtil pageUtil;
	
	/**
	 * 查询所有院系
	 * @author KONKA
	 * @param faculty
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/showAllFaculty")
    public String showAllFaculty(Faculty faculty,ModelMap modelMap,@RequestParam(required=true,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request,HttpSession session){
		Map<String, Object> map = new HashMap<>();
		if(faculty.getId()!=null) {
			map.put("id", faculty.getId());
		}
		if(faculty.getFacultyName()!=null && !"".equals(faculty.getFacultyName())) {
			map.put("facultyName", faculty.getFacultyName());
		}
    	PageHelper.startPage(index, pageSize);
    	Page<Faculty> facultyList = (Page<Faculty>) facultyService.showAllFaculty(map);
    	
    	/*String logInfo=Sjadmin.getUsername()+"搜索管理员信息";
    	if(Sjadmin.getTruename()!=null) {
    		logInfo+=",模糊搜索关键字:"+admin.getTruename();
    	}
    	logger.info(logInfo);*/
    	
    	pageUtil.setPageInfo(facultyList, index, pageSize,request);
    	
    	/*log.info(adminList.toString());*/
    	modelMap.put("facultyList", facultyList);
    	return "/faculty/list-faculty";
    }
	
	/**
	 * 跳转到更新页面
	 * @author KONKA
	 * @param faculty
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(Faculty faculty,ModelMap modelMap){
		if(faculty.getId()!=null) {
			List<Faculty> showAllFaculty = facultyService.selectAllFaculty(faculty);
			modelMap.put("faculty", showAllFaculty.get(0));
		}
		
		return "/faculty/edit-faculty";
	}
	
	/**
	 * 修改或新增院系
	 * @author KONKA
	 * @param faculty
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInfo")
	public String updateInfo(Faculty faculty,HttpSession session){
		Admin SjAdmin=(Admin) session.getAttribute("admin");
		Map<String, Object> map = new HashMap<>();
		
		if(faculty.getId()!=null) {
			if(facultyService.updateByPrimaryKeySelective(faculty)>0) {
				try {
					logger.info(SjAdmin.getUsername()+"修改了院系:"+faculty.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "success";
			}
		}else {
			if(faculty.getFacultyName()!=null && !"".equals(faculty.getFacultyName())) {
				map.put("facultyName", faculty.getFacultyName());
			}
			//判断是否有重复名称
			List<Faculty> facultyList = facultyService.showAllFaculty(map);
			if(facultyList.size()!=0) {
				return "exist";
			}
			if(facultyService.insertSelective(faculty)>0) {
				List<Subject> subjectList = faculty.getSubjectList();
				for (Subject subject : subjectList) {
					if(subject.getId()==null && subject.getSubjectName()!=null && !"".equals(subject.getSubjectName())) {
						subject.setFacultyId(faculty.getId());
						subjectService.insertSelective(subject);
						logger.info(SjAdmin.getUsername()+"新增科目:"+subject.getId());
					}
				}
				try {
					logger.info(SjAdmin.getUsername()+"新增院系:"+faculty.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "success";
			}
		}
		
		return "error";
	}
	
	/**
	 * 删除院系
	 * @author KONKA
	 * @param faculty
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteFaculty")
	public String deleteFaculty(Faculty faculty,HttpSession session){
		Admin SjAdmin=(Admin) session.getAttribute("admin");
		if(facultyService.deleteByPrimaryKey(faculty)>0){
			try {
				logger.info(SjAdmin.getUsername()+"删除院系:"+faculty.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "success";
		}
		return "error";
	}
	
	
	/**
	 * 后台页面获取院系专业集合
	 * @author KONKA
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllFaculty", produces = { "text/json;charset=UTF-8" })
	public String getAllFaculty(HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> argMap = new HashMap<>();
//		获取院系专业集合
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		argMap.put("facultyList", facultyList);
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 通过系查询其所有的科目
	 * @param faculty
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getSubjectFromFaculty", produces = { "text/json;charset=UTF-8" })
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
