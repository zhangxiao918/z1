
package com.bluestome.imageloader.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluestome.android.bean.ResultBean;
import com.bluestome.android.utils.AsyncImageLoader;
import com.bluestome.android.utils.StringUtil;
import com.bluestome.imageloader.R;
import com.bluestome.imageloader.biz.ParserBiz;
import com.bluestome.imageloader.common.Constants;
import com.bluestome.imageloader.domain.ImageBean;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexActivity extends BaseActivity implements Initialization {

    private static final String TAG = IndexActivity.class.getCanonicalName();
    private ListView indexImageList;
    private List<ImageBean> list = new ArrayList<ImageBean>();
    private ItemAdapter adapter = new ItemAdapter(null);
    private ResultBean result = null;

    private View loadMoreView;
    private Button loadMoreButton;

    private int count = 1;
    private int state = 0; // 0:等待, 1:正在提取数据并更新

    private Map<Integer, View> cacheHolder = new HashMap<Integer, View>(10);

    private static class MyHandler extends Handler {
        private WeakReference<IndexActivity> mActivity;

        public MyHandler(IndexActivity activity) {
            mActivity = new WeakReference<IndexActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            IndexActivity activity = mActivity.get();
            if (null != activity) {
                super.handleMessage(msg);
            }
        }

    }

    private MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initView();
        init();
        initData();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.post(new Runnable() {
            public void run() {
                // TODO 清理文件夹
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        finish();
    }

    public final int LOADING = 1001;

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog = null;
        switch (id) {
            case LOADING:
                dialog = new ProgressDialog(this);
                dialog.setTitle(null);
                dialog.setMessage("数据正在加载中...");
                dialog.setOnCancelListener(new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        client.cancelRequests(IndexActivity.this, true);
                    }
                });
                return dialog;
            default:
                return super.onCreateDialog(id);
        }
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        result = (ResultBean) intent.getSerializableExtra("RESULT_INFO");
        adapter = new ItemAdapter(null);
        indexImageList.setAdapter(adapter);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_index);
        loadMoreView = getLayoutInflater().inflate(R.layout.loadmore, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadMoreButton.setText("正在加载中..."); // 设置按钮文字
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                    }
                });

            }
        });
        indexImageList = (ListView) findViewById(R.id.index_image_list_id);
        indexImageList.addFooterView(loadMoreView); // 设置列表底部视图
        indexImageList.setRecyclerListener(new RecyclerListener() {

            @Override
            public void onMovedToScrapHeap(View view) {
                ImageView imageView = (ImageView) view.findViewById(R.id.item_image_id);
                if (null != imageView) {
                    imageView.setImageBitmap(null);
                }
            }
        });
        indexImageList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                final ImageBean bean = (ImageBean) adapter.getItemAtPosition(position);
                if (null != bean) {
                    Intent i = new Intent();
                    i.setClass(IndexActivity.this, GalleryActivity.class);
                    i.putExtra("DETAIL_URL", bean.getDetailLink());
                    i.putExtra("IMAGE_URL", bean.getImageUrl());
                    startActivity(i);
                }
            }
        });
        indexImageList.setOnScrollListener(new OnScrollListener() {
            private boolean state_idle = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Log.w(TAG, "scroll_state[" + scrollState + "], state[" +
                // state
                // + "]");
                //
                // if (OnScrollListener.SCROLL_STATE_IDLE != scrollState) {
                // return;
                // }
                //
                // if (0 != state)
                // return;
                //
                // this.state_idle = true;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                // if (!this.state_idle)
                // return;
                // if (firstVisibleItem == 0
                // || (firstVisibleItem + visibleItemCount) != totalItemCount) {
                // return;
                // }
                //
                // Log.w(TAG, "提取数据 ..., count[" + count + "]");
                // state = 1;
                // loadMoreData();
            }
        });
    }

    @Override
    public void initData() {
        showDialog(LOADING);
        client.get(this, Constants.URL + "/index/" + count + ".html", null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(final String content) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final List<ImageBean> lst = ParserBiz
                                            .getImageBeanList(content);
                                    mHandler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            removeDialog(LOADING);
                                            if (null != lst && lst.size() > 0) {
                                                loadMoreView.setVisibility(View.VISIBLE);
                                                Log.d(TAG, "获取首页的图片数据数量为:" + list.size());
                                                adapter.addAllItems(lst);
                                                // 设置新数据的起始列位置
                                                int pos = adapter.getCount() - lst.size() - 1;
                                                if (pos > 0) {
                                                    indexImageList.setSelection(pos);
                                                } else {
                                                    indexImageList.setSelection(0);
                                                }
                                            } else {
                                                Log.e(TAG, "没有获取到首页图片数据");
                                            }
                                            loadMoreButton.setText("查看更多..."); // 恢复按钮文字
                                        }
                                    });
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                    String tip = "解析首页分页数据异常," + e.getMessage();
                                    Log.e(TAG, tip);
                                }
                            }
                        }).start();
                        count++;
                    }

                    @Override
                    public void onFailure(final Throwable error, String content) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                error.printStackTrace();
                                Toast.makeText(IndexActivity.this, "获取图片列表失败!", Toast.LENGTH_LONG)
                                        .show();
                                finish();
                            }
                        });
                    }

                });

    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        int c = adapter.getCount();
        Log.d(TAG, "适配器中的记录数量:" + c);
        initData();
    }

    public class ItemAdapter extends BaseAdapter {
        private List<ImageBean> list = new ArrayList<ImageBean>(0);

        public ItemAdapter(List<ImageBean> list) {
            if (null != list && list.size() > 0) {
                this.list = list;
            }
        }

        @Override
        public int getCount() {
            if (null != list && list.size() > 0) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (null != list && list.size() > 0) {
                return list.get(position);
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
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) IndexActivity.this.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.image_list_item_adapter, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_image_id);
                holder.title = (TextView) convertView
                        .findViewById(R.id.item_title_id);
                holder.imageDesc = (TextView) convertView
                        .findViewById(R.id.item_image_desc_total_id);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null && list.size() > position) {
                final ImageBean bean = list.get(position);
                if (null != bean) {
                    final String url = bean.getImageUrl();
                    if (!StringUtil.isBlank(url)) {
                        // TODO 异步下载图片
                        AsyncImageLoader imageLoader = new AsyncImageLoader(holder.image);
                        imageLoader.execute(url);
                    }
                    holder.image.setImageResource(R.drawable.item_image_loading);
                    holder.imageDesc.setText(Html.fromHtml(bean.getImageDesc()));
                    holder.title.setText(bean.getTitle());
                }

            }
            return convertView;
        }

        /**
         * 添加新数据
         * 
         * @param bean
         */
        public void addItem(ImageBean bean) {
            list.add(bean);
            notifyDataSetChanged();
        }

        public void addAllItems(List<ImageBean> tList) {
            list.addAll(tList);
            notifyDataSetChanged();
        }

    }

    class ViewHolder {

        TextView title;
        ImageView image;
        TextView imageDesc;
        TextView screensize;
        TextView uploadTime;
    }

}
