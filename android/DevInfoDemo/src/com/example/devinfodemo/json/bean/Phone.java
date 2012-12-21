
package com.example.devinfodemo.json.bean;

import java.util.HashMap;

public class Phone extends BaseJson implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    // 客户端硬件信息
    private Hardware hardware;
    // 客户端软件信息
    private Software software;
    // 异常信息
    private HashMap<String, String> exceptions = new HashMap<String, String>();

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    /**
     * @return the exceptions
     */
    public HashMap<String, String> getExceptions() {
        return exceptions;
    }

    /**
     * @param exceptions the exceptions to set
     */
    public void setExceptions(HashMap<String, String> exceptions) {
        this.exceptions = exceptions;
    }

}
