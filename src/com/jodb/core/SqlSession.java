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
package com.jodb.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.jodb.datasource.Db;
import com.jodb.dialect.DefaultDialect;
import com.jodb.dialect.Dialect;
import com.jodb.dialect.NoSuchDialectException;

/**
 * A session is created every time you use Model,Model or Jodb to interact with your data base.
 * @author 帮杰
 *
 */
public class SqlSession {

	private Connection connection;
	private Dialect dialect;
	private boolean reportSql = false;
	private SqlReporter sqlReporter = new SystemSqlReporter();
	
	public static SqlSession open(Db db) throws SQLException {
		try {
			return new SqlSession(db.getConnection(), Dialect.forName(db.getDriverClass()));
		} catch (NoSuchDialectException e) {
			System.err.println("Warnning:No best fitted dialect found.Deafult dialect is applied.");
			return new SqlSession(db.getConnection(), new DefaultDialect());
		}
	}
	
	public SqlSession(Connection con,Dialect dialect) {
		this.connection = con;
		this.dialect = dialect;
	}

	public Connection getConnection() {
		return connection;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setConnection(Connection con) {
		this.connection = con;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public SqlReporter getSqlReporter() {
		return sqlReporter;
	}

	public void setSqlReporter(SqlReporter sqlReporter) {
		this.sqlReporter = sqlReporter;
	}

	public void close() {
		if (connection!=null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isClosed() throws SQLException {
		if (connection==null) {
			return true;
		}else {
			return connection.isClosed();
		}
	}
	
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}
	
	public boolean isAutoCommit() throws SQLException {
		return connection.getAutoCommit();
	}
	
	public void setTransactionIsolation(int level) throws SQLException {
		connection.setTransactionIsolation(level);
	}
	
	public int getTransactionIsolation() throws SQLException {
		return connection.getTransactionIsolation();
	}
	
	public boolean reportSql() {
		return reportSql;
	}

	public void setReportSql(boolean reportSql) {
		this.reportSql = reportSql;
	}

	public void commit() throws SQLException {
		connection.commit();
	}
	
	public void rollBack() throws SQLException {
		connection.rollback();
	}
	
	/////////////////////////////////////////////CURD//////////////////////////////////////////////////
	
	public void insert(Object o) throws SQLException {
		Jodb.insert(this, o);
	}
	
	public void delete(Object o) throws SQLException {
		Jodb.delete(this, o);
	}
	
	public void update(Object target,Object replacement) throws SQLException {
		Jodb.update(this, target, replacement);
	}
	
	public <T> List<T> select(T o) throws SQLException {
		return Jodb.select(this, o);
	}

	public <T> T selectOne(T o) throws SQLException {
		return Jodb.selectOne(this, o);
	}
	
	public boolean validate(Object o) throws SQLException {
		return Jodb.validate(this, o);
	}
	
	public long count(Object o) throws SQLException {
		return Jodb.count(this, o);
	}
	
}
