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

import java.util.List;

import com.jodb.core.Record;
import com.jodb.core.SqlSession;

/**
 * @author 帮杰
 *
 */
public class Execution {

	protected SqlSession sqlSession;
	protected Record record;
	protected String sql;
	protected List<Object> params;
	
	public Execution(SqlSession sqlSession,Record record) {
		this.sqlSession = sqlSession;
		this.record = record;
	}
	
	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	protected void reportSql() {
		if (sqlSession.reportSql()&&sqlSession.getSqlReporter()!=null) {
			sqlSession.getSqlReporter().report(getSql(), getParams().toArray());
		}
	}
}
