/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.kaartenbalie.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * B3partners B.V. http://www.b3partners.nl
 * @author Roy
 * Created on 30-aug-2011, 16:36:21
 */
public class URLCache {
    private final int cacheTime;
    private HashMap<String,CachedUrl> cachedUrls = new HashMap<String,CachedUrl>(); 
    
    private static final Log log = LogFactory.getLog(URLCache.class);
    //private static final Object monitor=new Object();
    public URLCache(int cacheTime){
        this.cacheTime=cacheTime;
    }
    /**
     * Caches a url. If forceRenew = true it renews the the cache for this url, even if
     * it's not expired.
     */
    public void cacheUrl(String url, boolean forceRenew){
        synchronized(cachedUrls){
            clearOldCache(cacheTime);
            CachedUrl cachedUrl=cachedUrls.get(url);            
            if (cachedUrl==null || forceRenew){
               cachedUrls.put(url, new CachedUrl(url,cachedUrls));
               cachedUrls.get(url).start();               
            }
        }
    }
    
    public String getFromCache(String url) throws InterruptedException{
        CachedUrl cachedUrl=null;
        synchronized(cachedUrls){
            clearOldCache(cacheTime);
            cachedUrl=cachedUrls.get(url);
            if (cachedUrl==null){
                cacheUrl(url,false);
                return getFromCache(url);
            }            
            cachedUrl=cachedUrls.get(url);
            
            if (cachedUrl!=null && (cachedUrl.getStatus()== CachedUrl.CREATED
                    || cachedUrl.getStatus()== CachedUrl.LOADING)){
                cachedUrls.wait();
                return getFromCache(url);
            }else if (cachedUrl!=null){
                return cachedUrl.getContent();
            }else{
                return null;
            }
        }
    }
    
    
    /**
     * clear the cache that is older then 'cacheTime'
     */
    private void clearOldCache(int olderThen) {        
        synchronized(cachedUrls){
            Date now = new Date();
            Iterator<String> it = cachedUrls.keySet().iterator();
            while(it.hasNext()){
                String url = it.next();
                CachedUrl cu = cachedUrls.get(url);
                if (cu.getCreationDate()!=null && cu.getCreationDate().getTime()+olderThen < now.getTime()){
                    it.remove();
                }
            }
        }
    }

    public void clearCache() {
        clearOldCache(0);
    }
}

