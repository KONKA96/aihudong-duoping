package com.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.model.Faculty;
import com.model.Student;
import com.model.Subject;
import com.model.Teacher;
import com.service.FacultyService;
import com.service.StudentService;
import com.service.TeacherService;
import com.util.ImportExcelUtil;
import com.util.PageUtil;
import com.util.ProduceId;

@Controller
@RequestMapping("/student")
public class StudentController {

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
		//查询院系、专业
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		
		PageHelper.startPage(index, pageSize);
		Page<Student> studentList=null;
		if(student.getSubjectId()!=null){
			studentList = (Page<Student>) studentService.selectAllStudent(student);
		}else{
			Map<String,Object> map=new HashMap<>();
			map.put("facultyId", facultyId);
			if(student.getUsername()!=null) {
				map.put("username", student.getUsername());
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
		}
		 
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
	 * 根据有无Id判断进行更新或者新增操作
	 * @param teacher
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInfo")
	public String updateInfo(Student student){
		List<Teacher> teacherList = teacherService.selectAllTeacher(null);
		for (Teacher teacher : teacherList) {
			if(student.getUsername().equals(teacher.getUsername())) {
				return "exists";
			}
		}
		if(student.getId()!=null && student.getId()!=""){
//			除了本身的用户名，还有和其用户名相同的，则判定为用户名重复
			Student studentOld=studentService.selectStudentByUsername(student);
			if(!studentOld.getId().equals(student.getId())){
				return "exists";
			}
			if(studentService.updateByPrimaryKeySelective(student)>0){
				return "success";
			}
		}else{
//			如果有相同用户名的则直接判定为用户名相同
			Student studentOld=studentService.selectStudentByUsername(student);
			if(studentOld!=null){
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
			if(studentService.insertSelective(student)>0){
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
	public String deleteStudent(Student student){
		if(studentService.deleteByPrimaryKey(student)>0){
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
                resultList.add(studentService.insertSelective(student));
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
        System.out.println(result);
        return "redirect:showAllStudent";  
    }
}
