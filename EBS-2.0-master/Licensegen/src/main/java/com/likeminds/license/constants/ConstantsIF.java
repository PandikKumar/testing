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
package com.likeminds.license.constants;

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
	String LICENSE_TYPE_PROMPT="LICENSE_TYPE_PROMPT";
	String LICENSE_EXPIRY_PROMPT="LICENSE_EXPIRY_PROMPT";
	String ORGANIZATION_NAME_PROMPT="ORGANIZATION_NAME_PROMPT";
	
	String LICENSE_PREFIX ="Likeminds";
	String CLAIM_ISSUER_VALUE = "Likeminds";
	String CLAIM_SUBJECT_VALUE = "subject";
	String CLAIM_NAME_LICENCE_TYPE = "Licensetype";
	String CLAIM_NAME_LICENCE = "License";

	String PROPERTY_LICENSE_ID = "license.id";
	String PROPERTY_LICENSE_KEY = "license.key";
	String PROPERTY_LICENSE_TYPE = "license.type";
	
	String MESSAGE_UNKNOWN_ERROR_OCCURED = "Unknown Error occured";
	String MESSAGE_ENCODED_LICENSE_VALUE = "Encoded license value is ->{}";
	String MESSAGE_GENERATED_JWT_TOKEN = "JWT token successfully generated {}";
	String MESSAGE_PROCESS_SUCCESSFULLY_COMPLETED = "Process successfully completed.";
	String MESSAGE_USER_ENTERED_VALUE = "User entered following value ->";
}
