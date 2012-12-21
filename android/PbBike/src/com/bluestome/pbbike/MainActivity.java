package com.bluestome.pbbike;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bluestome.pbbike.activity.BaseActivity;
import com.bluestome.pbbike.adpter.NearBy;
import com.bluestome.pbbike.adpter.NearByAdapter;
import com.bluestome.pbbike.biz.Biz;
import com.bluestome.pbbike.biz.BizImpl;
import com.bluestome.pbbike.cache.CacheInstance;
import com.bluestome.pbbike.common.MsgConstants;
import com.bluestome.pbbike.utils.AndroidSysUtils;
import com.bluestome.pbbike.utils.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener{
	
	protected String TAG = MainActivity.class.getSimpleName();

    //进度条
	private ProgressBar progressbar = null;
	//附近查找
	private Button searchBtnNearBy;
	//根据站点名称查找
	private Button searchBtnStationName;
	//根据站点ID查找
	private Button searchBtnStationID;
	
	List<NearBy> nList = new ArrayList<NearBy>();
	NearByAdapter nearByAdapter = null;
	
	//业务类
	final Biz biz = new BizImpl();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initNetwork();
        initUICompents();
       
    }
    
    //初始化网络
    private void initNetwork(){
    	int type = AndroidSysUtils.getNetworkType(mContext);
    	if(type != -1){
    		new Thread(doGetUUID).start();
    	}
    }
    
    //初始化UI组件
    private void initUICompents(){
		nearByAdapter = new NearByAdapter(mContext);
		
        searchBtnStationID = (Button)findViewById(R.id.stationID);
        searchBtnStationID.setOnClickListener(this);
        
        searchBtnStationName = (Button)findViewById(R.id.stationName);
        searchBtnStationName.setOnClickListener(this);
        
        searchBtnNearBy = (Button)findViewById(R.id.nearby);
        searchBtnNearBy.setOnClickListener(this);
        
        progressbar = (ProgressBar)findViewById(R.id.progress_bar_id);
        progressbar.setIndeterminate(false);
    }

    @Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_entry, null);
		final EditText text  = (EditText)textEntryView.findViewById(R.id.dialog_edit);
        Dialog dialog = null;
        Builder dialogBuilder = new AlertDialog.Builder(this);;
		switch (id) {
			case MsgConstants.MainActivityMsg.DIALOG_NEARBY:
				dialogBuilder
					.setTitle(R.string.notice_nearby)
					.setView(textEntryView)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									final String value = text.getText().toString();
									if(null != value && !value.equals("")){
										Message msg = new Message();
										msg.what = MsgConstants.MainActivityMsg.MSG_SEARCH_BY_NEARBY;
										msg.obj = value;
										handler.sendMessage(msg);
										progressbar.setVisibility(View.VISIBLE);
										Toast.makeText(mContext, "您输入的是:"+value, Toast.LENGTH_SHORT).show();
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
								}
							});
				break;
			case MsgConstants.MainActivityMsg.DIALOG_STATION_ID:
				dialogBuilder
						.setTitle(R.string.notice_station_id)
						.setView(textEntryView)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										String value = text.getText().toString();
										if(null != value && !value.equals("")){
	                                        Message msg = new Message();
	                                        msg.what = MsgConstants.MainActivityMsg.MSG_SEARCH_BY_STATION_NAME;
	                                        msg.obj = value;
	                                        handler.sendMessage(msg);
	                                        progressbar.setVisibility(View.VISIBLE);
											Toast.makeText(mContext, "您输入的是:"+value, Toast.LENGTH_SHORT).show();
										}
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								});
				break;
			case MsgConstants.MainActivityMsg.DIALOG_STATION_NAME:
				dialogBuilder
						.setTitle(R.string.notice_station_name)
						.setView(textEntryView)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										String value = text.getText().toString();
										if(null != value && !value.equals("")){
											progressbar.setVisibility(View.VISIBLE);
											Toast.makeText(mContext, "您输入的是:"+value, Toast.LENGTH_SHORT).show();
										}else{
											Toast.makeText(mContext, "请输入"+getString(R.string.notice_station_name)+"!", Toast.LENGTH_SHORT).show();
										}
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								});
				break;
			case MsgConstants.MainActivityMsg.DIALOG_WHERE_LIST:
				dialogBuilder
	            .setTitle(R.string.select_dialog)
	            .setAdapter(nearByAdapter, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	final NearBy item = (NearBy)nearByAdapter.getItem(which);
	                	if(null != item){
		                	//TODO 执行下一步显示操作
		                	Toast.makeText(mContext, " > 站点ID:"+item.getId(), Toast.LENGTH_LONG).show();
		                	new Thread(new Runnable(){
		                	    public void run(){
		                            try{
		                                String content = biz.findByWhere2(String.valueOf(item.getId()));
		                                if(null != content && !content.equals("")){
		                                    Log.d(TAG,content);
		                                    //TODO 解析网站内容
		                                    Parser parser = new Parser();
		                                    parser.setInputHTML(content);
		                                    NodeFilter fileter = new NodeClassFilter(Div.class);
		                                    NodeList list = parser.extractAllNodesThatMatch(fileter);
		                                    if(null != list && list.size() > 0){
		                                        nList.clear();
		                                        for(int i=0;i<list.size();i++){
		                                            Div div = (Div)list.elementAt(i);
		                                            if(div.getAttributesEx().size() == 1){
		                                                //TODO 这才是我需要的节点数据,<P> 节点
		                                                NodeList nlist = new NodeList ();
		                                                NodeFilter nfilter = new TagNameFilter ("p");
		                                                for (NodeIterator e = div.elements (); e.hasMoreNodes ();)
		                                                      e.nextNode ().collectInto (nlist, nfilter);
		                                                String addr = nlist.elementAt(0).toPlainTextString();
		                                                String info = nlist.elementAt(1).toPlainTextString();
		                                                String[] addrInfo = {addr,info};
		                                                Log.i(TAG,"|DIALOG_WHERE_LIST="+addrInfo[0]+"|"+addrInfo[1]);
		                                                NearBy bean = new NearBy();
		                                                bean.setId(String.valueOf(i));
		                                                bean.setName(addrInfo[0]+"|"+addrInfo[1]);
		                                                nList.add(0,bean);
		                                            }
		                                        }
		                                        if(nList.size() > 0){
		                                            nearByAdapter.notifyDataSetChanged();
    	                                            //更新适配器
    	                                            nearByAdapter.updateList(nList);
    	                                            handler.sendEmptyMessageDelayed(MsgConstants.MainActivityMsg.MSG_SHOW_LIST_DIALOG,500L);
		                                        }
		                                    }else{
		                                        handler.sendEmptyMessage(MsgConstants.MainActivityMsg.MSG_NO_DATA);
		                                    }
		                                }else{
		                                    handler.sendEmptyMessage(MsgConstants.MainActivityMsg.MSG_NETWORK_NO_RESPONSE);
		                                }
		                            }catch(Exception e){
		                                e.printStackTrace();
		                            }
		                	    }
		                	}).start();
	                	}
	                }
	            });
				break;
		}
        dialog = dialogBuilder.create();
        dialog.setCancelable(true);
        return dialog;
	}
    
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
			case R.id.nearby:
				showDialog(MsgConstants.MainActivityMsg.DIALOG_NEARBY);
				break;
			case R.id.stationName:
				showDialog(MsgConstants.MainActivityMsg.DIALOG_STATION_NAME);
				break;
			case R.id.stationID:
				showDialog(MsgConstants.MainActivityMsg.DIALOG_STATION_ID);
				break;
		}
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(null != msg){
				switch(msg.what){
				case MsgConstants.MainActivityMsg.MSG_SEARCH_BY_NEARBY:
					if(null != msg.obj){
						final String value = (String)msg.obj;
						new Thread(new Runnable(){
							public void run(){
							    List<NearBy>  tmpList = CacheInstance.getRoadNameCache(value);
							    if(null != tmpList && tmpList.size() > 0){
							        Log.d(TAG,"\t>>>>>> 从缓存中获取路标点数据:"+tmpList.size());
                                    nearByAdapter.notifyDataSetChanged();
                                    //更新适配器
                                    nearByAdapter.updateList(tmpList);
                                    handler.sendEmptyMessageDelayed(MsgConstants.MainActivityMsg.MSG_SHOW_LIST_DIALOG,500L);
							        
							    }else{
    								String content = biz.findByWhere(value);
    								if(null != content && !content.equals("")){
        								Parser parser = new Parser();
        								try {
        									parser.setInputHTML(content);
        									NodeFilter fileter = new NodeClassFilter(SelectTag.class);
        									NodeList list = parser.extractAllNodesThatMatch(fileter)
        											.extractAllNodesThatMatch(
        													new HasAttributeFilter("id", "Select1"));
        									if(null != list && list.size() > 0 ){
        										SelectTag select = (SelectTag)list.elementAt(0);
        										if(null != select){
        											OptionTag[] opts = select.getOptionTags();
        											int i=0;
        											nList.clear();
        											for(OptionTag opt:opts){
        												NearBy bean = new NearBy();
        												bean.setId(opt.getValue().trim());
        												bean.setName(opt.getOptionText().trim());
        												nList.add(i,bean);
        												Log.i(TAG,"|MSG_SEARCH_BY_NEARBY="+opt.getOptionText()+"|"+opt.getValue());
        												i++;
        											}
        							                nearByAdapter.notifyDataSetChanged();
        											//更新适配器
        											nearByAdapter.updateList(nList);
        											handler.sendEmptyMessageDelayed(MsgConstants.MainActivityMsg.MSG_SHOW_LIST_DIALOG,500L);
        											CacheInstance.setRoadNameCache(value, nList);
        										}
        									}else{
        									    handler.sendEmptyMessageDelayed(MsgConstants.MainActivityMsg.MSG_NO_DATA,500L);
        									}
        								} catch (ParserException e) {
        								    handler.sendEmptyMessageDelayed(MsgConstants.MSG_ERROR,500L);
        								}
    								}else{
    								    handler.sendEmptyMessageDelayed(MsgConstants.MainActivityMsg.MSG_NO_DATA,500L);
    								}
							    }
							}
						}).start();
					}
					break;
				case MsgConstants.MainActivityMsg.MSG_SEARCH_BY_STATION_NAME:
                    if(null != msg.obj){
                        final String value = (String)msg.obj;
                        new Thread(new Runnable(){
                            public void run(){
                                String content =  biz.findByStationName(value);
                                Parser parser = new Parser();
                                try {
                                    parser.setInputHTML(content);
                                    NodeFilter fileter = new NodeClassFilter(SelectTag.class);
                                    NodeList list = parser.extractAllNodesThatMatch(fileter)
                                            .extractAllNodesThatMatch(
                                                    new HasAttributeFilter("id", "Select1"));
                                    if(null != list && list.size() > 0 ){
                                        SelectTag select = (SelectTag)list.elementAt(0);
                                        if(null != select){
                                            OptionTag[] opts = select.getOptionTags();
                                            int i=0;
                                            nList.clear();
                                            for(OptionTag opt:opts){
                                                NearBy bean = new NearBy();
                                                bean.setId(opt.getValue().trim());
                                                bean.setName(opt.getOptionText().trim());
                                                nList.add(i,bean);
                                                Log.i(TAG,"|MSG_SEARCH_BY_NEARBY="+opt.getOptionText()+"|"+opt.getValue());
                                                i++;
                                            }
                                            //更新适配器
                                            nearByAdapter.updateList(nList);
                                            handler.sendEmptyMessageDelayed(MsgConstants.MainActivityMsg.MSG_SHOW_LIST_DIALOG,500L);
                                        }
                                    }
                                } catch (ParserException e) {
                                }
                            }
                        }).start();
                    }
				    break;
				case MsgConstants.MainActivityMsg.MSG_SHOW_LIST_DIALOG:
					progressbar.setVisibility(View.GONE);
					showDialog(MsgConstants.MainActivityMsg.DIALOG_WHERE_LIST);
					break;
				case MsgConstants.MainActivityMsg.MSG_NETWORK_NO_RESPONSE:
                    progressbar.setVisibility(View.GONE);
				    Toast.makeText(mContext, "没有获取服务端返回的数据", Toast.LENGTH_SHORT).show();
				    break;
				case MsgConstants.MainActivityMsg.MSG_NO_DATA:
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "没有该站点数据", Toast.LENGTH_SHORT).show();
				    break;
				case MsgConstants.MSG_ERROR:
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "异常请求", Toast.LENGTH_SHORT).show();
				    break;
				}
			}
		}
		
	};
	
	
	/**
	 * 获取计数器值
	 */
	private Runnable doGetUUID = new Runnable() {
		@Override
		public void run() {
			try {
				InputStream is = null;
				try {

					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost postMethod = new HttpPost(
							Constants.SERVER_URL);

					HttpResponse resp = httpClient.execute(postMethod);
					if (null != resp
							&& resp.getStatusLine().getStatusCode() == 200) {
						is = resp.getEntity().getContent();
//						byte[] rst = IOUtils.toByteArray(is);
//						String srst = new String(rst);
//						String[] splits = srst.split("\\|");
//						String count = splits[splits.length - 1];
//						uuid = splits[0];
//						Message msg = new Message();
//						msg.obj = count;
//						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Message msg = new Message();
					handler.sendMessage(msg);
				} finally {
					if (null != is) {
						is.close();
					}
				}
			} catch (IOException e) {
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}

	};

	protected void dialog() { 
        AlertDialog.Builder builder = new Builder(MainActivity.this); 
        builder.setMessage("确定要退出吗?"); 
        builder.setTitle("提示"); 
        builder.setPositiveButton("确认", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        MainActivity.this.finish(); 
                    } 
                }); 
        builder.setNegativeButton("取消", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    } 	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
            dialog(); 
            return false; 
        } 
        return false; 
    }

    @Override
    protected void onDestroy() {
        CacheInstance.clearAll();
        super.onDestroy();
    }


	
}