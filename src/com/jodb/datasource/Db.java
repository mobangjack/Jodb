/**
 * Copyright (c) 2011-2015, Mobangjack 莫帮杰 (mobangjack@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jodb.datasource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.jodb.util.Prop;
import com.jodb.util.Str;

/**
 * Db(none-pooled data source).Extends this class and override getConnection() method if you would like to use pooled data source.
 * @author 帮杰
 *
 */
public class Db {

	private String jdbcUrl;
	private String user;
	private String password;
	private String driverClass = "com.mysql.jdbc.Driver";
	
	public Db(String jdbcUrl,String user,String password,String driverClass) {
		init(jdbcUrl, user, password, driverClass);
	}
	
	private void init(String jdbcUrl,String user,String password,String driverClass) {
		if (Str.isBlank(jdbcUrl)) {
			throw new IllegalArgumentException("jdbcUrl can not be blank.");
		}
		this.jdbcUrl = jdbcUrl;
		if (Str.isBlank(user)) {
			throw new IllegalArgumentException("user can not be blank.");
		}
		this.user = user;
		this.password = password;
		if (!Str.isBlank(driverClass)) {
			this.driverClass = Str.isBlank(driverClass)?this.driverClass:driverClass;
		}
	}
	
	public Db(String jdbcUrl,String user,String password) {
		this(jdbcUrl, user, password, null);
	}
	
	public Db(String file) {
		this(new Prop(file).getProperties());
	}
	
	public Db(File file) {
		this(new Prop(file).getProperties());
	}
	
	public Db(Properties properties) {
		this(properties.getProperty("jdbcUrl"), properties.getProperty("user"), properties.getProperty("password"), properties.getProperty("driverClass"));
	}
	
	public Connection getConnection() throws SQLException {
		try {
			Class.forName(driverClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(jdbcUrl, user, password);
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		if (Str.isBlank(jdbcUrl)) {
			throw new IllegalArgumentException("jdbcUrl can not be blank.");
		}
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		if (Str.isBlank(user)) {
			throw new IllegalArgumentException("user can not be blank.");
		}
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (Str.isBlank(password)) {
			throw new IllegalArgumentException("password can not be blank.");
		}
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		if (Str.isBlank(driverClass)) {
			throw new IllegalArgumentException("driverClass can not be blank.");
		}
		this.driverClass = driverClass;
	}

}
