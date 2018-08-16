package com.iot.nero.smartcan.utils.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2018/8/5
 * Time   6:34 PM
 */
public class CacheFactory {


    private static LoadingCache<String,PushBean> graphs;

    public static CacheLoader<String, PushBean> createCacheLoader() {
        return new CacheLoader<String, PushBean>() {
            @Override
            public PushBean load(String key) throws Exception {
                return new PushBean();
            }
        };
    }
    public static LoadingCache getCache(){
        if(graphs==null) {
             graphs = CacheBuilder.newBuilder()
                    .expireAfterAccess(60, TimeUnit.MINUTES)
                     .maximumSize(10000)
                    .build(createCacheLoader());
             return graphs;
        }
        return graphs;
    }
}
