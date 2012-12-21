/**
 * 
 */
package com.skymobi.android.bean.mrp;

import com.skymobi.android.bean.bytebean.annotation.ByteField;
import com.skymobi.android.util.ByteOrder;

/**
 * @author isdom
 *
 */
public class MrpBean extends AbstractMrpBean {
    
    @ByteField(index = 0)
    private byte flag1 = 'M';

    @ByteField(index = 1)
    private byte flag2 = 'R';

    @ByteField(index = 2)
    private byte flag3 = 'P';

    @ByteField(index = 3)
    private byte flag4 = 'G';
    
    @ByteField(index = 4)
    private int appHeadLength;
    
    @ByteField(index = 5)
    private int mrpFileLength;
    
    @ByteField(index = 6)
    private int indexOffset;

    @ByteField(index = 7, fixedLength = 12, charset="GBK")
    private String mrpName;

    @ByteField(index = 8, fixedLength = 24, charset="GBK")
    private String appName;
    
    @SuppressWarnings("unused")
	@ByteField(index = 9, fixedLength = 16)
    private String reserved1;
    
    @ByteField(index = 10)
    private int uploadId;
    
    @ByteField(index = 11)
    private int appUploadVersion;
    
    @ByteField(index = 12)
    private int appAttribute;

    @ByteField(index = 13)
    private int mrpFileVersion;
    
    @ByteField(index = 14)
    private int crc32;
    
    @ByteField(index = 15, fixedLength=40, charset="GBK")
    private String vender;
    
    @ByteField(index = 16, fixedLength=64, charset="GBK")
    private String appDescription;
    
    @ByteField(index = 17, byteOrder=ByteOrder.BigEndian)
    private int appId;
    
    @ByteField(index = 18, byteOrder=ByteOrder.BigEndian)
    private int appVersion;
    
    @ByteField(index = 19, byteOrder=ByteOrder.BigEndian)
    private short paymentModuleVersion;
    
    @ByteField(index = 20, byteOrder=ByteOrder.BigEndian)
    private short paymentChannelNo;
    
    @ByteField(index = 21)
    private short screenWidth;
    
    @ByteField(index = 22)
    private short screenHeight;
    
    @ByteField(index = 23)
    private byte platform;
    
    @SuppressWarnings("unchecked")
    public static MrpBean fromBytes(byte[] bytes) {
        return  (MrpBean)AbstractMrpBean.fromBytes(bytes, MrpBean.class);
    }

    /**
     * @return the appHeadLength
     */
    public int getAppHeadLength() {
        return appHeadLength;
    }

    /**
     * @param appHeadLength the appHeadLength to set
     */
    public void setAppHeadLength(int appHeadLength) {
        this.appHeadLength = appHeadLength;
    }

    /**
     * @return the mrpFileLength
     */
    public int getMrpFileLength() {
        return mrpFileLength;
    }

    /**
     * @param mrpFileLength the mrpFileLength to set
     */
    public void setMrpFileLength(int mrpFileLength) {
        this.mrpFileLength = mrpFileLength;
    }

    /**
     * @return the indexOffset
     */
    public int getIndexOffset() {
        return indexOffset;
    }

    /**
     * @param indexOffset the indexOffset to set
     */
    public void setIndexOffset(int indexOffset) {
        this.indexOffset = indexOffset;
    }

    /**
     * @return the mrpName
     */
    public String getMrpName() {
        return mrpName;
    }

    /**
     * @param mrpName the mrpName to set
     */
    public void setMrpName(String mrpName) {
        this.mrpName = mrpName;
    }

    /**
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return the uploadId
     */
    public int getUploadId() {
        return uploadId;
    }

    /**
     * @param uploadId the uploadId to set
     */
    public void setUploadId(int uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * @return the appUploadVersion
     */
    public int getAppUploadVersion() {
        return appUploadVersion;
    }

    /**
     * @param appUploadVersion the appUploadVersion to set
     */
    public void setAppUploadVersion(int appUploadVersion) {
        this.appUploadVersion = appUploadVersion;
    }

    /**
     * @return the appAttribute
     */
    public int getAppAttribute() {
        return appAttribute;
    }

    /**
     * @param appAttribute the appAttribute to set
     */
    public void setAppAttribute(int appAttribute) {
        this.appAttribute = appAttribute;
    }

    /**
     * @return the mrpFileVersion
     */
    public int getMrpFileVersion() {
        return mrpFileVersion;
    }

    /**
     * @param mrpFileVersion the mrpFileVersion to set
     */
    public void setMrpFileVersion(int mrpFileVersion) {
        this.mrpFileVersion = mrpFileVersion;
    }

    /**
     * @return the crc32
     */
    public int getCrc32() {
        return crc32;
    }

    /**
     * @param crc32 the crc32 to set
     */
    public void setCrc32(int crc32) {
        this.crc32 = crc32;
    }

    /**
     * @return the vender
     */
    public String getVender() {
        return vender;
    }

    /**
     * @param vender the vender to set
     */
    public void setVender(String vender) {
        this.vender = vender;
    }

    /**
     * @return the appDescription
     */
    public String getAppDescription() {
        return appDescription;
    }

    /**
     * @param appDescription the appDescription to set
     */
    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    /**
     * @return the appId
     */
    public int getAppId() {
        return appId;
    }

    /**
     * @param appId the appId to set
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * @return the appVersion
     */
    public int getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion the appVersion to set
     */
    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * @return the paymentModuleVersion
     */
    public short getPaymentModuleVersion() {
        return paymentModuleVersion;
    }

    /**
     * @param paymentModuleVersion the paymentModuleVersion to set
     */
    public void setPaymentModuleVersion(short paymentModuleVersion) {
        this.paymentModuleVersion = paymentModuleVersion;
    }

    /**
     * @return the paymentChannelNo
     */
    public short getPaymentChannelNo() {
        return paymentChannelNo;
    }

    /**
     * @param paymentChannelNo the paymentChannelNo to set
     */
    public void setPaymentChannelNo(short paymentChannelNo) {
        this.paymentChannelNo = paymentChannelNo;
    }

    /**
     * @return the screenWidth
     */
    public short getScreenWidth() {
        return screenWidth;
    }

    /**
     * @param screenWidth the screenWidth to set
     */
    public void setScreenWidth(short screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * @return the screenHeight
     */
    public short getScreenHeight() {
        return screenHeight;
    }

    /**
     * @param screenHeight the screenHeight to set
     */
    public void setScreenHeight(short screenHeight) {
        this.screenHeight = screenHeight;
    }

    /**
     * @return the platform
     */
    public byte getPlatform() {
        return platform;
    }

    /**
     * @param platform the platform to set
     */
    public void setPlatform(byte platform) {
        this.platform = platform;
    }

    public boolean isValid() {
        return  (this.flag1 == 'M' && this.flag2 == 'R' && 
                this.flag3 == 'P' && this.flag4 == 'G');
    }
}
