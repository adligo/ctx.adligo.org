package org.adligo.ctx.shared;

public class CtxMutant extends AbstractRootCtx {

  public CtxMutant() {
    this(new CtxParams());
  }
  
  public CtxMutant(CtxParams params) {
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
    return r;
  }

}
