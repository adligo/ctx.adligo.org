package org.adligo.ctx.shared;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.adligo.i.ctx4jse.shared.CheckMixin;

/**
 * This class provides the Copy Constructor parameter to the {@link AbstractCtx}
 * class<br/>
 * <br/>
 * 
 * @author scott<br/>
 *         <br/>
 * 
 *         <pre>
 *         <code>
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
 * </code>
 * 
 *         <pre>
 */

public class CtxParams implements CheckMixin {
  public static final String A_NON_NULL_INSTANCE_IS_REQUIRED = "A non null instance is required!";
  public static final String A_NON_NULL_SUPPLIER_IS_REQUIRED = "A non null supplier is required!";
  public static final String A_NON_NULL_NAME_IS_REQUIRED = "A non null name is required!";
  @SuppressWarnings("rawtypes")
  private final Map<String, Supplier> creationMap;
  private final Map<String, Object> instanceMap;

  public CtxParams() {
    creationMap = new HashMap<>();
    instanceMap = new HashMap<>();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public Map<String, Supplier<Object>> getCreationMap() {
    // enforce encapsulation, block external mutation
    return Collections.unmodifiableMap((Map) creationMap);
  }

  public Map<String, Object> getInstanceMap() {
    // enforce encapsulation, block external mutation
    return Collections.unmodifiableMap(instanceMap);
  }

  public <T> CtxParams set(String name, T instance) {
    instanceMap.put(notNull(A_NON_NULL_NAME_IS_REQUIRED, name),
        notNull(A_NON_NULL_INSTANCE_IS_REQUIRED, instance));
    return this;
  }

  /**
   * Type inference is bad on GWT, and may have issues elsewhere
   * @param name
   * @param supplier
   * @return
   */
  public <T> CtxParams setCreator(String name, Supplier<T> supplier) {
    creationMap.put(notNull(A_NON_NULL_NAME_IS_REQUIRED, name),
        notNull(A_NON_NULL_SUPPLIER_IS_REQUIRED, supplier));
    return this;
  }
  

  public <T> CtxParams remove(String name) {
    instanceMap.remove(name);
    return this;
  }

  /**
   * Type inference is bad on GWT, and may have issues elsewhere
   * @param name
   * @param supplier
   * @return
   */
  public <T> CtxParams removeCreator(String name) {
    creationMap.remove(name);
    return this;
  }
}
