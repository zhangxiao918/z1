
package com.bluestome.javatest.sina.tech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @ClassName: ParserTools
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-4-1 下午12:54:27
 */
public class ParserTools {

    public static final String TECH_URL = "http://tech.sina.com.cn/";

    public void parser() throws ParserException {
        long s = System.currentTimeMillis();
        Parser p = new Parser();
        p.setURL(TECH_URL);
        p.setEncoding("GB2312");
        int count = 0;

        NodeFilter fileter = new NodeClassFilter(LinkTag.class);
        NodeList list = p.extractAllNodesThatMatch(fileter);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                long s1 = System.currentTimeMillis();
                LinkTag link = (LinkTag) list.elementAt(i);
                String u = link.getLink();
                String t = link.getLinkText().trim();
                // 获取URL中的最后一个网页地址,例如：http://tech.sina.com.cn/t/2013-04-01/00398198149.shtml
                // 中的 00398198149.shtml,并校验是否为数字类型
                String ends = "abcdefghijkl";
                int spos = u.lastIndexOf("/");
                int epos = u.lastIndexOf(".");
                if (spos != -1 && epos != -1 && epos > spos) {
                    ends = u.substring(spos + 1, epos);
                }
                if (u.startsWith(TECH_URL) && (null != t
                        && !t.equals("") && t.length() > 0)) {
                    if (applyURL(u) && applyNumberic(ends)) {
                        System.out.println((count++) + "\t" + u);
                    }
                }
                System.out.println("\t\t 一条循环耗时：" + (System.currentTimeMillis() - s1));
            }
        }
        System.out.println("耗时:" + (System.currentTimeMillis() - s));
    }

    public List<LinkBean> findList() throws ParserException {
        List<LinkBean> rList = new ArrayList<LinkBean>(25);
        Parser p = new Parser();
        p.setURL(TECH_URL);
        p.setEncoding("GB2312");
        LinkBean bean = null;
        NodeFilter fileter = new NodeClassFilter(LinkTag.class);
        NodeList list = p.extractAllNodesThatMatch(fileter);
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LinkTag link = (LinkTag) list.elementAt(i);
                String u = link.getLink();
                String t = link.getLinkText().trim();
                // 获取URL中的最后一个网页地址,例如：http://tech.sina.com.cn/t/2013-04-01/00398198149.shtml
                // 中的 00398198149.shtml,并校验是否为数字类型
                String ends = "abcdefghijkl";
                int spos = u.lastIndexOf("/");
                int epos = u.lastIndexOf(".");
                if (spos != -1 && epos != -1 && epos > spos) {
                    ends = u.substring(spos + 1, epos);
                }
                if (u.startsWith(TECH_URL) && (null != t
                        && !t.equals("") && t.length() > 0)) {
                    if (applyURL(u) && applyNumberic(ends)) {
                        bean = new LinkBean();
                        bean.setUrl(u);
                        bean.setTitl(t);
                        rList.add(0, bean);
                    }
                }
            }
        }
        p = null;
        return rList;
    }

    /**
     * 校验URL地址
     * 
     * @param value
     * @return
     */
    public boolean applyURL(String value) {
        String regx = "^" + TECH_URL + "+[\\w-]+(/[\\w-./]*).shtml$";
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(value);
        boolean b = m.matches();
        return b;
    }

    /**
     * 校验是否为数字类型
     * 
     * @param value
     * @return
     */
    public boolean applyNumberic(String value) {
        String regx = "^[0-9]*$";
        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(value);
        boolean b = m.matches();
        return b;
    }

    /**
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ParserTools tools = new ParserTools();
        try {
            List<LinkBean> list = tools.findList();
            // 排序
            Comparator<LinkBean> comparator = new Comparator<LinkBean>() {
                @Override
                public int compare(LinkBean lhs, LinkBean rhs) {
                    if (lhs.getUrl().compareTo(rhs.getUrl()) > 0) {
                        return -1;
                    } else if (lhs.getUrl().compareTo(rhs.getUrl()) < 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            };
            Collections.sort(list, comparator);
            for (LinkBean l : list) {
                System.out.println(l.getUrl());
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

}
