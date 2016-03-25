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
package com.jodb.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

/**
 * @author 帮杰
 *
 */
public class SqlExecutor implements ISqlExecutor {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private String sql;
	private Object[] params;
	
	public SqlExecutor(){}
	
	public SqlExecutor(Connection connection,String sql) {
		this(connection, sql, new Object[0]);
	}
	
	public SqlExecutor(Connection connection,String sql,Object...params) {
		this.connection = connection;
		this.sql = sql;
		this.params = params;
	}
	
	public SqlExecutor(Connection connection,String sql,List<Object> params) {
		this(connection, sql, params.toArray());
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
	
	protected void prepareStatement() throws SQLException {
		try {
			preparedStatement = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		} catch (SQLFeatureNotSupportedException e) {
			try {
				preparedStatement = connection.prepareStatement(sql);
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw e1;
			}
		} 
		for(int i=0;i<params.length;i++){
			preparedStatement.setObject(i+1, params[i]);
		}
	}
	
	@Override
	public void execute() throws SQLException {
		prepareStatement();
		preparedStatement.execute();
	}
	
	public ResultSet getResultSet() throws SQLException {
		return resultSet==null?(resultSet = preparedStatement.getResultSet()):resultSet;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return resultSet==null?(resultSet = preparedStatement.getGeneratedKeys()):resultSet;
	}
	
	public Object getGeneratedKey() throws SQLException {
		resultSet = getGeneratedKeys();
		if (resultSet.next()) {
			return resultSet.getObject(1);
		}else {
			return null;
		}
	}
	
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
