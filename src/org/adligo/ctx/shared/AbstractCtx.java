package org.adligo.ctx.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.adligo.i_ctx4jse.shared.Check;
import org.adligo.i_ctx4jse.shared.CheckMixin;
import org.adligo.i_ctx4jse.shared.I_PrintCtx;

/**
 * This class provides a Functional Implementation of the Context Creation and
 * Contextualtian patterns for GWT, JSweet's Javascript / Typescript and
 * applications that are compiled into Native Executibles from Java Source code.
 * <br/>
 * <br/>
 * If your running code on the JSE, the following project provides a Object
 * Oriented Implementation that is facilitated through Java's Reflection
 * API;<br/>
 * {@link <a href=
 * "https://github.com/adligo/ctx4jse.adligo.org">ctx4jse.adligo.org</a>} <br/>
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

public abstract class AbstractCtx implements CheckMixin, 
I_PrintCtx {
  public static final String NO_SUPPLIER_FOUND_FOR_S = "No Supplier found for ";
  public static final String NO_NULL_KEYS = "Null keys are NOT allowed!";
  public static final String NO_NULL_VALUES = "Null values are NOT allowed!";
  public static final String NO_INSTANCE_FOuND_FOR_KEY_1 = "No instance was found for '";
  public static final String NO_INSTANCE_FOuND_FOR_KEY_2 = "'.";
  public static final String THE_SUPPLIER_FOR_S_RETURNED_NULL_1 = "The Supplier for '";
  public static final String THE_SUPPLIER_FOR_S_RETURNED_NULL_2 = "' returned null?";

  @SuppressWarnings("unchecked")
  protected static Map<String, Object> createConcurrentInstanceMap(CtxParams params) {
    return Check.notNull(params.getConcurrentMapSupplier())
        .apply(Check.notNull(params.getInstanceMap()));
  }
  
  @SuppressWarnings("unchecked")
  protected static Map<String, Object> createInstanceMap(CtxParams params) {
    return Check.notNull(params.getUnmodifiableMapSupplier())
        .apply(Check.notNull(params.getInstanceMap()));
  }

  @SuppressWarnings("unchecked")
  protected static Map<String, Object> createMutableInstanceMap(CtxParams params) {
    return Check.notNull(params.getHashMapSupplier())
        .apply(Check.notNull(params.getInstanceMap()));
  }
  
  public static Consumer<Throwable> newHandler() {
    return (t) -> {
      t.printStackTrace(System.out);;
    };
  }
  protected final boolean _allowNullReturn;
  protected final Map<String, Supplier<Object>> creationMap;
  protected final Map<String, Object> instanceMap;

  @SuppressWarnings({"unchecked" })
  protected AbstractCtx(CtxParams params, 
      Map<String, Object> instanceMap) {
    this._allowNullReturn = params.isAllowNullReturn();
    creationMap = notNull(params.getUnmodifiableMapSupplier())
        .apply(checkParams(params.getCreationMap()));
    this.instanceMap = instanceMap;
    checkParams(instanceMap);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T create(Class<T> clazz) {
    return (T) createInternal(clazz.getName());
  }

  @Override
  public Object create(String name) {
    return createInternal(name);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T get(Class<T> clazz) {
    return (T) get(clazz.getName());
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  Map<?, ?> checkParams(Map<?, ?> map) {
    Map m = new HashMap<>();
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      m.put(notNull(NO_NULL_KEYS,entry.getKey()),
        notNull(NO_NULL_VALUES, entry.getValue()));
      
    }
    return m;
  }

  private Object createInternal(String name) {
    Supplier<Object> s = getSupplier(name, !_allowNullReturn);
    Object r = s.get();

    if (r == null) {
      if (_allowNullReturn) {
        return null;
      }
      throw new IllegalStateException(THE_SUPPLIER_FOR_S_RETURNED_NULL_1 +
          name + THE_SUPPLIER_FOR_S_RETURNED_NULL_2);
    }
    return r;
  }

  public boolean hasSupplier(String name, boolean throwException) {
    if (getSupplier(name, false) == null) {
      return false;
    }
    return true;
  }

  private Supplier<Object> getSupplier(String name, boolean throwException) {
    Supplier<Object> s = creationMap.get(name);
    if (s == null && throwException) {
      throw new IllegalStateException(NO_SUPPLIER_FOUND_FOR_S + name);
    }
    return s;
  }
}
