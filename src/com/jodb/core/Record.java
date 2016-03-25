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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Record.
 * @author 帮杰
 *
 */
public class Record {
	
	private String name;
	private Map<String, Object> attributeMap;
	private Object generatedKey;
	
	public Record(String name) {
		this.name = name;
		this.attributeMap = new HashMap<String, Object>();
	}
	
	public Record(String name,Map<String, Object> attributeMap) {
		this.name = name;
		this.attributeMap = attributeMap;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, Object> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public Object getGeneratedKey() {
		return generatedKey;
	}

	public void setGeneratedKey(Object generatedKey) {
		this.generatedKey = generatedKey;
	}

	public Object get(String key) {
		return attributeMap.get(key);
	}
	
	public Record set(String key,Object val) {
		attributeMap.put(key, val);
		return this;
	}
	
	public Record putAll(Map<String, Object> attributeMap) {
		this.attributeMap.putAll(attributeMap);
		return this;
	}
	
	public Record clearAttributeMap() {
		attributeMap.clear();
		return this;
	}
	
	public  Set<Map.Entry<String, Object>> entrySet() {
		return attributeMap.entrySet();
	}
	
	public Set<String> keySet() {
		return attributeMap.keySet();
	}
	
	public Collection<Object> values() {
		return attributeMap.values();
	}
	
	public boolean containsKey(String key) {
		return attributeMap.containsKey(key);
	}
	
	public boolean containsValue(Object value) {
		return attributeMap.containsValue(value);
	}
	
	public boolean isEmpty() {
		return attributeMap.isEmpty();
	}
	
	public int size() {
		return attributeMap.size();
	}
	
	@Override
	public String toString() {
		return attributeMap.toString();
	}
	
}