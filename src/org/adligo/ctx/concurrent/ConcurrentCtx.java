package org.adligo.ctx.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.adligo.ctx.shared.Ctx;
import org.adligo.ctx.shared.CtxMutant;
import org.adligo.i.threads.I_ThreadCtx;

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

public class ConcurrentCtx extends Ctx implements I_ThreadCtx {
  

  public ConcurrentCtx(CtxMutant cm) {
    super(cm, false);
  }
  
  @SuppressWarnings("unchecked")
  public ConcurrentCtx(CtxMutant cm, boolean allowNullReturn) {
    super(cm, allowNullReturn, 
        (m) -> Collections.unmodifiableMap(new HashMap<>(m)),
        (m) -> new ConcurrentHashMap<>(m));
  }
  

  @Override
  public Object get(String name) {
    Map<String, Object> instanceMap = getInstanceMap();
    Object r = instanceMap.get(name);
    if (r == null) {
      return synchronize(instanceMap, () -> {
        Object sr = instanceMap.get(name);
        if (sr == null) {
          sr = create(name);
          instanceMap.put(name, sr);
        }
        return sr;
      });
    }
    return r;
  }
}
