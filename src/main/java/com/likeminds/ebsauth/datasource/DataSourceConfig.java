/*
 * Copyright (c) 2020, Like Minds Consulting Inc. and/or its affiliates. All rights reserved.
 */ 
package com.likeminds.ebsauth.datasource;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>DataSourceConfig.java</p>
 * 
 * Copyright (c) 2020 by Like Minds Consulting Inc.
 * 
 * @version 2.0
 * @since   2020-02-12 
 */
public class DataSourceConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private HashMap<String, Object> confMap = null;
	
	public HashMap<String, Object> getConfMap() {
		return confMap;
	}

	public void setConfMap(HashMap<String, Object> confMap) {
		this.confMap = confMap;
	}

	public DataSourceConfig(HashMap<String, Object> confMap) {
		this.confMap = confMap;
	}
	
}
