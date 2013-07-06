
package com.bluestome.hzti.qry.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluestome.hzti.qry.R;
import com.bluestome.hzti.qry.bean.TBean;
import com.bluestome.hzti.qry.net.ParserHtml;

import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.List;

public class GridShowActivity extends Activity {

    private static final String TAG = GridShowActivity.class.getCanonicalName();
    private String content;
    private String cookie;
    private ListView listView;
    private ItemAdapter adapter;

    private LinearLayout l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcontent);
        Bundle extras = getIntent().getExtras();
        content = extras.getString("content");
        cookie = extras.getString("cookie");
        initView();
    }

    void initView() {
        l2 = (LinearLayout) findViewById(R.id.linearlayout3);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                TBean t = (TBean) arg0.getItemAtPosition(position);
                if (null != t) {
                    Toast.makeText(GridShowActivity.this,
                            "选择时间为:" + t.getDate() + "的记录",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        List<TBean> list = null;
        try {
            list = ParserHtml.parser(content);
        } catch (ParserException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        if (null != list && list.size() > 0) {
            adapter = new ItemAdapter(this, list);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }

    public class ItemAdapter extends BaseAdapter {

        private List<TBean> datas;
        private Context mContext = null;

        public ItemAdapter(Context mContext, List<TBean> datas) {
            this.mContext = mContext;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            boolean signle = true;
            if (position % 2 == 0) {
                signle = false;
            }
            ItemHolderView holder = null;
            if (convertView == null) {
                holder = new ItemHolderView();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.list_item, null);
                holder.date = (TextView) convertView.findViewById(R.id.main_violation_time);
                holder.company = (TextView)
                        convertView.findViewById(R.id.main_violation_detail);
                holder.self = (TextView)
                        convertView.findViewById(R.id.main_violation_address);
                holder.total = (TextView)
                        convertView.findViewById(R.id.main_violation_point);
                holder.pay = (TextView)
                        convertView.findViewById(R.id.main_violation_money);
                convertView.setTag(holder);
            } else {
                holder = (ItemHolderView) convertView.getTag();
            }
            TBean t = datas.get(position);
            holder.date.setText(t.getDate());
            holder.company.setText(t.getContent());
            holder.self.setText(t.getLoc());
            holder.total.setText(t.getDealResult());
            holder.pay.setText(t.getPayResult());
            return convertView;
        }
    }

    public class ItemHolderView {
        TextView index;
        TextView date;
        TextView type;
        TextView company;
        TextView self;
        TextView total;
        TextView pay;
    }
}
