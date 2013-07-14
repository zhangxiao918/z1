
package com.bluestome.imageloader.biz;

import com.bluestome.android.bean.LinkBean;
import com.bluestome.android.bean.ResultBean;
import com.bluestome.imageloader.common.Constants;
import com.bluestome.imageloader.domain.ImageBean;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LITag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.ULTag;
import org.htmlparser.util.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ParserBiz {
    /**
     * 获取分类下的分页信息
     * 
     * @param url
     * @param attribute
     * @param value
     * @return
     * @throws Exception
     */
    public static ResultBean indexHasPaging2(String content) throws Exception {
        ResultBean result = new ResultBean();
        Parser parser = new Parser();
        parser.setInputHTML(content);
        parser.setEncoding("UTF-8");
        // 获取指定ID的DIV内容
        NodeFilter filter = new NodeClassFilter(Div.class);
        NodeList list = parser.extractAllNodesThatMatch(filter)
                .extractAllNodesThatMatch(
                        new HasAttributeFilter("class", "page"));
        int pageNum = 1;
        if (list != null && list.size() > 0) {
            parser = new Parser();
            parser.setInputHTML(list.toHtml());
            filter = new NodeClassFilter(LinkTag.class);
            NodeList list2 = parser.extractAllNodesThatMatch(filter);
            if (null != list && list2.size() > 0) {
                String tmp = null;
                LinkBean l1 = null;
                if (list2.size() > 10) {
                    LinkTag tmpLink = (LinkTag) list2.elementAt(8);
                    try {
                        pageNum = Integer.valueOf(tmpLink.getLinkText());
                    } catch (Exception e) {
                        throw new Exception("分页出现异常");
                    }
                    for (int i = 1; i < pageNum + 1; i++) {
                        tmp = Constants.URL + "/index/" + i + ".html";
                        l1 = new LinkBean();
                        l1.setLink(tmp);
                        l1.setTitle("" + i);
                        result.put(tmp, l1);
                    }
                } else {
                    for (int i = 0; i < list2.size(); i++) {
                        l1 = new LinkBean();
                        LinkTag link2 = (LinkTag) list2.elementAt(i);
                        if (!link2.getLink().equalsIgnoreCase("#")) {
                            if (!link2.getLink().startsWith("http://")) {
                                tmp = Constants.URL + link2.getLink();
                            } else {
                                tmp = link2.getLink();
                            }
                        }
                        l1.setLink(tmp);
                        l1.setTitle(link2.getLinkText());
                        result.put(tmp, l1);
                    }
                }

            }
            if (null != parser)
                parser = null;
        }
        if (result.getMap().size() > 0) {
            result.setBool(true);
        }
        return result;
    }

    public static List<ImageBean> getImageBeanList(final String content) {
        List<ImageBean> result = new ArrayList<ImageBean>(10);
        Parser parser = null;
        NodeFilter filter = null;
        try {
            parser = new Parser();

            PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
            factory.registerTag(new ULTag());
            parser.setNodeFactory(factory);

            parser.setInputHTML(content);
            parser.setEncoding("UTF-8");
            // 获取指定ID的DIV内容
            filter = new NodeClassFilter(ULTag.class);
            NodeList list = parser.extractAllNodesThatMatch(filter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("class", "uip uip_250"));
            if (null != list && list.size() > 0) {

                ULTag div = (ULTag) list.elementAt(0);
                parser = new Parser();
                factory = new PrototypicalNodeFactory();
                factory.registerTag(new LITag());
                parser.setNodeFactory(factory);

                parser.setInputHTML(div.toHtml());
                filter = new NodeClassFilter(LITag.class);
                NodeList list2 = parser.extractAllNodesThatMatch(filter);
                if (null != list2 && list2.size() > 0) {
                    ImageBean bean = null;
                    for (int i = 0; i < list2.size(); i++) {
                        LITag li = (LITag) list2.elementAt(i);
                        NodeList tmpNode = li.getChildren();
                        if (null != tmpNode && tmpNode.size() > 0) {
                            Span span = (Span) tmpNode.elementAt(1);
                            NodeList tmpNode2 = span.getChildren();
                            if (null != tmpNode2 && tmpNode2.size() > 0) {
                                LinkTag linkTag = (LinkTag) tmpNode2.elementAt(0);
                                String link = Constants.URL;
                                if (!linkTag.getLink().startsWith("http://")) {
                                    link = Constants.URL + linkTag.getLink();
                                } else {
                                    link = linkTag.getLink();
                                }
                                NodeList tmpNode3 = linkTag.getChildren();
                                if (null != tmpNode3 && tmpNode3.size() > 0) {
                                    ImageTag imgTag = (ImageTag) tmpNode3
                                            .elementAt(0);
                                    String title = imgTag.getAttribute("alt");
                                    String imageUrl = imgTag.getImageURL();
                                    bean = new ImageBean();
                                    bean.setImageDesc(tmpNode.elementAt(5).toHtml());
                                    bean.setTitle(title);
                                    bean.setImageUrl(imageUrl);
                                    bean.setDetailLink(link);
                                    result.add(bean);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != parser)
                parser = null;
        }
        return result;
    }
}
