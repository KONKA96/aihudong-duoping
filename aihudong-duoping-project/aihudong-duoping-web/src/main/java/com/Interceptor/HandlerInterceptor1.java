package com.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.model.Admin;

public class HandlerInterceptor1 implements HandlerInterceptor{

	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		//
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		Admin admin=(Admin) session.getAttribute("admin");
		if(admin!=null){
			return true;
		}
		System.out.println("请先登录");
		/*request.getRequestDispatcher("login.jsp").forward(request, response);*/
		response.sendRedirect("/aihudong-duoping-web/login/toLogin");
		return false;
	}

	

	
	
}
