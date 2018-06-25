package com.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Building;
import com.model.Faculty;
import com.model.License;
import com.model.Subject;
import com.model.Teacher;
import com.service.AdminService;
import com.service.BuildingService;
import com.service.FacultyService;
import com.service.LicenseService;
import com.service.RecordService;
import com.service.TeacherService;
import com.util.JsonUtils;
import com.util.PageUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static Logger log=LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private FacultyService facultyService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private PageUtil pageUtil;
	
	
	/**
	 * 跳转到登录后首页
	 * @param session
	 * @return
	 */
	@RequestMapping("/test")
	public String test(HttpSession session){
		/*Admin admin=new Admin();
		admin.setUsername("genji");
		admin.setPassword("123");
		admin.setPower(0);
		admin = adminService.adminLogin(admin);
		session.setAttribute("admin", admin);*/
		return "index";
	}
	/**
	 * 登录后跳转的首页
	 * @return
	 */
	@RequestMapping("/toAdminIndex")
	public String toAdminIndex(ModelMap modelMap){
//		获取院系专业集合
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		return "/adminuser/admin-index";
	}
	
	/**
	 * 跳转到管理员个人信息页
	 * @return
	 */
	@RequestMapping("/toAdminInfo")
	public String toAdminInfo(){
		return "/adminuser/admin-info";
	}
    /**
     * 查询所有下级管理员，包含模糊搜索、分页
     * @param admin
     * @param modelMap
     * @param index
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("/showAllAdmin")
    public String showAllAdmin(Admin admin,ModelMap modelMap,@RequestParam(required=true,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request,HttpSession session){
    	Admin Sjadmin=(Admin) session.getAttribute("admin");
//    	如果是一级管理员，则只能查询操作管理员
    	if(Sjadmin.getPower()==1){
    		admin.setPower(2);
    	}
    	
    	PageHelper.startPage(index, pageSize);
    	Page<Admin> adminList = (Page<Admin>) adminService.selectAllAdmin(admin);
    	pageUtil.setPageInfo(adminList, index, pageSize,request);
    	
    	/*log.info(adminList.toString());*/
    	modelMap.put("adminList", adminList);
    	/*try {
            int i = 1 / 0;
        } catch (Exception e) {
            log.error("故意除零了", e);
        }*/
    	return "/adminuser/list-admin";
    }
    
    /**
     * 获取一级管理员所管理的所有院系
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getAllFacultyByAdmin",produces = "text/json;charset=UTF-8")
    public String getAllFacultyByAdmin(Admin admin){
    	admin = adminService.selectAllFaculty(admin);
    	List<Faculty> facultyList = admin.getFacultyList();
    	return JsonUtils.objectToJson(facultyList);
    }
    /**
     * 获取操作管理员所管理的所有科目
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getAllSubjectByAdmin",produces = "text/json;charset=UTF-8")
    public String getAllSubjectByAdmin(Admin admin){
    	admin = adminService.selectAllBuilding(admin);
    	List<Building> buildingList = admin.getBuildingList();
    	return JsonUtils.objectToJson(buildingList);
    }
    /**
     * 跳转到修改、新增页面
     * @param admin
     * @param modelMap
     * @return
     */
    @RequestMapping("/toUpdateAdmin")
    public String toUpdateAdmin(Admin admin,ModelMap modelMap){
		if (admin.getId() != null) {
			admin = adminService.selectByPrimaryKey(admin);
			modelMap.put("admin", admin);
		}
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		List<Building> buildingList = buildingService.selectAllBuilding(null);
		modelMap.put("buildingList", buildingList);
    	return "/adminuser/edit-admin";
    }
    
    @ResponseBody
    @RequestMapping(value="/selectAllYiJiAdmin",produces = "text/json;charset=UTF-8")
    public String selectAllYiJiAdmin(){
    	List<Admin> YijiAdminList = adminService.selectYijiAdmin(null);
    	
    	return JsonUtils.objectToJson(YijiAdminList);
    }
    /**
     * 修改、新增管理员
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateAdmin")
    public String updateAdmin(Admin admin,HttpSession session){
//    	如果是一级管理员，则只能添加操作管理员
    	Admin SjAdmin=(Admin) session.getAttribute("admin");
    	if(SjAdmin.getPower()==1){
    		admin.setPower(2);
    	}
//    	如果修改为一级管理员，则其上一级为根级管理员
    	if(admin.getPower()==1){
			Admin GenJiAdmin = adminService.selectGenJiAdmin();
			admin.setHigherId(GenJiAdmin.getId());
		}
    	List<Admin> adminList = adminService.selectAllAdmin(null);
    	for (Admin ad : adminList) {
			if(ad.getUsername().equals(admin.getUsername())){
				return "exist";
			}
		}
//    	进行修改和添加
    	if(admin.getId()!=null){
    		if(adminService.updateByPrimaryKeySelective(admin)>0){
    			if(admin.getId().equals(SjAdmin.getId())){
    				session.setAttribute("admin", admin);
    			}
    			return "success";
    		}
    	}else{
    		if(adminService.insertSelective(admin)>0){
    			return "success";
    		}
    	}
    	return "error";
    }
    /**
     * 删除管理员信息
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteAdmin")
    public String deleteAdmin(Admin admin){
//    	将自己剩余的屏幕数量还给自己上级管理员
    	admin=adminService.selectByPrimaryKey(admin);
    	Admin shangji=new Admin();
    	shangji.setId(admin.getHigherId());
    	shangji=adminService.selectByPrimaryKey(shangji);
    	shangji.setScreenRemain(shangji.getScreenRemain()+admin.getScreenRemain());
    	adminService.updateByPrimaryKeySelective(shangji);
//    	进行删除
    	if(adminService.deleteByPrimaryKey(admin)>0){
    		return "success";
    	}
    	return "error";
    }
    /**
     * license管理
     * @param modelMap
     * @return
     */
    @RequestMapping("/showLicense")
    public String showLicense(ModelMap modelMap){
    	List<License> licenseList = licenseService.selectAllLicense();
    	int current=0;
    	for (License license : licenseList) {
    		licenseList.remove(license);
			if(license.getIsCurrent()==1){
				modelMap.put("license", license);
				current=license.getId();
			}
			if(current!=0 && license.getId()>current){
				licenseList.add(license);
			}
		}
    	modelMap.put("licenseList", licenseList);
    	return "/license/license";
    }
   
   
    
    /**
     * 获取管理员个人信息
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getAdminInfo",produces = "text/json;charset=UTF-8")
    public String getAdminInfo(Admin admin){
    	admin = adminService.selectByPrimaryKey(admin);
    	return JsonUtils.objectToJson(admin);
    }
    /**
     * 向下级分配屏幕数量及校验
     * @param admin
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateScreenNum")
    public String updateScreenNum(Admin admin,HttpSession session){
//    	admin = adminService.selectByPrimaryKey(admin);
    	Map<String,Object> map=new HashMap<>();
    	int changeNum=0;
//    	判断要操作的对象是几级管理员
    	if(admin.getPower()==2){
    		map.put("adminId2", admin.getId());
//    		先查到该操作管理员的上级管理员
    		List<Admin> yijiAdminList=adminService.selectYijiAdmin(map);
        	Admin YijiAdmin = yijiAdminList.get(0);
        	List<Admin> erjiAdminList=YijiAdmin.getAdminList();
//        	获得修改的管理员原有的数据
        	Admin erjiAdminOld=null;
        	for (Admin i : erjiAdminList) {
        		if(i.getId()==admin.getId()){
        			erjiAdminOld=i;
        		}
    		}
//        	获得变动的屏幕数量
        	changeNum=admin.getScreenNum()-erjiAdminOld.getScreenNum();
        	int already=erjiAdminOld.getScreenNum()-erjiAdminOld.getScreenRemain();
//        	如果屏幕数量加上新修改的屏幕数量大于上一级的屏幕数量，则修改失败
        	if(YijiAdmin.getScreenRemain()-changeNum<0){
        		return "error";
        	}
//        	如果分配的屏幕小于已经分配的屏幕数量，同样失败
        	if(admin.getScreenNum()<already){
        		return "error2";
        	}
        	YijiAdmin.setScreenRemain(YijiAdmin.getScreenRemain()-changeNum);
        	adminService.updateByPrimaryKeySelective(YijiAdmin);
        	erjiAdminOld.setScreenNum(admin.getScreenNum());
        	erjiAdminOld.setScreenRemain(erjiAdminOld.getScreenRemain()+changeNum);
        	if(admin.getId()!=null){
        		if(adminService.updateByPrimaryKeySelective(erjiAdminOld)>0){
            		return "success";
            	}
        	}else{
        		if(adminService.insertSelective(erjiAdminOld)>0){
            		return "success";
            	}
        	}
        	
    	}else if(admin.getPower()==1){
    		map.put("adminId1", admin.getId());
//    		查询所有一级管理员
    		List<Admin> yijiAdminList = adminService.selectYijiAdmin(map);
    		Admin yijiAdminOld = yijiAdminList.get(0);
    		Admin genjiAdmin=(Admin) session.getAttribute("admin");
    		
    		changeNum=admin.getScreenNum()-yijiAdminOld.getScreenNum();
    		if(genjiAdmin.getScreenRemain()<changeNum){
    			return "error";
    		}
    		genjiAdmin.setScreenRemain(genjiAdmin.getScreenRemain()-changeNum);
    		session.setAttribute("admin", genjiAdmin);
    		adminService.updateByPrimaryKeySelective(genjiAdmin);
    		
    		yijiAdminOld.setScreenNum(admin.getScreenNum());
    		yijiAdminOld.setScreenRemain(yijiAdminOld.getScreenRemain()+changeNum);
    		if(adminService.updateByPrimaryKeySelective(yijiAdminOld)>0){
    			return "success";
    		}
    	}
    	return "";
    }
    
    @ResponseBody
    @RequestMapping("/insertAdminScreen")
    public String insertAdminScreen(Admin admin,HttpSession session){
    	
    	List<Admin> adminList = adminService.selectAllAdmin(null);
    	for (Admin ad : adminList) {
			if(ad.getUsername().equals(admin.getUsername())){
				return "exist";
			}
		}
    	Map<String,Object> map=new HashMap<>();
    	if(admin.getPower()==2){
    		map.put("adminId1", admin.getHigherId());
//    		先查到该操作管理员的上级管理员
    		List<Admin> yijiAdminList=adminService.selectYijiAdmin(map);
        	Admin YijiAdmin = yijiAdminList.get(0);
        	if(YijiAdmin.getScreenRemain()<=admin.getScreenNum()){
        		return "error";
        	}
        	YijiAdmin.setScreenRemain(YijiAdmin.getScreenRemain()-admin.getScreenNum());
        	adminService.updateByPrimaryKeySelective(YijiAdmin);
        	admin.setScreenRemain(admin.getScreenNum());
        	if(adminService.insertSelective(admin)>0){
        		return "success";
        	}
    	}else if(admin.getPower()==1){
    		Admin genjiAdmin=(Admin) session.getAttribute("admin");
    		if(genjiAdmin.getScreenRemain()<=admin.getScreenNum()){
    			return "error";
    		}
    		genjiAdmin.setScreenRemain(genjiAdmin.getScreenRemain()-admin.getScreenNum());
    		adminService.updateByPrimaryKeySelective(genjiAdmin);
    		session.setAttribute("admin", genjiAdmin);
    		admin.setScreenRemain(admin.getScreenNum());
    		if(adminService.insertSelective(admin)>0){
        		return "success";
        	}
    	}
    	return "";
    }
    
    /**
     * 管理员登出
     * @param session
     * @return
     */
    @RequestMapping("/adminLogout")
    public String adminLogout(HttpSession session){
    	session.invalidate();
    	return "login";
    }
}
