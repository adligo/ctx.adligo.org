package org.adligo.ctx.jse;

import java.util.concurrent.Executor;

import org.adligo.ctx.shared.AbstractSubCtx;
import org.adligo.ctx.shared.CtxParams;
import org.adligo.i_threads.I_ThreadCtx;
import org.adligo.i_threads4jse.I_ThreadJseCtx;

public class ConcurrentSubCtxMutant extends AbstractSubCtx 
implements I_ThreadJseCtx {
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

  @Override
  public Executor getDefaultExecutor() {
    return newWorkStealingPool(availableProcessors());
  }
}
