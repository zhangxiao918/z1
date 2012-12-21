/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/* from

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
public class TerminalCookie implements Cloneable {
	
	private long id;

	private byte localIPType;

	private String localIP;

	private int localPort;

	private byte remoteIPType;

	private String remoteIP;

	private int remotePort;

	private byte clientIPType;

	private String clientIP;

	private int clientPort;

	private String country;

	private String province;

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result
				+ ((clientIP == null) ? 0 : clientIP.hashCode());
		result = prime * result + clientIPType;
		result = prime * result + clientPort;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((localIP == null) ? 0 : localIP.hashCode());
		result = prime * result + localIPType;
		result = prime * result + localPort;
		result = prime * result
				+ ((province == null) ? 0 : province.hashCode());
		result = prime * result
				+ ((remoteIP == null) ? 0 : remoteIP.hashCode());
		result = prime * result + remoteIPType;
		result = prime * result + remotePort;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerminalCookie other = (TerminalCookie) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (clientIP == null) {
			if (other.clientIP != null)
				return false;
		} else if (!clientIP.equals(other.clientIP))
			return false;
		if (clientIPType != other.clientIPType)
			return false;
		if (clientPort != other.clientPort)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (id != other.id)
			return false;
		if (localIP == null) {
			if (other.localIP != null)
				return false;
		} else if (!localIP.equals(other.localIP))
			return false;
		if (localIPType != other.localIPType)
			return false;
		if (localPort != other.localPort)
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (remoteIP == null) {
			if (other.remoteIP != null)
				return false;
		} else if (!remoteIP.equals(other.remoteIP))
			return false;
		if (remoteIPType != other.remoteIPType)
			return false;
		if (remotePort != other.remotePort)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TerminalCookie clone() throws CloneNotSupportedException {
		return (TerminalCookie)super.clone();
	}
    
}
