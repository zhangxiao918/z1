
package com.example.devinfodemo.json.bean.sd;

import com.example.devinfodemo.json.bean.Info;

/**
 * @ClassName: DatabaseInfo
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-10-8 上午10:19:06
 */
public class DatabaseInfo extends Info {

    private boolean isLanuncher;
    private String lanuncherExceptionId;
    private boolean isContactOk;
    private String contactExceptionId;
    private boolean isRawContactOk;
    private String rawContactExceptionId;

    /**
     * @return the isLanuncher
     */
    public boolean isLanuncher() {
        return isLanuncher;
    }

    /**
     * @param isLanuncher the isLanuncher to set
     */
    public void setLanuncher(boolean isLanuncher) {
        this.isLanuncher = isLanuncher;
    }

    /**
     * @return the isContactOk
     */
    public boolean isContactOk() {
        return isContactOk;
    }

    /**
     * @param isContactOk the isContactOk to set
     */
    public void setContactOk(boolean isContactOk) {
        this.isContactOk = isContactOk;
    }

    /**
     * @return the isRawContactOk
     */
    public boolean isRawContactOk() {
        return isRawContactOk;
    }

    /**
     * @param isRawContactOk the isRawContactOk to set
     */
    public void setRawContactOk(boolean isRawContactOk) {
        this.isRawContactOk = isRawContactOk;
    }

    /**
     * @return the lanuncherExceptionId
     */
    public String getLanuncherExceptionId() {
        return lanuncherExceptionId;
    }

    /**
     * @param lanuncherExceptionId the lanuncherExceptionId to set
     */
    public void setLanuncherExceptionId(String lanuncherExceptionId) {
        this.lanuncherExceptionId = lanuncherExceptionId;
    }

    /**
     * @return the contactExceptionId
     */
    public String getContactExceptionId() {
        return contactExceptionId;
    }

    /**
     * @param contactExceptionId the contactExceptionId to set
     */
    public void setContactExceptionId(String contactExceptionId) {
        this.contactExceptionId = contactExceptionId;
    }

    /**
     * @return the rawContactExceptionId
     */
    public String getRawContactExceptionId() {
        return rawContactExceptionId;
    }

    /**
     * @param rawContactExceptionId the rawContactExceptionId to set
     */
    public void setRawContactExceptionId(String rawContactExceptionId) {
        this.rawContactExceptionId = rawContactExceptionId;
    }

}
