package org.adligo.ctx.jse;

import java.util.concurrent.Executor;

import org.adligo.ctx.shared.AbstractRootCtx;
import org.adligo.ctx.shared.CtxParams;
import org.adligo.i_threads.I_ThreadCtx;
import org.adligo.i_threads4jse.I_ThreadJseCtx;

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

public class ConcurrentCtxMutant extends AbstractRootCtx 
implements I_ThreadJseCtx {
  //this simply allows the stubbing of the synchronized method
  private final I_ThreadCtx theadCtx;
  
  public ConcurrentCtxMutant() {
    this(new CtxParams());
  }

  public ConcurrentCtxMutant(CtxParams params) {
    super(params, createConcurrentInstanceMap(params));
    theadCtx = notNull(params.getThreadCtx());
  }

  @Override
  public Object get(String name) {
    Object r = instanceMap.get(name);
    if (r == null) {
      return theadCtx.synchronize(instanceMap, () -> {
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
  

  @Override
  public Executor getDefaultExecutor() {
    return newWorkStealingPool(availableProcessors());
  }
}
