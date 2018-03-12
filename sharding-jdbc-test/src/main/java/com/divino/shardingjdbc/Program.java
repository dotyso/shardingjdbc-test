package com.divino.shardingjdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.divino.shardingjdbc.service.DemoService;

public class Program {

	
	public static void main(String[] args) {
		
		try {
			
	        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/mybatisShardingTableOnlyContext.xml");
	        DemoService demoService = applicationContext.getBean(DemoService.class);
	        demoService.demo();
	        
			//new JdbcRepository().run();
	        
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	

}
