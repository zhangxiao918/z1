/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.hdr;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.esb.annotation.EsbField;
import com.skymobi.android.bean.esb.core.TerminalCookie;

/* from <ESB鏈嶅姟绔唴閮ㄦā鍧椾氦浜掑崗璁�txt>

typedef struct {
	long long   cookieId;  // uint64
	uint8       localIpVer;
	char        localIp[16];
	uint32      localPort;

	uint8       remoteIpVer;
	char        remoteIp[16];
	uint32      remotePort;
	
	uint8       clientIpVer;
	char        clientIp[16];
	uint32      clientPort;

	char        remoteCountry[32];
	char        remoteProvince[32];
	char        remoteArea[32];
}
sanp_net_cookie_t;
*/

/**
 * @author Marvin.Ma
 *
 */
public class ETFTerminalCookie {
	
    private static final Logger logger 
		= LoggerFactory.getLogger(ETFTerminalCookie.class);
    
	public final static short TERMINAL_COOKIE_SIZE = 167;
		
	@EsbField(index = 1)
	private long id;

	@EsbField(index = 2)
	private byte localIPType;

	@EsbField(index = 3, fixedLength = 16, charset = "UTF-8")
	private String localIP;

	@EsbField(index = 4)
	private int localPort;

	@EsbField(index = 5)
	private byte remoteIPType;

	@EsbField(index = 6, fixedLength = 16, charset = "UTF-8")
	private String remoteIP;

	@EsbField(index = 7)
	private int remotePort;

	@EsbField(index = 8)
	private byte clientIPType;

	@EsbField(index = 9, fixedLength = 16, charset = "UTF-8")
	private String clientIP;

	@EsbField(index = 10)
	private int clientPort;

	@EsbField(index = 11, fixedLength = 32, charset = "UTF-8")
	private String country;

	@EsbField(index = 12, fixedLength = 32, charset = "UTF-8")
	private String province;

	@EsbField(index = 13, fixedLength = 32, charset = "UTF-8")
	private String area;

    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the localIPType
	 */
	public byte getLocalIPType() {
		return localIPType;
	}

	/**
	 * @return the localIP
	 */
	public String getLocalIP() {
		return localIP;
	}

	/**
	 * @return the localPort
	 */
	public int getLocalPort() {
		return localPort;
	}

	/**
	 * @return the remoteIPType
	 */
	public byte getRemoteIPType() {
		return remoteIPType;
	}

	/**
	 * @return the remoteIP
	 */
	public String getRemoteIP() {
		return remoteIP;
	}

	/**
	 * @return the remotePort
	 */
	public int getRemotePort() {
		return remotePort;
	}

	/**
	 * @return the clientIPType
	 */
	public byte getClientIPType() {
		return clientIPType;
	}

	/**
	 * @return the clientIP
	 */
	public String getClientIP() {
		return clientIP;
	}

	/**
	 * @return the clientPort
	 */
	public int getClientPort() {
		return clientPort;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param localIPType the localIPType to set
	 */
	public void setLocalIPType(byte localIPType) {
		this.localIPType = localIPType;
	}

	/**
	 * @param localIP the localIP to set
	 */
	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}

	/**
	 * @param localPort the localPort to set
	 */
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	/**
	 * @param remoteIPType the remoteIPType to set
	 */
	public void setRemoteIPType(byte remoteIPType) {
		this.remoteIPType = remoteIPType;
	}

	/**
	 * @param remoteIP the remoteIP to set
	 */
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	/**
	 * @param remotePort the remotePort to set
	 */
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	/**
	 * @param clientIPType the clientIPType to set
	 */
	public void setClientIPType(byte clientIPType) {
		this.clientIPType = clientIPType;
	}

	/**
	 * @param clientIP the clientIP to set
	 */
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	/**
	 * @param clientPort the clientPort to set
	 */
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}
    
	public TerminalCookie toTerminalCookie() {
		TerminalCookie ret = new TerminalCookie();
		
		try {
			BeanUtils.copyProperties(ret, this);
		} catch (Exception e) {
			logger.error("toTerminalCookie:", e);
		}
		
		return	ret;
	}
	
	public static ETFTerminalCookie fromTerminalCookie(TerminalCookie orig) {
		ETFTerminalCookie ret = new ETFTerminalCookie();
		
		try {
			BeanUtils.copyProperties(ret, orig);
		} catch (Exception e) {
			logger.error("fromTerminalCookie:", e);
		}
		
		return	ret;
	}
}
