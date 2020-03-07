/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
  */ 
package com.likeminds.ebsauth.core;

/**
 * <p>AuthConstants.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public interface AuthConstants {

	public String LIC_PATH = "/ebsauth.lic";
	public String CONFIG_PATH = "/eikauth.config";
	public String DBCX_PATH = "/eik.dbcx";
	public String JDBC_DRIVER = "jdbc_driver";
	public String DB_URL = "ebs_db_url";	
	public String USER = "ebs_db_user";
	public String PASS = "ebs_db_pass";
	public String EIK_HOME = "eik.home";
	public String ICX_COOKIE_DOMAIN = "icx_cookie_domain";
	public String ICX_COOKIE_PATH = "icx_cookie_path";
	public String EBS_LANDING_PAGE = "ebs_landing_page";
	public String EBS_LOGOUT_PAGE = "ebs_logout_page";
	public String IS_USER_EXISTS = "isUserExistsInEBS";
	public String CONFIG = "config";
	public String LIC_VALID = "licValid";
	public String LIC_KEY = "license.key";
	public String JIT = "jit";
	public String TRUE = "true";
	public String FALSE = "false";
	public String USER_CREATION_PASS = "welcome123";
	public String AUTH_TYPE = "auth_type";
	public String OIDC = "OIDC";
	public String LICENSE = "License";
	public String LICENSE_KEY = "license.key";
	public String LICENSE_ID = "license.id";
	public String INDEX_OF = "z";
	public String DATE_FORMAT = "yyyy-MM-dd";
	public String REQUEST_URL = "requestUrl";
	public String HAEDER_USER_NAME = "USERNAME";
	public String RBAC_USER ="RBACuser";
	public String APPL_CONN_ID = "Appl_conn_id";
	public String XID = "select xsid from ICX.icx_sessions where session_id = ?";
	public String USER_ID = "select user_id from fnd_user where user_name= ? ";
	public String USER_EXIST = "select count(user_name) from fnd_user where user_name= ? ";
	public String CREATE_SESSION = "BEGIN ? := FND_SESSION_MANAGEMENT.CREATESESSION( ? , ? , ? , ? , ? , ? , ? ); END;";
	public String COOKIE_NAME = "{ ? = call FND_SESSION_MANAGEMENT.GETSESSIONCOOKIENAME() }";
	public String CREATE_USER = "{call FND_USER_PKG.CreateUser(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public String RESET_SESSION = "{call FND_SESSION_MANAGEMENT.reset_session(?)}";
	public String ROLE_CHECK = "select count(ROLE_NAME) from wf_user_roles where ROLE_NAME = 'UMX|APPS_SCHEMA_CONNECT' and user_name= ?";
	public String ACCESS_TOKEN = "access_token";
	public String CLIENT_ID = "client_id";
	public String CLIENT_SECRET = "client_secret";
	public String REFRESH_TOKEN = "refresh_token";
	public String USERNAME = "username";
	public String PASSWORD = "password";
	public String INTROSPECT = "introspect";
	public String ROPC = "ropc";
	public String CLIENT_CRED = "client_cred";
	public String CODE = "code";
	public String CALLER = "caller";
	public String TOKEN = "token";
	public String ID_TOKEN = "id_token";
	public String SUB = "sub";
	public String LANG = "US";
	public String CREATE_SESSION_CODE = "115J";
	public String APPROVAL_PROMPT_KEY = "approval_prompt_key";
	public String ACCESS_TYPE_KEY = "access_type_key";
	public String REDIRECT_URI = "redirect_uri";
	public String RESPONSE_TYPE = "response_type";
	public String APPROVAL_PROMPT_VALUE = "approval_prompt_value";
	public String ACCESS_TYPE_VALUE = "access_type_value";
	public String INTROSPECT_SERVER_URL = "introspect_url";
	public String JWKS_VALIDATION_URL = "jwks_validation_url";
	public String AUTHENTICATION_SERVER_URL = "authentication_server_url";
	public String TOKEN_ENDPOINT_URL = "token_endpoint_url";
	public String RESOURCE_SERVER_URL = "resource_server_url";
	public String GRANT_TYPE = "grant_type";
	public String GRANT_TYPE_PASSWORD = "password";
	public String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
	public String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
	public String SCOPE = "scope";
	public String LOCATION_HEADER = "Location";
	public String STATE = "state";
	public String AUTHORIZATION = "Authorization";
	public String BEARER = "Bearer";
	public String BASIC = "Basic";
	public String JSON_CONTENT = "application/json";
	public String XML_CONTENT = "application/xml";
	public String URL_ENCODED_CONTENT = "application/x-www-form-urlencoded";

	public int HTTP_OK = 200;
	public int HTTP_FORBIDDEN = 403;
	public int HTTP_UNAUTHORIZED = 401;
	public int HTTP_SEND_REDIRECT = 302;

}
