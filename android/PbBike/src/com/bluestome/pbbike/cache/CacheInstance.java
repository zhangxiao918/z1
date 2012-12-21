package com.bluestome.pbbike.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存实例
 * @ClassName: CacheInstance
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-9-10 下午03:29:08
 */
public class CacheInstance {

    //路名
    private static Map<String,List> roadNameCache = new HashMap<String,List>();
    
    public static void setRoadNameCache(String roadName,List roadList){
        roadNameCache.put(roadName, roadList);
    }
    
    public static List getRoadNameCache(String roadName){
        return roadNameCache.get(roadName);
    }
    
    public static void clearAll(){
        if(null != roadNameCache && roadNameCache.size() > 0)
            roadNameCache.clear();
    }
}
