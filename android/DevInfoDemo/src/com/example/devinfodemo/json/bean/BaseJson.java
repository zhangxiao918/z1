
package com.example.devinfodemo.json.bean;

import java.util.Date;

/**
 * @ClassName: BaseJson
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-10-8 上午09:37:20
 */
public class BaseJson {

    /**
     * 创建时间
     */
    protected Date currentTime = new Date();
    /**
     * 产生数据的终端IP
     */
    protected String ip;

    /**
     * @return the currentTime
     */
    public Date getCurrentTime() {
        return currentTime;
    }

    /**
     * @param currentTime the currentTime to set
     */
    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

}
