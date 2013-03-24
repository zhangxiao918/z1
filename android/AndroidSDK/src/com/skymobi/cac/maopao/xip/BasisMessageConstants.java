package com.skymobi.cac.maopao.xip;

public class BasisMessageConstants {

	public static final char SMOD_ID_ACCESS_TCP = 0xF000;

	public static final char EXT_ID_BASE = 0x0100;
	
	public static final byte HEARTBEAT_DATA_TYPE_LOGIN = '4';
	public static final byte HEARTBEAT_DATA_TYPE_ACCESS = '3';
	public static final byte HEARTBEAT_DATA_TYPE_TRAFFIC = '2';
	public static final byte HEARTBEAT_DATA_TYPE_PACKAGE = '0';
	
	public static final short MSG_TEMP_CLIENT_MODEL_ID = 0x2; /* 客户端源模块号，写死，服务端不用  */
	
	public static final short MSG_FLAG_TEMP_NORMAL = 0x020; /* 临时UA */
	public static final short MSG_FLAG_ENCRYPT_TEMP_UA = 0x20; /* 临时UA 0x0020*/
	
	public static final char PACKET_TYPE_NORM = 0x0;/* 普通消息 */
	public static final char PACKET_TYPE_ENCRYPT = 0x0400;/* 加密消息 */
	public static final char PACKET_ATTACH_UA = 0x0001;/* 接入添加UA信息  0x0020*/
	public static final char PACKET_TYPE_SEQ = 0x0100;/* 添加了序列号的消息 */
	
	public static final char MSGCODE_ACCESS_TCP_UA_NEW_REQ = 0xF011;// 终端向接入发存UA信息
	public static final char MSGCODE_ACCESS_TCP_UA_NEW_RESP = 0xF004;
	
	public static final char MSGCODE_MAIN_HEARTBEAT_REQ = 0xF005; // 模块心跳消息请求
	public static final char MSGCODE_MAIN_HEARTBEAT_RSP = 0xF006; // 模块心跳消息响应
	
	public static final char MSGCODE_MAIN_RECONNECT_REQ = 0xF007; // 终端请求重连接到接入服务器
	public static final char MSGCODE_MAIN_RECONNECT_RSP = 0xF008;

	
	public static final int PACKET_COMM_HEAD_LEN = 8;
	
	public static final int EC_SUCCESS = 0;// 成功的情况下会返回接入层的index 和authentication code
	public static final int EC_FAILURE = 1;// 终端的消息格式有问题
	public static final int EC_REDIRECT = 2;// 终端被重定向到另外一个接入服务器
	
	public static final short MSG_FLAG_ENCRYPT_SEQ_UA = BasisMessageConstants.PACKET_TYPE_ENCRYPT|
			BasisMessageConstants.PACKET_TYPE_SEQ|
			BasisMessageConstants.PACKET_ATTACH_UA;
	public static final short MSG_FLAG_ENCRYPT_SEQ = BasisMessageConstants.PACKET_TYPE_ENCRYPT|
			BasisMessageConstants.PACKET_TYPE_SEQ;
	
    public static final int REFRESH_ROOMONLINE_INTERVAL = 60;//单位秒
    public static final String HEART_BEAT_KEY = "heart_beat";
    
    
    /**
     * 登陆部分目标模块号。
     * 消息号定义来源：王慎为 2012-2-18
     */
    public static final int SMOD_ID_CAC_ACCOUNT = 0xDA00;
    
    public static final int MSGCODE_CAC_USER_LOGIN_REQ = 0xDA01;		// 登陆请求
    public static final int MSGCODE_CAC_USER_LOGIN_RESP = 0xDA02;		// 登陆响应
    public static final int MSGCODE_CAC_USER_REGISTER_REQ = 0xDA03;		// 注册请求
    public static final int MSGCODE_CAC_USER_REGISTER_RESP = 0xDA04;	// 注册响应
    
    /**
     * Basis模块信息。
     * 消息号定义来源：方杨统 2012-2-21
     */
    public static final int SMOD_ID_CAC_BASIS = 0xD600;
    
    public static final int MSGCODE_CAC_GET_GAME_INFO_REQ = 0xD601;		// 获取房间信息
    public static final int MSGCODE_CAC_GET_GAME_INFO_RESP = 0xD602;	// 获取房间信息响应
    public static final int MSGCODE_CAC_ENTER_ROOM_STYLE_REQ = 0xD605;	// 进入房间请求
    
    
    /**
     *  斗地主平台部分
     */
    
    public static final int MSGCODE_CAC_LAUNCH_GAME_NOTIFY = 0xD712;	// 进入房间响应
    
    public static final int MSGCODE_CAC_ADD_PLAYER_NOTIFY = 0xD733;		// 有玩家进入游戏
    
    
    /**
     * 斗地主游戏。
     * 消息发往的模块号由#MSGCODE_CAC_LAUNCH_GAME_NOTIFY 带入。
     */
    
    public static final int MSGCODE_CAC_QUIT_GAME_REQ = 0xD722;			// 退出桌请求
    public static final int MSGCODE_CAC_QUIT_GAME_RESP = 0xD723;		// 退出桌响应
    
   
    
    
    
    
    /**
     * 斗地主开始玩牌。
     */
    
    public static final int MSGCODE_CAC_RAISE_HAND_NOTIFY = 0xD734;		// 其它玩家进入游戏并举手
    public static final int MSGCODE_CAC_STANDUP_NOTIFY = 0xD710;		// 其它玩家退出游戏
    
    public static final int MSGCODE_CAC_DDZ_DISPATCH_CARD_NOTIFY = 0xD101;	// 发牌
    
    public static final int MSGCODE_CAC_DDZ_START_BID_NOTIFY = 0xD104;	// 开始叫分消息
    public static final int MSGCODE_CAC_DDZ_BID_REQ = 0xD105;			// 请求叫分
    public static final int MSGCODE_CAC_DDZ_BID_RESP = 0xD106;			// 叫分响应
    public static final int MSGCODE_CAC_DDZ_BID_NOTIFY = 0xD107;		// 叫分信息变更通知
    public static final int MSGCODE_CAC_DDZ_BID_FINISH_NOTIFY = 0xD108;	// 叫分结束通知
    
    public static final int MSGCODE_CAC_DDZ_PLAY_CARD_REQ = 0xD112;		// 玩家出牌请求
    public static final int MSGCODE_CAC_DDZ_PLAY_CARD_RESP = 0xD113;	// 玩家出牌响应
    public static final int MSGCODE_CAC_DDZ_PLAY_CARD_NOTIFY = 0xD114;	// 其它玩家出牌通知
}
