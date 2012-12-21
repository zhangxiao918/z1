/**
 * 
 */
package com.skymobi.android.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 *
 */
public class PropertyableUtils {
	
    private static final Logger logger = 
    	LoggerFactory.getLogger(PropertyableUtils.class);
    
	public static void copyProperties(
			Propertyable from, MutablePropertyable to, String ... keys) {
		for (String key : keys ) {
			to.setProperty(key, from.getProperty(key));
		}
	}
	
    public static void copyPropertiesToPojo(
    		Propertyable from, Object to, String ... keys) {
	    if ( null != from && null != to ) {
	        for ( String name : keys ) {
	        	try {
	        		PropertyUtils.setProperty(to, name, from.getProperty(name) );
	    		} catch (Exception e) {
	    			logger.error("error in copy properties : {}", e);
	    		} 
	        }
	    }
    }
    
	static private class POJOInvocationHandler 
		implements InvocationHandler {
		
	    private static final Logger logger = 
	    	LoggerFactory.getLogger(POJOInvocationHandler.class);
		
		private MutablePropertyable propertyable;
		
		public POJOInvocationHandler(MutablePropertyable propertyable) {
			this.propertyable = propertyable;
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
            final String methodName = method.getName();
            final Class<?> returnType = method.getReturnType();

            /* Inexplicably, InvocationHandler specifies that args is null
               when the method takes no arguments rather than a
               zero-length array.  */
            final int nargs = (args == null) ? 0 : args.length;

            if (methodName.startsWith("get")
                && methodName.length() > 3
                && nargs == 0
                && !returnType.equals(Void.TYPE)) {
                return propertyable.getProperty(methodName.substring(3));
            }

            if (methodName.startsWith("is")
                && methodName.length() > 2
                && nargs == 0
                && (returnType.equals(Boolean.TYPE)
                || returnType.equals(Boolean.class))) {
                return propertyable.getProperty(methodName.substring(2));
            }

            if (methodName.startsWith("set")
                && methodName.length() > 3
                && nargs == 1
                && returnType.equals(Void.TYPE)) {
            	propertyable.setProperty(methodName.substring(3), args[0]);
                return null;
            }
            
            logger.warn("Invalid method {} while valid is : getXXX/isXXX/setXXX"
            		+"\r\n Or invalid parameters count {}", 
            		methodName, nargs );
			return null;
		}
		
	}
	
	
    @SuppressWarnings("unchecked")
	public static <T> T newPOJOProxy(
    		MutablePropertyable propertyable,
		       Class<T> interfacePOJO) {
		return (T)Proxy.newProxyInstance(
				interfacePOJO.getClassLoader(), 
				new Class<?>[]{interfacePOJO}, 
				new POJOInvocationHandler(propertyable) );
    }
}
