/**
 * Copyright (c) 2013-2020, cpthack 成佩涛 (cpt@jianzhimao.com).
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
package com.github.bakerzhu.commons.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <b>AssertHelper.java</b></br>
 * 
 * <pre>
 * 断言工具类
 * </pre>
 *
 * @author cpthack cpt@jianzhimao.com
 * @date May 16, 2017 3:46:27 PM
 * @since JDK 1.8
 */
public class AssertHelper {
	
	/**
	 * 
	 * <b>notNull </b> <br/>
	 * 
	 * When the param "object" is null ,Then throw new RedisClientException(message). <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param object
	 * @param message
	 *            void
	 *
	 */
	public static void notNull(Object object, String message) {
		if (null == object) {
			throw new RedisClientException(message);
		}
	}
	
	/**
	 * 
	 * <b>notBlank </b> <br/>
	 * 
	 * if a CharSequence is whitespace, empty ("") or null Then "throw new
	 * RedisClientException(message)".<br/>
	 * 
	 * <pre>
	 * AssertHelper.notBlank(null)      = throw new RedisClientException(message)
	 * AssertHelper.notBlank("")        = throw new RedisClientException(message)
	 * AssertHelper.notBlank(" ")       = throw new RedisClientException(message)
	 * AssertHelper.notBlank("bob")     = true
	 * AssertHelper.notBlank("  bob  ") = true
	 * </pre>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param cs
	 * @param message
	 *            void
	 *
	 */
	public static boolean notBlank(CharSequence cs, String message) {
		if (StringUtils.isBlank(cs)) {
			throw new RedisClientException(message);
		}
		return true;
	}
	
	/**
	 * 
	 * <b>isTrue </b> <br/>
	 * 
	 * When the param "isTrue" is flase ,Then throw new RedisClientException(message). <br/>
	 * 
	 * @author cpthack cpt@jianzhimao.com
	 * @param isTrue
	 * @param message
	 *            void
	 *
	 */
	public static void isTrue(boolean isTrue, String message) {
		if (!isTrue) {
			throw new RedisClientException(message);
		}
	}
	
}
