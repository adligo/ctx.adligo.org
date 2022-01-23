package org.adligo.ctx.shared;

import org.adligo.i.ctx4jse.shared.CheckMixin;

/**
 * A immutable Ctx, useful in some situations for making sure
 * all instances are created before use of instances of the Class.
 * However in most situations a CtxMutant will be preferred 
 * mostly for the lazy / just in time contetualtian creation pattern.
 * 
 * @author scott
 *
 */
public class Ctx extends AbstractRootCtx implements CheckMixin {
  
  public Ctx(CtxParams params) {
    super(params, createInstanceMap(params));
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
