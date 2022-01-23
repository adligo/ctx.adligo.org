package org.adligo.ctx.shared;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CtxMutant extends AbstractCtx {

  public CtxMutant() {
    this(new CtxParams(), false);
  }
  
  public CtxMutant(CtxParams params) {
    this(params, false);
  }
  
  @SuppressWarnings("unchecked")
  public CtxMutant(CtxParams params, boolean allowNullReturn) {
    this(params, allowNullReturn, 
        (m) -> Collections.unmodifiableMap(new HashMap<>(m)),
        (m) -> new HashMap<>(m));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected CtxMutant(CtxParams params, boolean allowNullReturn, 
      Function<Map, Map> unmodifiableMapSupplier,
      Function<Map, Map> hashMapSupplier) {
    super(params, allowNullReturn, unmodifiableMapSupplier,
        hashMapSupplier.apply(params.getInstanceMap()));
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
