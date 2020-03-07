/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */ 

package com.likeminds.ebsauth.core;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <p>AuthUtil.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class AuthUtil {

	private static final Logger log = LogManager.getLogger(AuthUtil.class.getName());

	public AuthDetails createOAuthDetails(Properties config) {
		log.debug("Creating OAuth details");
		AuthDetails oauthDetails = new AuthDetails();
		try {
			oauthDetails.setAccessToken((String) config.get(AuthConstants.ACCESS_TOKEN));
			oauthDetails.setRefreshToken((String) config.get(AuthConstants.REFRESH_TOKEN));
			oauthDetails.setGrantType((String) config.get(AuthConstants.GRANT_TYPE));
			oauthDetails.setClientId((String) config.get(AuthConstants.CLIENT_ID));
			oauthDetails.setClientSecret((String) config.get(AuthConstants.CLIENT_SECRET));
			oauthDetails.setScope((String) config.get(AuthConstants.SCOPE));
			oauthDetails.setAuthenticationServerUrl((String) config.get(AuthConstants.AUTHENTICATION_SERVER_URL));
			oauthDetails.setIntrospectServerUrl((String) config.get(AuthConstants.INTROSPECT_SERVER_URL));
			oauthDetails.setUsername((String) config.get(AuthConstants.USERNAME));
			oauthDetails.setPassword((String) config.get(AuthConstants.PASSWORD));
			oauthDetails.setResourceServerUrl((String) config.get(AuthConstants.RESOURCE_SERVER_URL));
			oauthDetails.setTokenEndpointUrl((String) config.get(AuthConstants.TOKEN_ENDPOINT_URL));
			oauthDetails.setRedirectURI((String) config.get(AuthConstants.REDIRECT_URI));
			oauthDetails.setState((String) config.get(AuthConstants.STATE));
			oauthDetails.setApprovalPromptKey((String) config.get(AuthConstants.APPROVAL_PROMPT_KEY));
			oauthDetails.setApprovalPromptValue((String) config.get(AuthConstants.APPROVAL_PROMPT_VALUE));
			oauthDetails.setAccessTypeKey((String) config.get(AuthConstants.ACCESS_TYPE_KEY));
			oauthDetails.setAccessTypeValue((String) config.get(AuthConstants.ACCESS_TYPE_VALUE));
			log.debug("Exiting OAuth details");
		} catch (Exception e) {
			log.info("Exception occured in Creating OAuth Details");
			log.error(e.getLocalizedMessage());
		}
		return oauthDetails;
	}

	public String buildAuthCodeUri(Properties config) {
		log.debug("Building Authcode URI ");
		String uri = null;
		try {
			uri = (String) config.get(AuthConstants.AUTHENTICATION_SERVER_URL) + "?response_type=code&client_id="
					+ (String) config.get(AuthConstants.CLIENT_ID) + "&scope=openid&redirect_uri="
					+ (String) config.get(AuthConstants.REDIRECT_URI);
			log.info("AuthCode uri = " + uri);
			log.debug("Authcode URI Built");
		} catch (Exception e) {
			log.info("Exception occured in Building Authcode URI");
			log.error(e.getLocalizedMessage());
		}
		return uri;
	}

	public boolean validateJWT(String signedJWT, Properties config) throws InvalidJwtException {
		log.debug("Inside JWT validation");
		HttpsJwks httpsJkws = new HttpsJwks(config.getProperty(AuthConstants.JWKS_VALIDATION_URL));

		HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);

		JwtConsumer consumer = new JwtConsumerBuilder()

				.setExpectedAudience(config.getProperty(AuthConstants.CLIENT_ID))

				.setVerificationKeyResolver(httpsJwksKeyResolver)

				.setRequireSubject()

				.build();

		JwtClaims receivedClaims = consumer.processToClaims(signedJWT);

		log.info("Validated JWT claims :" + receivedClaims.toJson());
		log.debug("Exiting JWT validation");
		return true;

	}

	/**
	 * Exchange the authorization code for the access and refresh tokens
	 * 
	 * @param oauthDetails
	 * @param authorizationCode
	 * @return
	 */

	public Map<String, String> getAccessToken(AuthDetails oauthDetails, String authorizationCode) {
		log.debug("Getting Access token");
		HttpPost post = new HttpPost(oauthDetails.getTokenEndpointUrl());
		String clientId = oauthDetails.getClientId();
		String clientSecret = oauthDetails.getClientSecret();
		Map<String, String> map = new HashMap<String, String>();

		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

		parametersBody.add(new BasicNameValuePair(AuthConstants.GRANT_TYPE, oauthDetails.getGrantType()));

		parametersBody.add(new BasicNameValuePair(AuthConstants.CODE, authorizationCode));

		parametersBody.add(new BasicNameValuePair(AuthConstants.CLIENT_ID, clientId));

		if(StringUtils.isEmpty(clientSecret)) {
			parametersBody.add(new BasicNameValuePair(AuthConstants.CLIENT_SECRET, clientSecret));
		}

		parametersBody.add(new BasicNameValuePair(AuthConstants.REDIRECT_URI, oauthDetails.getRedirectURI()));

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			map = handleResponse(response);
			String accessToken = map.get(AuthConstants.ACCESS_TOKEN);

		} catch (ClientProtocolException e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			throw new RuntimeException(e.getMessage());
		}
		log.debug("Exiting getting Access token");
		return map;
	}

	/**
	 * Handles the Server response. Delegates to appropriate handler
	 * 
	 * @param response
	 * @return
	 */
	public Map<String,String> handleResponse(HttpResponse response) {
		log.debug("Inside Handle Response");
		String contentType = AuthConstants.JSON_CONTENT;
		if (response.getEntity().getContentType() != null) {
			contentType = response.getEntity().getContentType().getValue();
		}
		if (contentType.contains(AuthConstants.JSON_CONTENT)) {
			return handleJsonResponse(response);
		} 
		else if (contentType.contains(AuthConstants.URL_ENCODED_CONTENT)) {
			return handleURLEncodedResponse(response);
		} 
		else if (contentType.contains(AuthConstants.XML_CONTENT)) {
			return handleXMLResponse(response);
		} 
		else {
			throw new RuntimeException("Cannot handle " + contentType + " content type. Supported content types include JSON, XML and URLEncoded");
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> handleJsonResponse(HttpResponse response) {
		log.debug("Inside Handle Json Response");
		Map<String, String> oauthLoginResponse = null;
		try {
			oauthLoginResponse = (Map<String, String>) new JSONParser().parse(EntityUtils.toString(response.getEntity()));
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage());
			throw new RuntimeException();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			throw new RuntimeException();
		} catch (Exception e) {
			log.info("Could not parse JSON response");
			throw new RuntimeException(e.getLocalizedMessage());
		}
		log.info("********** Response Received **********");
		for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet()) {
			log.info(String.format("  %s = %s", entry.getKey(), entry.getValue()));
		}
		log.debug("Exiting Handle Json Response");
		return oauthLoginResponse;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> decodeJWT(String jwtToken) {
		log.debug("Inside Decode JWT");
		Map<String, String> oauthLoginResponse = null;
		try {
			log.info("------------ Decode JWT ------------");
			String[] split_string = jwtToken.split("\\.");
			String base64EncodedHeader = split_string[0];
			String base64EncodedBody = split_string[1];

			log.info("~~~~~~~~~ JWT Header ~~~~~~~");
			Base64 base64Url = new Base64(true);
			String header = new String(base64Url.decode(base64EncodedHeader));
			log.info("JWT Header : " + header);

			log.info("~~~~~~~~~ JWT Body ~~~~~~~");
			String body = new String(base64Url.decode(base64EncodedBody));
			log.info("JWT Body : " + body);
			oauthLoginResponse = (Map<String, String>) new JSONParser().parse(body);
		} 
		catch (ParseException e) {
			log.error(e.getLocalizedMessage());
			throw new RuntimeException();
		} 
		catch (Exception e) {
			log.info("Could not parse JSON response");
			throw new RuntimeException(e.getLocalizedMessage());
		}
		log.info("********** Response Received **********");
		for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet()) {
			log.info(String.format("  %s = %s", entry.getKey(), entry.getValue()));
		}
		log.debug("Exiting Decode JWT");
		return oauthLoginResponse;
	}

	public Map<String,String> handleURLEncodedResponse(HttpResponse response) {
		log.debug("Inside Handle URL Encoded Response");
		Map<String, Charset> map = Charset.availableCharsets();
		Map<String, String> oauthResponse = new HashMap<String, String>();
		Set<Map.Entry<String, Charset>> set = map.entrySet();
		Charset charset = null;
		HttpEntity entity = response.getEntity();
		log.info("********** Response Received **********");

		for (Map.Entry<String, Charset> entry : set) {
			log.info(String.format("  %s = %s", entry.getKey(), entry.getValue()));
			if (entry.getKey().equalsIgnoreCase(HTTP.UTF_8)) {
				charset = entry.getValue();
			}
		}

		try {
			List<NameValuePair> list = URLEncodedUtils.parse(EntityUtils.toString(entity), Charset.forName(HTTP.UTF_8));
			for (NameValuePair pair : list) {
				log.info(String.format("  %s = %s", pair.getName(), pair.getValue()));
				oauthResponse.put(pair.getName(), pair.getValue());
			}

		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
			throw new RuntimeException("Could not parse URLEncoded Response");
		}
		log.debug("Exiting Handle URL Encoded Response");
		return oauthResponse;
	}

	public Map<String, String> handleXMLResponse(HttpResponse response) {
		log.debug("Inside Handle XML Response");
		Map<String, String> oauthResponse = new HashMap<String, String>();
		try {
			String xmlString = EntityUtils.toString(response.getEntity());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlString));
			Document doc = db.parse(inStream);

			log.info("********** Response Receieved **********");
			parseXMLDoc(null, doc, oauthResponse);
		} 
		catch (Exception e) {
			log.error(e.getLocalizedMessage());
			throw new RuntimeException("Exception occurred while parsing XML response");
		}
		log.debug("Exiting Handle XML Response");
		return oauthResponse;
	}

	public void parseXMLDoc(Element element, Document doc, Map<String, String> oauthResponse) {
		log.debug("Inside parse XML Doc");
		NodeList child = null;
		if (element == null) {
			child = doc.getChildNodes();

		} 
		else {
			child = element.getChildNodes();
		}
		for (int j = 0; j < child.getLength(); j++) {
			if (child.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
				org.w3c.dom.Element childElement = (org.w3c.dom.Element) child.item(j);
				if (childElement.hasChildNodes()) {
					log.info(childElement.getTagName() + " : " + childElement.getTextContent());
					oauthResponse.put(childElement.getTagName(), childElement.getTextContent());
					parseXMLDoc(childElement, null, oauthResponse);
				}

			}
		}
		log.debug("Exiting parse XML Doc");
	}

	public String getAuthorizationHeaderForAccessToken(String accessToken) {
		return AuthConstants.BEARER + " " + accessToken;
	}

	public String getBasicAuthorizationHeader(String username, String password) {
		return AuthConstants.BASIC + " " + encodeCredentials(username, password);
	}

	public String encodeCredentials(String username, String password) {
		String cred = username + ":" + password;
		String encodedValue = null;
		byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
		encodedValue = new String(encodedBytes);
		log.info("encodedBytes " + new String(encodedBytes));

		byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
		log.info("decodedBytes " + new String(decodedBytes));

		return encodedValue;

	}

	public List<String> validateInput(AuthDetails input) {
		log.debug("Entering validating Input");
		List<String> invalidProps = new ArrayList<String>();

		if (input == null) {
			invalidProps.add("The EBSAuthDetails bean itself is null. Please check the EBSOAuthClient code");
			return invalidProps;
		}

		String grantType = input.getGrantType();

		if(StringUtils.isEmpty(grantType)) {
			log.info("Please provide valid value for grant_type");
			invalidProps.add(AuthConstants.GRANT_TYPE);
		}

		if(StringUtils.isEmpty(input.getAuthenticationServerUrl())) {
			log.info("Please provide valid value for authentication server url");
			invalidProps.add(AuthConstants.AUTHENTICATION_SERVER_URL);
		}

		if(StringUtils.isEmpty(input.getTokenEndpointUrl())) {
			log.info("Please provide valid value for token endpoint url");
			invalidProps.add(AuthConstants.TOKEN_ENDPOINT_URL);
		}

		if(StringUtils.isEmpty(input.getApprovalPromptValue())) {
			log.info("Please provide valid value for approval prompt value");
			invalidProps.add(AuthConstants.APPROVAL_PROMPT_VALUE);
		}

		if(StringUtils.isEmpty(input.getApprovalPromptKey())) {
			log.info("Please provide valid value for approval prompt key");
			invalidProps.add(AuthConstants.APPROVAL_PROMPT_KEY);
		}

		if(StringUtils.isEmpty(input.getRedirectURI())) {
			log.info("Please provide valid value for redirect uri");
			invalidProps.add(AuthConstants.REDIRECT_URI);
		}
		log.debug(" exiting Validation Input");
		return invalidProps;

	}

	public String getHeader(Header[] headers, String name) {

		String header = null;
		if (headers != null) {
			for (Header h : headers) {
				if (h.getName().equalsIgnoreCase(name)) {
					header = h.getValue();
					break;
				}
			}
		}

		return header;

	}

}
