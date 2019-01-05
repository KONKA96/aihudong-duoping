package com.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Faculty;
import com.model.Logger;
import com.model.Student;
import com.model.Subject;
import com.model.Teacher;
import com.service.FacultyService;
import com.service.RoomService;
import com.service.StudentService;
import com.service.SubjectService;
import com.service.TeacherService;
import com.util.ImportExcelUtil;
import com.util.JsonUtils;
import com.util.PageUtil;
import com.util.ProduceId;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
	
	@Value("${defaultPwd}")
	private String defaultPwd;
	@Value("${virtualRoomSwitch}")
	private boolean virtualRoomSwitch;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private FacultyService facultyService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private PageUtil pageUtil;
	
	
	/**
	 * 查询所有教师，可带参数，分页，模糊
	 * @param teacher
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/showAllTeacher")
	public String showAllTeacher(Teacher teacher,ModelMap modelMap,@RequestParam(required=true,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request,@RequestParam(required=false) String facultyId){
		//查询院系、专业
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		
		HttpSession session = request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		
		String logSubject=null;
		String logfaculty=null;
		String logUsername=null;
		
		PageHelper.startPage(index, pageSize);
		Page<Teacher> teacherList=null;
		if(teacher.getSubjectId()!=null){
			teacherList = (Page<Teacher>) teacherService.selectAllTeacher(teacher);
			Subject subject = subjectService.selectByPrimaryKey(teacher.getSubjectId());
			logSubject=subject.getSubjectName();
		}else{
			Map<String,Object> map=new HashMap<>();
			map.put("facultyId", facultyId);
			if(teacher.getUsername()!=null) {
				map.put("username", teacher.getUsername());
				logUsername=teacher.getUsername();
			}
			teacherList = (Page<Teacher>) teacherService.selectTeacherByFaculty(map);
		}
		if(facultyId!=null && facultyId!=""){
//			将系、专业对象设置进教师对象内，使查询条件回显
			Faculty faculty=new Faculty();
			faculty.setId(Integer.parseInt(facultyId));
			Subject subject=new Subject();
			subject.setId(teacher.getSubjectId());
			subject.setFaculty(faculty);
			teacher.setSubject(subject);
			
			logfaculty=faculty.getFacultyName();
		}
		
		
		String logInfo=admin.getUsername()+"搜索教师信息";
		if(logSubject!=null) {
			logInfo+=",院系:"+logfaculty+",专业:"+logSubject;
		}else if(logfaculty!=null) {
			logInfo+=",院系:"+logfaculty;
		}
		if(logUsername!=null) {
			logInfo+=",模糊搜索关键字:"+logUsername;
		}
		logger.info(logInfo);
		pageUtil.setPageInfo(teacherList, index, pageSize,request);
		modelMap.put("teacherList", teacherList);
		modelMap.put("teacher", teacher);
		return "/adminuser/list-user";
	}
	/**
	 * 跳转到更新页面，需要院系、专业传入
	 * @param teacher
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(Teacher teacher,ModelMap modelMap){
		if(teacher.getId()!=null){
			teacher = teacherService.selectTeacherById(teacher);
			modelMap.put("teacher", teacher);
		}
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		return "/adminuser/edit-user";
	}
	
	/**
	 * 对比输入的密码和旧密码是否一致
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/testTeacherOldPwd")
	public String testTeacherOldPwd(Teacher teacher) {
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		List<Teacher> selectAllTeacher = teacherService.selectAllTeacher(teacher);
		String password = new String(encoder.encode(selectAllTeacher.get(0).getPassword().getBytes()));
		password = new String(encoder.encode(password.getBytes()));
		if(selectAllTeacher.size()==0) {
			return "error";
		}else {
			if(password.equals(selectAllTeacher.get(0).getPassword())) {
				return "same";
			}
		}
		return "success";
	}
	
	/**
	 * 根据有无Id判断进行更新或者新增操作
	 * @param teacher
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInfo")
	public String updateInfo(Teacher teacher,HttpSession session){
		List<Teacher> teacherList = teacherService.selectAllTeacher(null);
		Admin admin=(Admin) session.getAttribute("admin");
		logger.info("admin:"+admin);
		for (Teacher tea : teacherList) {
			if(tea.getUsername().equals(teacher.getUsername())&& !tea.getId().equals(teacher.getId())){
				logger.info(admin.getUsername()+"修改失败，用户名重复");
				return "same";
			}
		}
		List<Student> studentList = studentService.selectAllStudent(null);
		for (Student student : studentList) {
			if(teacher.getUsername()!=null && teacher.getUsername().equals(student.getUsername())) {
				logger.info(admin.getUsername()+"修改失败，用户名重复");
				return "same";
			}
		}
		if(teacher.getId()!=null && teacher.getId()!=""){
			
			if(teacherService.updateTeacherSelected(teacher)>0){
				try {
					logger.info(admin.getUsername()+"修改了"+teacher.getId()+"的信息");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "success";
			}
		}else{
			List<String> idList = teacherService.selectAllId();
			String newId=null;
			if(idList.size()==0){
//				如果表内没有数据，手动生成id
				newId="te1";
			}else{
				newId=ProduceId.produceUserId(idList);
			}
			if(newId!=null){
				teacher.setId(newId);
			}
			if(teacher.getPassword()==null) {
				teacher.setPassword(defaultPwd);
			}
			if(teacherService.insertTeacherSelected(teacher,virtualRoomSwitch)>0){
				
				try {
					logger.info(admin.getUsername()+"添加了教师:"+teacher.getUsername());
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
	 * 删除教师数据
	 * @param teacher
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteTeacher")
	public String deleteTeacher(Teacher teacher,HttpServletResponse response,HttpSession session){
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Admin admin=(Admin) session.getAttribute("admin");
		if(teacherService.deleteTeacherById(teacher)>0){
			try {
				logger.info(admin.getUsername()+"删除了教师:"+teacher.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "success";
		}
		return "error";
	}
	
	/**  
     * 描述：通过传统方式form表单提交方式导入excel文件  
     * @param request  
     * @throws Exception  
     */  
    @RequestMapping(value="uploadExcel",method={RequestMethod.GET,RequestMethod.POST})  
    public  String  uploadExcel(HttpServletRequest request) throws Exception {  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
        System.out.println("通过传统方式form表单提交方式导入excel文件！");  
          
        InputStream in =null;  
        List<List<Object>> listob = null;  
        MultipartFile file = multipartRequest.getFile("upfile");  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());  
        in.close();  
          
        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出  
        List<Integer> resultList=new ArrayList<>(listob.size());
        try{
        	for (int i = 0; i < listob.size(); i++) {  
                List<Object> lo = listob.get(i); 
                Teacher teacher=new Teacher();
                
                String id=ProduceId.produceUserId(teacherService.selectAllId());
                teacher.setId(id);
                teacher.setUsername(String.valueOf(lo.get(0)));
                teacher.setPassword(String.valueOf(lo.get(1)));
                teacher.setTruename(String.valueOf(lo.get(2)));
                teacher.setSex(Integer.parseInt(String.valueOf(lo.get(3))));
                teacher.setTelephone(String.valueOf(lo.get(4)));
                teacher.setEmail(String.valueOf(lo.get(5)));
                teacher.setJob(String.valueOf(lo.get(6)));
                teacher.setSubjectId(Integer.parseInt(String.valueOf(lo.get(7))));
                teacher.setScreenNumSametime(Integer.parseInt(String.valueOf(lo.get(8))));
                teacher.setRemake(String.valueOf(lo.get(9)));
                resultList.add(teacherService.insertTeacherSelected(teacher,virtualRoomSwitch));
                System.out.println(teacher);
            } 
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
         
        String result="";
        int i=0;
        for (Integer integer : resultList) {
			if(integer==1){
				i++;
			}
		}
        if(i==0){
        	result+="导入失败！";
        }else{
        	result+="共"+resultList.size()+"条信息，成功导入"+i+"条信息，导入失败"+(resultList.size()-i)+"条信息";
        }
        logger.info(result);
        return "redirect:/teacher/showAllTeacher";  
    }
    
    /**
     * 获取教师个人信息
     * @param teacher
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getTeacherInfo",produces = "text/json;charset=UTF-8")
    public String getTeacherInfo(Teacher teacher){
    	List<Teacher> teacherList = teacherService.selectAllTeacher(teacher);
    	teacher=teacherList.get(0);
    	return JsonUtils.objectToJson(teacher);
    }
}
