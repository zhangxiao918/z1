/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.util.meta;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.collections.Transformer;

import com.skymobi.cac.maopao.passport.android.util.PackageUtils;



/**
 * @author hp
 *
 */
public class BeanMetainfoUtils {
	
    //private static final Logger logger = 
    //	LoggerFactory.getLogger(BeanMetainfoUtils.class);
	
    static public Int2TypeMetainfo createTypeMetainfo(Collection<String> packages, 
    		Transformer transformer) {
    	DefaultInt2TypeMetainfo typeMetainfo = new DefaultInt2TypeMetainfo();
		
		if (null != packages) {
			for (String pkgName : packages) {
				try {
					String[] clsNames = PackageUtils.findClassesInPackage(
							pkgName, null, null);
					for (String clsName : clsNames) {
						try {
							ClassLoader cl = Thread.currentThread().getContextClassLoader();
							//if ( logger.isDebugEnabled() ) {
							//	logger.debug("using ClassLoader {} to load Class {}", cl, clsName);
							//}
							Class<?> cls =  cl.loadClass(clsName);
							Integer value = (Integer)transformer.transform(cls);
							if ( null != value ) {
								typeMetainfo.add(value, cls);
								//logger.info("metainfo: add " + value + ":=>" + cls);
							}
						} catch (ClassNotFoundException e) {
							//logger.error("createTypeMetainfo: ", e);
						}
					}
				} catch (IOException e) {
					//logger.error("createTypeMetainfo: ", e);
				}
			}
		}
		
		return	typeMetainfo;
	}
    
    static public Int2TypeMetainfo createTypeMetainfoByClasses(Collection<Class<?>> clazzes, 
    		Transformer transformer) {
    	DefaultInt2TypeMetainfo typeMetainfo = new DefaultInt2TypeMetainfo();
		
		for (Class<?> cls : clazzes) {
			Integer value = (Integer)transformer.transform(cls);
			if ( null != value ) {
				typeMetainfo.add(value, cls);
			}
		}
		
		return	typeMetainfo;
	}
}
