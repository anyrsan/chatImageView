package any.com.loadbitmap.http;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author any
 * @date 2017/12/8
 */

public class ProgressResponse extends ResponseBody {

    private String key;

    private ResponseBody responseBody;

    private OnProgressListener progressListener;

    private BufferedSource bufferedSource;

    public ProgressResponse(String key, ResponseBody responseBody, OnProgressListener progressListener) {
        this.key = key;
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }


    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(handlerSource(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source handlerSource(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += (bytesRead == -1) ? 0 : bytesRead;
                final int percent = (int) ((totalBytesRead * 1.0f / contentLength()) * 100.0f);
                if (progressListener != null) {
                    progressListener.onProgress(key, percent, (bytesRead == -1), null);
                }
                return bytesRead;
            }
        };
    }
}
