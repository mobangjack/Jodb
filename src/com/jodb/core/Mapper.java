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

import java.lang.reflect.Field;

import com.jodb.annotation.ColumnMapping;
import com.jodb.annotation.TableMapping;
import com.jodb.util.DuplexMap;
import com.jodb.util.Ref;
import com.jodb.util.Str;

/**
 * Mapper.
 * @author 帮杰
 *
 */
public class Mapper {

	private String table;
	private DuplexMap<String, String> fieldDuplexMap;
	
	public Mapper() {}
	
	public Mapper(String table,DuplexMap<String, String> fieldDuplexMap) {
		this.table = table;
		this.fieldDuplexMap = fieldDuplexMap;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public DuplexMap<String, String> getFieldDuplexMap() {
		return fieldDuplexMap;
	}

	public void setFieldDuplexMap(DuplexMap<String, String> fieldDuplexMap) {
		this.fieldDuplexMap = fieldDuplexMap;
	}

	public static Mapper getMapper(Class<?> clazz){
		DuplexMap<String, String> columnFieldDuplexMap = new DuplexMap<String, String>();
		Class<?> superClass = Ref.cloneClass(clazz);
		Field[] fields;
		while (superClass!=null) {
			fields = superClass.getDeclaredFields();
			if(fields!=null){
				for(Field field:fields){
					ColumnMapping columnMapping = field.getAnnotation(ColumnMapping.class);
					if(columnMapping!=null){
						String column = Str.isBlank(columnMapping.value())?Str.humpToUnderline(field.getName()):columnMapping.value();
						columnFieldDuplexMap.put(column, field.getName());
					}
				}
			}
			superClass = superClass.getSuperclass();
		}
		if(columnFieldDuplexMap.isEmpty()){
			throw new RuntimeException("There is no ColumnMapping in your bean class '"+clazz.getName()+"'.You must use @ColumnMapping annotation(s) to map your bean field(s) to the table field(s).");
		}
		TableMapping tableMapping = clazz.getAnnotation(TableMapping.class);
		String table = (tableMapping==null||Str.isBlank(tableMapping.value()))?Str.humpToUnderline(clazz.getSimpleName()):tableMapping.value();
		return new Mapper(table, columnFieldDuplexMap);
	}

	public static String getTable(Class<?> clazz){
		return getMapper(clazz).getTable();
	}
	
	public static DuplexMap<String, String> getFieldDuplexMap(Class<?> clazz){
		return getMapper(clazz).getFieldDuplexMap();
	}
}
