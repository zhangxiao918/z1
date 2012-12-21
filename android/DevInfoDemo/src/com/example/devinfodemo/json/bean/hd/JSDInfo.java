
package com.example.devinfodemo.json.bean.hd;

import com.example.devinfodemo.json.bean.Info;

/**
 * @ClassName: SDInfo
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-10-8 上午10:15:59
 */
public class JSDInfo extends Info {

    // SD卡总大小
    private String totalSize;
    // SD卡可用大小
    private String availableSize;
    // 内存大小
    private String internalMemorySize;

    /**
     * @return the totalSize
     */
    public String getTotalSize() {
        return totalSize;
    }

    /**
     * @param totalSize the totalSize to set
     */
    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * @return the availableSize
     */
    public String getAvailableSize() {
        return availableSize;
    }

    /**
     * @param availableSize the availableSize to set
     */
    public void setAvailableSize(String availableSize) {
        this.availableSize = availableSize;
    }

    /**
     * @return the internalMemorySize
     */
    public String getInternalMemorySize() {
        return internalMemorySize;
    }

    /**
     * @param internalMemorySize the internalMemorySize to set
     */
    public void setInternalMemorySize(String internalMemorySize) {
        this.internalMemorySize = internalMemorySize;
    }

}
