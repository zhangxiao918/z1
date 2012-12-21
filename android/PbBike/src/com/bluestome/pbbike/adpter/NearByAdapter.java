package com.bluestome.pbbike.adpter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bluestome.pbbike.R;

public class NearByAdapter extends BaseAdapter{
	
	protected String TAG = NearByAdapter.class.getSimpleName();
	private Context context;
	private List<NearBy> list = new ArrayList<NearBy>(); //
	 protected final HashMap<Long, View> itemCache = new HashMap<Long, View>();
	
	public NearByAdapter(Context ctx){
		this.context = ctx;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if(null != list && list.size() > 0){
			return list.get(position);
		}
		return null;
	}

	/**
	 * 更新列表数据
	 * @param lst
	 */
    public void updateList(List<NearBy> lst) {
    	list.clear();
    	list.addAll(lst);
    }
    
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null == convertView){
			//TODO 重构一条记录
			convertView = LayoutInflater.from(context).inflate(R.layout.nearby_list_item_child, null);
		}
		TextView view = (TextView)convertView.findViewById(R.id.nearby_station_name);
		Log.d(TAG,"|NearByAdapter.getView:"+list.get(position).getName());
		view.setText(list.get(position).getName());
		return convertView;
	}
	
}
