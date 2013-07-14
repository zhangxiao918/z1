/**
 * 
 */

package com.bluestome.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URLEncoder;

/**
 * @author bluestome.zhang
 */
public class AsyncImageLoader extends AsyncTask<String, Void, byte[]> {

    private ImageView imageView;

    public AsyncImageLoader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected byte[] doInBackground(String... params) {
        String url = params[0];
        byte[] body = null;
        try {
            body = ImageUtils
                    .loadImageFromLocal(URLEncoder.encode(url, "UTF-8"));
            if (null == body) {
                body = ImageUtils.loadImageFromServer(url);
                if (null != body) {
                    return body;
                }
            } else {
                return body;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(byte[] result) {
        if (null != result) {
            Bitmap bm = BitmapFactory.decodeByteArray(result, 0, result.length);
            imageView.setImageBitmap(bm);
        }
    }

}
