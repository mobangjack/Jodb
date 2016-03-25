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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jodb.util.DuplexMap;
import com.jodb.util.Ref;

/**
 * Object relationship mapping.
 * @author 帮杰
 *
 */
public class ORM {

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object... objects) {
		if (objects==null) {
			return true;
		}
		for (Object object : objects) {
			if (objects==null) {
				return true;
			}
			if (object instanceof Collection) {
				return ((Collection)object).isEmpty();
			}
			if (object instanceof Map) {
				return ((Map)object).isEmpty();
			}
			if (object instanceof DuplexMap) {
				return ((DuplexMap)object).isEmpty();
			}
		}
		return false;
	}
	
	/**
	 * Transfer map.
	 * @param map
	 * @param fieldDuplexMap
	 * @param orientation orientation>0,Bean->Map,else Map->Bean.
	 * @return
	 */
	public static Map<String, Object> transMap(Map<String, Object> map,DuplexMap<String, String> fieldDuplexMap,int orientation) {
		if (isEmpty(map)) {
			return null;
		}
		if (isEmpty(fieldDuplexMap)) {
			return map;
		}
		Map<String, Object> transMap = new HashMap<String, Object>(map.size());
		for (Entry<String, Object> e:map.entrySet()){
			if (fieldDuplexMap.containsV(e.getKey())) {
				transMap.put(orientation>0?fieldDuplexMap.getByK(e.getKey()):fieldDuplexMap.getByV(e.getKey()), e.getValue());
			}else {
				transMap.put(e.getKey(), e.getValue());
			}
		}
		return transMap;
	}
	
	public static Map<String, Object> beanToMap(Object bean) {
		if (bean==null) {
			return null;
		}
		Field[] fields = Ref.getBeanFields(bean.getClass());
		Map<String, Object> map = null;
		Object value = null;
		if(fields!=null){
			map = new HashMap<String, Object>(fields.length);
			for(Field field:fields){
				value = Ref.getFieldVal(bean, field);
				if (value!=null) {
					map.put(field.getName(), value);
				}
			}
		}
		return map;
	}
	
	public static List<Map<String, Object>> beansToMaps(List<Object> beans) {
		if (isEmpty(beans)) {
			return null;
		}
		List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
		for (Object bean : beans) {
			maps.add(beanToMap(bean));
		}
		return maps;
	}
	
	public static Map<String, Object> beanToMap(Object bean,DuplexMap<String, String> fieldDuplexMap) {
		return transMap(beanToMap(bean), fieldDuplexMap, 1);
	}
	
	public static List<Map<String, Object>> beansToMaps(List<Object> beans,DuplexMap<String, String> fieldDuplexMap) {
		if (isEmpty(beans)) {
			return null;
		}
		List<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
		for (Object bean : beans) {
			maps.add(beanToMap(bean,fieldDuplexMap));
		}
		return maps;
	}
	
	public static <T> T mapToBean(Class<?> beanClass,Map<String, Object> map) {
		if (isEmpty(beanClass,map)) {
			return null;
		}
		T bean = Ref.newInstance(beanClass);
		Field[] fields = Ref.getBeanFields(beanClass);
		Object value = null;
		if (fields!=null) {
			for (Field field : fields) {
				value = map.get(field.getName());
				if (value!=null) {
					Ref.setFieldVal(bean, field, value);
				}
			}
		}
		return bean;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> mapsToBeans(Class<?> beanClass,List<Map<String, Object>> maps) {
		if (isEmpty(beanClass,maps)) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		for(Map<String, Object> map:maps)
			list.add((T) mapToBean(beanClass, map));
		return list.isEmpty()?null:list;
	}
	
	
	public static <T> T mapToBean(Class<T> beanClass,Map<String, Object> map,DuplexMap<String, String> fieldDuplexMap) {
		return mapToBean(beanClass, transMap(map, fieldDuplexMap, -1));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> mapsToBeans(Class<?> beanClass,List<Map<String, Object>> maps,DuplexMap<String, String> fieldDuplexMap) {
		if (isEmpty(beanClass,maps,fieldDuplexMap)) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		for(Map<String, Object> map:maps){
			list.add((T) mapToBean(beanClass, map,fieldDuplexMap));
		}
		return list.isEmpty()?null:list;
	}
	
}
