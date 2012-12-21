package com.chinamilitary.test;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.URL;  
import java.net.URLConnection;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
  
public class GoogleMusicUtil{  
      
    /**正则表达式Pattern: 提取歌曲*/  
    private Pattern patternName;  
      
    /**正则表达式Pattern: 提取Artist*/  
    private Pattern patternSonger;  
      
    /**正则表达式Pattern: 提取文件大小*/  
    private Pattern patternSize;  
      
    /**正则表达式Pattern: 提取格式*/  
    private Pattern patternFormat;  
      
    /**正则表达式Pattern: 提取下载地址*/  
    private Pattern patternUrl;  
      
    /** 
     * 构造方法 
     */  
    public GoogleMusicUtil(){  
        int flags = Pattern.CASE_INSENSITIVE | Pattern.DOTALL;  
        patternName = Pattern.compile("<tr class=\"meta-data-tr\"><td class=\"td-song-name\">(.*?)</td>", flags);  
        patternSonger = Pattern.compile("<tr class=\"meta-data-tr\">.+?<td class=\"td-singer\">(.*?)</td>", flags);  
        patternSize = Pattern.compile("<tr class=\"meta-data-tr\">.+?<td class=\"td-size\">(.*?)</td>", flags);  
        patternFormat = Pattern.compile("<tr class=\"meta-data-tr\">.+?<td class=\"td-format\">(.*?)</td>", flags);  
//        patternUrl = Pattern.compile("<a href=\"\" mce_href=\"""/music/top100/url\\?q=(.*?)&ct=.+?", flags);  
    }  
      
    public static void main(String[] args) {  
        String url = "http://www.google.cn/music/homepage"; //首页  
        /* 
         * 同时还可以用以下 URL 进行测试 
         * "http://www.google.cn/music/chartlisting?q=chinese_new_songs_cn&cat=song&grouping=new-release_music" //排行版-->华语新歌 
         * http://www.google.cn/music/chartlisting?q=chinese_new-release_albums_cn&cat=album"                   //排行版-->最新专辑 
         * "http://www.google.cn/music/topiclisting?q=top100_new_age1_2009&cat=song"                            //专题-->天籁新世纪之音1  
         * http://www.google.cn/music/album?id=Befc2c2000898fb75"                                               //专辑-->《我的黄金时代》 
         * http://www.google.cn/music/artist?id=A745deb6e0af83070"                                              //歌手-->王力宏 
         */  
        GoogleMusicUtil g = new GoogleMusicUtil();  
        List<String> list = g.getMusicIds(url);     
        if (!list.isEmpty()) {  
            System.out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta http-equiv=\"Content-type\" content=\"text/html; charset=UTF-8\"><title>谷歌音乐批量下载</title></head><body><table>");  
            String id;  
            GoogleMusic music;  
            String alink;  
            for (int i=0; i<list.size(); i++) {  
                if(i>0 && i%10==0){  //每循环10次后休息2.5秒再进行请求, 否则可能被Google当作网络攻击  
                    try {  
                        Thread.sleep(2500);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
                  
                id = list.get(i);  
                music = g.getGoogleMusic(id);  
                if(music.getUrl() == null){  
//                    alink = "<a href="\" mce_href="\""http://g.top100.cn/7872775/html/download.html?id=" + id + "\" target=\"_blank\">点击进入下载页面</a>";  
                }else{  
//                    alink = "<a href="\" mce_href="\""" + music.getUrl().replaceAll("%3A", ":").replaceAll("%2F", "/") + "\" target=\"_blank\">下载</a>";;  
                }  
//                System.out.println("<tr><td>"+music.getId()+"</td><td>"+music.getName()+"</td><td>"+music.getSonger()+"</td><td>"+music.getSize()+"</td><td>"+music.getFormat()+"</td><td>"+alink+"</td><tr>");  
            }  
            System.out.println("</table></body></html>");  
        }else{  
            System.out.println(url + "不包含任何GOOGLE音乐的ID");  
        }  
    }  
  
    /** 
     * 根据 URL 读取应对页面的HTML源码 
     * @param url Google音乐的URL 
     * @return String  URL应对页面的HTML源码, 如果连接到指定URL, 则返回一个空字符串("") 
     */  
    public String getHtmlCode(String url) {  
        try {  
            URL u = new URL(url);  
            URLConnection urlConnection = u.openConnection();  
            urlConnection.setAllowUserInteraction(false);  
            // 使用openStream得到一输入流并由此构造一个BufferedReader对象  
            BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));  
            String inputLine;  
            StringBuffer tempHtml = new StringBuffer();  
            while ((inputLine = in.readLine()) != null){ // 从输入流不断的读数据，直到读完为止  
                tempHtml.append(inputLine).append("\n");  
            }  
            return tempHtml.toString();  
        }catch (IOException e) {  
            return "";  
        }  
    }  
      
    /** 
     * 根据 URL 获取对应页面上的所有歌曲ID 
     * @param url google音乐的URL 
     * @return URL对应页面的所有歌曲ID的List, 如果此页面,没有歌曲ID,则返回一个长度为 0 的List 
     */  
    public List<String> getMusicIds(String url){  
        String html = getHtmlCode(url);  
//        String idPatternStr = "onclick=\"return _sl_onclickStreaming\\("/music/url\\?q\\\\x3dhttp%3A%2F%2Fwww\\.google\\.cn.*?id%3D(.+?)%26type%3Dsong%26autoplay%3D.+?"\\);\">";  
//        Pattern p = Pattern.compile(idPatternStr, Pattern.CASE_INSENSITIVE);  
//        Matcher matcher = p.matcher(html);  
        List<String> list = new ArrayList<String>();  
        String id = null;  
//        while(matcher.find()){  
//            id =  matcher.group(1);  
//            if(!list.contains(id)){  
//                list.add(id);  
//            }  
//        }  
        return list;  
    }  
      
    /** 
     * 根据歌曲ID查询歌曲的详细信息 
     * @param id 歌曲ID 
     * @return 返回一个 GoogleMusic 对象, 此 GoogleMusic 对象的ID即为传过来的ID 
     */  
    public GoogleMusic getGoogleMusic(String id){  
        String html = getHtmlCode("http://www.google.cn/music/top100/musicdownload?id=" + id);  
        Matcher mName = patternName.matcher(html);  
        Matcher mSonger = patternSonger.matcher(html);  
        Matcher mSize = patternSize.matcher(html);  
        Matcher mFormat = patternFormat.matcher(html);  
        Matcher mUrl = patternUrl.matcher(html);  
          
        GoogleMusic music = new GoogleMusic();  
        music.setId(id);  
        if(mName.find()){  
            music.setName(mName.group(1));  
        }  
        if(mSonger.find()){  
            music.setSonger(mSonger.group(1));  
        }  
        if(mSize.find()){  
            music.setSize(mSize.group(1));  
        }  
        if(mFormat.find()){  
            music.setFormat(mFormat.group(1));  
        }  
        if(mUrl.find()){  
            music.setUrl(mUrl.group(1));  
        }  
        return music;  
    }  
}