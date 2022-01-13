package org.adligo.ctx.shared;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.adligo.i.ctx4jse.shared.I_PrintCtx;


/**
 * This class provides a Functional Implementation of the Context Creation
 * and Contextualtian patterns for GWT, JSweet's Javascript / Typescript
 * and applications that are compiled into Native Executibles from
 * Java Source code. <br/><br/>
 *   If your running code on the JSE, the following project provides
 * a Object Oriented Implementation that is facilitated through Java's 
 * Reflection API;<br/>
 * {@link <a href="https://github.com/adligo/ctx4jse.adligo.org">ctx4jse.adligo.org</a>}
 * <br/><br/>
 * @author scott<br/><br/>
 * <pre><code>
 * ---------------- Apache ICENSE-2.0 --------------------------
 *
 * Copyright 2022 Adligo Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </code><pre>
 */

public class Ctx implements I_PrintCtx {
	public static final String THE_SUPPLIER_FOR_S_RETURNED_NULL = "The Supplier for '%s' returned null?";
	public static final String NO_SUPPLIER_FOUND_FOR_S = "No Supplier found for %s";
	public static final String NO_NULL_KEYS = "Null keys are NOT allowed!";
	public static final String NO_NULL_VALUES = "Null values are NOT allowed!";
	
	private final Map<String, Supplier<Object>> creationMap;
	private final Map<String, Object> instanceMap;
  
	public Ctx(CtxMutant cm) {
		
		creationMap = Collections.unmodifiableMap(new HashMap<>(cm.getCreationMap()));
		checkParams(creationMap);
		instanceMap = new ConcurrentHashMap<>(cm.getInstanceMap());
		checkParams(instanceMap);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(Class<T> clazz) {
		return (T) create(clazz.getName());
	}

	@Override
	public Object create(String name) {
		Supplier<Object> s = creationMap.get(name);
		if (s == null) {
			throw new IllegalStateException(String.format(
					NO_SUPPLIER_FOUND_FOR_S, name));
		}
		Object r = s.get();
		if (r == null) {
			throw new IllegalStateException(String.format(
					THE_SUPPLIER_FOR_S_RETURNED_NULL, name));
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		return (T) get(clazz.getName());
	}

	@Override
	public Object get(String name) {
		Object r = instanceMap.get(name);
		if (r == null) {
			synchronized(instanceMap) {
				r = instanceMap.get(name);
				if (r == null) {
			    r = create(name);
			    instanceMap.put(name, r);
				}
			}
		}
		return r;
	}


	private void checkParams(Map<?,?> map) {
		if (map.containsKey(null)) {
			throw new IllegalArgumentException(NO_NULL_KEYS);
		}
		if (map.containsValue(null)) {
			throw new IllegalArgumentException(NO_NULL_VALUES);
		}
	}
}
