/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */
package com.likeminds.ebsauth.controller;

import java.io.FileNotFoundException;
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
 * <p> AuthController.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since 2020-02-12
 */

@RequestMapping("ssologin")
@Controller
public class AuthController extends BaseController{
	
	private static final Logger log = LogManager.getLogger(AuthController.class);
	
	@Autowired
	private AuthService authService;
	
	@GetMapping
	public String doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("AuthController.doLogin Entering");
		}
		try {
			String contextPath = request.getContextPath();
			if (StringUtils.isEmpty(contextPath)) {
				throw new AuthException("Context path not found");
			}
	
			HashMap<String, Object> licDesMap = authService.getLicenseDecision(contextPath);
			if (licDesMap == null) {
				throw new AuthException("LicDes Map value is null");
			}
	
			Properties config = (Properties) licDesMap.get(AuthConstants.CONFIG);
			if (config == null) {
				throw new AuthException("Config value is null");
			}
	
			String licType = (String) licDesMap.get("LicType");
			boolean isLICValid = (boolean) licDesMap.get(AuthConstants.LIC_VALID);
	
			if (isLICValid) {
				log.debug("License is Valid and proceeding");
				String requestUrl = request.getParameter(AuthConstants.REQUEST_URL);
				String authType = (String) config.getProperty(AuthConstants.AUTH_TYPE);
				log.info("Authentication type : " + authType);
	
				if (authType.equalsIgnoreCase(AuthConstants.OIDC)) {
					log.debug("Inside OIDC flow");
					HttpSession session = request.getSession();
					session.setAttribute(AuthConstants.CONFIG, config);
					session.setAttribute("LicType", licType);
					if (requestUrl != null && requestUrl != "") {
						log.info("Request url found");
						log.debug("Request URL : " + requestUrl);
						session.setAttribute(AuthConstants.REQUEST_URL, requestUrl);
					}
					String authCodeURI = authService.constructAuthCodeUri(config);
					if (StringUtils.isEmpty(authCodeURI)) {
						throw new AuthException("No AuthCode uri found");
					}
					response.sendRedirect(authCodeURI);
				} else {
					log.debug("Inside HEADER flow");
					String UNAME = request.getHeader(AuthConstants.HAEDER_USER_NAME);
	
					if (StringUtils.isEmpty(UNAME)) {
						throw new AuthException("No Authenticated user found");
					}
					log.info(" The Authenticated User is = " + UNAME);
	
					HashMap<String, Object> esDetMap = authService.createEBSSession(UNAME, config, licType);
					if (esDetMap == null) {
						throw new AuthException("Error in Session creation no value found");
					}
					esDetMap.put("requestUrl", requestUrl);
					sendToFinalResponse(response, esDetMap);
				}
			} 
			else {
				log.error("License not valid/Expired");
				response.sendRedirect("error.jsp?message=License has expired. Please contact your system administrator for assistance.");
			}
		}
		catch(FileNotFoundException e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response.sendRedirect("error.jsp?message=file not found. Please contact your system administrator for assistance.");
		}
		catch(Exception e){
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response.sendRedirect("error.jsp?message=Oops!! System error occured. Please contact your administrator");
		}
		if(log.isDebugEnabled()){
			log.debug("AuthController.doLogin Exiting");
		}
		return null;
	}
	
}
