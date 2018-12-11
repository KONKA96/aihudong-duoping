package com.task;

import com.model.Student;
import com.service.StudentService;

/**
 * 删除临时账号、访客
 * @author KONKA
 *
 */
public class DeleteTemporaryUser implements Runnable {

	private String id;
	
	private StudentService studentService;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StudentService getStudentService() {
		return studentService;
	}

	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Student stu = new Student();
		stu.setId(id);
		try {
			Thread.sleep(1000*60*10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		studentService.deleteByPrimaryKey(stu);

	}

}
