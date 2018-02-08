package any.com.chatimageview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import any.com.loadbitmap.GlideLoadProgress;
import any.com.loadbitmap.http.ICallBack;

public class MainActivity extends AppCompatActivity {


    MyChatShowImageView imageView;

    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.chat_img);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBitmap();
            }
        });
    }

    private void loadBitmap() {
        String url = "http://img.cyol.com/img/junshi/attachement/jpg/site2/20160513/IMGb083fe71c7bf41310748715.jpg";

        GlideLoadProgress.loadBitmap(imageView, url, new ICallBack() {
            @Override
            public void onCallBack(String key, int percent, boolean isDone) {
                imageView.setProgress(percent);
            }
        });

        // 可以用Glide实现进度加载
        progress = 0;
        test();
    }


    private void test() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progress++;
                imageView.setProgress(progress);
                if (progress == 100) {
                    return;
                }

                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                test();
            }
        });
    }


    private Handler handler = new Handler();
}
