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
	String DOMAIN_PROMPT = "DOMAIN_PROMPT";
	String DATABASE_DRIVER_PROMPT = "DATABASE_DRIVER_PROMPT";
	String JDBC_URL_PROMPT = "JDBC_URL_PROMPT";
	String APPS_USERNAME_PROMPT = "APPS_USERNAME_PROMPT";
	String APPS_PASSWORD_PROMPT = "APPS_PASSWORD_PROMPT";
	String EIK_USERNAME_PROMPT = "EIK_USERNAME_PROMPT";
	String EIK_PASSWORD_PROMPT = "EIK_PASSWORD_PROMPT";
	String CONTEXT_PATH_PROMPT = "CONTEXT_PATH_PROMPT";

	String PROPERTY_APPLICATION_CONNECTION_ID = "Appl_conn_id";
	String PROPERTY_JDBC_DRIVER = "jdbc_driver";
	String PROPERTY_JDBC_URL = "jdbc_url";
	String PROPERTY_RBAC_USER = "RBACuser";
	String PROPERTY_RBAC_PASS = "RBACpass";
	String PROPERTY_CONTEXT_URL = "ContextPath";

	String MESSAGE_UNKNOWN_ERROR_OCCURED = "Unknown Error occured";
	String MESSAGE_USER_REGISTERING_MESSAGE = "Trying to Registering User";
	String MESSAGE_USER_REGISTERING_SUCCESS_MESSAGE = "User has been registered successfully";
	String MESSAGE_USER_REGISTERING_FAILURE_MESSAGE = "User does't have privilage to access";
	String MESSAGE_FILE_PATH = "Name of the DBCX file with path where database configuration will be saved {}";
	String MESSAGE_PROCESS_SUCCESSFULLY_COMPLETED = "Process successfully completed.";
	String MESSAGE_USER_ENTERED_VALUE = "User entered following value ->";
}
