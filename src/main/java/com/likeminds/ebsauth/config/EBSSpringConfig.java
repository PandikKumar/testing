/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */
package com.likeminds.ebsauth.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.likeminds.ebsauth.core.AuthUtil;
import com.likeminds.ebsauth.datasource.DataSourceConfig;
import com.likeminds.ebsauth.datasource.DataSourceManager;
import com.likeminds.ebsauth.service.AuthService;

/**
 * <p> EBSSpringConfig.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @author Ramesh Dhason
 * @version 2.0
 * @since 2020-02-12
 */

@Import({ AuthUtil.class, AuthService.class, DataSourceConfig.class, DataSourceManager.class })
@ComponentScan({ "com.likeminds.ebsauth.controller" })
@Configuration
public class EBSSpringConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(Locale.US);
	    return slr;
	}

}
