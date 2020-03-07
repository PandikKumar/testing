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
package com.likeminds.license;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import com.likeminds.license.constants.ConstantsIF;

/**
 * <p>LicenseGenerator.java</p>
 * 
 * This class responsible to generate License file for the customer.
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class LicenseGenerator implements ConstantsIF {
	private static transient final Logger LOGGER = LogManager.getLogger(LicenseGenerator.class.getName());
	private final transient static String LICENSE_FILE_NAME = "/ebsauth.lic";
	private final transient static String PROPERTY_LICENSE_FILE_LOCATION = "eik.home";
	private final transient static Locale locale = new Locale("en", "US");
	private final transient static ResourceBundle labels = ResourceBundle.getBundle("licensebundle", locale);
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		String licenseType = getUserInputFromPrompt(scanner,labels.getString(LICENSE_TYPE_PROMPT));
		String licenseExpiry = getUserInputFromPrompt(scanner,labels.getString(LICENSE_EXPIRY_PROMPT));
		String audience = getUserInputFromPrompt(scanner,labels.getString(ORGANIZATION_NAME_PROMPT));
		scanner.close();

		String license = Base64.getEncoder().encodeToString((LICENSE_PREFIX + licenseExpiry).getBytes());
		LOGGER.info(MESSAGE_ENCODED_LICENSE_VALUE,license);

		RsaJsonWebKey rsaJsonWebKey;
		try {
			rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
			Key privateKey = rsaJsonWebKey.getPrivateKey();
			Key publicKey = rsaJsonWebKey.getPublicKey();

			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			byte[] bytesPublicKey = x509EncodedKeySpec.getEncoded();
			String base64PublicKey = Base64.getEncoder().encodeToString(bytesPublicKey);

			rsaJsonWebKey.setKeyId("k1");

			JwtClaims claims = new JwtClaims();
			claims.setIssuer(CLAIM_ISSUER_VALUE); //Who creates the token and signs it
			claims.setAudience(audience); //To whom the token is intended to be sent
			claims.setSubject(CLAIM_SUBJECT_VALUE); //The subject principal is for whom the token is about
			//Additional claims/attributes about the subject can be added
			claims.setClaim(CLAIM_NAME_LICENCE_TYPE, licenseType); 
			claims.setClaim(CLAIM_NAME_LICENCE, license);

			JsonWebSignature jws = new JsonWebSignature();
			jws.setPayload(claims.toJson());
			jws.setKey(privateKey);
			jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
			jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
			String jwt = jws.getCompactSerialization();

			LOGGER.info(MESSAGE_GENERATED_JWT_TOKEN,jwt);

			Properties properties = new Properties();
			properties.setProperty(PROPERTY_LICENSE_ID, base64PublicKey);
			properties.setProperty(PROPERTY_LICENSE_KEY, jwt);
			properties.setProperty(PROPERTY_LICENSE_TYPE, licenseType);
			FileOutputStream fileOut = new FileOutputStream(getLicenseWritePath());
			properties.store(fileOut, "");
			fileOut.close();
			LOGGER.info(MESSAGE_PROCESS_SUCCESSFULLY_COMPLETED);
		} catch (JoseException e) {
			e.printStackTrace();
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(MESSAGE_UNKNOWN_ERROR_OCCURED,e);
		}
	}

	/**
	 * This method reads written path where to create EIK DBCX file.
	 * @return
	 */
	private static String getLicenseWritePath() {
	
		String licenseFileLocation = System.getenv(PROPERTY_LICENSE_FILE_LOCATION);
		if (licenseFileLocation == null || "".equalsIgnoreCase(licenseFileLocation)) {
			licenseFileLocation = System.getProperty(PROPERTY_LICENSE_FILE_LOCATION);
		}
		
		if (licenseFileLocation == null || "".equalsIgnoreCase(licenseFileLocation)) {
			licenseFileLocation = LICENSE_FILE_NAME;
		} else {
			licenseFileLocation = licenseFileLocation + LICENSE_FILE_NAME;
		}
		
		return licenseFileLocation;
	}

	/**
	 * This method reads written path where to create EIK DBCX file.
	 * @return
	 */
	private static String getUserInputFromPrompt(Scanner scanner,String promptName) {
		System.out.print(promptName);
		String value = scanner.nextLine();
		System.out.println(MESSAGE_USER_ENTERED_VALUE + value);
		return value;
	}
}
