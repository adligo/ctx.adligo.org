package org.adligo.ctx.shared;

/**
 * This class provides a mutable sub context that can wrap any kind of
 * ctx.  
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
public class SubCtxMutant extends AbstractSubCtx {

  public SubCtxMutant() {
    this(new CtxParams());
  }
  
  public SubCtxMutant(CtxParams params) {
    super(params, createMutableInstanceMap(params));
  }
  
  @Override
  public Object get(String name) {
    Object r = instanceMap.get(name);
    if (r == null) {
      r = create(name);
      if (r != null) {
        instanceMap.put(name, r);
      }
    }
    if (r == null) {
      return parent.create(name);
    }
    return r;
  }
}
