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

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author 帮杰
 *
 */
public class SystemSqlReporter implements SqlReporter {

	@Override
	public void report(String sql, Object... params) {
		StringBuilder sb = new StringBuilder("["+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+"]\n");
		sb.append("sql:").append(sql).append("\n");
		sb.append("params:");
		if (params!=null) {
			for (Object param:params) {
				sb.append(param).append(",");
			}
		}
		System.out.println(sb.substring(0, sb.length()-1));
	}

}
