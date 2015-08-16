package com.troy.jianyue.util;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by chenlongfei on 15/5/9.
 */
public class OkHttpUtil {
    private static OkHttpClient mOkHttpClient;

    public static void request(String url, final Callback callback) {
        OkHttpClient okHttpClient = getOkHttplClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.onResponse(response);
            }
        });
    }

    public static OkHttpClient getOkHttplClient() {
        new Request.Builder();
        if (mOkHttpClient == null) {
            return new OkHttpClient();
        }
        return mOkHttpClient;
    }

    public interface Callback {
        void onFailure(Request request, IOException e);

        void onResponse(Response response) throws IOException;
    }
}
