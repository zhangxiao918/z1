
package com.bluestome.hzti.qry.net;

import java.io.IOException;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.util.Log;

/**
 * @ClassName: ParserHtml
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-3-29 下午5:06:30
 */
public class ParserHtml {

    private static final String FILE_PATH = "F:\\temp\\query.html";

    private static final String TAG = ParserHtml.class.getSimpleName();

    public static void parser() throws IOException, ParserException {
        Parser p = new Parser();
        p.setResource(FILE_PATH);
        p.setEncoding("UTF8");

        NodeFilter fileter = new NodeClassFilter(TableTag.class);
        NodeList list = p.extractAllNodesThatMatch(fileter)
                .extractAllNodesThatMatch(
                        new HasAttributeFilter("class", "xxcxsspopmain"));
        if (null != list && list.size() > 0) {
            TableTag table = (TableTag) list.elementAt(0);
            p = new Parser();
            p.setInputHTML(table.toHtml());
            fileter = new NodeClassFilter(TableRow.class);
            list = p.extractAllNodesThatMatch(fileter);
            if (null != list) {
                int c = list.size();
                int e = c - 1; // 最后一列为提示内容,无关
                // 获取最终数据
                for (int i = 0; i < list.size(); i++) {
                    TableRow tr = (TableRow) list.elementAt(i);
                    if (i == e || i < 2) { // 前2项为提示和标题，不需要
                        continue;
                    }
                    Log.d(TAG, i + ":" + tr.toPlainTextString());
                }
            } else {
                System.err.println("No Sub Element Fond!");
            }
        } else {
            System.err.println("No Element Fond!");
        }
        p = null;
    }

    public static void parser(String content) throws IOException, ParserException {
        Parser p = new Parser();
        p.setInputHTML(content);

        NodeFilter fileter = new NodeClassFilter(TableTag.class);
        NodeList list = p.extractAllNodesThatMatch(fileter)
                .extractAllNodesThatMatch(
                        new HasAttributeFilter("class", "xxcxsspopmain"));
        if (null != list && list.size() > 0) {
            TableTag table = (TableTag) list.elementAt(0);
            p = new Parser();
            p.setInputHTML(table.toHtml());
            fileter = new NodeClassFilter(TableRow.class);
            list = p.extractAllNodesThatMatch(fileter);
            if (null != list) {
                int c = list.size();
                int e = c - 1; // 最后一列为提示内容,无关
                // 获取最终数据
                for (int i = 0; i < list.size(); i++) {
                    TableRow tr = (TableRow) list.elementAt(i);
                    if (i == e || i < 2) { // 前2项为提示和标题，不需要
                        continue;
                    }
                    String tmp = tr.toPlainTextString().trim();
                    String[] ts = tmp.split("\r\n");
                    Log.d(TAG, "号牌号码:" + ts[0].trim());
                    Log.d(TAG, "号牌种类:" + ts[1].trim());
                    Log.d(TAG, "违法时间:" + ts[2].trim());
                    Log.d(TAG, "违法地点:" + ts[3].trim());
                    Log.d(TAG, "违法行为:" + ts[4].trim());
                    Log.d(TAG, "处理标记:" + ts[5].trim());
                    Log.d(TAG, "缴款标记:" + ts[6].trim());
                }
            } else {
                Log.e(TAG, "No Sub Element Fond!");
            }
        } else {
            Log.e(TAG, "No Element Fond!");
        }
        p = null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            parser();
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
