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
package com.jodb.dialect;

import java.util.List;
import java.util.Map;

/**
 * Dialect.
 * @author 帮杰
 *
 */

public abstract class Dialect {
	
	public abstract String insert(String table,Map<String, Object> map,List<Object> params);
	
	public abstract String delete(String table,Map<String, Object> map,List<Object> params);
	
	public abstract String update(String table,Map<String, Object> target,Map<String, Object> replacement,List<Object> params);
	
	public abstract String select(String table,Map<String, Object> map,List<Object> params);
	
	public abstract String paginate(String table,Map<String, Object> map,int pageNumber,int pageSize,List<Object> params);
	
	public abstract String count(String table,Map<String, Object> map,List<Object> params);
	
	public abstract String validate(String table,Map<String, Object> map,List<Object> params);
	
	public static Dialect forName(String name) throws NoSuchDialectException {
		name = name.toLowerCase();
		if(name.contains("mysql")){
			return new MysqlDialect();
		}else if(name.contains("oracle")) {
			return new OracleDialect();
		}else if(name.contains("db2")) {
			return new Db2Dialect();
		}else if(name.contains("sqlserver")) {
			return new SqlServerDialect();
		}else if(name.contains("sqlite")) {
			return new SqliteDialect();
		}else if(name.contains("postgresql")) {
			return new PostgreSqlDialect();
		}else{
			throw new NoSuchDialectException("Unsupported dialect '"+name+".");
		}
	}
}
