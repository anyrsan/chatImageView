package any.com.chatimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @author any
 * @date 2017/11/30
 */
public class MyChatShowImageView extends AppCompatImageView {

    private Paint mPaint;

    private Paint progressPaint;

    private Bitmap drawBitmap;

    private PorterDuffXfermode porterDuffX;

    private RectF progressRt;

    private Drawable progressDrawable;

    private boolean isDrawProgress = false;

    public MyChatShowImageView(Context context) {
        this(context, null);
    }

    public MyChatShowImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyChatShowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        porterDuffX = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

        progressPaint = new Paint(mPaint);
        progressPaint.setXfermode(porterDuffX);
        progressPaint.setColor(Color.BLACK);
        progressPaint.setAlpha(1);

        progressRt = new RectF();

    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        drawBitmap = bm;
        invalidate();
    }


    public void setProgress(int progress) {
        if (progressRt.isEmpty()) {
            progressRt.set(0, 0, getWidth(), getHeight());
        }
        float progressHeight = getHeight() * (progress / 100f);

//        progressRt.set(0, progressHeight, getWidth(), getHeight());

        progressRt.set(0, 0, getWidth(), progressHeight);

        isDrawProgress = progress == 100 ? false : true;

        invalidate();
    }


    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        if (bitmapDrawable != null) {
            drawBitmap = bitmapDrawable.getBitmap();
        } else {
            drawBitmap = null;
        }
        //数据清空
        progressRt.setEmpty();
        invalidate();
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable background = getBackground();
        if (background == null) return;
        background.setBounds(0, 0, getWidth(), getHeight());
        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, saveFlags);
        background.draw(canvas);
        mPaint.setXfermode(porterDuffX);
        if (drawBitmap != null) {
            canvas.drawBitmap(drawBitmap, getMatrix(), mPaint);
        }
        mPaint.setXfermode(null);
        canvas.restore();

      
        if (!isDrawProgress) return;
        //产生阴影
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, saveFlags);
        if (progressDrawable == null) {
            progressDrawable = cloneDrawable();
//            progressDrawable = newDrawable();
            progressDrawable.setAlpha(225);   //这样就可以看到底图bitmap
        }
        progressDrawable.setBounds(0, 0, getWidth(), getHeight());
        progressDrawable.draw(canvas);
        canvas.drawRect(progressRt, progressPaint);
        canvas.restore();

    }


    private Drawable cloneDrawable() {
        Drawable drawable = getBackground();
        if (drawable == null) {
            return null;
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);  //重叠绘制用白色
        drawable.draw(canvas);
        Drawable newDrawable = new BitmapDrawable(bitmap);
        return newDrawable;
    }


    /**
     * 还是会改变原有Drawable对象
     *
     * @return
     */
    private Drawable newDrawable() {
        Drawable drawable = getBackground();
        if (drawable == null) {
            return null;
        }
        Drawable newDrawable = drawable.getConstantState().newDrawable();
        return newDrawable;
    }

}
