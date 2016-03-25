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

import java.sql.SQLException;
import java.util.ArrayList;

import com.jodb.core.Record;
import com.jodb.core.SqlSession;

/**
 * @author 帮杰
 *
 */
public class Update extends Execution {

	public Update(SqlSession sqlSession,Record record) {
		super(sqlSession, record);
	}
	
	public void insert() throws SQLException {
		params = new ArrayList<Object>();
		sql = getSqlSession().getDialect().insert(getRecord().getName(), getRecord().getAttributeMap(), params);
		reportSql();
		SqlExecutor sqlExecutor = new SqlExecutor(getSqlSession().getConnection(), sql, params);
		try {
			sqlExecutor.execute();
			record.setGeneratedKey(sqlExecutor.getGeneratedKey());
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			sqlExecutor.close();
		}
	}
	
	public void update(Record target) throws SQLException {
		params = new ArrayList<Object>();
		sql = getSqlSession().getDialect().update(getRecord().getName(), target.getAttributeMap(), getRecord().getAttributeMap(), params);
		reportSql();
		SqlExecutor sqlExecutor = new SqlExecutor(getSqlSession().getConnection(), sql, params);
		try {
			sqlExecutor.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			sqlExecutor.close();
		}
	}
	
	public void delete() throws SQLException {
		params = new ArrayList<Object>();
		sql = getSqlSession().getDialect().delete(getRecord().getName(), getRecord().getAttributeMap(), params);
		reportSql();
		SqlExecutor sqlExecutor = new SqlExecutor(getSqlSession().getConnection(), sql, params);
		try {
			sqlExecutor.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			sqlExecutor.close();
		}
	}
}
