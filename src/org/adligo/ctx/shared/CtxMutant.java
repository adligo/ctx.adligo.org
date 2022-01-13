package org.adligo.ctx.shared;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class CtxMutant {
	public static final String A_NON_NULL_INSTANCE_IS_REQUIRED = "A non null instance is required!";
	public static final String A_NON_NULL_SUPPLIER_IS_REQUIRED = "A non null supplier is required!";
	public static final String A_NON_NULL_NAME_IS_REQUIRED = "A non null name is required!";
	private final Map<String, Supplier<Object>> creationMap = new HashMap<>();
	private final Map<String, Object> instanceMap = new HashMap<>();
	
	public CtxMutant add(String name, Supplier<Object> supplier) {
		creationMap.put(Objects.requireNonNull(name, A_NON_NULL_NAME_IS_REQUIRED),
				Objects.requireNonNull(supplier,  A_NON_NULL_SUPPLIER_IS_REQUIRED));
		return this;
	}
	
	public CtxMutant add(String name, Object instance) {
		instanceMap.put(Objects.requireNonNull(name, A_NON_NULL_NAME_IS_REQUIRED),
				Objects.requireNonNull(instance,  A_NON_NULL_INSTANCE_IS_REQUIRED));
		return this;
	}
	
	public Map<String, Supplier<Object>> getCreationMap() {
		//enforce encapsulation, block external mutation
		return Collections.unmodifiableMap(creationMap);
	}
	
	public Map<String, Object> getInstanceMap() {
	  //enforce encapsulation, block external mutation
		return Collections.unmodifiableMap(instanceMap);
	}
	
}
