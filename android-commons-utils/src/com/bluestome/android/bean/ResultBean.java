
package com.bluestome.android.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultBean implements Serializable {

    private boolean bool;
    private List<LinkBean> list = new ArrayList<LinkBean>();
    private HashMap<String, LinkBean> map = new HashMap<String, LinkBean>();

    public ResultBean() {
        bool = false;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public List<LinkBean> getList() {
        return list;
    }

    public void setList(List<LinkBean> list) {
        this.list = list;
    }

    public synchronized void add(LinkBean bean) {
        list.add(bean);
    }

    public void clear() {
        list.clear();
    }

    public HashMap<String, LinkBean> getMap() {
        return map;
    }

    public void setMap(HashMap<String, LinkBean> map) {
        this.map = map;
    }

    public synchronized void put(String key, LinkBean value) {
        map.put(key, value);
    }

    public synchronized void clearHash() {
        map.clear();
    }

}
