package any.com.loadbitmap.http;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author any
 * @date 2017/12/8
 */

public class ProgressManager {

    private static volatile Map<String, OnProgressListener> listeners = new HashMap<>();
    private static OkHttpClient okHttpClient;

    public static OkHttpClient configOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    return response.newBuilder().body(new ProgressResponse(request.url().toString(), response.body(), LISTENER)).build();
                }
            }).build();
            listeners.clear();
        }
        return okHttpClient;
    }


    private static OnProgressListener LISTENER = new OnProgressListener() {
        @Override
        public void onProgress(String key, int percent, boolean isDone, Exception ex) {
            if (listeners == null || listeners.isEmpty()) return;
            OnProgressListener listener = listeners.get(key);
            if (listener != null) {
                listener.onProgress(key, percent, isDone, ex);
                if (isDone) {
                    removeOnProgressListener(key);
                }
            } else {
                removeOnProgressListener(key);
            }
        }
    };


    public static void addOnProgressListener(String key, OnProgressListener listener) {
        if (listener == null) return;
        if (!checkNotProgressListener(key)) {
            listeners.put(key, listener);
        }
    }

    /**
     * 直接删除，如果不存在，删除无效
     * 主要是发生了错误时，由Glide RequestListener 回调
     *
     * @param key
     */
    public static void removeOnProgressListener(String key) {
        if (key == null) return;
        listeners.remove(key);
    }


    /**
     * 如果不存在则返回true，其它则不用添加监听
     *
     * @return
     */
    private static boolean checkNotProgressListener(String key) {
        if (listeners.containsKey(key)) return true;
        return false;
    }


    public static void testLists() {
        Log.e("msg", "-->" + listeners.size());
    }

}
