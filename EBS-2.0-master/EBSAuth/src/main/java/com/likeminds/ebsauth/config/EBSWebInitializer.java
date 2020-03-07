/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
  */
package com.likeminds.ebsauth.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * <p>
 * EBSWebInitializer.java
 * </p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @author Ramesh Dhason
 * @version 2.0
 * @since 2020-02-12
 */
public class EBSWebInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(EBSSpringConfig.class);
		servletContext.addListener(new ContextLoaderListener(rootContext));

		ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
		registration.setLoadOnStartup(1);
		registration.addMapping("/");
	}

}
