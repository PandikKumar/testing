/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */
package com.likeminds.ebsauth.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.likeminds.ebsauth.core.AuthConstants;
import com.likeminds.ebsauth.exception.AuthException;
import com.likeminds.ebsauth.service.AuthService;
/**
 * <p> LogoutController.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since 2020-02-12
 */


@RequestMapping("ssologout.do")
@Controller
public class LogoutController {
	
	private static final Logger log = LogManager.getLogger(LogoutController.class);
	
	@Autowired
	private AuthService authService;
	
	
	@GetMapping
	public String doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(log.isDebugEnabled()){
			log.debug("Entering LogoutServlet.doGet");
		}
		Connection conn = null;
		try {			
			String contextPath = request.getContextPath();
			String eikhome = System.getenv(AuthConstants.EIK_HOME) + "/" + contextPath;
			if (StringUtils.isEmpty(eikhome)) {	
				throw new AuthException("EIK HOME not found");
			}

			String path = eikhome + AuthConstants.CONFIG_PATH;
			if (StringUtils.isEmpty(path)) {	
				throw new AuthException("Config path not found");
			}
			
			Properties config = authService.getClientConfigProps(path);
			if (config == null) {
				throw new AuthException("Config file is missing");
			}
			
			Cookie cookie = new Cookie(authService.getSessionCookieName(), "");
			
			log.info("Cookie value VIS ::" + cookie.getValue());
			log.info("Domain is ::" + cookie.getDomain());
			log.info("Path ::" + cookie.getPath());
			
			cookie.setDomain("");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			
			response.addCookie(cookie);
			response.sendRedirect((String) config.getProperty(AuthConstants.EBS_LOGOUT_PAGE));
		} 
		catch (IOException e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response.sendRedirect("error.jsp?message=Error Encountered");
		} 
		catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response.sendRedirect("error.jsp?message=Error Encountered");
		} 
		finally {
			try {
				conn.close();
			} 
			catch (SQLException e) {
				log.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		if(log.isDebugEnabled()){
			log.debug("Exiting LogoutServlet.doGet");
		}
		return null;
	}

}
