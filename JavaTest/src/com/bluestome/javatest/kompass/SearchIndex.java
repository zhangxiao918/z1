
package com.bluestome.javatest.kompass;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @ClassName: SearchIndex
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-4-8 上午11:23:55
 */
public class SearchIndex {

    public static void main(String args[]) {
        MobileGo go = new MobileGo();
        go.request(Constants.KOMPASS_URL);
        System.out.println("cookie:" + go.getCookie().toString());

        byte[] body = go.request4Body(Constants.SEARCH_URL.replace("{1}", "fabric"),
                go.getCookie().toString(),
                Constants.KOMPASS_URL);
        System.out.println(body.length);
        if (body.length < 10 * 1024) {
            System.out.println("打印文件");
            try {
                System.out.println(new String(body));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            String content = new String(body);
            // TODO 生成到指定文件
            // File file = new File("d:\\kompass_" + System.currentTimeMillis()
            // + ".html");
            // try {
            // FileOutputStream fos = new FileOutputStream(file);
            // OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
            // osw.write(new String(body, "gb2312"));
            // osw.close();
            // fos.close();
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // 使用HTMLParser进行解析
            Parser p = new Parser();
            try {
                // p.setEncoding("utf-8");
                p.setInputHTML(content);
                // p.setResource(file.getAbsolutePath());
                NodeFilter fileter = new NodeClassFilter(Div.class);
                NodeList list = p.extractAllNodesThatMatch(fileter)
                        .extractAllNodesThatMatch(
                                new HasAttributeFilter("class", "item_resultat_mea"));
                if (null != list && list.size() > 0) {
                    if (null != list && list.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        System.out.println("item.size:" + list.size());
                        for (int k = 0; k < list.size(); k++) {
                            Div div = (Div) list.elementAt(k);
                            String txt = div.toHtml();
                            txt = txt.trim().replace("\r\n", "").replace("\t", "").trim();
                            // System.out.println(txt);
                            // TODO 解析公司名称和网址
                            p = new Parser();
                            p.setInputHTML(txt);
                            fileter = new NodeClassFilter(LinkTag.class);
                            NodeList tList = p.extractAllNodesThatMatch(fileter)
                                    .extractAllNodesThatMatch(
                                            new HasAttributeFilter("class", "color_bleu"));
                            if (null != tList && tList.size() > 0) {
                                LinkTag link = (LinkTag) tList.elementAt(0);
                                sb.append("公司名称:" + link.toPlainTextString()).append("\n");
                                sb.append("网址"
                                        + link.getAttribute("href")).append("\n");
                            }
                            // 回收内存
                            tList = null;
                            p = new Parser();
                            p.setInputHTML(txt);
                            fileter = new NodeClassFilter(Span.class);
                            tList = p.extractAllNodesThatMatch(fileter)
                                    .extractAllNodesThatMatch(
                                            new HasAttributeFilter("class", "floatLeft text09em"));
                            if (null != tList && tList.size() > 0) {
                                // TODO 解析国家
                                Span span = (Span) tList.elementAt(0);
                                sb.append("国家:" + span.toPlainTextString()).append("\n");
                                // TODO 解析城市
                                span = (Span) tList.elementAt(1);
                                sb.append("城市:" + span.toPlainTextString()).append("\n");
                                // TODO 解析邮编
                                span = (Span) tList.elementAt(2);
                                sb.append("邮编:" + span.toPlainTextString()).append("\n");

                            }
                            sb.append("\r\n");
                            // 回收内存
                            tList = null;
                        }
                        System.out.println(sb.toString());
                    }
                    list = null;
                    // TODO 获取分页
                    p = new Parser();
                    p.setInputHTML(content);
                    fileter = new NodeClassFilter(Span.class);
                    list = p.extractAllNodesThatMatch(fileter)
                            .extractAllNodesThatMatch(
                                    new HasAttributeFilter("id", "pagination"));
                    if (null != list && list.size() > 0) {
                        System.out.println("页数:" + list.size());
                    }

                } else {
                    System.out.println("解析的数据内容为空");
                }
            } catch (ParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                p = null;
            }
        }

    }
}
