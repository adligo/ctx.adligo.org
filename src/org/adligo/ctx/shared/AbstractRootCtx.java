package org.adligo.ctx.shared;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

public abstract class AbstractRootCtx extends AbstractCtx {
  public static final String HANDLER = "handler";

  public static Consumer<Throwable> newHandler() {
    return (t) -> {
      t.printStackTrace(System.out);;
    };
  }
  protected final Consumer<Throwable> handler;

  protected AbstractRootCtx(CtxParams params, 
      Map<String, Object> instanceMap) {
    super(params, instanceMap);
    handler = setHandler(instanceMap);
  }

  @Override
  public void handle(Throwable t) {
    handler.accept(t);
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
