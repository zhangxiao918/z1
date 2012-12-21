/**
 * 
 */
package com.skymobi.android.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 *
 */
public class Functor implements ClosureEx {
	
    private static final Logger logger = LoggerFactory.getLogger(Functor.class);
    
    private	boolean	canceled = false;
	private	Object 	target = null;
	private	Method	method = null;
	
	public Functor(Object target, String methodName) {
		this.target = target;
		if ( null == this.target ) {
			throw new RuntimeException(" target is null.");
		}
		
        Method[] methods = null;
        Class<?> itr = target.getClass();
        while ( !itr.equals(Object.class)) {
            methods = (Method[]) ArrayUtils.addAll(itr.getDeclaredMethods(), methods);
            itr = itr.getSuperclass();
        }
        for ( Method methodItr : methods ) {
            if ( methodItr.getName().equals(methodName) ) {
                methodItr.setAccessible(true);
                this.method = methodItr;
            }
        }
		if ( null == this.method ) {
			throw new RuntimeException("method [" + target.getClass() + "." + methodName + "] !NOT! exist.");
		}
	}

	public void execute(Object... args) {
		if ( !canceled ) {
			try {
				method.invoke(this.target, args);
			} catch (IllegalArgumentException e) {
				logger.error("execute", e);
			} catch (IllegalAccessException e) {
				logger.error("execute", e);
			} catch (InvocationTargetException e) {
				logger.error("execute", e);
			}
		}
	}

	/**
	 * @return the canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}

	/**
	 * @param canceled the canceled to set
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.target);
		sb.append(".");
		sb.append(this.method.getName());
		
		if ( this.canceled ) {
			sb.append("[canceled]");
		}
		return sb.toString();
	}
}
