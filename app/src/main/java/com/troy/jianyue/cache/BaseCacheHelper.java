package com.troy.jianyue.cache;

import java.util.List;

/**
 * Created by chenlongfei on 15/8/28.
 */
public abstract class BaseCacheHelper<T> {

    //根据页码写缓存
    public abstract void wirteCacheForPage(int page, String result);

    //根据页码读缓存
    public abstract List<T> readCacheForPage(int page);

    //清理缓存
    public abstract void cleanAllCache();
}
