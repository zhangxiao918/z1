package com.skymobi.android.transport.protocol.esb.hdr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/* from <ESB鏈嶅姟绔唴閮ㄦā鍧椾氦浜掑崗璁�txt>
 * 缃戠粶绔紶杈撶殑UA缁撴瀯瀹氫箟  
 * 娉ㄦ剰锛氱粓绔拰鎺ュ叆涔嬮棿鐨刄A瀹氫箟鍜岀綉缁滅殑鐣ユ湁宸埆銆�
 
typdef struct {
  unsigned char factoryCode[8];
  unsigned char terminalCode[8];

  uint32   VMV;
  uint32   HSV;
  uint16   screenSize_w, screenSize_h;
  uint8    terminalFont_w, terminalFont_h;
  uint16   terminalMemory;
  uint8   terminalTouch;
  unsigned char   IMEI[16];
  unsigned char   IMSI[16];
  unsigned char   phoneNum[16];
  unsigned char   phoneCountry[16];
  unsigned char   phoneProvince[32];
  unsigned char   phoneArea[32];
  
    //鏂板鍔�
  uint32 entrance;
  uint32 appId;
  uint8 platform;
  uint8 screenType;
  uint8 reserved1;
  uint8 reserved2;
  uint16 reserved3;
  uint32 reserved4;
  byte[16] ipAddress
} sanp_net_ua_t;
*/

/**
 * @author bluces.wang@sky-mobi.com
 */
public class TerminalUserAgentNew implements Cloneable {
	public static byte[] key  = { 0x1a, (byte)0xeb, 0x68, (byte)0x90, (byte)0xae, 0x12, 0x44, 0x25 };
	static {
		try {
			DesEncrypt.generateKey(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String factoryCode="";

	private String terminalCode="";

	private int vmv;

	private int hsv;

	private short screenSizeWidth;

	private short screenSizeHeight;

	private byte terminalFontWidth;

	private byte terminalFontHeight;

	private short terminalMemory;

	private byte terminalTouch;

	private String imei="";

	private String imsi="";

	private String phone="";

	private String country="";

	private String province="";

	private String area="";

	  //鏂板鍔�
	private long entrance;
	
	private long appId;
	
	private int platform;
	
	private int screenType;
	
	private int reserved1;
	
	private int reserved2;
	
	private int reserved3;
	
	private long reserved4;
	
	private String ipAddress="";
	  
    public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public int getVmv() {
		return vmv;
	}

	public void setVmv(int vmv) {
		this.vmv = vmv;
	}

	public int getHsv() {
		return hsv;
	}

	public void setHsv(int hsv) {
		this.hsv = hsv;
	}

	public short getScreenSizeWidth() {
		return screenSizeWidth;
	}

	public void setScreenSizeWidth(short screenSizeWidth) {
		this.screenSizeWidth = screenSizeWidth;
	}

	public short getScreenSizeHeight() {
		return screenSizeHeight;
	}

	public void setScreenSizeHeight(short screenSizeHeight) {
		this.screenSizeHeight = screenSizeHeight;
	}

	public byte getTerminalFontWidth() {
		return terminalFontWidth;
	}

	public void setTerminalFontWidth(byte terminalFontWidth) {
		this.terminalFontWidth = terminalFontWidth;
	}

	public byte getTerminalFontHeight() {
		return terminalFontHeight;
	}

	public void setTerminalFontHeight(byte terminalFontHeight) {
		this.terminalFontHeight = terminalFontHeight;
	}

	public short getTerminalMemory() {
		return terminalMemory;
	}

	public void setTerminalMemory(short terminalMemory) {
		this.terminalMemory = terminalMemory;
	}

	public byte getTerminalTouch() {
		return terminalTouch;
	}

	public void setTerminalTouch(byte terminalTouch) {
		this.terminalTouch = terminalTouch;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public long getEntrance() {
		return entrance;
	}

	public void setEntrance(long entrance) {
		this.entrance = entrance;
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getScreenType() {
		return screenType;
	}

	public void setScreenType(int screenType) {
		this.screenType = screenType;
	}

	public int getReserved1() {
		return reserved1;
	}

	public void setReserved1(int reserved1) {
		this.reserved1 = reserved1;
	}

	public int getReserved2() {
		return reserved2;
	}

	public void setReserved2(int reserved2) {
		this.reserved2 = reserved2;
	}

	public int getReserved3() {
		return reserved3;
	}

	public void setReserved3(int reserved3) {
		this.reserved3 = reserved3;
	}

	public long getReserved4() {
		return reserved4;
	}

	public void setReserved4(long reserved4) {
		this.reserved4 = reserved4;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

    //妯℃嫙access鍔犲瘑 鍦ㄦ祴璇曟椂鍊欐瘮杈冩湁鐢�
    public byte[] getTerminalUserAgentNew(){
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
    	DataOutputStream dos = new DataOutputStream(baos);
    	try{
    		dos.write(toFixupLengthByte(factoryCode,8 ));
			dos.write(toFixupLengthByte(terminalCode, 8));
			dos.writeInt(vmv);
			dos.writeInt(hsv);
			dos.writeShort(screenSizeWidth);
			dos.writeShort(screenSizeHeight);
			dos.write(terminalFontWidth);
			dos.write(terminalFontHeight);
			dos.writeShort(terminalMemory);
			dos.write(terminalTouch);
			dos.write( toFixupLengthByte(imei, 16));
			dos.write( toFixupLengthByte(imsi, 16));
			dos.write( toFixupLengthByte(phone, 16 ));
			dos.write( toFixupLengthByte(country, 16));
			dos.write( toFixupLengthByte(province, 32));
			dos.write( toFixupLengthByte(area, 32));
	
			  //鏂板鍔�
			dos.writeInt((int)entrance);
			dos.writeInt((int)appId);
			dos.write((byte)platform);
			dos.write((byte)screenType);
			dos.write((byte)reserved1);
			dos.write((byte)reserved2);
			dos.writeShort((short)reserved3);
			dos.writeInt((int)reserved4);
			String[] ips = ipAddress.split("\\.");
			if(ips.length !=4 ){
				//ipaddress 鎬诲叡鏄�6瀛楄妭闀垮害
				dos.write(new byte[16]);
			}else{
				for(int i=0;i<ips.length;i++){
					try{
						dos.write((byte)Short.parseShort(ips[i]));
					}catch(NumberFormatException  e){
						dos.write(0);
					}
				}
				dos.write(new byte[12]);
			}
			//鍙互缁�鏁撮櫎 195 + 5
			dos.write(new byte[5]);
			
			return DesEncrypt.encrypt(baos.toByteArray());
    	} catch(UnsupportedEncodingException e){
    		e.printStackTrace();
    	} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				dos.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new byte[200];
    }
    
    private byte[] toFixupLengthByte(String src, int length) throws UnsupportedEncodingException {
		byte[] dist = new byte[length];
		byte[] bytes = src.getBytes("UTF-8");
		for (int i = 0; i < length & i < bytes.length; i++) {
			dist[i] = bytes[i];
		}
		return dist;
	}

	public void setTerminalUserAgentNew(byte[] data){
    	try {
			byte[] decryptedData = DesEncrypt.decrypt(data);
			initValue(decryptedData);
		} catch (Exception e) {
			throw new RuntimeException("decrypt terInfo error", e);
		}
    }
    
	private String getFixupLengthString(DataInputStream in,int size) throws IOException{
		byte[] t1 = new byte[size];
		in.read(t1,0,size);
		return new String(t1,"UTF-8").trim();
	}
	
	private void initValue(byte[] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data); 
		DataInputStream in = new DataInputStream(bais);
		try {
			factoryCode = getFixupLengthString(in,8);
			terminalCode = getFixupLengthString(in,8);
			vmv = in.readInt();
			hsv = in.readInt();
			screenSizeWidth = in.readShort();
			screenSizeHeight = in.readShort();
			terminalFontWidth = in.readByte();
			terminalFontHeight = in.readByte();
			terminalMemory = in.readShort();
			terminalTouch = in.readByte();
			imei = getFixupLengthString(in,16);
			imsi = getFixupLengthString(in,16);
			phone = getFixupLengthString(in,16);
			country = getFixupLengthString(in,16);
			province = getFixupLengthString(in,32);
			area = getFixupLengthString(in,32);
			entrance = in.readInt();
			appId = in.readInt();
			platform = in.readUnsignedByte();
			screenType = in.readUnsignedByte();
			reserved1 = in.readUnsignedByte();
			reserved2 = in.readUnsignedByte();
			reserved3 = in.readUnsignedShort();
			reserved4 = in.readInt();
			byte[] b = new byte[16];
			in.read(b);
			ipAddress = (b[0] & 0xff) + "." + (b[1] & 0xff) + "." + (b[2] & 0xff) + "." + (b[3] & 0xff);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
				bais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((factoryCode == null) ? 0 : factoryCode.hashCode());
		result = prime * result + hsv;
		result = prime * result + ((imei == null) ? 0 : imei.hashCode());
		result = prime * result + ((imsi == null) ? 0 : imsi.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result
				+ ((province == null) ? 0 : province.hashCode());
		result = prime * result + screenSizeHeight;
		result = prime * result + screenSizeWidth;
		result = prime * result
				+ ((terminalCode == null) ? 0 : terminalCode.hashCode());
		result = prime * result + terminalFontHeight;
		result = prime * result + terminalFontWidth;
		result = prime * result + terminalMemory;
		result = prime * result + terminalTouch;
		result = prime * result + vmv;
		
		result = prime * result + (int)entrance;
		result = prime * result + (int)appId;
		result = prime * result + platform;
		result = prime * result + screenType;
		result = prime * result + reserved1;
		result = prime * result + reserved2;
		result = prime * result + reserved3;
		result = prime * result + (int)reserved4;
		result = prime * result + ((ipAddress==null)?0:ipAddress.hashCode());
		return result;
	}

 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerminalUserAgentNew other = (TerminalUserAgentNew) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (factoryCode == null) {
			if (other.factoryCode != null)
				return false;
		} else if (!factoryCode.equals(other.factoryCode))
			return false;
		if (hsv != other.hsv)
			return false;
		if (imei == null) {
			if (other.imei != null)
				return false;
		} else if (!imei.equals(other.imei))
			return false;
		if (imsi == null) {
			if (other.imsi != null)
				return false;
		} else if (!imsi.equals(other.imsi))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (screenSizeHeight != other.screenSizeHeight)
			return false;
		if (screenSizeWidth != other.screenSizeWidth)
			return false;
		if (terminalCode == null) {
			if (other.terminalCode != null)
				return false;
		} else if (!terminalCode.equals(other.terminalCode))
			return false;
		if (terminalFontHeight != other.terminalFontHeight)
			return false;
		if (terminalFontWidth != other.terminalFontWidth)
			return false;
		if (terminalMemory != other.terminalMemory)
			return false;
		if (terminalTouch != other.terminalTouch)
			return false;
		if (vmv != other.vmv)
			return false;
		
		if (entrance != other.entrance)
			return false;
		if (appId != other.appId)
			return false;
		if (platform != other.platform)
			return false;
		if (screenType != other.screenType)
			return false;
		if (reserved1 != other.reserved1)
			return false;
		if (reserved2 != other.reserved2)
			return false;
		if (reserved3 != other.reserved3)
			return false;
		if (reserved4 != other.reserved4)
			return false;
 
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		return true;
	}

 
	public TerminalUserAgentNew clone() throws CloneNotSupportedException {
		return (TerminalUserAgentNew)super.clone();
	}
    
	public static void main(String[] args) throws Exception {
		TerminalUserAgentNew ter = new TerminalUserAgentNew();
		ter.setImei("1111111111");
		ter.setIpAddress("192.168.171.255");
		ter.setFactoryCode("11111111111");
		ter.setTerminalCode("8888888");
		ter.setAppId(222222l);
		try {
			System.out.println("-----------");
			ETFTerminalUserAgentNew ua = ETFTerminalUserAgentNew.fromTerminalUserAgentNew(ter);
			System.out.println(ua.toTerminalUserAgentNew());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
