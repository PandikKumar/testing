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
package com.likeminds.EBSAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.likeminds.EBSAuth.constants.ConstantsIF;

/**
 * <p>EIKAdminUtility.java</p>
 * 
 * This class is responsible to generate EIK Auth config file.
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class EIKAdminUtility implements ConstantsIF {
	private static transient final Logger LOGGER = LogManager.getLogger(EIKAdminUtility.class.getName());
	private final transient static String EIKAUTHCONFIG_FILE_NAME = "/eik.config";
	private final transient static String PROPERTY_EIKAUTHCONFIG_FILE_LOCATION = "eik.home";
	private final transient static Locale locale = new Locale("en", "US");
	private final transient static ResourceBundle labels = ResourceBundle.getBundle("eikutility", locale);
	private final transient static Map<String,JTextField> fieldsMap = new ConcurrentHashMap<String,JTextField>();


	public static <E> void main(String[] args) {
		
		boolean exit = false;
		Base64.Encoder encoder = Base64.getEncoder();
		String jit[] = {"true", "false"};
		String authType[] = {"OIDC", "HEADER"};
		String grantType[] = {"authorization_code"};
		while (!exit) {

			JComboBox<String> field14 = new JComboBox<String>(grantType);
			JComboBox<String> field15 = new JComboBox<String>(authType);
			JComboBox<String> field16 = new JComboBox<String>(jit);

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
			addFieldAndLabelToPanel(jPanel, false, ICX_COOKIE_DOMAIN_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, ICX_COOKIE_PATH_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, EBS_LANDING_PAGE_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, EBS_LOGOUT_PAGE_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, AUTHENTICATION_SERVER_URL_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, TOKEN_ENDPOINT_URL_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, INSTROSPECT_URL_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, REDIRECT_URI_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, JWKS_VALIDATION_URL_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, GRANT_TYPE_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, AUTH_TYPE_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, JIT_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, CLIENT_ID_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, CLIENT_SECRET_PROMPT);
			addFieldAndLabelToPanel(jPanel, true, WAR_CONTEXT_PATH_PROMPT);
			
			jPanel.add(Box.createHorizontalStrut(5));

			int result = JOptionPane.showConfirmDialog(null, jPanel,"Please enter the following:",JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				Properties properties = new Properties();
				properties.setProperty(PROPERTY_ICX_COOKIE_DOMAIN, fieldsMap.get(ICX_COOKIE_DOMAIN_PROMPT).getText());
				properties.setProperty(PROPERTY_ICX_COOKIE_PATH, fieldsMap.get(ICX_COOKIE_PATH_PROMPT).getText());
				properties.setProperty(PROPERTY_EBS_LANDING_PAGE, fieldsMap.get(EBS_LANDING_PAGE_PROMPT).getText());
				properties.setProperty(PROPERTY_EBS_LOGOUT_PAGE, fieldsMap.get(EBS_LOGOUT_PAGE_PROMPT).getText());
				properties.setProperty(PROPERTY_AUTHENTICATION_SERVER_URL, fieldsMap.get(AUTHENTICATION_SERVER_URL_PROMPT).getText());
				properties.setProperty(PROPERTY_TOKEN_ENDPOINT_URL, fieldsMap.get(TOKEN_ENDPOINT_URL_PROMPT).getText());
				properties.setProperty(PROPERTY_INSTROSPECT_URL, fieldsMap.get(INSTROSPECT_URL_PROMPT).getText());
				properties.setProperty(PROPERTY_REDIRECT_URI, fieldsMap.get(REDIRECT_URI_PROMPT).getText());
				properties.setProperty(PROPERTY_JWKS_VALIDATION_URL, fieldsMap.get(JWKS_VALIDATION_URL_PROMPT).getText());
				properties.setProperty(PROPERTY_GRANT_TYPE, (String) field14.getSelectedItem());
				properties.setProperty(PROPERTY_AUTH_TYPE, (String) field15.getSelectedItem());
				properties.setProperty(PROPERTY_JIT, (String) field16.getSelectedItem());
				properties.setProperty(PROPERTY_CLIENT_ID, encoder.encodeToString(fieldsMap.get(CLIENT_ID_PROMPT).getText().getBytes()));
				properties.setProperty(PROPERTY_CLIENT_SECRET, encoder.encodeToString(fieldsMap.get(CLIENT_SECRET_PROMPT).getText().getBytes()));
				properties.setProperty(PROPERTY_WAR_CONTEXT_PATH,fieldsMap.get(WAR_CONTEXT_PATH_PROMPT).getText());
				properties.setProperty(PROPERTY_APPROVAL_PROMPT_KEY, "prompt");
				properties.setProperty(PROPERTY_APPROVAL_PROMPT_VALUE, "login consent");
				properties.setProperty(PROPERTY_SCOPE, "openid");
				properties.setProperty(PROPERTY_ACCESS_TYPE_KEY, "access_type");
				properties.setProperty(PROPERTY_ACCESS_TYPE_VALUE, "offline");
				String confOut = getLicenseWritePath();
				File file = new File(confOut);
				FileOutputStream fileOut;
				try {
					fileOut = new FileOutputStream(file);
					properties.store(fileOut, "");
					fileOut.close();
					LOGGER.info(MESSAGE_PROCESS_SUCCESSFULLY_COMPLETED);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
				} catch (IOException e) {
					e.printStackTrace();
					LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
				}
			}
			exit = true;
		}
	}
	
	/**
	 * @param jPanel
	 * @param createHorizontalStrut
	 * @param prompt
	 */
	private static void addFieldAndLabelToPanel(JPanel jPanel,boolean createHorizontalStrut,String prompt) {
		if (createHorizontalStrut) {
			jPanel.add(Box.createHorizontalStrut(5));
		}
		JTextField jField = new JTextField();
		jPanel.add(new JLabel(labels.getString(prompt)));
		jPanel.add(jField);
		fieldsMap.put(prompt,jField);
	}
	
	/**
	 * This method reads written path where to create EIK DBCX file.
	 * @return
	 */
	private static String getLicenseWritePath() {
		String authConfig = System.getenv(PROPERTY_EIKAUTHCONFIG_FILE_LOCATION);
		if (authConfig == null || "".equalsIgnoreCase(authConfig)) {
			authConfig = System.getProperty(PROPERTY_EIKAUTHCONFIG_FILE_LOCATION);
		}
		
		if (authConfig == null || "".equalsIgnoreCase(authConfig)) {
			authConfig = EIKAUTHCONFIG_FILE_NAME;
		} else {
			authConfig = authConfig + EIKAUTHCONFIG_FILE_NAME;
		}
		return authConfig;
	}
}
