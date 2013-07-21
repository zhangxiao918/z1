
package com.bluestome.imageloader.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluestome.android.utils.AsyncImageLoader;
import com.bluestome.android.utils.ImageUtils;
import com.bluestome.android.utils.StringUtil;
import com.bluestome.imageloader.R;
import com.bluestome.imageloader.biz.ParserBiz;
import com.bluestome.imageloader.common.Constants;
import com.bluestome.imageloader.domain.ImageBean;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends BaseActivity implements Initialization {

    private static final String TAG = GalleryActivity.class.getCanonicalName();
    private ImageAdapter adapter = new ImageAdapter(this);
    private String link = null;
    private ImageView imageView;
    private View detailImageView;
    private Gallery g;
    private boolean isLoad = true;
    private Bitmap bigMap;
    private String currentImageUrl = null;
    private String smallImageUrl = null;

    // 页码
    private int page = 1;

    public static class MyHandler extends Handler {
        private WeakReference<GalleryActivity> mActivity;

        public MyHandler(GalleryActivity activity) {
            mActivity = new WeakReference<GalleryActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GalleryActivity activity = mActivity.get();
            if (null != activity) {
                super.handleMessage(msg);
            }
        }

    }

    private final MyHandler mHandler = new MyHandler(this);

    @Override
    public void init() {
        Intent i = getIntent();
        link = i.getStringExtra("DETAIL_URL");
        smallImageUrl = i.getStringExtra("IMAGE_URL");
        Log.e(TAG, "子界面的地址：" + link);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_gallery);
        imageView = (ImageView) findViewById(R.id.gallery_image_id);
        detailImageView = findViewById(R.id.gallery_detail_area);
        detailImageView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton close = (ImageButton) v.findViewById(R.id.gallery_image_close_id);
                close.setVisibility(View.VISIBLE);
                // }
                ImageButton download = (ImageButton) v.findViewById(R.id.gallery_image_download_id);
                download.setVisibility(View.VISIBLE);
                v.postDelayed(hiddenCloseRunnable, 5000L);
                return true;
            }
        });
        g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(adapter);
        // Set a item click listener, and just Toast the clicked position
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (null != parent) {
                    showDialog(LOADING_IMG);
                    g.setVisibility(View.GONE);
                    final ImageBean bean = (ImageBean) parent.getItemAtPosition(position);
                    final String url = getDownloadImageStr(bean.getDetailLink());
                    currentImageUrl = url;
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            byte[] body = ImageUtils.loadImageFromLocal(URLEncoder.encode(bean
                                    .getImageUrl()));
                            if (null != body && body.length > 0) {
                                Bitmap bm = ImageUtils.decodeFile(body);
                                imageView.setImageBitmap(bm);
                            }
                            detailImageView.setVisibility(View.VISIBLE);
                            detailImageView
                                    .postDelayed(hiddenCloseRunnable,
                                            5000L);
                        }
                    });
                    if (null != bean) {
                        new Thread(new Runnable() {
                            public void run() {
                                Log.e(TAG,
                                        "大图地址为:" + bean.getDetailLink() + "\r\n"
                                                + bean.getImageUrl());
                                byte[] body = ImageUtils.loadImageFromLocal(URLEncoder.encode(url));
                                if (null != body) {
                                    Log.d(TAG, "从本地获取图片[" + url + "]下载成功，大小为：" + body.length);
                                } else {
                                    body = ImageUtils.loadImageFromServer(url);
                                }
                                if (null != body) {
                                    bigMap =
                                            BitmapFactory.decodeByteArray(body, 0,
                                                    body.length);
                                    imageView.post(new Runnable() {
                                        public void run() {
                                            if (null != bigMap) {
                                                removeDialog(LOADING_IMG);
                                                btnShow();
                                                imageView.setImageBitmap(bigMap);
                                            }
                                        }
                                    });
                                } else {
                                    mHandler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            removeDialog(LOADING_IMG);
                                            Toast.makeText(GalleryActivity.this, "获取图片失败",
                                                    Toast.LENGTH_LONG)
                                                    .show();

                                        }
                                    });
                                }
                            }
                        }).start();
                    } else {

                    }
                }
            }
        });

    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        if (!StringUtil.isBlank(link)) {
            requestData(link);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
        initView();
        initData();
    }

    public static final int LOADING_LIST = 1001;
    public static final int LOADING_IMG = 1002;

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = null;
        switch (id) {
            case LOADING_LIST:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("数据正在加载中...");
                dialog.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        client.cancelRequests(GalleryActivity.this, true);
                    }
                });
                return dialog;
            case LOADING_IMG:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("大图片正在加载中...");
                dialog.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        client.cancelRequests(GalleryActivity.this, true);
                    }
                });
                return dialog;

        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private class ImageAdapter extends BaseAdapter {
        private Context ctx;
        private List<ImageBean> datas = new ArrayList<ImageBean>(0);

        public ImageAdapter(Context ctx) {
            this.ctx = ctx;
        }

        /**
         * @param datas the datas to set
         */
        public void setDatas(List<ImageBean> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            if (null != datas && datas.size() > 0) {
                return datas.size();
            }
            return 0;
        }

        @Override
        public ImageBean getItem(int position) {
            if (null != datas && datas.size() > 0) {
                return datas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) GalleryActivity.this.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder = null;
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.item_image, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_image_id);
                holder.image.setImageResource(R.drawable.item_image_loading);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (null != datas && datas.size() > 0 && position < datas.size()) {
                ImageBean imgBean = datas.get(position);
                AsyncImageLoader imageLoader = new AsyncImageLoader(holder.image);
                imageLoader.execute(imgBean.getImageUrl());
            }
            return convertView;
        }

        public void addItem(ImageBean bean) {
            datas.add(bean);
            notifyDataSetChanged();
        }

        public void addAllItem(List<ImageBean> lst) {
            datas.addAll(lst);
            notifyDataSetChanged();
        }
    }

    class ViewHolder {
        ImageView image;
    }

    private void requestData(final String link) {
        if (isLoad) {
            showDialog(LOADING_LIST);
            client.get(link, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(final int statusCode, final String content) {
                    super.onSuccess(statusCode, content);
                    new Thread(new Runnable() {
                        public void run() {
                            final List<ImageBean> iList = ParserBiz.getArticleImageList(content);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    removeDialog(LOADING_LIST);
                                    if (null != iList && iList.size() > 0) {
                                        for (final ImageBean bean : iList) {
                                            adapter.addAllItem(iList);
                                        }
                                        page++;
                                        getImagePageListThread.start();
                                    } else {
                                        Toast.makeText(GalleryActivity.this, "获取数据失败!",
                                                Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                        }
                    }).start();
                }

                @Override
                public void onFailure(final Throwable error, final String content) {
                    super.onFailure(error, content);
                    mHandler.post(new Runnable() {
                        public void run() {
                            removeDialog(LOADING_LIST);
                            isLoad = false;
                            Log.e(TAG, content);
                            Toast.makeText(GalleryActivity.this,
                                    "出现错误:" + error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            });
        }
    }

    private void btnShow() {
        ImageButton close = (ImageButton) detailImageView.findViewById(R.id.gallery_image_close_id);
        close.setVisibility(View.VISIBLE);
        ImageButton download = (ImageButton) detailImageView
                .findViewById(R.id.gallery_image_download_id);
        download.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭大图
     * 
     * @param view
     */
    public void close(View view) {
        detailImageView.setVisibility(View.GONE);
        g.setVisibility(View.VISIBLE);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != bigMap) {
                    bigMap.recycle();
                    imageView.setImageBitmap(null);
                    Log.d(TAG, "图片资源回收");
                }
            }
        });
    }

    /**
     * 处理下载大图片的地址
     * 
     * @param url
     * @return
     */
    private String getDownloadImageStr(String url) {
        if (url.indexOf("=") > 0) {
            url = url.split("=")[1];
            return Constants.IMAGE_PREFIX_URL + url;
        }
        return url;
    }

    /**
     * 隐藏关闭按钮
     */
    private Runnable hiddenCloseRunnable = new Runnable() {
        @Override
        public void run() {
            ImageButton close = (ImageButton) detailImageView
                    .findViewById(R.id.gallery_image_close_id);
            close.setVisibility(View.INVISIBLE);
            ImageButton download = (ImageButton) detailImageView
                    .findViewById(R.id.gallery_image_download_id);
            download.setVisibility(View.INVISIBLE);
        }
    };

    public Thread getImagePageListThread = new Thread() {

        private boolean isRun = true;

        @Override
        public synchronized void run() {
            while (isRun) {
                final String url = link.replace("_1.html", "_" + page + ".html");
                Log.d(TAG, "正在加载第" + page + "页,地址为:" + url);
                client.get(url, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        super.onSuccess(statusCode, content);
                        if (statusCode == 200) {
                            final List<ImageBean> iList = ParserBiz.getArticleImageList(content);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (null != iList && iList.size() > 0) {
                                        Log.d(TAG, url + ",分页数量:" + iList.size());
                                        adapter.addAllItem(iList);
                                    }
                                }
                            });
                            SystemClock.sleep(1000L);
                        } else {
                            isRun = false;
                            page = 1;
                        }
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        // TODO Auto-generated method stub
                        super.onFailure(error, content);
                        isRun = false;
                        page = 1;
                    }

                });
                if (page > 4) {
                    page = 1;
                    isRun = false;
                } else {
                    page++;
                }
            }
        }
    };

    /**
     * 下载操作
     * 
     * @param view
     */
    public void download(View view) {
        if (null != currentImageUrl) {
            mHandler.post(new Runnable() {
                public void run() {
                    Toast.makeText(
                            GalleryActivity.this,
                            "图片已保存在:" + ImageUtils.IMAGE_PATH + File.separator
                                    + URLEncoder.encode(currentImageUrl),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
