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
import java.util.List;

import com.jodb.core.Jodb;
import com.jodb.core.Record;
import com.jodb.core.SqlSession;

/**
 * @author 帮杰
 *
 */
public class Query extends Execution {

	public Query(SqlSession sqlSession,Record record) {
		super(sqlSession, record);
	}
	
	public List<Record> select() throws SQLException {
		params = new ArrayList<Object>();
		sql = getSqlSession().getDialect().select(getRecord().getName(), getRecord().getAttributeMap(), params);
		reportSql();
		SqlExecutor sqlExecutor = new SqlExecutor(getSqlSession().getConnection(), sql, params);
		try {
			sqlExecutor.execute();
			return Jodb.resultSetToRecords(sqlExecutor.getResultSet());
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			sqlExecutor.close();
		}
	}
	
	public Record selectOne() throws SQLException {
		List<Record> records = select();
		return records.isEmpty()?null:records.get(0);
	}
	
	public boolean validate() throws SQLException {
		params = new ArrayList<Object>();
		sql = getSqlSession().getDialect().validate(getRecord().getName(), getRecord().getAttributeMap(), params);
		reportSql();
		SqlExecutor sqlExecutor = new SqlExecutor(getSqlSession().getConnection(), sql, params);
		try {
			sqlExecutor.execute();
			return sqlExecutor.getResultSet().next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			sqlExecutor.close();
		}
	}
	
	public long count() throws SQLException {
		params = new ArrayList<Object>();
		sql = getSqlSession().getDialect().count(getRecord().getName(), getRecord().getAttributeMap(), params);
		reportSql();
		SqlExecutor sqlExecutor = new SqlExecutor(getSqlSession().getConnection(), sql, params);
		try {
			sqlExecutor.execute();
			if (sqlExecutor.getResultSet().next()) {
				return sqlExecutor.getResultSet().getLong(1);
			}else {
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			sqlExecutor.close();
		}
	}
}
