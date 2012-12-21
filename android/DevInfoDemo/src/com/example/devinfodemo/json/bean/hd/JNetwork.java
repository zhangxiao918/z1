
package com.example.devinfodemo.json.bean.hd;

import com.example.devinfodemo.json.bean.Info;

/**
 * @ClassName: PhoneNetworkInfo
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-10-8 上午10:13:54
 */
public class JNetwork extends Info {

    public static final String TYPE_UNKNOW = "TYPE_UNKNOW";
    private String activiteNetwork;
    private String phoneNetworkType;

    /**
     * @return the activiteNetwork
     */
    public String getActiviteNetwork() {
        return activiteNetwork;
    }

    /**
     * @param activiteNetwork the activiteNetwork to set
     */
    public void setActiviteNetwork(String activiteNetwork) {
        this.activiteNetwork = activiteNetwork;
    }

    /**
     * @return the phoneNetworkType
     */
    public String getPhoneNetworkType() {
        return phoneNetworkType;
    }

    /**
     * @param phoneNetworkType the phoneNetworkType to set
     */
    public void setPhoneNetworkType(String phoneNetworkType) {
        this.phoneNetworkType = phoneNetworkType;
    }

}
