
package com.bluestome.javatest.hzti;

import java.io.IOException;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * @ClassName: ParserHtml
 * @Description: TODO
 * @author bluestome.zhang
 * @date 2013-3-29 下午5:06:30
 */
public class ParserHtml {

    private static final String FILE_PATH = "F:\\temp\\query.html";

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
                    System.out.println(i + ":" + tr.toPlainTextString());
                }
            } else {
                System.err.println("No Sub Element Fond!");
            }
        } else {
            System.err.println("No Element Fond!");
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
