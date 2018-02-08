package any.com.loadbitmap.http;

/**
 * @author any
 * @date 2017/12/11
 */

public class ProgressRunnable implements Runnable {

    public ICallBack callBack;

    public String key;
    public int percent;
    private boolean isDone;

    public ProgressRunnable(ICallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        if (isDone) {
            return;
        }
        isDone = percent == 100;
        callBack.onCallBack(key, percent, isDone);
    }
}
