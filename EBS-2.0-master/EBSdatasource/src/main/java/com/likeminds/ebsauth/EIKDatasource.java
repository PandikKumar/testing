/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Like Minds Consulting Inc or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 
package com.likeminds.ebsauth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>EIKDatasource.java</p>
 * 
 * This class responsible to generate configuration file for database connectivity used by EBS Adaptor.
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class EIKDatasource implements Serializable,ConstantsIF {
	private static transient final Logger LOGGER = LogManager.getLogger(EIKDatasource.class.getName());

	private static final long serialVersionUID = 1L;
	private final transient static String DEFAULT_DBCX_FILE_LOCATION = "/eik.dbcx";
	private final transient static String PROPERTY_DBCX_FILE_LOCATION = "eik.home";
	private final transient static Locale locale = new Locale("en", "US");
	private final transient static ResourceBundle labels = ResourceBundle.getBundle("elkbundle", locale);
	
	/* Map which will hold all required properties */
	private HashMap<String, Object> confMap = null;

	/**
	 * Constructor which read the set the Map
	 * @param confMap
	 */
	public EIKDatasource(HashMap<String, Object> confMap) {
		this.confMap = confMap;
	}

	/**
	 * Getter method to return the hash map.
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getConfMap() {
		return confMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DBConnectionValidator dbConnectionValidator = null;
		String filename = getConfigWritePath();
		LOGGER.info(MESSAGE_FILE_PATH,filename);
		try {
			
			Scanner scanner = new Scanner(System.in);  
			String domain = getUserInputFromPrompt(scanner,labels.getString(DOMAIN_PROMPT));
			String driver = getUserInputFromPrompt(scanner,labels.getString(DATABASE_DRIVER_PROMPT));
			String url = getUserInputFromPrompt(scanner,labels.getString(JDBC_URL_PROMPT));
			String uname = getUserInputFromPrompt(scanner,labels.getString(APPS_USERNAME_PROMPT));
			String pass =  getUserInputFromPrompt(scanner,labels.getString(APPS_PASSWORD_PROMPT));
			String Eikuname = getUserInputFromPrompt(scanner,labels.getString(EIK_USERNAME_PROMPT));
			String Eikpass = getUserInputFromPrompt(scanner,labels.getString(EIK_PASSWORD_PROMPT));
			String contextPath = getUserInputFromPrompt(scanner,labels.getString(CONTEXT_PATH_PROMPT));
			scanner.close();
			
			dbConnectionValidator = new DBConnectionValidator();
			dbConnectionValidator.getDBConnection(driver, url, uname, pass);
			int result = dbConnectionValidator.executeQuery(Eikuname);
			
			if (result > 0) {
				LOGGER.info(MESSAGE_USER_REGISTERING_MESSAGE);
				
				HashMap<String, Object> confMap = new HashMap<String, Object>();
				confMap.put("domain", domain);
				confMap.put("driver", driver);
				confMap.put("url", url);
				confMap.put("uname", uname);
				confMap.put("upass", pass);
				confMap.put("RBACuser", Eikuname);
				confMap.put("RBACpass", Eikpass);

				
				EIKDatasource eikDS = new EIKDatasource(confMap);

				/* Serialize EIKDatasource */
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(eikDS);
				oos.close();
				
				Base64.Encoder encoder = Base64.getEncoder();
				String SerEnc = encoder.encodeToString(baos.toByteArray());

				Properties properties = new Properties();
				properties.setProperty(PROPERTY_APPLICATION_CONNECTION_ID, SerEnc);
				properties.setProperty(PROPERTY_JDBC_DRIVER, driver);
				properties.setProperty(PROPERTY_JDBC_URL, url);
				properties.setProperty(PROPERTY_RBAC_USER, Eikuname);
				properties.setProperty(PROPERTY_RBAC_PASS, encoder.encodeToString(Eikpass.getBytes()));
				properties.setProperty(PROPERTY_CONTEXT_URL, contextPath);
				
				File file = new File(filename);
				FileOutputStream fileOut = new FileOutputStream(file);
				properties.store(fileOut, "");
				fileOut.close();

				LOGGER.info(MESSAGE_USER_REGISTERING_SUCCESS_MESSAGE);
			} else {
				LOGGER.error(MESSAGE_USER_REGISTERING_FAILURE_MESSAGE);
			}
			LOGGER.info(MESSAGE_PROCESS_SUCCESSFULLY_COMPLETED);
		}
		catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
		} finally {
			if (dbConnectionValidator != null) {
				dbConnectionValidator.releaseConnection();
			}
		}
		
	}
	
	/**
	 * This method reads written path where to create EIK DBCX file.
	 * @return
	 */
	private static String getConfigWritePath() {
		String dbcxLocation = System.getenv(PROPERTY_DBCX_FILE_LOCATION);
		if (dbcxLocation == null || "".equalsIgnoreCase(dbcxLocation)) {
			dbcxLocation = System.getProperty(PROPERTY_DBCX_FILE_LOCATION);
		}
		
		if (dbcxLocation == null || "".equalsIgnoreCase(dbcxLocation)) {
			dbcxLocation = DEFAULT_DBCX_FILE_LOCATION;
		} else {
			dbcxLocation = dbcxLocation + DEFAULT_DBCX_FILE_LOCATION;
		}
		return dbcxLocation;
	}

	/**
	 * This method reads written path where to create EIK DBCX file.
	 * @return
	 */
	private static String getUserInputFromPrompt(Scanner scanner,String promptName) {
		System.out.print(promptName);
		String value = scanner.nextLine();
		System.out.println(MESSAGE_USER_ENTERED_VALUE + value);
		return value;
	}

}
