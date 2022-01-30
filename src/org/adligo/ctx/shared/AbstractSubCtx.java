package org.adligo.ctx.shared;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.adligo.i_ctx4jse.shared.I_JseCtx;

/**
 * This class a sub context under some sort of root context or other
 * sub context.
 * 
 * @author scott 
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

public abstract class AbstractSubCtx extends AbstractCtx {
  public static final String HANDLER = "handler";

  protected final I_JseCtx parent;
  protected final Optional<Consumer<Throwable>> handlerOpt;

  protected AbstractSubCtx(CtxParams params, 
      Map<String, Object> instanceMap) {
    super(params, instanceMap);
    parent = notNull(params.getParent());
    handlerOpt = setHandler(instanceMap);
  }


  @Override
  public <T> T create(Class<T> clazz) {
    T r = super.create(clazz);
    if (r != null) {
      return r;
    }
    return parent.create(clazz);
  }

  @Override
  public Object create(String name) {
    Object r = super.create(name);
    if (r != null) {
      return r;
    }
    return parent.create(name);
  }

  @Override
  public <T> T get(Class<T> clazz) {
    T r = super.get(clazz);
    if (r == null) {
      return parent.get(clazz);
    }
    return r;
  }

  @Override
  public void handle(Throwable t) {
    if (handlerOpt.isPresent()) {
      handlerOpt.get().accept(t);
    } else {
      parent.handle(t);
    }
  }
 
  @SuppressWarnings({ "unchecked", "rawtypes" })
  Optional<Consumer<Throwable>> setHandler(Map<String, ?> instanceMap) {
    Consumer<Throwable> hc = (Consumer) instanceMap.get(HANDLER);
    if (hc == null) {
      Supplier hcSup = creationMap.get(HANDLER);
      if (hcSup != null) {
        hc = (Consumer) hcSup.get();
      }
    }
    if (hc != null) {
      return Optional.of(hc);  
    } else {
      return Optional.empty();
    }
  }
}
