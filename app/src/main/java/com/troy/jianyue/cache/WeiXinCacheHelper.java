package com.troy.jianyue.cache;

import com.troy.greendao.WeiXinCache;
import com.troy.greendao.WeiXinCacheDao;
import com.troy.jianyue.bean.WeiXin;
import com.troy.jianyue.json.WeiXinJSONParser;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class WeiXinCacheHelper extends BaseCacheHelper<WeiXin> {
    private static WeiXinCacheHelper mInstance;

    private WeiXinCacheHelper() {
    }

    public static WeiXinCacheHelper getInstance() {
        if (mInstance == null) {
            mInstance = new WeiXinCacheHelper();
        }
        return mInstance;
    }

    @Override
    public void wirteCacheForPage(int page, String result) {
        WeiXinCache weiXinCache = new WeiXinCache();
        weiXinCache.setPage(page);
        weiXinCache.setResult(result);
        GreenDaoHelper.getInstance().getWeiXinCacheDao().insert(weiXinCache);
    }

    @Override
    public List<WeiXin> readCacheForPage(int page) {
        QueryBuilder<WeiXinCache> queryBuilder = GreenDaoHelper.getInstance().getWeiXinCacheDao().queryBuilder().where(WeiXinCacheDao.Properties.Page.eq("" + page));
        List<WeiXin> weiXinList = null;
        if (queryBuilder.list().size() > 0) {
            String result = queryBuilder.list().get(0).getResult();
            WeiXinJSONParser weiXinJSONParser = new WeiXinJSONParser(result);
            weiXinList = weiXinJSONParser.getWeiXinList();
        } else {
            weiXinList = new ArrayList<WeiXin>();
        }
        return weiXinList;
    }

    @Override
    public void cleanAllCache() {
        GreenDaoHelper.getInstance().getWeiXinCacheDao().deleteAll();
    }

}
