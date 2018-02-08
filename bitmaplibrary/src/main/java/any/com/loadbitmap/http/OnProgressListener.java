package any.com.loadbitmap.http;

/**
 * @author any
 * @date 2017/12/8
 */
public interface OnProgressListener {
    void onProgress(String key, int percent, boolean isDone, Exception ex);
}
