package com.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class httpsFile implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("运行文件接收程序");
		FileTransferServer server=null;
		try {
			server = new FileTransferServer();
			Thread thread = new Thread(server);
			thread.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
