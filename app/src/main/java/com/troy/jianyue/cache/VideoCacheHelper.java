package com.troy.jianyue.cache;

import android.util.Log;

import com.troy.greendao.VideoCache;
import com.troy.greendao.VideoCacheDao;
import com.troy.jianyue.bean.Video;
import com.troy.jianyue.json.VideoJSONParser;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class VideoCacheHelper extends BaseCacheHelper<Video> {
    private static VideoCacheHelper mInstance;

    private VideoCacheHelper() {
    }

    public static VideoCacheHelper getInstance() {
        if (mInstance == null) {
            mInstance = new VideoCacheHelper();
        }
        return mInstance;
    }

    @Override
    public void wirteCacheForPage(int page, String result) {
        VideoCache videoCache = new VideoCache();
        videoCache.setPage(page);
        videoCache.setResult(result);
        GreenDaoHelper.getInstance().getVideoCacheDao().insert(videoCache);
    }

    @Override
    public List<Video> readCacheForPage(int page) {
        QueryBuilder<VideoCache> queryBuilder = GreenDaoHelper.getInstance().getVideoCacheDao().queryBuilder().where(VideoCacheDao.Properties.Page.eq("" + page));
        List<Video> videoList = null;
        if (queryBuilder.list().size() > 0) {
            String result = queryBuilder.list().get(0).getResult();
            VideoJSONParser videoJSONParser = new VideoJSONParser();
            videoList = videoJSONParser.jsonToVideoList(result);
        } else {
            videoList = new ArrayList<Video>();
        }
        return videoList;
    }

    @Override
    public void cleanAllCache() {
        GreenDaoHelper.getInstance().getVideoCacheDao().deleteAll();
    }
}
