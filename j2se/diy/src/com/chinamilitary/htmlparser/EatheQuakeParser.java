package com.chinamilitary.htmlparser;

import java.io.*;
import java.util.*;

import org.htmlparser.filters.*;
import org.htmlparser.*;
import org.htmlparser.util.*;
import org.htmlparser.tags.*;

import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.CommonUtil;
import com.chinamilitary.util.IOUtil;

public class EatheQuakeParser {

	// http://earthquake.usgs.gov/
	final static String USGS_URL_PREFIX = "http://earthquake.usgs.gov";
	final static String USGS_LATEST_LIST = "http://neic.usgs.gov/neis/qed/";
	final static String USGS_REALTIME_LIST_URL = "http://earthquake.usgs.gov/earthquakes/recenteqsww/";
	final static HashMap<String, Integer> SYS_HASH = new HashMap<String, Integer>();
	final static String FILE_DIR_PATH = "D:\\tools\\1.JAVA\\java\\20110301_USGS\\";
	static MemcacheClient client = MemcacheClient.getInstance();

	static {
		SYS_HASH.put("Magnitude", 1);
		SYS_HASH.put("Date-Time", 2);
		SYS_HASH.put("Location", 3);
		SYS_HASH.put("Depth", 4);
		SYS_HASH.put("Region", 5);
		SYS_HASH.put("Distances", 6);
		SYS_HASH.put("Location Uncertainty", 7);
		SYS_HASH.put("Parameters", 8);
		SYS_HASH.put("Source", 9);
		SYS_HASH.put("Remarks", 10);
		SYS_HASH.put("Event ID", 11);
	}

	public static void main(String[] args) {
		String name = "ak10414214";
		System.out.println(" > 1:"+name.substring(0,2));
		System.out.println(" > 2:"+name.substring(2,5));
		showRegionList();
//		showLatestEarthQuake();
	}
	
	/**
	 * 显示最新的地震
	 *
	 */
	private static void showLatestEarthQuake(){
		 List<EarthQuakeInfo> list = getLatestEarthQuake();
		 for(EarthQuakeInfo info:list){
			 if(null == info.getUrl() ||"null".equals(info.getUrl())){ 
				 continue; 
			 } 
			 System.out.println(" >> url:"+info.getUrl()); 
			 getEarthQuakeDetail(info.getUrl()); 
		 }
		
	}
	
	/**
	 * 显示区域数据
	 *
	 */
	private static void showRegionList(){
		StringBuffer buffer = null;
		HashMap<String, String> umap = getRegionListUrl();
		Iterator it = umap.keySet().iterator();
		while (it.hasNext()) {
				buffer = new StringBuffer();
				String url = (String) it.next();
				String name = getNameFromUrl(url);
				System.out.println("name:"+name+"\t url:"+url);
				List<EarthQuakeInfo> earthlist = getRegionRealTimeQuake(url);
				System.out.println(" >> start to get info from url[" + url + "]");
				buffer.append(" >> url:").append(url).append("\r\n");
				for (EarthQuakeInfo info : earthlist) {
					buffer.append(" >> info.Magnitude:" + info.getMagnitude())
							.append("\r\n");
					buffer.append(" >> info.Url:" + info.getUrl()).append("\r\n");
					buffer.append(" >> info.Date:" + info.getDate()).append("\r\n");
					buffer.append(" >> info.Depth:" + info.getDepth()).append(
							"\r\n");
					buffer.append(" >> info.Latitude" + info.getLatitude()).append(
							"\r\n");
					buffer.append(" >> info.Longitude" + info.getLongitude())
							.append("\r\n");
					buffer.append(" >> info.Comments:" + info.getComments())
							.append("\r\n");
					buffer.append("\r\n");
					try{
						getEarthQuakeDetail(info.getUrl());
					}catch(Exception e){
						continue;
					}
				}
				buffer = null;
//				break;
		}
		umap.clear();
		
	}
	
	private static void RegionRealTimeQuake(String url){
		StringBuffer buffer = new StringBuffer();
		List<EarthQuakeInfo> earthlist = getRegionRealTimeQuake(url);
		System.out.println(" >> start to get info from url[" + url + "]");
		buffer.append(" >> url:").append(url).append("\r\n");
		for (EarthQuakeInfo info : earthlist) {
			buffer.append(" >> info.Magnitude:" + info.getMagnitude())
					.append("\r\n");
			buffer.append(" >> info.Url:" + info.getUrl()).append("\r\n");
			buffer.append(" >> info.Date:" + info.getDate()).append("\r\n");
			buffer.append(" >> info.Depth:" + info.getDepth()).append(
					"\r\n");
			buffer.append(" >> info.Latitude" + info.getLatitude()).append(
					"\r\n");
			buffer.append(" >> info.Longitude" + info.getLongitude())
					.append("\r\n");
			buffer.append(" >> info.Comments:" + info.getComments())
					.append("\r\n");
			buffer.append("\r\n");
			getEarthQuakeDetail(info.getUrl());
		}
		System.out.println(buffer);
		buffer = null;
	}

	/**
	 * 获取区域地震列表
	 */
	static HashMap<String, String> getRegionListUrl() {
		Parser p1 = null;
		Parser p2 = null;
		Parser p3 = null;
		HashMap<String, String> umap = new HashMap<String, String>();
		try {
			p1 = new Parser();
			p1.setURL(USGS_REALTIME_LIST_URL);
			p1.setEncoding("UTF-8");

			NodeFilter filter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(filter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "five column"));

			if (null != list) {
				if (list.size() == 2) {
					Div div = (Div) list.elementAt(1);
					p2 = new Parser();
					p2.setInputHTML(div.toHtml());
					p2.setEncoding("UTF-8");

					NodeList list2 = p2
							.extractAllNodesThatMatch(new TagNameFilter("ul"));
					if (null != list2 && list2.size() == 2) {
						p3 = new Parser();
						p3.setInputHTML(list2.toHtml());
						p3.setEncoding("UTF-8");

						NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
						NodeList list3 = p3.extractAllNodesThatMatch(filter2);
						for (int i = 0; i < list3.size(); i++) {
							LinkTag link = (LinkTag) list3.elementAt(i);
							if (null != link
									&& link.getLink().startsWith(
											"/earthquakes/recenteqsww/Maps/")) {
								String url = link.getLink();
								url = url.replace(".php", "_eqs.php");
								if (url.startsWith("http://")) {
									umap.put(url, link.getLinkText());
								} else {
									umap.put(USGS_URL_PREFIX + url, link
											.getLinkText());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != p1) {
				p1 = null;
			}
			if (null != p2) {
				p2 = null;
			}
			if (null != p3) {
				p3 = null;
			}
		}
		return umap;
	}

	/**
	 * 获取实时地震列表数据
	 */
	public static List<EarthQuakeInfo> getRegionRealTimeQuake(String url) {
		Parser p1 = null;
		Parser p2 = null;
		String content = null;
		List<EarthQuakeInfo> elist = new ArrayList<EarthQuakeInfo>();
		try {
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("UTF-8");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("id", "main"));
			if (null != list && list.size() > 0) {

				p2 = new Parser();
				p2.setInputHTML(list.toHtml());
				p2.setEncoding("UTF-8");
				NodeFilter fileter2 = new NodeClassFilter(TableTag.class);
				NodeList list2 = p2.extractAllNodesThatMatch(fileter2);
				TableTag table = (TableTag) list2.elementAt(0);

				if (null != table) {
					int rows = table.getRowCount();
					for (int i = 0; i < rows; i++) {
						if (i == 0) {
							continue;
						}
						TableRow row = table.getRow(i);
						if (null != row) {
							TableColumn[] cols = row.getColumns();
							EarthQuakeInfo info = new EarthQuakeInfo();
							if (cols.length == 7) {
								int k = 0;
								for (TableColumn col : cols) {
									switch (k) {
									case 1:
										// 震级
										String magnitude = col.getStringText()
												.replace("<b>", "").replace(
														"</b>", "").replace(
														"&nbsp;", "").replace(
														"<font color='red'>",
														"").replace("</font>",
														"");
										info.setMagnitude(Float
												.valueOf(magnitude));
										break;
									case 2:
										// 时间
										String date = null;
										if (col.getChild(0) instanceof LinkTag) {
											LinkTag link = (LinkTag) col
													.getChild(0);
											info.setUrl(USGS_URL_PREFIX
													+ link.getLink());
											if (null != link.getLinkText()
													&& !"".equals(link
															.getLinkText())) {
												date = col
														.toPlainTextString()
														.replace("<b>", "")
														.replace("</b>", "")
														.replace("&nbsp;", "")
														.replace(
																"<font color='red'>",
																"").replace(
																"</font>", "");
												info.setDate(date);
											}
										}
										// System.out.println(" >>
										// date:"+col.toPlainTextString());
										break;
									case 3:
										// 震源深度
										String depth = col.getStringText()
												.replace("<b>", "").replace(
														"</b>", "").replace(
														"&nbsp;", "");
										info.setDepth(Float.valueOf(depth
												.trim()));
										break;
									case 4:
										// 纬度
										String latitude = col.getStringText()
												.replace("<b>", "").replace(
														"</b>", "").replace(
														"&nbsp;", "");
										info.setLatitude(latitude);
										break;
									case 5:
										// 经度
										String longitude = col.getStringText()
												.replace("<b>", "").replace(
														"</b>", "").replace(
														"&nbsp;", "");
										info.setLongitude(longitude);
										break;
									case 6:
										// 地点
										String location = col.getStringText()
												.replace("<b>", "").replace(
														"</b>", "").replace(
														"&nbsp;", "");
										info.setComments(location);
										break;

									}
									k++;
								}
							}
							elist.add(info);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != p1) {
				p1 = null;
			}
			if (null != p2) {
				p2 = null;
			}
		}
		return elist;
	}

	/**
	 * 获取过去8-30天的地震列表数据
	 */
	public static List<EarthQuakeInfo> getLatestEarthQuake() {
		Parser p1 = null;
		String content = null;
		List<EarthQuakeInfo> elist = new ArrayList<EarthQuakeInfo>();
		try {
			p1 = new Parser();
			p1.setURL(USGS_LATEST_LIST);
			p1.setEncoding("UTF-8");
			NodeFilter fileter = new NodeClassFilter(TableTag.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "qed"));
			if (null != list && list.size() > 0) {
				TableTag table = (TableTag) list.elementAt(0);
				if (null != table) {
					int rows = table.getRowCount();
					for (int i = 0; i < rows; i++) {
						if (i == 0 || (i % 17 == 0)) {
							continue;
						}
						TableRow row = table.getRow(i);
						if (null != row) {
							TableColumn[] cols = row.getColumns();
							EarthQuakeInfo info = new EarthQuakeInfo();
							if (cols.length == 6) {
								int k = 0;
								for (TableColumn col : cols) {
									switch (k) {
									case 0:
										if (col.getChild(0) instanceof LinkTag) {
											LinkTag link = (LinkTag) col
													.getChild(0);
											if (null != link.getLink()) {
												info.setUrl(link.getLink());
											}
											info.setDate(link.getLinkText());
										}
										break;
									case 1:
										// 纬度
										String latitude = col.getStringText();
										info.setLatitude(latitude);
										break;
									case 2:
										// 经度
										String longitude = col.getStringText();
										info.setLongitude(longitude);
										break;
									case 3:
										// 震源深度
										String depth = col.getStringText();
										info.setDepth(Float.valueOf(depth));
										break;
									case 4:
										// 地震级别
										String magnitude = col.getStringText();
										info.setMagnitude(Float
												.valueOf(magnitude));
										break;
									case 5:
										// 备注
										String comments = col.getStringText();
										info.setComments(comments);
										break;

									}
									k++;
								}
							}
							elist.add(info);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != p1) {
				p1 = null;
			}
		}
		return elist;
	}

	/**
	 * 获取地震详情
	 */
	public static void getEarthQuakeDetail(String url) {
		Parser p1 = null;
		StringBuffer buffer = null;
		String name = null;
		if(null == client.get(url)){
			try {
				buffer = new StringBuffer(url).append("\r\n");
				p1 = new Parser();
				p1.setURL(url);
				NodeFilter fileter = new NodeClassFilter(TableTag.class);
				NodeList list = p1.extractAllNodesThatMatch(fileter)
						.extractAllNodesThatMatch(
								new HasAttributeFilter("id", "parameters"));
				if (null != list && list.size() > 0) {
					TableTag table = (TableTag) list.elementAt(0);
					if (null != table) {
						int rows = table.getRowCount();
						for (int i = 0; i < rows; i++) {
							TableRow row = table.getRow(i);
							if (null != row) {
								TableColumn[] cols = row.getColumns();
								Node node = row.getChild(0);
								if (null != node) {
									String key = null;
									Integer id = -1;
									String value = null;
									try {
										NodeList nl = node.getChildren();
										// System.out.println(" >>
										// nl:"+nl.toHtml());
										if (nl.elementAt(1) instanceof LinkTag) {
											LinkTag link = (LinkTag) nl
													.elementAt(1);
											if (null != link) {
												key = link.getLinkText().trim();
											} else {
												key = node.toPlainTextString()
														.trim();
											}
										} else {
											key = node.toPlainTextString().trim();
										}
										// System.out.println("key:"+key);
										id = SYS_HASH.get(key);
										for (TableColumn col : cols) {
											value = col.toPlainTextString();
										}
										// System.out.println(" >> id:"+id);
										if (null == id) {
											break;
										}
										switch (id) {
										case 1:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 2:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 3:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 4:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 5:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 6:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 7:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 8:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 9:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 10:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											break;
										case 11:
											buffer.append(" >> " + id + "\t" + key
													+ "\t" + value);
											name = value;
											break;
										default:
											buffer.append(" >> " + id
													+ " no action");
											break;
										}
										buffer.append("\r\n");
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							// break;
						}
						buffer.append("\r\n");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != p1) {
					p1 = null;
				}
				if (null == name) {
					name = CommonUtil.GenerateSequence(0);
				}
			}
		
			// System.out.println(" >> name:"+name+"\tbuffer:"+buffer.toString());CommonUtil.getDate("-") + 
			IOUtil.createFile(buffer.toString(), FILE_DIR_PATH
					+ "\\DETAIL\\"+name.substring(0,2)+File.separator, name + ".txt");
			client.add(url, buffer);
			System.out.println("Finish [" + url + "]");
		}else{
			System.err.println( " 缓存中已经存在:" + url);
		}
	}

	static String getNameFromUrl(String url) {
		String name = null;
		try {
			int start = url.lastIndexOf("/");
			int end = url.lastIndexOf(".");
			if (start != -1 && end != -1) {
				name = url.substring(start + 1, end);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null == name) {
				name = CommonUtil.GenerateSequence(0);
			}
		}
		return name;
	}
}

class EarthQuakeInfo {
	// 序列号
	private long id;
	// 发布时间
	private String date;
	// 纬度
	private String latitude;
	// 经度
	private String longitude;
	// 震源深度
	private Float depth;
	// 震级
	private Float magnitude;
	// 备注
	private String comments;
	// 连接的URL
	private String url;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Float getDepth() {
		return depth;
	}

	public void setDepth(Float depth) {
		this.depth = depth;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Float getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(Float magnitude) {
		this.magnitude = magnitude;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}

class EarthQuakeDetail extends EarthQuakeInfo {
	// 位置 纬度+经度
	private String location;
	// 区域
	private String region;
	// 距离
	private String distinces;
	// 不确定位置Location Uncertainty
	private String lu;
	// 参数
	private String parameters;
	// 源
	private String source;
	// 标记
	private String remarks;
	// 事件编号
	private String eventId;

	public String getDistinces() {
		return distinces;
	}

	public void setDistinces(String distinces) {
		this.distinces = distinces;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLu() {
		return lu;
	}

	public void setLu(String lu) {
		this.lu = lu;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
