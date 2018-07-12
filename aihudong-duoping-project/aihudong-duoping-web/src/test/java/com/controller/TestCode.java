package com.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-ioc.xml", "classpath:spring-mvc.xml" ,"classpath:mybatis-config.xml" })
public class TestCode {

	@Value("${httpUrl}")
	private String httpUrl;
	@Value("${salt}")
	private String salt;
	
	@Test
	public void testProperties() {
		System.out.println("httpUrl"+httpUrl);
		System.out.println("salt"+salt);
	}
	
}
