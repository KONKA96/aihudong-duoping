package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.model.Logger;
import com.service.AdminService;
import com.service.BuildingService;
import com.service.FacultyService;
import com.service.LicenseService;
import com.service.impl.AdminServiceImpl;
import com.util.JsonUtils;
import com.util.PageUtil;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	
	@Value("${defaultPwd}")
	private String defaultPwd;
	@Autowired
	private FacultyService facultyService;
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
	 * 检测输入的旧密码是否和原来匹配（个人）
	 * 
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/testOldPwd")
	public String testOldPwd(String password,Admin admin) {
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		if(admin.getId()==null) {
			return "id is null";
		}
		admin = adminService.selectByPrimaryKey(admin);
		password = new String(encoder.encode(password.getBytes()));
		password = new String(encoder.encode(password.getBytes()));
		if (password.equals(admin.getPassword())) {
			return "success";
		}
		return "";
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
    	
    	String logInfo=Sjadmin.getUsername()+"搜索管理员信息";
    	if(Sjadmin.getTruename()!=null) {
    		logInfo+=",模糊搜索关键字:"+admin.getTruename();
    	}
    	logger.info(logInfo);
    	
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
    	if(admin.getPower()!=null && admin.getPower()==1){
			Admin GenJiAdmin = adminService.selectGenJiAdmin();
			admin.setHigherId(GenJiAdmin.getId());
		}
    	List<Admin> adminList = adminService.selectAllAdmin(null);
    	for (Admin ad : adminList) {
			if(ad.getId()!=admin.getId() && ad.getUsername().equals(admin.getUsername())){
				logger.info(admin.getUsername()+"用户名已存在，修改失败!");
				return "exist";
			}
		}
//    	进行修改和添加
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		if(admin.getPassword()!=null) {
			String password = new String(encoder.encode(admin.getPassword().getBytes()));
			password = new String(encoder.encode(password.getBytes()));
			admin.setPassword(password);
		}
    	if(admin.getId()!=null){
    		if(adminService.updateByPrimaryKeySelective(admin)>0){
    			if(admin.getId().equals(SjAdmin.getId())){
    				//将管理员自身的密码设置到session中
    				if(admin.getPassword()==null) {
    					admin.setPassword(SjAdmin.getPassword());
    				}
    				session.setAttribute("admin", SjAdmin);
    			}
    			logger.info(SjAdmin.getUsername()+"修改"+admin.getId()+"的信息成功!");
    			return "success";
    		}
    	}else{
    		if(adminService.insertSelective(admin)>0){
    			logger.info(SjAdmin.getUsername()+"添加管理员:"+admin.getUsername());
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
    public String deleteAdmin(Admin admin,HttpSession session){
    	Admin Sjadmin=(Admin) session.getAttribute("admin");
    	
//    	将自己剩余的屏幕数量还给自己上级管理员
    	admin=adminService.selectByPrimaryKey(admin);
    	Admin shangji=new Admin();
    	shangji.setId(admin.getHigherId());
    	shangji=adminService.selectByPrimaryKey(shangji);
    	shangji.setPassword(null);
    	shangji.setScreenRemain(shangji.getScreenRemain()+admin.getScreenRemain());
    	adminService.updateByPrimaryKeySelective(shangji);
//    	进行删除
    	if(adminService.deleteByPrimaryKey(admin)>0){
    		logger.info(Sjadmin.getUsername()+"删除了管理员:"+admin.getId());
    		if(shangji.getId()==Sjadmin.getId()) {
    			Sjadmin.setScreenRemain(shangji.getScreenRemain());
    			session.setAttribute("admin", Sjadmin);
    		}
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
    	Admin Sjadmin=(Admin) session.getAttribute("admin");
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
        		logger.info(Sjadmin.getUsername()+"进行屏幕数量分配操作,"+YijiAdmin.getUsername()+"数量不足,分配失败!");
        		return "error";
        	}
//        	如果分配的屏幕小于已经分配的屏幕数量，同样失败
        	if(admin.getScreenNum()<already){
        		logger.info(Sjadmin.getUsername()+"进行屏幕数量分配操作,"+admin.getId()+"数量不足,分配失败!");
        		return "error2";
        	}
        	YijiAdmin.setScreenRemain(YijiAdmin.getScreenRemain()-changeNum);
        	YijiAdmin.setPassword(null);
        	adminService.updateByPrimaryKeySelective(YijiAdmin);
        	erjiAdminOld.setScreenNum(admin.getScreenNum());
        	erjiAdminOld.setScreenRemain(erjiAdminOld.getScreenRemain()+changeNum);
        	erjiAdminOld.setPassword(null);
        	if(admin.getId()!=null){
        		if(adminService.updateByPrimaryKeySelective(erjiAdminOld)>0){
        			logger.info(Sjadmin.getUsername()+"进行屏幕数量分配操作,"+erjiAdminOld.getUsername()+"的剩余屏幕数为:"+erjiAdminOld.getScreenRemain());
            		return "success";
            	}
        	}else{
        		if(adminService.insertSelective(erjiAdminOld)>0){
        			logger.info(Sjadmin.getUsername()+"添加操作管理员:"+erjiAdminOld.getUsername());
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
    			logger.info(Sjadmin.getUsername()+"进行屏幕数量分配操作,"+genjiAdmin.getUsername()+"数量不足,分配失败!");
    			return "error";
    		}
    		genjiAdmin.setScreenRemain(genjiAdmin.getScreenRemain()-changeNum);
    		session.setAttribute("admin", genjiAdmin);
    		genjiAdmin.setPassword(null);
    		adminService.updateByPrimaryKeySelective(genjiAdmin);
    		
    		yijiAdminOld.setScreenNum(admin.getScreenNum());
    		yijiAdminOld.setScreenRemain(yijiAdminOld.getScreenRemain()+changeNum);
    		if(adminService.updateByPrimaryKeySelective(yijiAdminOld)>0){
    			logger.info(Sjadmin.getUsername()+"进行屏幕数量分配操作,"+yijiAdminOld.getUsername()+"的剩余屏幕数为:"+yijiAdminOld.getScreenRemain());
    			return "success";
    		}
    	}
    	return "";
    }
    
    @ResponseBody
    @RequestMapping("/insertAdminScreen")
    public String insertAdminScreen(Admin admin,HttpSession session){
    	Admin genjiAdmin=(Admin) session.getAttribute("admin");
    	
    	List<Admin> adminList = adminService.selectAllAdmin(null);
    	for (Admin ad : adminList) {
			if(ad.getUsername().equals(admin.getUsername())){
				logger.info(genjiAdmin.getUsername()+"正在添加管理员,"+admin.getUsername()+"用户名已经存在!");
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
        		logger.info(genjiAdmin.getUsername()+"正在添加管理员,屏幕剩余数量不足!");
        		return "error";
        	}
        	YijiAdmin.setScreenRemain(YijiAdmin.getScreenRemain()-admin.getScreenNum());
        	YijiAdmin.setPassword(null);
        	adminService.updateByPrimaryKeySelective(YijiAdmin);
        	admin.setScreenRemain(admin.getScreenNum());
        	if(adminService.insertSelective(admin)>0){
        		logger.info(genjiAdmin.getUsername()+"添加管理员:"+admin.getUsername());
        		return "success";
        	}
    	}else if(admin.getPower()==1){
    		if(genjiAdmin.getScreenRemain()<=admin.getScreenNum()){
    			logger.info(genjiAdmin.getUsername()+"正在添加管理员,屏幕剩余数量不足!");
    			return "error";
    		}
    		genjiAdmin.setScreenRemain(genjiAdmin.getScreenRemain()-admin.getScreenNum());
    		genjiAdmin.setPassword(null);
    		adminService.updateByPrimaryKeySelective(genjiAdmin);
    		session.setAttribute("admin", genjiAdmin);
    		admin.setScreenRemain(admin.getScreenNum());
    		admin.setPassword(defaultPwd);
    		if(adminService.insertSelective(admin)>0){
    			logger.info(genjiAdmin.getUsername()+"添加管理员:"+admin.getUsername());
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
