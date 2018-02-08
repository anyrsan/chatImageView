package any.com.loadbitmap;

import android.support.v7.widget.RecyclerView;

/**
 * Created by any on 2016/10/24.
 */
public class GlideScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
        // Recycle的加载优化,只在拖动和静止时加载，自动滑动时不加载。
        switch (scrollState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
            case RecyclerView.SCROLL_STATE_IDLE:
                GlideImageLoader.resumeRequests(recyclerView.getContext());
                break;
            default:
                GlideImageLoader.pauseRequests(recyclerView.getContext());
                break;
        }
    }
}
