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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jodb.executor.Query;
import com.jodb.executor.Update;

/**
 * @author 帮杰
 *
 */
public class Jodb {

	public static void insert(SqlSession sqlSession,Object o) throws SQLException {
		new Update(sqlSession,toRecord(o)).insert();;
	}
	
	public static void delete(SqlSession sqlSession,Object o) throws SQLException {
		new Update(sqlSession,toRecord(o)).delete();
	}
	
	public static void update(SqlSession sqlSession,Object target,Object replacement) throws SQLException {
		new Update(sqlSession,toRecord(replacement)).update(toRecord(target));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> select(SqlSession sqlSession,T o) throws SQLException {
		List<Record> records = new Query(sqlSession, toRecord(o)).select();
		if (o instanceof Record) {
			return (List<T>) records;
		} else {
			return recordsToBeans(records, o.getClass());
		}
	}

	public static <T> T selectOne(SqlSession sqlSession,T o) throws SQLException {
		List<T> records = select(sqlSession, o);
		return records.isEmpty()?null:records.get(0);
	}
	
	public static boolean validate(SqlSession sqlSession,Object o) throws SQLException {
		return new Query(sqlSession, toRecord(o)).validate();
	}
	
	public static long count(SqlSession sqlSession,Object o) throws SQLException {
		return new Query(sqlSession, toRecord(o)).count();
	}
	
	public static Record toRecord(Object o) {
		Record record;
		if (o instanceof Record) {
			record = (Record)o;
		} else {
			record = beanToRecord(o);
		}
		return record;
	}
	
	public static <M> Record beanToRecord(M bean) {
		Mapper mapper = Mapper.getMapper(bean.getClass());
		String name = mapper.getTable();
		Map<String, Object> attributeMap = ORM.beanToMap(bean,mapper.getFieldDuplexMap());
		return new Record(name, attributeMap);
	}
	
	@SuppressWarnings("unchecked")
	public static <M> M recordToBean(Record record,Class<?> beanClass) {
		return (M) ORM.mapToBean(beanClass, record.getAttributeMap(), Mapper.getFieldDuplexMap(beanClass));
	}
	
	@SuppressWarnings("unchecked")
	public static <M> List<M> recordsToBeans(List<Record> records,Class<?> beanClass) {
		List<M> beans = new ArrayList<M>();
		for (Record record:records){
			beans.add((M) recordToBean(record, beanClass));
		}
		return beans;
	}
	
	public static <T> List<T> resultSetToBeans(ResultSet rs,Class<T> beanClass) {
		return ORM.mapsToBeans(beanClass, resultSetToMaps(rs), Mapper.getFieldDuplexMap(beanClass));
	}
	
	public static List<Record> resultSetToRecords(ResultSet rs) {
		List<Record> records = new ArrayList<Record>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] labelNames = new String[columnCount];
			int[] types = new int[columnCount];
			for (int i=0; i<columnCount; i++) {
				labelNames[i] = rsmd.getColumnLabel(i+1);
				types[i] = rsmd.getColumnType(i+1);
			}
			Record record;
			while(rs.next()){
				record = new Record(rsmd.getTableName(1));
				Object value = null;
				for(int i=0;i<columnCount;i++){
					if (types[i] < Types.BLOB)
						value = rs.getObject(i+1);
					else if (types[i] == Types.CLOB)
						value = handleClob(rs.getClob(i+1));
					else if (types[i] == Types.NCLOB)
						value = handleClob(rs.getNClob(i+1));
					else if (types[i] == Types.BLOB)
						value = handleBlob(rs.getBlob(i+1));
					else
						value = rs.getObject(i+1);
					record.set(labelNames[i], value);
				}
				records.add(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
	}
	
	public static List<Map<String, Object>> resultSetToMaps(ResultSet rs) {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		List<Record> records = resultSetToRecords(rs);
		for(Record record:records){
			maps.add(record.getAttributeMap());
		}
		return maps;
	}
	
	public static byte[] handleBlob(Blob blob) throws IOException, SQLException {
		InputStream is = null;
		try {
			is = blob.getBinaryStream();
			byte[] bytes = new byte[(int)blob.length()];
			is.read(bytes);
			return bytes;
		} catch (IOException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (SQLException e2) {
			e2.printStackTrace();
			throw e2;
		} finally {
			if (is!=null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String handleClob(Clob clob) throws SQLException, IOException {
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			char[] buffer = new char[(int)clob.length()];
			reader.read(buffer);
			return new String(buffer);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (SQLException e2) {
			e2.printStackTrace();
			throw e2;
		} finally {
			if (reader!=null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
