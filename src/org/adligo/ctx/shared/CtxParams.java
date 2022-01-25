package org.adligo.ctx.shared;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.adligo.i.ctx4jse.shared.CheckMixin;
import org.adligo.i.ctx4jse.shared.I_JseCtx;
import org.adligo.i.ctx4jse.shared.I_JseCtxAware;
import org.adligo.i.threads.I_ThreadCtx;

/**
 * This class provides the Copy Constructor parameter to the {@link AbstractRootCtx}
 * class<br/>
 * <br/>
 * 
 * @author scott<br/>
 *         <br/>
 * 
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

@SuppressWarnings ({"unchecked", "rawtypes"})
public class CtxParams implements CheckMixin {
  public static final String A_NON_NULL_INSTANCE_IS_REQUIRED = "A non null instance is required!";
  public static final String A_NON_NULL_SUPPLIER_IS_REQUIRED = "A non null supplier is required!";
  public static final String A_NON_NULL_NAME_IS_REQUIRED = "A non null name is required!";
  
  private boolean _allowNullReturn;
  private final Map<String, Supplier> _creationMap = new HashMap<>();
  private final Map<String, Object> _instanceMap = new HashMap<>();
  private I_JseCtx _parent;
  
  /**
   * This is a map that slightly improves the efficiency of the 
   * identification of I_JseCtxAware classes for Reflection Creation
   * on the JVM.
   */
  private Set<String> _contextClasses = new TreeSet<>();
  private Function<Map, Map> _concurrentMapSupplier = (m) -> new ConcurrentHashMap<>();
  private Function<Map, Map> _hashMapSupplier = (m) -> new HashMap<>();
  private I_ThreadCtx _threadCtx = new I_ThreadCtx() {};
  private Function<Set, TreeSet> _treeSetSupplier = (m) -> new TreeSet<>();
  private Function<Map, Map> _unmodifiableMapSupplier = (m) -> Collections.unmodifiableMap(m);

  public Set<String> getContextClasses() {
    return _contextClasses;
  }
  
  public Function<Map, Map> getConcurrentMapSupplier() {
    return _concurrentMapSupplier;
  }
  
  public Map<String, Supplier<Object>> getCreationMap() {
    // enforce encapsulation, block external mutation
    return Collections.unmodifiableMap((Map) _creationMap);
  }

  public Function<Map, Map> getHashMapSupplier() {
    return _hashMapSupplier;
  }
  
  public Map<String, Object> getInstanceMap() {
    // enforce encapsulation, block external mutation
    return Collections.unmodifiableMap(_instanceMap);
  }

  public I_JseCtx getParent() {
    return _parent;
  }

  public I_ThreadCtx getThreadCtx() {
    return _threadCtx;
  }

  public Function<Set, TreeSet> getTreeSetSupplier() {
    return _treeSetSupplier;
  }

  public Function<Map, Map> getUnmodifiableMapSupplier() {
    return _unmodifiableMapSupplier;
  }

  public <T> CtxParams set(String name, T instance) {
    _instanceMap.put(notNull(A_NON_NULL_NAME_IS_REQUIRED, name),
        notNull(A_NON_NULL_INSTANCE_IS_REQUIRED, instance));
    return this;
  }

  public CtxParams setAllowNullReturn(boolean allowNullReturn) {
    _allowNullReturn = allowNullReturn;
    return this;
  }

  public <T> CtxParams setCreator(Class<?> clazz, Supplier<T> supplier) {
    return setCreator(clazz.getName(), supplier);
  }
  
  /**
   * Type inference is bad on GWT, and may have issues elsewhere
   * @param name
   * @param supplier
   * @return
   */
  public <T> CtxParams setCreator(String name, Supplier<T> supplier) {
    _creationMap.put(notNull(A_NON_NULL_NAME_IS_REQUIRED, name),
        notNull(A_NON_NULL_SUPPLIER_IS_REQUIRED, supplier));
    return this;
  }

  public void setParent(I_JseCtx parent) {
    _parent = notNull(parent);
  }

  public CtxParams setConcurrentMapSupplier(Function<Map, Map> concurrentMapSupplier) {
    _concurrentMapSupplier = concurrentMapSupplier;
    return this;
  }

  public CtxParams setHashMapSupplier(Function<Map, Map> hashMapSupplier) {
    _hashMapSupplier = hashMapSupplier;
    return this;
  }

  public CtxParams setThreadCtx(I_ThreadCtx threadCtx) {
    _threadCtx = threadCtx;
    return this;
  }

  public CtxParams setUnmodifiableMapSupplier(Function<Map, Map> unmodifiableMapSupplier) {
    _unmodifiableMapSupplier = unmodifiableMapSupplier;
    return this;
  }

  public boolean isAllowNullReturn() {
    return _allowNullReturn;
  }

  public CtxParams setContextClasses(Collection<Class<?>> contextClasses) {
    _contextClasses.clear();
    for (Class<?> c : contextClasses) {
      _contextClasses.add(c.getName());
    }
    return this;
  }


  public <T> CtxParams remove(String name) {
    _instanceMap.remove(name);
    return this;
  }

  /**
   * Type inference is bad on GWT, and may have issues elsewhere
   * @param name
   * @param supplier
   * @return
   */
  public <T> CtxParams removeCreator(String name) {
    _creationMap.remove(name);
    return this;
  }

  public <T> CtxParams removeContextClass(Class<? super I_JseCtxAware> clazz) {
    _contextClasses.remove(clazz);
    return this;
  }

  public CtxParams setTreeSetSupplier(Function<Set, TreeSet> treeSetSupplier) {
    _treeSetSupplier = treeSetSupplier;
    return this;
  }

}
