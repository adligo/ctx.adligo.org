package org.adligo.ctx.shared;

import org.adligo.i_ctx4jse.shared.CheckMixin;
import org.adligo.i_ctx4jse.shared.I_JseCtx;
import org.adligo.i_ctx4jse.shared.I_JseCtxAware;

/**
 * This class provides a base {@link I_JseCtxAware}, note there is no
 * similar class that is {@link I_CtxAware} in this library.  {@link I_CtxAware}
 * is used for backward compatible old JME stuff.
 * <br/>
 * 
 * @author scott<br/>
 *         <br/>
 * 
 *<pre><code>
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
public class CtxObject implements CheckMixin, I_JseCtxAware {
  private final I_JseCtx _ctx;
  
  public CtxObject(I_JseCtx ctx) {
    _ctx = notNull(ctx);
  }

  @Override
  public I_JseCtx getCtx() {
    return _ctx;
  }
}
