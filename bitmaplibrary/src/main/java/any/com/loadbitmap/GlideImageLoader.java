package any.com.loadbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Grady on 2016/10/24.
 */

public class GlideImageLoader {

    /**
     * RecyclerView 滚动时，暂停加载
     */
    @UiThread
    public static void addScrollListener(RecyclerView view) {
        GlideScrollListener listener = new GlideScrollListener();
        view.addOnScrollListener(listener);
    }

    @UiThread
    public static void pauseRequests(Context context) {
        if (Util.isOnMainThread()) {
            GlideApp.with(context).pauseRequests();
        }
    }

    @UiThread
    public static void resumeRequests(Context context) {
        if (Util.isOnMainThread()) {
            GlideApp.with(context).resumeRequests();
        }
    }


    /**
     * 加载图片
     *
     * @param view
     * @param url
     */
    public static void loadBitmap(ImageView view, String url) {
        if (Util.isOnMainThread()) {
            GlideApp.with(view.getContext()).load(url).dontAnimate().into(view);
        }
    }


    /**
     * @param view
     * @param url
     * @param defaultRId
     * @param errorId
     */
    public static void loadBitmap(ImageView view, String url, @DrawableRes int defaultRId, @DrawableRes int errorId) {
        if (Util.isOnMainThread()) {
            GlideApp.with(view.getContext()).load(url).placeholder(defaultRId).error(errorId).dontAnimate().into(view);
        }
    }


    /**
     * 加载本地资源图片
     *
     * @param url
     * @param view
     * @param transformation
     */
    public static void loadBitmap(ImageView view, String url, BitmapTransformation transformation) {
        if (Util.isOnMainThread()) {
            if (transformation != null) {
                RequestOptions options = null;
                if (options == null) {
                    options = new RequestOptions();
                }
                options.transform(transformation);
                GlideApp.with(view.getContext()).asBitmap().load(url).apply(options).into(view);
            } else {
                GlideApp.with(view.getContext()).asBitmap().load(url).into(view);
            }
        }
    }


    /**
     * 加载本地文件路径的图片
     *
     * @param view       ImageView
     * @param filePath   文件路径   string
     * @param defaultRId 默认图片资源
     */
    public static void loadBitmapByLocal(ImageView view, String filePath, int defaultRId) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        GlideApp.with(view.getContext()).load(file).placeholder(defaultRId).into(view);
    }


    /**
     * PS:  运行在 backThread
     * 根据网络图片的链接，获取本地图片文件的路径 string
     *
     * @param context 上下文对象
     * @param url     网络路径
     * @return String
     */
    public static String getFilePathByUrl(Context context, String url) {
        try {
            File file = GlideApp.with(context).downloadOnly().load(url).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (file != null) {
                return file.getAbsolutePath();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 以Glide目录保存
     * @param context
     * @param bitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap bitmap) {
        File cacheDir = GlideApp.getPhotoCacheDir(context);
        File fileLocal = new File(cacheDir, "media" + System.currentTimeMillis());
        String filePath = isSaveBitmap(fileLocal, bitmap) ? fileLocal.getAbsolutePath() : "";
        return filePath;
    }


    public static boolean isSaveBitmap(File filePath, Bitmap bitmap) {
        createFile(filePath.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public static void createFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        File parenFile = file.getParentFile();
        if (parenFile != null) {
            boolean bool = parenFile.mkdirs();
            Log.e("msg", "boolean: " + bool);
        }
    }

}
