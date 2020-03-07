/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */ 
package com.likeminds.ebsauth.exception;

/**
 * <p>EIKException.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class AuthException extends Exception {
	
	private static final long serialVersionUID = 378768436633237673L;

	/**
	 * @param message
	 */
	public AuthException(String message) {
        super(message);
    }
}
