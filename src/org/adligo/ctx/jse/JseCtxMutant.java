package org.adligo.ctx.jse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.function.BiFunction;

import org.adligo.ctx.shared.CtxParams;
import org.adligo.i_ctx.shared.I_Ctx;
import org.adligo.i_ctx.shared.I_CtxAware;
import org.adligo.i_ctx4jse.shared.I_JseCtx;
import org.adligo.i_ctx4jse.shared.I_JseCtxAware;

/**
 * This class provides a Mutable Concurrent Context useable on the standard JVM.
 * However this code will NOT compile to a native executable due to its use of
 * reflections, for that use {@link ConcurrentCtxMutant}.
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

public class JseCtxMutant extends ConcurrentCtxMutant {
  public static final String UNABLE_TO_FIND_CONSTRUCTOR_WITH_I_JSE_CTX_ON_S = "Unable to find constructor with I_JseCtx on %s ";
  public static final String BAD_NAME = "Names passed to the create bean method MUST be java.lang.Class names!\n\t%s";
  public static final String UNABLE_TO_FIND_BEAN_CONSTRUCTOR_FOR_S = "Unable to find bean constructor for %s!";
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};
  
  public static final String UNABLE_TO_CREATE_INSTANCE_OF_S = "Unable to create instance of %s";
  public static final String UNABLE_TO_FIND_S_IN_THIS_CONTEXT = "Unable to find '%s' in this context!";
  
  /**
   * prevent external mutation of array
   */
  private static final Class<?> [] CTX_CLAZZ_ARRAY = new Class<?>[] {I_Ctx.class};
  /**
   * prevent external mutation of array
   */
  private static final Class<?> [] EMPTY_CLAZZ_ARRAY = new Class<?>[] {};
  /**
   * prevent external mutation of array
   */
  private static final Class<?> [] JSE_CTX_CLAZZ_ARRAY = new Class<?>[] {I_JseCtx.class};

  static <T> T createThroughReflection(I_JseCtx ctx, Class<T> clazz) {
    if (clazz.isAssignableFrom(I_JseCtxAware.class)) {
      return createWithJseCtxConstructor(ctx, clazz);
    }
    if (clazz.isAssignableFrom(I_CtxAware.class)) {
      return createWithCtxConstructor(ctx, clazz);
    }    
    return createWithBeanConstructor(ctx, clazz);
  }

  @SuppressWarnings("unchecked")
  static <T> T createWithBeanConstructor(I_JseCtx ctx, Class<T> clazz) {
    try {
      Constructor<?> c = clazz.getConstructor(EMPTY_CLAZZ_ARRAY);
      try {
        return (T) c.newInstance(EMPTY_OBJECT_ARRAY);
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new IllegalStateException(String.format(UNABLE_TO_CREATE_INSTANCE_OF_S, clazz), e);
      }
    } catch (NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(String.format(UNABLE_TO_FIND_BEAN_CONSTRUCTOR_FOR_S, clazz), e);
    }
  }

  @SuppressWarnings("unchecked")
  static <T> T createWithCtxConstructor(I_JseCtx ctx, Class<T> clazz) {
    try {
      Constructor<?> c = clazz.getConstructor(CTX_CLAZZ_ARRAY);
      try {
        return (T) c.newInstance(new Object[] {ctx});
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new IllegalStateException(String.format(UNABLE_TO_CREATE_INSTANCE_OF_S, clazz), e);
      }
    } catch (NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(String.format(UNABLE_TO_FIND_CONSTRUCTOR_WITH_I_JSE_CTX_ON_S, clazz), e);
    }
  }
  
  @SuppressWarnings("unchecked")
  static <T> T createWithJseCtxConstructor(I_JseCtx ctx, Class<T> clazz) {
    try {
      Constructor<?> c = clazz.getConstructor(JSE_CTX_CLAZZ_ARRAY);
      try {
        return (T) c.newInstance(new Object[] {ctx});
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new IllegalStateException(String.format(UNABLE_TO_CREATE_INSTANCE_OF_S, clazz), e);
      }
    } catch (NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(String.format(UNABLE_TO_FIND_CONSTRUCTOR_WITH_I_JSE_CTX_ON_S, clazz), e);
    }
  }

  @SuppressWarnings("unchecked")
  static BiFunction<I_JseCtx, Class<?>,?> setCreationWrapper(CtxParams params) {
    Set<String> ctxClasses = params.getContextClasses();
    if (ctxClasses.size() >= 0) {
      Set<String> clazzNames = params.getTreeSetSupplier().apply(ctxClasses);
      return (ctx, clazz) -> {
        if (clazzNames.contains(clazz.getName())) {
          return createWithJseCtxConstructor(ctx, clazz);
        }
        if (I_JseCtxAware.class.isAssignableFrom(clazz)) {
          return createWithJseCtxConstructor(ctx, clazz);
        }
        return createWithBeanConstructor(ctx, clazz);
      };      
    } else {
      return (ctx, clazz) -> {
        return createThroughReflection(ctx, clazz);
      };
    }
  }
  private final BiFunction<I_JseCtx, Class<?>,?> _creationWrapper;
  
  public JseCtxMutant() {
    this(new CtxParams());
  }

  public JseCtxMutant(CtxParams params) {
    super(params);
    _creationWrapper = setCreationWrapper(params);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T create(Class<T> clazz) {
    String name = clazz.getName();
    if (super.hasSupplier(name, false)) {
      return super.create(clazz);
    }
    //any kind of reflection will throw it's own exception if 
    //returning null here
    return (T) _creationWrapper.apply(this, clazz);
  }

  @Override
  public Object create(String name) {
    if (super.hasSupplier(name, false)) {
      return super.create(name);
    } 
    try {
      //any kind of reflection will throw it's own exception if 
      //returning null here
      return _creationWrapper.apply(this, Class.forName(name));
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(String.format(
          BAD_NAME, name));
    }
  }

}
