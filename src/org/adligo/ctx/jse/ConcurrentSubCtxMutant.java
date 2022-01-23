package org.adligo.ctx.jse;

import org.adligo.ctx.shared.AbstractSubCtx;
import org.adligo.ctx.shared.CtxParams;
import org.adligo.i.threads.I_ThreadCtx;

public class ConcurrentSubCtxMutant extends AbstractSubCtx 
implements I_ThreadCtx {
  //this simply allows the stubbing of the synchronized method
  private final I_ThreadCtx threadCtx;
  
  public ConcurrentSubCtxMutant() {
    this(new CtxParams());
  }
  
  public ConcurrentSubCtxMutant(CtxParams params) {
    super(params, createConcurrentInstanceMap(params));
    threadCtx = notNull(params.getThreadCtx());
  }
  
  @Override
  public Object get(String name) {
    Object r = instanceMap.get(name);
    if (r == null) {
      r = threadCtx.synchronize(instanceMap, () -> {
        Object sr = instanceMap.get(name);
        if (sr == null) {
          sr = create(name);
          instanceMap.put(name, sr);
        }
        return sr;
      });
    }
    if (r == null) {
      return parent.create(name);
    }
    return r;
  }
}
