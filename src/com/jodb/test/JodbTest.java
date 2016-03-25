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
package com.jodb.test;

import java.sql.SQLException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.jodb.core.Record;
import com.jodb.core.SqlSession;
import com.jodb.datasource.Db;

/**
 * JodbTest.
 * @author 帮杰
 *
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class JodbTest {

	private Db datasource = new Db("db.properties");
	private User user = new User(10,"mobangjack","iloveu");
	private Record user_replacement = new Record("user").set("id", 10).set("username", "mbj").set("password", "idonotloveu");
//	public C3p0 datasource = new C3p0("c3p0.properties");
	
	public JodbTest() {
	}
	
	@Test
	public void insert() throws SQLException {
		SqlSession sqlSession = null;
		try {
			sqlSession = SqlSession.open(datasource);
			if (sqlSession.validate(user)) {
				sqlSession.delete(user);
			}
			sqlSession.insert(user);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (sqlSession!=null) {
				sqlSession.close();
				sqlSession = null;
			}
		}
	}
	
	@Test
	public void count() throws SQLException {
		SqlSession sqlSession = null;
		try {
			sqlSession = SqlSession.open(datasource);
			sqlSession.count(user);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (sqlSession!=null) {
				sqlSession.close();
				sqlSession = null;
			}
		}
	}
	
	@Test
	public void update() throws SQLException {
		SqlSession sqlSession = null;
		try {
			sqlSession = SqlSession.open(datasource);
			if (!sqlSession.validate(user)) {
				sqlSession.insert(user);
			}
			sqlSession.update(user,user_replacement);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (sqlSession!=null) {
				sqlSession.close();
				sqlSession = null;
			}
		}
	}
	
	@Test
	public void delete() throws SQLException {
		SqlSession sqlSession = null;
		try {
			sqlSession = SqlSession.open(datasource);
			sqlSession.delete(user_replacement);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (sqlSession!=null) {
				sqlSession.close();
				sqlSession = null;
			}
		}
	}
	
	@Test
	public void select() throws SQLException {
		SqlSession sqlSession = null;
		try {
			sqlSession = SqlSession.open(datasource);
			System.out.println(sqlSession.selectOne(user_replacement));
		} catch (SQLException e) {
			throw e;
		} finally {
			if (sqlSession!=null) {
				sqlSession.close();
				sqlSession = null;
			}
		}
	}
	
	public static void main(String[] args) throws SQLException {
		JodbTest test = new JodbTest();
		test.insert();
		test.delete();
		test.update();
		test.select();
	}
}
