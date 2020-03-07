/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */

package com.likeminds.ebsauth.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import com.likeminds.ebsauth.core.AuthConstants;
import com.likeminds.ebsauth.exception.AuthException;

/**
 * <p> BaseController.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since 2020-02-12
 */


public class BaseController {
	
	private static final Logger log = LogManager.getLogger(BaseController.class);

	public void sendToFinalResponse(HttpServletResponse response, HashMap<String, Object> esDetMap) throws ServletException, IOException, FileNotFoundException {
		try {
			String requestUrl = (String) esDetMap.get(AuthConstants.REQUEST_URL);
			Properties config = (Properties) esDetMap.get(AuthConstants.CONFIG);

			if (esDetMap.get("isUserExistsInEBS").toString().equalsIgnoreCase(AuthConstants.TRUE) && esDetMap.get("userStatus").toString().equalsIgnoreCase("INACTIVE")) {
				log.info("User is Disabled");
				response.sendRedirect("error.jsp?message=User status is INACTIVE and not authorized to access. Please contact your system administrator for assistance.");
			} 
			else if (esDetMap.get("isUserExistsInEBS").toString().equalsIgnoreCase(AuthConstants.TRUE) && esDetMap.get("userStatus").toString().equalsIgnoreCase("ACTIVE")) {
				Cookie cookie = (Cookie) esDetMap.get("sessionCookieObj");
				response.addCookie(cookie);
				if (StringUtils.isEmpty(requestUrl)) {
					log.info("Processing Request URL ");
					response.sendRedirect(requestUrl);
				} 
				else {
					log.info("Processing Target Resource URL ");
					if (config == null) {
						throw new AuthException("Config value is null");
					}
					response.sendRedirect((String) config.getProperty(AuthConstants.EBS_LANDING_PAGE));
				}
			} 
			else if (esDetMap.get("isUserExistsInEBS").toString().equalsIgnoreCase(AuthConstants.FALSE) && esDetMap.get("jit").toString().equalsIgnoreCase("false")) {
				log.info("Error Encounted : User not found");
				response.sendRedirect("error.jsp?message=User not found or not authorized to access. Please contact your system administrator for assistance.");
			} 
			else if (esDetMap.get("isUserExistsInEBS").toString().equalsIgnoreCase(AuthConstants.FALSE)
					&& esDetMap.get("LicType").toString().equalsIgnoreCase("trial")
					&& esDetMap.get("jit").toString().equalsIgnoreCase("true")) {
				log.info("Error Encounted : Trial License found Just in time provisioning cannot be processed");
				response.sendRedirect("error.jsp?message=Your are using Trial License. Please purchase full version to enable Just in time Provisioning.");
			}
		} 
		catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			response.sendRedirect("error.jsp?message=Oops!! System error occured. Please contact your administrator");
		}
	}
}
