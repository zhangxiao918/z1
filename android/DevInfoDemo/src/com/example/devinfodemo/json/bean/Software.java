
package com.example.devinfodemo.json.bean;

import com.example.devinfodemo.json.bean.sd.DatabaseInfo;

public class Software {
    // 数据库信息
    private DatabaseInfo databas;

    /**
     * 手机基本信息
     */
    private String sysVersion;
    private String hsman;
    private String hstype;
    private String screenSize;

    /**
     * @return the databas
     */
    public DatabaseInfo getDatabas() {
        return databas;
    }

    /**
     * @param databas the databas to set
     */
    public void setDatabas(DatabaseInfo databas) {
        this.databas = databas;
    }

    /**
     * @return the sysVersion
     */
    public String getSysVersion() {
        return sysVersion;
    }

    /**
     * @param sysVersion the sysVersion to set
     */
    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    /**
     * @return the hsman
     */
    public String getHsman() {
        return hsman;
    }

    /**
     * @param hsman the hsman to set
     */
    public void setHsman(String hsman) {
        this.hsman = hsman;
    }

    /**
     * @return the hstype
     */
    public String getHstype() {
        return hstype;
    }

    /**
     * @param hstype the hstype to set
     */
    public void setHstype(String hstype) {
        this.hstype = hstype;
    }

    /**
     * @return the screenSize
     */
    public String getScreenSize() {
        return screenSize;
    }

    /**
     * @param screenSize the screenSize to set
     */
    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

}
