package com.scott.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ubuntu on 2017/5/24.
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX = "token_";
    private static LoadingCache<String, String> localhostCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        //默认的方法实现，key没有命中的时候，就调用这个方法
        @Override
        public String load(String s) throws Exception {
            return "null";
        }
    });

    public static void setKey(String key, String value) {
        localhostCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = localhostCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.debug("the key " + key + "is doest exist", e);
        }
        return null;
    }
}
