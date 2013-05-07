
package com.bluestome.hzti.qry;

import java.io.IOException;
import java.util.List;

import org.htmlparser.util.ParserException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bluestome.hzti.qry.bean.TBean;
import com.bluestome.hzti.qry.net.ParserHtml;

public class GridShowActivity extends Activity {

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
        List<TBean> list = null;
        try {
            list = ParserHtml.parser(content);
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
                // holder.index = (TextView)
                // convertView.findViewById(R.id.item_index_id);
                holder.date = (TextView) convertView.findViewById(R.id.item_date);
                // holder.type = (TextView)
                // convertView.findViewById(R.id.item_type);
                // holder.company = (TextView)
                // convertView.findViewById(R.id.item_company);
                holder.self = (TextView) convertView.findViewById(R.id.item_self);
                holder.total = (TextView) convertView.findViewById(R.id.item_total);
                // holder.pay = (TextView)
                // convertView.findViewById(R.id.item_pay);
                convertView.setTag(holder);
            } else {
                holder = (ItemHolderView) convertView.getTag();
            }
            TBean t = datas.get(position);
            holder.date.setText(t.getDate());
            // holder.index.setText(t.getCarNum());
            // holder.type.setText(t.getCarType());
            // holder.company.setText(t.getContent());
            holder.self.setText(t.getLoc());
            holder.total.setText(t.getDealResult());
            // holder.pay.setText(t.getPayResult());
            if (signle) {
                convertView.setBackgroundColor(Color.DKGRAY);
            }
            else {
                convertView.setBackgroundColor(Color.LTGRAY);
            }
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
