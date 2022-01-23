package org.adligo.ctx.shared;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.adligo.i.ctx4jse.shared.CheckMixin;
import org.adligo.i.ctx4jse.shared.I_PrintCtx;

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
I_PrintCtx, Consumer<Throwable> {
  public static final String HANDLER = "handler";
  public static final String NO_SUPPLIER_FOUND_FOR_S = "No Supplier found for ";
  public static final String NO_NULL_KEYS = "Null keys are NOT allowed!";
  public static final String NO_NULL_VALUES = "Null values are NOT allowed!";
  public static final String NO_INSTANCE_FOuND_FOR_KEY_1 = "No instance was found for '";
  public static final String NO_INSTANCE_FOuND_FOR_KEY_2 = "'.";
  public static final String THE_SUPPLIER_FOR_S_RETURNED_NULL_1 = "The Supplier for '";
  public static final String THE_SUPPLIER_FOR_S_RETURNED_NULL_2 = "' returned null?";
  
  public static Consumer<Throwable> newHandler() {
    return (t) -> {
      t.printStackTrace(System.out);;
    };
  }
  protected final boolean allowNullReturn;
  protected final Map<String, Supplier<Object>> creationMap;
  protected final Consumer<Throwable> handler;
  protected final Map<String, Object> instanceMap;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected AbstractCtx(CtxParams params, boolean allowNullReturn,
      Function<Map, Map> unmodifiableMapSupplier,
      Map<String, Object> instanceMap) {
    this.allowNullReturn = allowNullReturn;
    creationMap = unmodifiableMapSupplier.apply(params.getCreationMap());
    checkParams(creationMap);
    this.instanceMap = instanceMap;
    checkParams(instanceMap);
    handler = setHandler(instanceMap);
  }
 
  @Override
  public void accept(Throwable t) {
    handle(t);
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
      throw new IllegalStateException(NO_SUPPLIER_FOUND_FOR_S + name);
    }
    Object r = s.get();

    if (r == null) {
      if (allowNullReturn) {
        return null;
      }
      throw new IllegalStateException(THE_SUPPLIER_FOR_S_RETURNED_NULL_1 +
          name + THE_SUPPLIER_FOR_S_RETURNED_NULL_2);
    }
    return r;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T get(Class<T> clazz) {
    return (T) get(clazz.getName());
  }

  @Override
  public void handle(Throwable t) {
    handler.accept(t);
  }

  void checkParams(Map<?, ?> map) {
    notContainsKey(NO_NULL_KEYS, map, null);
    notContainsValue(NO_NULL_VALUES, map, null);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  Consumer<Throwable> setHandler(Map<String, ?> instanceMap) {
    Consumer<Throwable> hc = (Consumer) instanceMap.get(HANDLER);
    if (hc == null) {
      Supplier hcSup = creationMap.get(HANDLER);
      if (hcSup != null) {
        hc = (Consumer) hcSup.get();
      }
    }
    if (hc != null) {
      return hc;  
    } else {
      return newHandler();
    }
  }
}
