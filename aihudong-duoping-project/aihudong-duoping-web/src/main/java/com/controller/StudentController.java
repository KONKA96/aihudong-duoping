package com.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.service.StudentService;
import com.service.TeacherService;
import com.util.ImportExcelUtil;
import com.util.PageUtil;
import com.util.ProduceId;

import sun.misc.BASE64Encoder;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/student")
public class StudentController {
	
	@Value("${defaultPwd}")
	private String defaultPwd;
	@Value("${virtualRoomSwitch}")
	private boolean virtualRoomSwitch;
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private FacultyService facultyService;
	@Autowired
	private PageUtil pageUtil;
	
	/**
	 * 跳转到学生管理首页，可带参数，分页，模糊
	 * @param student
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/showAllStudent")
	public String showAllStudent(Student student,ModelMap modelMap,@RequestParam(required=true,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request,@RequestParam(required=false) String facultyId){
		HttpSession session = request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		
		//查询院系、专业
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		
		//打印日志
		String logSubject=null;
		String logfaculty=null;
		String logUsername=null;
		
		PageHelper.startPage(index, pageSize);
		Page<Student> studentList=null;
		if(student.getSubjectId()!=null){
			studentList = (Page<Student>) studentService.selectAllStudent(student);
		}else{
			Map<String,Object> map=new HashMap<>();
			map.put("facultyId", facultyId);
			if(student.getUsername()!=null) {
				map.put("username", student.getUsername());
				logUsername=student.getUsername();
			}
			studentList = (Page<Student>) studentService.selectStudentByFaculty(map);
		}
		if(facultyId!=null && facultyId!=""){
			Faculty faculty=new Faculty();
			faculty.setId(Integer.parseInt(facultyId));
			Subject subject=new Subject();
			subject.setId(student.getSubjectId());
			subject.setFaculty(faculty);
			student.setSubject(subject);
			
			logSubject=subject.getSubjectName();
			logfaculty=faculty.getFacultyName();
		}
		
		String logInfo=admin.getUsername()+"搜索学生信息";
		if(logSubject!=null) {
			logInfo+=",院系:"+logfaculty+",专业:"+logSubject;
		}else if(logfaculty!=null) {
			logInfo+=",院系:"+logfaculty;
		}
		if(logUsername!=null) {
			logInfo+=",模糊搜索关键字:"+logUsername;
		}
		logger.info(logInfo);
		 
		pageUtil.setPageInfo(studentList, index, pageSize,request);
		modelMap.put("studentList", studentList);
		modelMap.put("student", student);
		return "/student/list-student";
	}
	
	/**
	 * 跳转到更新页面
	 * @param student
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(Student student,ModelMap modelMap){
		if(student.getId()!=null){
			List<Student> studentList = studentService.selectAllStudent(student);
			student=studentList.get(0);
			modelMap.put("student", student);
		}
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		return "/student/edit-student";
	}
	
	/**
	 * 对比输入的密码和旧密码是否一致
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/testStudentOldPwd")
	public String testStudentOldPwd(Student student) {
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		List<Student> selectAllStudent = studentService.selectAllStudent(student);
		String password = new String(encoder.encode(student.getPassword().getBytes()));
		password = new String(encoder.encode(password.getBytes()));
		if(selectAllStudent.size()==0) {
			return "error";
		}else {
			if(password.equals(selectAllStudent.get(0).getPassword())) {
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
	public String updateInfo(Student student,HttpSession session){
		Admin SjAdmin=(Admin) session.getAttribute("admin");
		
		List<Teacher> teacherList = teacherService.selectAllTeacher(null);
		for (Teacher teacher : teacherList) {
			if(student.getUsername()!=null && student.getUsername().equals(teacher.getUsername())) {
				logger.info("用户名存在,操作失败!");
				return "exists";
			}
		}
		if(student.getId()!=null && student.getId()!=""){
//			除了本身的用户名，还有和其用户名相同的，则判定为用户名重复
			if(student.getUsername()!=null) {
				Student studentOld=studentService.selectStudentByUsername(student);
				if(!studentOld.getId().equals(student.getId())){
					logger.info("用户名存在,操作失败!");
					return "exists";
				}
			}
			if(studentService.updateByPrimaryKeySelective(student)>0){
				try {
					logger.info(SjAdmin.getUsername()+"修改了学生:"+student.getId()+"的信息");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "success";
			}
		}else{
//			如果有相同用户名的则直接判定为用户名相同
			Student studentOld=studentService.selectStudentByUsername(student);
			if(studentOld!=null){
				logger.info("用户名存在,操作失败!");
				return "exists";
			}
//			查询所有id,将参数传递
			List<String> idList = studentService.selectAllId();
			String newId=null;
			if(idList.size()==0){
//				如果表内没有数据，手动生成id
				newId="st1";
			}else{
//				生成一个ID
				newId=ProduceId.produceUserId(idList);
			}
			if(newId!=null){
				student.setId(newId);
			}
			if(student.getPassword()==null) {
				student.setPassword(defaultPwd);
			}
			if(studentService.insertSelective(student,virtualRoomSwitch)>0){
				try {
					logger.info(SjAdmin.getUsername()+"添加学生:"+student.getUsername());
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
	 * 删除学生数据
	 * @param teacher
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteStudent")
	public String deleteStudent(Student student,HttpSession session){
		Admin SjAdmin=(Admin) session.getAttribute("admin");
		if(studentService.deleteByPrimaryKey(student)>0){
			try {
				logger.info(SjAdmin.getUsername()+"删除学生:"+student.getId());
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
    public  String  uploadExcel(HttpServletRequest request,HttpSession session) throws Exception {
    	Admin SjAdmin=(Admin) session.getAttribute("admin");
    	
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
                Student student=new Student();
                
                String id=ProduceId.produceUserId(studentService.selectAllId());
                student.setId(id);
                student.setUsername(String.valueOf(lo.get(0)));
                student.setPassword(String.valueOf(lo.get(1)));
                student.setTruename(String.valueOf(lo.get(2)));
                student.setSex(Integer.parseInt(String.valueOf(lo.get(3))));
                student.setTelephone(String.valueOf(lo.get(4)));
                student.setEmail(String.valueOf(lo.get(5)));
                student.setSubjectId(Integer.parseInt(String.valueOf(lo.get(6))));
                student.setRemake(String.valueOf(lo.get(7)));
                resultList.add(studentService.insertSelective(student,virtualRoomSwitch));
                System.out.println(student);
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
        logger.info(SjAdmin.getUsername()+result);
        return "redirect:showAllStudent";  
    }
}
