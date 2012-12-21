
package com.example.devinfodemo.json.bean.hd;

import com.example.devinfodemo.json.bean.Info;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Sersor
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-10-8 上午10:07:18
 */
public class JSensor extends Info {

    private int total = 0;
    private List<JSensorDetail> list = new ArrayList<JSensorDetail>();

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the list
     */
    public List<JSensorDetail> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<JSensorDetail> list) {
        this.list = list;
    }

}
