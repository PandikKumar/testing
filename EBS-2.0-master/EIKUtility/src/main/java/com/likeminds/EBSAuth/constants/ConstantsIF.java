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
package com.likeminds.EBSAuth.constants;

/**
 * <p>ConstantsIF.java</p>
 * 
 * This Interface will going to hold the keys for constants used across this project.
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public interface ConstantsIF {
	String ICX_COOKIE_DOMAIN_PROMPT = "ICX_COOKIE_DOMAIN_PROMPT";
	String ICX_COOKIE_PATH_PROMPT = "ICX_COOKIE_PATH_PROMPT";
	String EBS_LANDING_PAGE_PROMPT = "EBS_LANDING_PAGE_PROMPT";
	String EBS_LOGOUT_PAGE_PROMPT = "EBS_LOGOUT_PAGE_PROMPT";
	String AUTHENTICATION_SERVER_URL_PROMPT = "AUTHENTICATION_SERVER_URL_PROMPT";
	String TOKEN_ENDPOINT_URL_PROMPT = "TOKEN_ENDPOINT_URL_PROMPT";
	String INSTROSPECT_URL_PROMPT = "INSTROSPECT_URL_PROMPT";
	String REDIRECT_URI_PROMPT = "REDIRECT_URI_PROMPT";
	String JWKS_VALIDATION_URL_PROMPT = "JWKS_VALIDATION_URL_PROMPT";
	String GRANT_TYPE_PROMPT = "GRANT_TYPE_PROMPT";
	String AUTH_TYPE_PROMPT = "AUTH_TYPE_PROMPT";
	String JIT_PROMPT = "JIT_PROMPT";
	String CLIENT_ID_PROMPT = "CLIENT_ID_PROMPT";
	String CLIENT_SECRET_PROMPT = "CLIENT_SECRET_PROMPT";
	String WAR_CONTEXT_PATH_PROMPT = "WAR_CONTEXT_PATH_PROMPT";	
	
	String PROPERTY_ICX_COOKIE_DOMAIN = "icx_cookie_domain";
	String PROPERTY_ICX_COOKIE_PATH = "icx_cookie_path";
	String PROPERTY_EBS_LANDING_PAGE = "ebs_landing_page";
	String PROPERTY_EBS_LOGOUT_PAGE = "ebs_logout_page";
	String PROPERTY_AUTHENTICATION_SERVER_URL = "authentication_server_url";
	String PROPERTY_TOKEN_ENDPOINT_URL = "token_endpoint_url";
	String PROPERTY_INSTROSPECT_URL = "introspect_url";
	String PROPERTY_REDIRECT_URI = "redirect_uri";
	String PROPERTY_JWKS_VALIDATION_URL = "jwks_validation_url";
	String PROPERTY_GRANT_TYPE = "grant_type";
	String PROPERTY_AUTH_TYPE = "auth_type";
	String PROPERTY_JIT = "jit";
	String PROPERTY_CLIENT_ID = "client_id";
	String PROPERTY_CLIENT_SECRET = "client_secret";
	String PROPERTY_WAR_CONTEXT_PATH = "ContextPath";
	String PROPERTY_APPROVAL_PROMPT_KEY = "approval_prompt_key";
	String PROPERTY_APPROVAL_PROMPT_VALUE = "approval_prompt_value";
	String PROPERTY_SCOPE = "scope";
	String PROPERTY_ACCESS_TYPE_VALUE = "access_type_value";
	String PROPERTY_ACCESS_TYPE_KEY = "access_type_key";
	
	String MESSAGE_UNKNOWN_ERROR_OCCURED = "Unknown Error occured";
	String MESSAGE_PROCESS_SUCCESSFULLY_COMPLETED = "Process successfully completed.";
}
