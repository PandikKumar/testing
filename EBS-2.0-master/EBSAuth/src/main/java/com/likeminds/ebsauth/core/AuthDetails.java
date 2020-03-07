/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */ 
package com.likeminds.ebsauth.core;

import java.io.Serializable;

/**
 * <p>AuthDetails.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class AuthDetails implements Serializable{
	
	private static final long serialVersionUID = -3035054201841411100L;

	private String scope = null;
	
	private String state = null;
	
	private String grantType = null;
	
	private String clientId = null;
	
	private String clientSecret = null;
	
	private String accessToken = null;
	
	private String refreshToken = null;
	
	private String approvalPromptKey = null;
	
	private String approvalPromptValue = null;
	
	private String accessTypeKey = null;
	
	private String accessTypeValue = null;
	
	private String redirectURI = null;
	
	private String username = null;
	
	private String password = null;
	
	private String authenticationServerUrl = null;
	
	private String introspectServerUrl = null;
	
	private String tokenEndpointUrl = null;
	
	private String resourceServerUrl = null;
	
	private boolean isAccessTokenRequest = false;

	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the grantType
	 */
	public String getGrantType() {
		return grantType;
	}

	/**
	 * @param grantType the grantType to set
	 */
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the clientSecret
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * @param clientSecret the clientSecret to set
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * @return the approvalPromptKey
	 */
	public String getApprovalPromptKey() {
		return approvalPromptKey;
	}

	/**
	 * @param approvalPromptKey the approvalPromptKey to set
	 */
	public void setApprovalPromptKey(String approvalPromptKey) {
		this.approvalPromptKey = approvalPromptKey;
	}

	/**
	 * @return the approvalPromptValue
	 */
	public String getApprovalPromptValue() {
		return approvalPromptValue;
	}

	/**
	 * @param approvalPromptValue the approvalPromptValue to set
	 */
	public void setApprovalPromptValue(String approvalPromptValue) {
		this.approvalPromptValue = approvalPromptValue;
	}

	/**
	 * @return the accessTypeKey
	 */
	public String getAccessTypeKey() {
		return accessTypeKey;
	}

	/**
	 * @param accessTypeKey the accessTypeKey to set
	 */
	public void setAccessTypeKey(String accessTypeKey) {
		this.accessTypeKey = accessTypeKey;
	}

	/**
	 * @return the accessTypeValue
	 */
	public String getAccessTypeValue() {
		return accessTypeValue;
	}

	/**
	 * @param accessTypeValue the accessTypeValue to set
	 */
	public void setAccessTypeValue(String accessTypeValue) {
		this.accessTypeValue = accessTypeValue;
	}

	/**
	 * @return the redirectURI
	 */
	public String getRedirectURI() {
		return redirectURI;
	}

	/**
	 * @param redirectURI the redirectURI to set
	 */
	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the authenticationServerUrl
	 */
	public String getAuthenticationServerUrl() {
		return authenticationServerUrl;
	}

	/**
	 * @param authenticationServerUrl the authenticationServerUrl to set
	 */
	public void setAuthenticationServerUrl(String authenticationServerUrl) {
		this.authenticationServerUrl = authenticationServerUrl;
	}

	/**
	 * @return the introspectServerUrl
	 */
	public String getIntrospectServerUrl() {
		return introspectServerUrl;
	}

	/**
	 * @param introspectServerUrl the introspectServerUrl to set
	 */
	public void setIntrospectServerUrl(String introspectServerUrl) {
		this.introspectServerUrl = introspectServerUrl;
	}

	/**
	 * @return the tokenEndpointUrl
	 */
	public String getTokenEndpointUrl() {
		return tokenEndpointUrl;
	}

	/**
	 * @param tokenEndpointUrl the tokenEndpointUrl to set
	 */
	public void setTokenEndpointUrl(String tokenEndpointUrl) {
		this.tokenEndpointUrl = tokenEndpointUrl;
	}

	/**
	 * @return the resourceServerUrl
	 */
	public String getResourceServerUrl() {
		return resourceServerUrl;
	}

	/**
	 * @param resourceServerUrl the resourceServerUrl to set
	 */
	public void setResourceServerUrl(String resourceServerUrl) {
		this.resourceServerUrl = resourceServerUrl;
	}

	/**
	 * @return the isAccessTokenRequest
	 */
	public boolean isAccessTokenRequest() {
		return isAccessTokenRequest;
	}

	/**
	 * @param isAccessTokenRequest the isAccessTokenRequest to set
	 */
	public void setAccessTokenRequest(boolean isAccessTokenRequest) {
		this.isAccessTokenRequest = isAccessTokenRequest;
	}
	

	
}
