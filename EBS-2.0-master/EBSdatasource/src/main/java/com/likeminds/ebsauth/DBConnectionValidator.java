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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>DBConnectionValidator.java</p>
 * 
 * This class will be used to validate the DB connectivity.
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class DBConnectionValidator implements ConstantsIF {
	private static final Logger LOGGER = LogManager.getLogger(EIKDatasource.class.getName());
	
	private Connection dbConnection;
	
	/**
	 * the method will load the specific driver and will connect with DB.
	 * @param driverName
	 * @param url
	 * @param uid
	 * @param pwd
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void getDBConnection(String driverName,String url,String uid,String pwd) throws ClassNotFoundException, SQLException {
		LOGGER.info("Entering inside getDBConnection with values driver - {},url - {}, uid - {}, pwd - {}",driverName,url,uid,pwd);
		Class.forName(driverName);
		dbConnection = DriverManager.getConnection(url,uid,pwd);
		LOGGER.info("Exiting getDBConnection");
	}

	/**
	 * This method will be executing Query in DB.
	 * @param Eikuname
	 * @return int
	 */
	public int executeQuery(String Eikuname) {
		LOGGER.info("Entering inside executeQuery with values Eikuname - {}",Eikuname);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int res = 0;
		try {
			pstmt = dbConnection.prepareStatement("select count(ROLE_NAME) from wf_user_roles where ROLE_NAME = 'UMX|APPS_SCHEMA_CONNECT' and user_name= ?");
			pstmt.setString(1, Eikuname.toUpperCase());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
				e.printStackTrace();
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
				e.printStackTrace();
			}
		}
		LOGGER.info("Exiting executeQuery");
		return res;
	}
	
	/**
	 * This method releases database connection.
	 */
	public void releaseConnection() {
		LOGGER.info("Entering inside releaseConnection");
		try {
			if (dbConnection != null) {
				dbConnection.close();
			}
		} catch (SQLException e) {
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
			e.printStackTrace();
		}
		LOGGER.info("Exiting releaseConnection");
	}
}
