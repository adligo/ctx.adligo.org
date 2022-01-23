package org.adligo.ctx.jse;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.adligo.ctx.shared.AbstractCtx;
import org.adligo.ctx.shared.CtxParams;
import org.adligo.i.ctx4jse.shared.I_PrintCtx;
import org.adligo.i.threads.I_ThreadCtx;

/**
 * This class provides a Mutable Concurrent Context useable on the JVM
 * with applications compiling to Native executables (i.e. with GraalVM etc). 
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

public class ConcurrentCtxMutant extends AbstractCtx implements I_ThreadCtx, I_PrintCtx {

  public ConcurrentCtxMutant() {
    this(new CtxParams(), false);
  }

  public ConcurrentCtxMutant(CtxParams cm) {
    this(cm, false);
  }
  
  @SuppressWarnings("unchecked")
  public ConcurrentCtxMutant(CtxParams cm, boolean allowNullReturn) {
    this(cm, allowNullReturn, 
        (m) -> Collections.unmodifiableMap(m),
        (m) -> new ConcurrentHashMap<>(m));
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected ConcurrentCtxMutant(CtxParams params, boolean allowNullReturn,
      Function<Map, Map> unmodifiableMapSupplier,
      Function<Map, Map> concurrentMapSupplier) {
    super(params, allowNullReturn, unmodifiableMapSupplier,
        concurrentMapSupplier.apply(params.getInstanceMap()));
  }

  @Override
  public Object get(String name) {
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
