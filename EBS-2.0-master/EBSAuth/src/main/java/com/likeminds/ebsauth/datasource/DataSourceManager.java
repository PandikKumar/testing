/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */ 
package com.likeminds.ebsauth.datasource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.PooledConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.likeminds.ebsauth.core.AuthConstants;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

/**
 * <p>DataSourceManager.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
@Component
public class DataSourceManager {
	
	private static final Logger log = LogManager.getLogger(DataSourceManager.class.getName());
	
	private DataSourceConfig dataSourceConfig = null;
	
	@Value("${eik.home}")
	private String eikHome;
	
	private PooledConnection pc;

	@PostConstruct
	private PooledConnection createConnection() {
		OracleConnectionPoolDataSource ocpds = null;
		log.debug("Inside Creating connection Pool");
		try {
			Map<String, Object> connectionMap = this.deserializeDS();
			// Connection properties
			Properties prop = new Properties();
			prop.setProperty("MinLimit", "5"); // the cache size is 5 at least
			prop.setProperty("MaxLimit", "25");
			prop.setProperty("InitialLimit", "10"); // create 3 connections at startup
			prop.setProperty("InactivityTimeout", "1800"); // seconds
			prop.setProperty("AbandonedConnectionTimeout", "900"); // seconds
			prop.setProperty("MaxStatementsLimit", "10");
			prop.setProperty("PropertyCheckInterval", "60"); // seconds

			String url = (String) connectionMap.get("url");
			String uname = (String) connectionMap.get("uname");
			String upass = (String) connectionMap.get("upass");
			String encodepass = URLEncoder.encode(upass, "UTF-8");
			String decodepass = URLDecoder.decode((String) encodepass, "UTF-8");
			ocpds = new OracleConnectionPoolDataSource();
			ocpds.setConnectionProperties(prop);
			ocpds.setURL(url);
			ocpds.setUser(uname);
			ocpds.setPassword(decodepass);
			pc = ocpds.getPooledConnection();
			
		} 
		catch (Exception e) {
			log.error("Exception occured in Connection Pooling", e.getLocalizedMessage());
		}
		log.debug("Exiting Creating connection Pool");
		return pc;
	}

	public PooledConnection getConnection() throws SQLException, ClassNotFoundException, IOException {
		log.debug("Inside getting pooled connection");
		return pc;
	}

	public int roleDecesion(Connection conn) throws ClassNotFoundException, SQLException, IOException {
		log.debug("Inside Checking Role Decision");
		int val = 0;
		try {
			Map<String, Object> connectionMap = this.deserializeDS();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			pstmt = conn.prepareStatement(AuthConstants.ROLE_CHECK);
			pstmt.setString(1, ((String) connectionMap.get(AuthConstants.RBAC_USER)).toUpperCase());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				val = rs.getInt(1);
				log.info("EIKUSER count :" + val);
			}

		} catch (SQLException sql) {
			conn.close();
			log.error("Sql Exception occured in Role Decision", sql.getLocalizedMessage());

		} catch (Exception e) {
			log.error("Exception occured in getting User role", e.getLocalizedMessage());
		} finally {
			conn.close();
		}
		log.debug("Exiting Checking Role Decision");
		return val;

	}

	public void reset_session(int p_session_id, Connection conn) {
		log.debug("Inside reset session");
		try {
			CallableStatement stmt = conn.prepareCall(AuthConstants.RESET_SESSION);
			stmt.setInt(1, p_session_id);
			stmt.executeUpdate();
		} catch (SQLException se) {
			log.error("SQL Error occured in Resetting Session", se.getLocalizedMessage());
		} catch (Exception e) {
			log.error("Exception occured in Resetting Session", e.getLocalizedMessage());
		}
		log.debug("Exiting Reset Session");
	}

	public HashMap<String, Object> deserializeDS() {
		HashMap<String, Object> connectionMap = new HashMap<String,Object>();
		Properties config = null;
		try {
			log.debug("Inside DBCX method");
			// Reading the object from a file
			log.debug("Reading dbcx file from path : " + this.eikHome + AuthConstants.DBCX_PATH);
			config = this.getClientConfigProps(this.eikHome + AuthConstants.DBCX_PATH);

			String serbase64 = config.getProperty(AuthConstants.APPL_CONN_ID);
			byte[] data = Base64.getDecoder().decode(serbase64);
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));

			// Method for deserialization of object
			dataSourceConfig = (DataSourceConfig) in.readObject();
			connectionMap = dataSourceConfig.getConfMap();
			in.close();
		} catch (FileNotFoundException fnf) {
			log.error("File not found Exception occured for DBCX file", fnf.getLocalizedMessage());
		} catch (IOException ex) {
			log.error("Config File not found ", ex.getLocalizedMessage());
		} catch (Exception e) {
			log.error("Unknow Error Occured ", e.getLocalizedMessage());
		}
		log.debug("Exiting DBCX method");
		return connectionMap;
	}

	public String getxid(int sid, Connection conn) throws SQLException {
		log.debug("Inside getting XID");
		String user = null;
		try {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			pstmt = conn.prepareStatement(AuthConstants.XID);
			pstmt.setInt(1, sid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				user = rs.getString(1);
				log.info("XSID : " + " " + user);
			}
		} 
		catch (SQLException se) {
			log.error("SQL Error occured in Getting XID", se.getLocalizedMessage());
		} 
		catch (Exception e) {
			log.error("Error occured in Getting XID : " + e.getLocalizedMessage());
		}
		log.debug("Exiting getting XID");
		return user;
	}

	public int getUserID(String uname, Connection conn) throws SQLException {
		log.debug("Entering get user id ");
		int uid = 0;
		try {
			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;
			pstmt1 = conn.prepareStatement(AuthConstants.USER_ID);
			pstmt1.setString(1, uname.toUpperCase());
			rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				uid = rs1.getInt(1);
				log.info("UID = " + " " + uid);
			}

		} 
		catch (SQLException se) {
			log.error("SQL Exception Occured in Getting USER ID", se.getLocalizedMessage());
		} 
		catch (Exception e) {
			log.error("Error occured Getting USER ID", e.getLocalizedMessage());
		}
		log.debug("Exiting geting user id ");
		return uid;
	}

	public boolean isUserExistsInEBS(String uname, Connection conn) throws NullPointerException, SQLException {
		log.debug("Entering is User Exists In EBS ");
		boolean userExists = false;
		int returnCount = 0;
		try {
			PreparedStatement pstmt1 = null;
			ResultSet rs1 = null;
			pstmt1 = conn.prepareStatement(AuthConstants.USER_EXIST);
			pstmt1.setString(1, uname.toUpperCase());
			rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				returnCount = rs1.getInt(1);
				log.info("returnCount = " + " " + returnCount);
				if (returnCount > 0) {
					userExists = true;
					log.debug("user exists");
				} else {
					log.info("user does not exist");
				}
			}
		} 
		catch (SQLException se) {
			log.error("SQL Error occured in Verifying User Existance", se.getLocalizedMessage());
		} 
		catch (Exception e) {
			log.error("Error occured in Verifying User Existance ", e.getLocalizedMessage());
		}
		log.debug("Exiting is User Exists In EBS ");
		return userExists;
	}

	public String userStatus(String uname, Connection conn) throws NullPointerException, SQLException {
			log.debug("Getting User Status");
			PreparedStatement pstmt = null;			
			String Status = null;
			try {
				ResultSet rs = null;
				pstmt = conn.prepareStatement("SELECT \r\n" + 
						"  CASE\r\n" + 
						"    WHEN end_date IS NULL  OR end_date > sysdate\r\n" + 
						"    THEN 'ACTIVE'\r\n" + 
						"    ELSE 'INACTIVE'\r\n" + 
						"  END user_status\r\n" + 
						"FROM apps.fnd_user\r\n" + 
						"where USER_NAME = ?");
				pstmt.setString(1, uname.toUpperCase());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					Status = rs.getString("USER_STATUS");
					log.debug("User Status : " + Status);
				}
				
			}
			catch(SQLException sql) {
				log.debug("Exception occured getting User Status" + sql.getLocalizedMessage());
			}
			catch(Exception e) {
				log.debug("Exception occured getting User Status" + e.getLocalizedMessage());
			}
			return Status;
	}
	
	public int create_session(int user_id, Connection conn) throws SQLException {
		log.debug("Entering Creating session");
		int result = 0;
		try {
			String plSql = AuthConstants.CREATE_SESSION;
			CallableStatement stmt = conn.prepareCall(plSql);
			stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setInt(2, user_id);
			stmt.setString(3, AuthConstants.CREATE_SESSION_CODE);
			stmt.setNull(4, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setString(5, null);
			stmt.setString(6, null);
			stmt.setString(7, AuthConstants.LANG);
			stmt.setNull(8, oracle.jdbc.OracleTypes.NUMBER);
			stmt.execute();

			result = stmt.getInt(1);
			log.info("Session ID : " + result);

		} 
		catch (SQLException se) {
			log.error("SQL Error occured in Creating Session", se.getLocalizedMessage());
		} 
		catch (Exception e) {
			log.error("Error occured in Creating Session", e.getLocalizedMessage());
		}
		log.debug("Exiting Creating session");
		return result;
	}

	public String getSessionCookieName(Connection conn) throws SQLException {
		log.debug("Entering getting cookie name");
		String sessionCookieName = null;
		try {
			String plSql = AuthConstants.COOKIE_NAME;
			CallableStatement stmt = conn.prepareCall(plSql);
			stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.VARCHAR);
			stmt.execute();
			sessionCookieName = stmt.getString(1);
			log.info("Session Cookie Name : " + sessionCookieName);

		} 
		catch (SQLException se) {
			log.error("SQL Error occured in Getting Cookie Name", se.getLocalizedMessage());
		} 
		catch (Exception e) {
			log.error("Error occured Getting Cookie Name", e.getLocalizedMessage());
		}
		log.debug("Exiting getting cookie name");
		return sessionCookieName;
	}

	public Properties getClientConfigProps(String pfconfig) {
		log.debug("Entering Getting properties");
		Properties config = new Properties();
		try {
			InputStream is = new FileInputStream(pfconfig);
			config.load(is);
			is.close();
		} catch (FileNotFoundException fnf) {
			log.error("Config File not found ", fnf.getLocalizedMessage());

		} 
		catch (IOException ioe) {
			log.error("IO Exception occured in Getting file", ioe.getLocalizedMessage());
		} 
		catch (Exception e) {
			log.error("Error occured in Getting file", e.getLocalizedMessage());
		}
		log.debug("Exiting Getting properties");
		return config;
	}

	public void createuser(String user_name, String password, Connection conn) throws SQLException {
		log.debug("Entering Creating user");
		try {

			String plSql = AuthConstants.CREATE_USER;
			CallableStatement stmt = conn.prepareCall(plSql);
			stmt.setString(1, user_name);
			stmt.setNull(2, oracle.jdbc.OracleTypes.VARCHAR);
			stmt.setString(3, password);
			stmt.setInt(4, 0);
			stmt.setNull(5, oracle.jdbc.OracleTypes.DATE);
			stmt.setNull(6, oracle.jdbc.OracleTypes.DATE);
			stmt.setNull(7, oracle.jdbc.OracleTypes.DATE);
			stmt.setNull(8, oracle.jdbc.OracleTypes.VARCHAR);
			stmt.setNull(9, oracle.jdbc.OracleTypes.DATE);
			stmt.setNull(10, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setNull(11, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setNull(12, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setNull(13, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setNull(14, oracle.jdbc.OracleTypes.VARCHAR);
			stmt.setNull(15, oracle.jdbc.OracleTypes.VARCHAR);
			stmt.setNull(16, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setNull(17, oracle.jdbc.OracleTypes.NUMBER);
			stmt.setNull(18, oracle.jdbc.OracleTypes.RAW);
			stmt.setNull(19, oracle.jdbc.OracleTypes.NUMBER);

			stmt.executeUpdate();

		} 
		catch (SQLException se) {
			log.error("SQL Error occured While Creating User", se.getLocalizedMessage());
		} 
		catch (Exception e) {
			log.error("Error occured while Creating User", e.getLocalizedMessage());
		}
		log.debug("Exiting Creating user");

	}

	public PublicKey getPublicKey(String encpubjwt) {
		log.debug("Entering Getting Public key");
		PublicKey publicKey = null;
		try {
			byte[] data = Base64.getDecoder().decode(encpubjwt);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(data);
			publicKey = keyFactory.generatePublic(publicKeySpec);
		} 
		catch (Exception e) {
			log.error("Error occured while getting Key for License", e.getLocalizedMessage());
		}
		log.debug("Exiting Getting Public key");
		return publicKey;
	}

	public JwtConsumer validateLicJWT(PublicKey publicKey) {
		log.debug("Entering Validating JWT");
		JwtConsumer jwtConsumer = null;
		try {
			jwtConsumer = new JwtConsumerBuilder().setRequireSubject().setExpectedIssuer("Likeminds")
					.setExpectedAudience("Audience").setVerificationKey(publicKey).build();
		} 
		catch (Exception e) {
			log.error("Exception occured while Validating JWT", e.getLocalizedMessage());
		}
		log.debug("Exiting Validating JWT");
		return jwtConsumer;
	}

	public HashMap<String, Object>Licedate(Properties licConfig) {
		log.debug("Inside License date");
		HashMap<String, Object> licDateMap = new HashMap<String, Object>();
		String expdate = null;
		String LicType = null;
		try {
			String jsonencoded = licConfig.getProperty(AuthConstants.LIC_KEY);
			String pubkey = licConfig.getProperty(AuthConstants.LICENSE_ID);
			PublicKey pbk = this.getPublicKey(pubkey);
			JwtConsumer jwtConsumer = this.validateLicJWT(pbk);
			JwtClaims jwtClaims = jwtConsumer.processToClaims(jsonencoded);
			
			LicType = (String) jwtClaims.getClaimValue("Licensetype");
			String encoded = (String) jwtClaims.getClaimValue(AuthConstants.LICENSE);
			String decode = encoded.substring(encoded.indexOf(AuthConstants.INDEX_OF) + 1);
			byte[] actualByte = java.util.Base64.getDecoder().decode(decode);
			expdate = new String(actualByte);
			licDateMap.put("expdate", expdate);
			licDateMap.put("LicType", LicType);
		} catch (Exception e) {
			log.error("Exception occured while getting Lic exp date", e.getLocalizedMessage());
		}
		log.debug("Exiting License date");
		return licDateMap;
	}
	
	
}
