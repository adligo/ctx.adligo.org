package org.adligo.ctx.jse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.adligo.ctx.shared.CtxParams;

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
  public static final String UNABLE_TO_FIND_S_IN_THIS_CONTEXT = "Unable to find '%s' in this context!";
  public static final String BAD_NAME = "Names passed to the create bean method MUST be java.lang.Class names!\n\t%s";
  public static final String UNABLE_TO_FIND_BEAN_CONSTRUCTOR_FOR_S = "Unable to find bean constructor for %s!";
  public static final Class<?> [] EMPTY_CLAZZ_ARRAY = new Class<?>[] {};
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[] {};
  public static final String UNABLE_TO_CREATE_INSTANCE_OF_S = "Unable to create instance of %s";

  @SuppressWarnings("unchecked")
  static <T> T createThroughReflection(Class<T> clazz) {
    Constructor<?> c = null;
    try {
      c = clazz.getConstructor(EMPTY_CLAZZ_ARRAY);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new IllegalStateException(String.format(UNABLE_TO_FIND_BEAN_CONSTRUCTOR_FOR_S, clazz), e);
    }
    
    try {
      return (T) c.newInstance(EMPTY_OBJECT_ARRAY);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new IllegalStateException(String.format(UNABLE_TO_CREATE_INSTANCE_OF_S, clazz), e);
    }
  }
  public JseCtxMutant() {
    this(new CtxParams());
  }

  public JseCtxMutant(CtxParams params) {
    super(params);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T create(Class<T> clazz) {
    Object r = super.create(clazz);  
    if (r != null) {
      return (T) r;
    }
    return createThroughReflection(clazz);
  }

  @Override
  public Object create(String name) {
    Object r =  super.create(name);  
    if (r != null) {
      return r;
    }
    try {
      return create(Class.forName(name));
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(String.format(
          BAD_NAME, name));
    }
  }

}
