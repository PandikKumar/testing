/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */ 
package com.likeminds.ebsauth.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.sql.PooledConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.likeminds.ebsauth.core.AuthConstants;
import com.likeminds.ebsauth.core.AuthDetails;
import com.likeminds.ebsauth.core.AuthUtil;
import com.likeminds.ebsauth.datasource.DataSourceManager;
import com.likeminds.ebsauth.exception.AuthException;

/**
 * <p>EBSAuthService.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class AuthService {
	
	private static final Logger logger = LogManager.getLogger(AuthService.class.getName());
	
	private Base64.Decoder decoder = Base64.getDecoder();
	
	@Autowired
	private DataSourceManager dataSourceManager;
	
	@Autowired
	private AuthUtil authutils;
	
	public HashMap<String, Object> createEBSSession(String userName, Properties config, String LicType) throws ClassNotFoundException, SQLException, IOException, AuthException {
		logger.debug("Entering Creating EBS Session");
		
		boolean isUserExistsInEBS = false;
		String userStatus;
		int sessionID = 0;
		Connection conn = null;
		String isJIT = null;
		HashMap<String, Object> esDetMap = null;
		HashMap<String, Object> conMap = null;
		PooledConnection pc = null;
		try {
			esDetMap = new HashMap<String, Object>();

//			conMap = this.dataSourceService.deserializeDS();
//			this.dataSourceService.setConnectionMap(conMap);
			
			pc = this.dataSourceManager.getConnection();
			conn = pc.getConnection();
			
			if (conn == null) {
				throw new AuthException("Database connection failed");
			}
			int EIKdecesion = this.dataSourceManager.roleDecesion(conn);
			if (EIKdecesion > 0) {
				logger.info("USER role Enabled");
				isJIT = config.getProperty(AuthConstants.JIT);
				esDetMap.put(AuthConstants.IS_USER_EXISTS, AuthConstants.TRUE);
				isUserExistsInEBS = this.dataSourceManager.isUserExistsInEBS(userName, conn);
				logger.info("User Existance in EBS : " + isUserExistsInEBS);				
				if ((!isUserExistsInEBS) && (isJIT.equalsIgnoreCase(AuthConstants.TRUE)) && LicType.equalsIgnoreCase("production")) {
				//	esDetMap.put("LicType", LicType);
					this.dataSourceManager.createuser(userName, AuthConstants.USER_CREATION_PASS, conn);
					logger.debug("Creating " + userName + " user");
					isUserExistsInEBS = true;
				} 
				else if ((!isUserExistsInEBS) && (isJIT.equalsIgnoreCase(AuthConstants.TRUE)) && LicType.equalsIgnoreCase("trial")) {
					
		//			throw new EBSEikException("Your are using Trial License. Please purchase full version to enable Just in time Provisioning");					
					esDetMap.put(AuthConstants.IS_USER_EXISTS, AuthConstants.FALSE);
					esDetMap.put("LicType", LicType);
					esDetMap.put("jit", isJIT);
					esDetMap.put("userStatus", "INACTIVE");
					logger.info("Your are using Trial License. Please purchase full version to enable Just in time Provisioning");
					HashMap<String, Object> localHashMap1 = esDetMap;
					return localHashMap1;
				}  
				else if ((!isUserExistsInEBS) && (isJIT.equalsIgnoreCase(AuthConstants.FALSE))) {
					
					esDetMap.put(AuthConstants.IS_USER_EXISTS, AuthConstants.FALSE);
					esDetMap.put("jit", isJIT);
					esDetMap.put("userStatus", "INACTIVE");
					esDetMap.put("LicType", LicType);
					logger.info("User Does not Exists");
					HashMap<String, Object> localHashMap1 = esDetMap;
					return localHashMap1;
				}
				if ((isUserExistsInEBS) )
				{
					userStatus = this.dataSourceManager.userStatus(userName, conn);
					if(userStatus.equalsIgnoreCase("INACTIVE"))
					{
						esDetMap.put("userStatus", userStatus);
						esDetMap.put("LicType", LicType);
						esDetMap.put("jit", isJIT);
						HashMap<String, Object> localHashMap1 = esDetMap;
						return localHashMap1;
					}
				
				
				sessionID = this.dataSourceManager.create_session(this.dataSourceManager.getUserID(userName, conn), conn);

				String xSid = this.dataSourceManager.getxid(sessionID, conn);

				String sessionCookieName = this.dataSourceManager.getSessionCookieName(conn);

				Cookie cookie = new Cookie(sessionCookieName, xSid);
				cookie.setDomain((String) config.get(AuthConstants.ICX_COOKIE_DOMAIN));
				cookie.setPath((String) config.get(AuthConstants.ICX_COOKIE_PATH));
				cookie.setHttpOnly(true);
				// cookie.setSecure(true);
				esDetMap.put("xSid", xSid);
				esDetMap.put("sessionCookieName", sessionCookieName);
				esDetMap.put("sessionCookieObj", cookie);
				esDetMap.put("config", config);	
				esDetMap.put("LicType", LicType);
				esDetMap.put("userStatus", userStatus);
				conn.close();
				}
			} else if (EIKdecesion < 0 || EIKdecesion == 0) {
				logger.info("USER role not found for Database transaction ");
				throw new AuthException("USER role not found for Database transaction");
			}
		} catch (ClassNotFoundException cnf) {
			logger.error("ClassNotFoundException occured while creating Session", cnf.getLocalizedMessage());
			conn.close();
		} catch (NullPointerException npe) {
			logger.error("Null pointer Exception occured while creating Session", npe.getLocalizedMessage());
			conn.close();
		} catch (IOException io) {
			logger.error("IO Exception occured while creating Session", io.getLocalizedMessage());
			conn.close();
		} catch (SQLException sql) {
			logger.error("SQL Error occured while creating Session", sql.getLocalizedMessage());
			conn.close();
		} catch (Exception e) {
			logger.error("Error occured while closing Connection", e.getLocalizedMessage());
			conn.close();
		} finally {
			conn.close();
		}
		logger.debug("Exiting EBS Session creation");
		return esDetMap;

	}

	public HashMap<String, Object> getLicenseDecision(String contextPath) throws ParseException {
		HashMap<String, Object> licDesMap = new HashMap<String, Object>();
		logger.debug("Entering License Decision");
		try {
			String eikhome = System.getenv(AuthConstants.EIK_HOME) + "/" + contextPath;
			if (StringUtils.isEmpty(eikhome)) {
				throw new AuthException("EIK HOME not found");
			}
			String licpath = eikhome + AuthConstants.LIC_PATH;
			if (StringUtils.isEmpty(licpath)) {
				throw new AuthException("License Path not found");
			}
			String path = eikhome + AuthConstants.CONFIG_PATH;
			if (StringUtils.isEmpty(path)) {
				throw new AuthException("Config path not found");
			}
			logger.debug("Getting Lic Config from Path : " + licpath);
			Properties licConfig = dataSourceManager.getClientConfigProps(licpath);
			if (licConfig.isEmpty() || licConfig.equals(null)) {
				throw new AuthException("License file is missing");
			}
			HashMap<String, Object> licDateMap = dataSourceManager.Licedate(licConfig);
			String expdate = (String) licDateMap.get("expdate");
			String LicType = (String) licDateMap.get("LicType");
	
			SimpleDateFormat dateFormat = new SimpleDateFormat(AuthConstants.DATE_FORMAT);
			Date date = new Date();
			String sysdate = dateFormat.format(date);

			Date date1 = dateFormat.parse(expdate);
			Date date2 = dateFormat.parse(sysdate);
			logger.info("EIK License Expiry Date: " + dateFormat.format(date1));
			long diff = date1.getTime() - date2.getTime();			 
			int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
			if(diffDays < 31)
			{
			logger.warn("Your EIK License will expire in " + diffDays + " days");
			}
			logger.debug("Getting EIK Config from Path : " + path);
			Properties config = dataSourceManager.getClientConfigProps(path);
			if (config.isEmpty() || config.equals(null)) {
				throw new AuthException("Config file is missing");
			} else {
				String clientid = config.getProperty(AuthConstants.CLIENT_ID);
				String clientsecret = config.getProperty(AuthConstants.CLIENT_SECRET);
				String decodedClientId = new String(decoder.decode(clientid));
				String decodedClientSecret = new String(decoder.decode(clientsecret));
				config.setProperty("eikhome", eikhome);
				config.setProperty(AuthConstants.CLIENT_ID, decodedClientId);
				config.setProperty(AuthConstants.CLIENT_SECRET, decodedClientSecret);
				licDesMap.put("config", config);
				licDesMap.put("LicType", LicType);
			}
			if ((date1.after(date2)) || (date1.equals(date2))) {
				logger.info("License Valid");
				licDesMap.put("licValid", Boolean.valueOf(true));
			} else if (date1.before(date2)) {
				logger.info("License not valid/Expired");
				licDesMap.put("licValid", Boolean.valueOf(false));
			} else {
				licDesMap.put("licValid", Boolean.valueOf(false));
			}
		} catch (ParseException pe) {
			logger.error("Please enter the valid License", pe.getLocalizedMessage());
		} catch (IllegalArgumentException iae) {
			logger.error("Please enter the valid License", iae.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Error Encountered validatig License", e.getLocalizedMessage());
			e.printStackTrace();
		}
		logger.debug("Exiting License Decision");
		return licDesMap;
	}

	public String constructAuthCodeUri(Properties config) {
		logger.debug("Entering Construction AuthCode URI");
		String authCodeUri = null;
		try {
			authCodeUri = authutils.buildAuthCodeUri(config);
		} catch (NullPointerException npe) {
			logger.error("Null pointer Exception in Authcode URI", npe.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Exception occured in Authcode URI", e.getLocalizedMessage());
		}
		logger.debug("Exiting Construction AuthCode URI");
		return authCodeUri;
	}

	public String getOIDCAuthNUser(String code, Properties config) throws Exception {
		logger.debug("Entering Getting Authn User");
		String authnUser = null;
		try {
			AuthDetails oauthDetails = authutils.createOAuthDetails(config);
			List<String> invalidProps = authutils.validateInput(oauthDetails);
			if ((invalidProps != null) && (invalidProps.size() == 0)) {
				if (!StringUtils.isEmpty(code)) {
					Map<String, String> map = authutils.getAccessToken(oauthDetails, code);
					String idTok = (String) map.get(AuthConstants.ID_TOKEN);
					boolean isValidJWT = authutils.validateJWT(idTok, config);
					logger.info("JWT validation response is :" + isValidJWT);
					Map<String, String> payloadMap = authutils.decodeJWT(idTok);
					authnUser = (String) payloadMap.get(AuthConstants.SUB);
					if (authnUser == null) {
						throw new NullPointerException("Authenticated username value is null");
					}
					logger.info(" The Authenticated User is : " + authnUser);
				}
			} else {
				throw new Exception();
			}
		} catch (NullPointerException npe) {
			logger.error("Null pointer Exception in get OIDC Authn User", npe.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Exception occured in get OIDC Authn User", e.getLocalizedMessage());
		}
		logger.debug("Exiting Getting Authn User");
		return authnUser;
	}
	
	public String getSessionCookieName() throws SQLException {
		logger.debug("Entering getting cookie name");
		String sessionCookieName = null;
		PooledConnection pc = null;
		try {
			pc = dataSourceManager.getConnection();
			Connection conn = pc.getConnection();
			String plSql = AuthConstants.COOKIE_NAME;
			CallableStatement stmt = conn.prepareCall(plSql);
			stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.VARCHAR);
			stmt.execute();
			sessionCookieName = stmt.getString(1);
			logger.info("Session Cookie Name : " + sessionCookieName);

		} 
		catch (SQLException se) {
			logger.error("SQL Error occured in Getting Cookie Name", se.getLocalizedMessage());
		} 
		catch (NullPointerException npe) {
			logger.error("NullPointer Exception occured in Getting Cookie Name", npe.getLocalizedMessage());
		} 
		catch (Exception e) {
			logger.error("Error occured Getting Cookie Name", e.getLocalizedMessage());
		}
		finally{
			if(pc != null){
				pc.close();
			}
		}
		logger.debug("Exiting getting cookie name");
		return sessionCookieName;
	}
	
	public Properties getClientConfigProps(String pfconfig) {
		logger.debug("Entering Getting properties");
		Properties config = new Properties();
		try {
			InputStream is = new FileInputStream(pfconfig);
			config.load(is);
			is.close();
		} catch (FileNotFoundException fnf) {
			logger.error("File not found ", fnf.getLocalizedMessage());

		} catch (IOException ioe) {
			logger.error("IO Exception occured in Getting file", ioe.getLocalizedMessage());
		} catch (NullPointerException npe) {
			logger.error("NullPointer Exception occured in Getting file", npe.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("Error occured in Getting file", e.getLocalizedMessage());
		}
		logger.debug("Exiting Getting properties");
		return config;
	}
}