package org.adligo.ctx.shared;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A immutable Ctx, useful in some situations for making sure
 * all instances are created before use of instances of the Class.
 * However in most situations a CtxMutant will be preferred 
 * mostly for the lazy / just in time contetualtian creation pattern.
 * 
 * @author scott
 *
 */
public class Ctx extends AbstractCtx {
  
  public Ctx(CtxParams params) {
    this(params, false);
  }
  
  @SuppressWarnings("unchecked")
  public Ctx(CtxParams params, boolean allowNullReturn) {
    this(params, allowNullReturn, 
        (m) -> Collections.unmodifiableMap(new HashMap<>(m)));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected Ctx(CtxParams params, boolean allowNullReturn, 
      Function<Map, Map> unmodifiableMapSupplier) {
    super(params, allowNullReturn, unmodifiableMapSupplier,
        unmodifiableMapSupplier.apply(params.getInstanceMap()));
  }

  @Override
  public Object get(String name) {
    Object r = instanceMap.get(name);
    if (r == null) {
      if (!allowNullReturn) {
        throw new IllegalStateException(NO_INSTANCE_FOuND_FOR_KEY_1 +
            name + NO_INSTANCE_FOuND_FOR_KEY_2);
      }
    }
    return r;
  }
}
