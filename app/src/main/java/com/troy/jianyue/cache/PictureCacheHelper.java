package com.troy.jianyue.cache;

import com.troy.greendao.PictureCache;
import com.troy.greendao.PictureCacheDao;
import com.troy.jianyue.bean.Picture;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class PictureCacheHelper extends BaseCacheHelper<Picture> {
    private static PictureCacheHelper mInstance;

    private PictureCacheHelper() {
    }

    public static PictureCacheHelper getInstance() {
        if (mInstance == null) {
            mInstance = new PictureCacheHelper();
        }
        return mInstance;
    }

    @Override
    public void wirteCacheForPage(int page, String result) {
        PictureCache pictureCache = new PictureCache();
        pictureCache.setPage(page);
        pictureCache.setResult(result);
        GreenDaoHelper.getInstance().getPictureCacheDao().insert(pictureCache);
    }

    @Override
    public List<Picture> readCacheForPage(int page) {
        QueryBuilder<PictureCache> queryBuilder = GreenDaoHelper.getInstance().getPictureCacheDao().queryBuilder().where(PictureCacheDao.Properties.Page.eq("" + page));
        List<Picture> pictureList = null;
        if (queryBuilder.list().size() > 0) {
            String result = queryBuilder.list().get(0).getResult();
        } else {
            pictureList = new ArrayList<Picture>();
        }
        return pictureList;
    }

    @Override
    public void cleanAllCache() {
        GreenDaoHelper.getInstance().getPictureCacheDao().deleteAll();
    }

}
