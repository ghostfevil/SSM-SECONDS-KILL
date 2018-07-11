package com.crossoverjie.seconds.kill.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboProvider {
		
		private static final Log log = LogFactory.getLog(DubboProvider.class);

		public static void main(String[] args) {
			try {
				ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
				context.start();
			} catch (Exception e) {
				log.error("== DubboProvider context start error:",e);
			}
			synchronized (DubboProvider.class) {
				while (true) {
					try {
						DubboProvider.class.wait();
					} catch (InterruptedException e) {
						log.error("== synchronized error:",e);
					}
				}
			}
		}	    

}
