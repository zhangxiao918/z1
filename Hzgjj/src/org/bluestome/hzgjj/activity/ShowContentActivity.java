
package org.bluestome.hzgjj.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.bluestome.hzgjj.R;
import org.bluestome.hzgjj.bean.DepositBean;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ShowContentActivity extends BaseActivity {

    private TextView tv;
    private String content;
    private String cookie;
    private ListView listView;
    private ItemAdapter adapter;

    private LinearLayout l1;
    private LinearLayout l2;

    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showcontent);
        Bundle extras = getIntent().getExtras();
        content = extras.getString("content");
        cookie = extras.getString("cookie");
        initView();
    }

    @Override
    void initView() {
        tv = (TextView) findViewById(R.id.text_show_log);
        if (null != content) {
            // tv.setText(Html.fromHtml(content));
            tv.setText(content);
        }
        listView = (ListView) findViewById(R.id.listView);
        l1 = (LinearLayout) findViewById(R.id.linearlayout1);
        l2 = (LinearLayout) findViewById(R.id.linearlayout3);
        progress = (ProgressBar) findViewById(R.id.showcontent_progress_id);
    }

    /**
     * 明细
     */
    public void detail(View view) {
        new Handler().post(new Runnable() {
            public void run() {
                if (progress.getVisibility() == View.GONE) {
                    progress.setVisibility(View.VISIBLE);
                }
            }
        });
        String requestURL = "http://www.hzgjj.gov.cn:8080/WebAccounts/comPerInfo.do?pagenum=1&serialVersionUID=1&cust_no=100013155208&ccust_no=0100992509&fund_type=10&entry_flag=1&perFlag=3&serialVersionUID=1&pagesize=999&pagenum=1";
        String reffer = "http://www.hzgjj.gov.cn:8080/WebAccounts/comPerInfo.do?perFlag=3&ccust_no=0100992509&cust_no=100013155208&fund_type=10";
        if (null != go && null != cookie) {
            final byte[] body = go.request4Body(requestURL, cookie, reffer);
            new Handler().post(new Runnable() {
                public void run() {
                    if (null != body) {
                        try {
                            content = new String(body, "GBK");
                            tv.setText("");
                            tv.setText(Html.fromHtml(content));
                            List<DepositBean> list = go.parser(content);
                            if (null != list && list.size() > 0) {
                                progress.setVisibility(View.GONE);
                                l1.setVisibility(View.GONE);
                                l2.setVisibility(View.VISIBLE);
                                adapter = new ItemAdapter(getApplicationContext(), list);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);
                                listView.setVisibility(View.VISIBLE);
                            }
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ParserException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "MobileGo is null!", Toast.LENGTH_SHORT).show();
        }
    }

    public class ItemAdapter extends BaseAdapter {

        private List<DepositBean> datas;
        private Context mContext = null;

        public ItemAdapter(Context mContext, List<DepositBean> datas) {
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
                holder.index = (TextView) convertView.findViewById(R.id.item_index_id);
                holder.date = (TextView) convertView.findViewById(R.id.item_date);
                holder.type = (TextView) convertView.findViewById(R.id.item_type);
                holder.company = (TextView) convertView.findViewById(R.id.item_company);
                holder.self = (TextView) convertView.findViewById(R.id.item_self);
                holder.total = (TextView) convertView.findViewById(R.id.item_total);
                holder.pay = (TextView) convertView.findViewById(R.id.item_pay);
                holder.in = (TextView) convertView.findViewById(R.id.item_in);
                convertView.setTag(holder);
            } else {
                holder = (ItemHolderView) convertView.getTag();
            }
            DepositBean t = datas.get(position);
            holder.index.setText(String.valueOf(t.getIndex()));
            holder.date.setText(t.getDate());
            holder.type.setText(t.getType());
            holder.company.setText(t.getCompany());
            holder.self.setText(t.getSelf());
            holder.total.setText(t.getTotal());
            holder.pay.setText(t.getPay());
            holder.in.setText(t.getIn());
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
        TextView in;
    }
}
