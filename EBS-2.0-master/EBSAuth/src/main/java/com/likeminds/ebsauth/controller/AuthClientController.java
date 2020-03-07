/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */
package com.likeminds.ebsauth.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
 * <p> AuthClientController.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since 2020-02-12
 */

@RequestMapping("handler")
@Controller
public class AuthClientController extends BaseController{
	
	private static final Logger log = LogManager.getLogger(AuthClientController.class);
	
	@Autowired
	private AuthService authService;
	
	
	@GetMapping
	public String doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(log.isDebugEnabled()){
			log.debug("Entering AuthClientController.doGet");
		}
		try {
			String code = request.getParameter(AuthConstants.CODE);
			if(StringUtils.isEmpty(code)) {
				throw new AuthException("Code value not found");
			}
			
			HttpSession session = request.getSession();
			
			String requestUrl = (String) session.getAttribute(AuthConstants.REQUEST_URL);
			Properties config = (Properties) session.getAttribute(AuthConstants.CONFIG);
			String licType = (String) session.getAttribute("LicType");			
			
			if (config == null) {
				throw new AuthException("Config value is null");
			}
			
			String authnUser = authService.getOIDCAuthNUser(code, config);
			if (StringUtils.isEmpty(authnUser)) {
				throw new AuthException("Authenticated user value is NULL");
			}
			
			log.info(" The Authenticated User is = " + authnUser);
			
			HashMap<String, Object> ebsSession = authService.createEBSSession(authnUser, config, licType);
			if (ebsSession == null || ebsSession.isEmpty()) {
				throw new AuthException("Error in Session creation : no value found");
			}
			ebsSession.put("requestUrl", requestUrl);
			sendToFinalResponse(response, ebsSession);
		} 
		catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response.sendRedirect("error.jsp?message=Oops!! System error occured. Please contact your administrator");
		} 
		if(log.isDebugEnabled()){
			log.debug("Exiting AuthClientController.doGet");
		}
		return null;
	}

}
