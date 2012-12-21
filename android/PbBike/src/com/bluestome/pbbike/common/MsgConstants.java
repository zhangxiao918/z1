package com.bluestome.pbbike.common;

/**
 * 消息码对象
 * @author bluestome
 *
 */
public class MsgConstants {

    /**
     * 公用错误消息
     */
    public static final int MSG_ERROR = 0x400;
    /**
     * 全局成功代码
     */
    public static final int MSG_SUCC  = 0x200;
    
	/**
	 * MainActivity类中使用到的消息码
	 * @author bluestome
	 *
	 */
	public static class MainActivityMsg{
	    
		/** MainActivity Msg **/
	    public static final int DIALOG_NEARBY = 7;
	    public static final int DIALOG_STATION_ID = 8;
	    public static final int DIALOG_STATION_NAME = 9;
	    public static final int DIALOG_WHERE_LIST = 10;
		public static final int MSG_SEARCH_BY_NEARBY = 0x100;
		public static final int MSG_SEARCH_BY_STATION_ID = 0x101;
		public static final int MSG_SEARCH_BY_STATION_NAME = 0x102;
		public static final int MSG_SHOW_LIST_DIALOG =  0x103;
	    public static final int MSG_NETWORK_NO_RESPONSE = 0x104;
	    public static final int MSG_NO_DATA = 0x105;
	}

}
