/**
 * 
 */
package com.skymobi.android.bean.xip.core;

import com.skymobi.android.bean.xip.annotation.ApplistField;
import com.skymobi.android.bean.xip.annotation.SaipField;
import com.skymobi.android.bean.xip.annotation.SsipField;

/**
 * @author isdom
 *
 */
public class SsipResponse extends AbstractSsipSignal implements XipResponse {
    
    @SaipField(index = 0, description= "")
    @SsipField(index = 0, description= "")
    @ApplistField(index = 0, description= "")
    private int     errorCode;
    
    @SaipField(index = 1, description= "")
    @ApplistField(index = 1, description= "")
    private String  errorMessage;
    
//    public static <T extends SsipResponse> T createRespForError(Class<T> clazz, XipProtocol protocol, int errorCode, String errorMessage) {
//        T resp;
//        try {
//            resp = clazz.newInstance();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//            return  null;
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            return  null;
//        }
//        
//        ((SsipResponse)resp).setErrorCode(errorCode);
//        ((SsipResponse)resp).setErrorMessage(errorMessage);
//        ((SsipResponse)resp).setXipProtocol(protocol);
//        
//        return  resp;
//    }
//    
    
    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }
    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
