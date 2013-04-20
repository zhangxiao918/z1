
package com.skymobi.cac.maopao.xip;

public class BasisMessageConstants {

    public static final char SMOD_ID_ACCESS_TCP = 0xF000;

    public static final char EXT_ID_BASE = 0x0100;

    public static final byte HEARTBEAT_DATA_TYPE_LOGIN = '4';
    public static final byte HEARTBEAT_DATA_TYPE_ACCESS = '3';
    public static final byte HEARTBEAT_DATA_TYPE_TRAFFIC = '2';
    public static final byte HEARTBEAT_DATA_TYPE_PACKAGE = '0';

    public static final short MSG_TEMP_CLIENT_MODEL_ID = 0x2; /*
                                                               * 客户端源模块号，写死，服务端不用
                                                               */

    public static final short MSG_FLAG_TEMP_NORMAL = 0x020; /* 临时UA */
    public static final short MSG_FLAG_ENCRYPT_TEMP_UA = 0x20; /* 临时UA 0x0020 */

    public static final char PACKET_TYPE_NORM = 0x0;/* 普通消息 */
    public static final char PACKET_TYPE_ENCRYPT = 0x0400;/* 加密消息 */
    public static final char PACKET_ATTACH_UA = 0x0001;/* 接入添加UA信息 0x0020 */
    public static final char PACKET_TYPE_SEQ = 0x0100;/* 添加了序列号的消息 */

    public static final char MSGCODE_ACCESS_TCP_UA_NEW_REQ = 0xF011;// 终端向接入发存UA信息
    public static final char MSGCODE_ACCESS_TCP_UA_NEW_RESP = 0xF004;

    public static final char MSGCODE_MAIN_HEARTBEAT_REQ = 0xF005; // 模块心跳消息请求
    public static final char MSGCODE_MAIN_HEARTBEAT_RSP = 0xF006; // 模块心跳消息响应

    public static final char MSGCODE_MAIN_RECONNECT_REQ = 0xF007; // 终端请求重连接到接入服务器
    public static final char MSGCODE_MAIN_RECONNECT_RSP = 0xF008;

    public static final int PACKET_COMM_HEAD_LEN = 8;

    public static final int EC_SUCCESS = 0;// 成功的情况下会返回接入层的index 和authentication
                                           // code
    public static final int EC_FAILURE = 1;// 终端的消息格式有问题
    public static final int EC_REDIRECT = 2;// 终端被重定向到另外一个接入服务器

    public static final short MSG_FLAG_ENCRYPT_SEQ_UA = BasisMessageConstants.PACKET_TYPE_ENCRYPT |
            BasisMessageConstants.PACKET_TYPE_SEQ |
            BasisMessageConstants.PACKET_ATTACH_UA;
    public static final short MSG_FLAG_ENCRYPT_SEQ = BasisMessageConstants.PACKET_TYPE_ENCRYPT |
            BasisMessageConstants.PACKET_TYPE_SEQ;

    public static final int REFRESH_ROOMONLINE_INTERVAL = 60;// 单位秒
    public static final String HEART_BEAT_KEY = "heart_beat";

    /**
     * 登陆部分目标模块号。 消息号定义来源：王慎为 2012-2-18
     */
    public static final int SMOD_ID_CAC_ACCOUNT = 0xDA00;

    public static final int MSGCODE_CAC_USER_LOGIN_REQ = 0xDA01; // 登陆请求
    public static final int MSGCODE_CAC_USER_LOGIN_RESP = 0xDA02; // 登陆响应
    public static final int MSGCODE_CAC_REGISTER_REQ = 0xDA03; // 注册请求
    public static final int MSGCODE_CAC_REGISTER_RESP = 0xDA04; // 注册响应
    public static final int MSGCODE_CAC_QUICKREGISTER_REQ = 0xDA05; // 快速注册
    public static final int MSGCODE_CAC_QUICKREGISTER_RESP = 0xDA06;// 快速注册响应
    public static final int MSGCODE_CAC_THIRD_LOGIN_REQ = 0xDA0D;// 第三方注册
    public static final int MSGCODE_CAC_THIRD_LOGIN_RESP = 0xDA0E;// 第三方注册响应

    /**
     * Basis模块信息。 消息号定义来源：方杨统 2012-2-21
     */
    public static final int SMOD_ID_CAC_BASIS = 0xD600;

    public static final int MSGCODE_CAC_GET_GAME_INFO_REQ = 0xD601; // 获取房间信息
    public static final int MSGCODE_CAC_GET_GAME_INFO_RESP = 0xD602; // 获取房间信息响应
    public static final int MSGCODE_CAC_ENTER_ROOM_STYLE_REQ = 0xD605; // 进入房间请求
    public static final int MSGCODE_CAC_ENTER_ROOM_STYLE_RESP = 0xD606;

    public static final int MSGCODE_CAC_USER_REPORT_REQ = 0xD615; // 用户反馈
    public static final int MSGCODE_CAC_USER_REPORT_RESP = 0xD616; // 用户反馈响应

    /**
     * 斗地主平台部分
     */

    public static final int MSGCODE_CAC_LAUNCH_GAME_NOTIFY = 0xD712; // 进入房间响应

    public static final int MSGCODE_CAC_ADD_PLAYER_NOTIFY = 0xD733; // 有玩家进入游戏

    /**
     * 斗地主游戏。 消息发往的模块号由#MSGCODE_CAC_LAUNCH_GAME_NOTIFY 带入。
     */

    public static final int MSGCODE_CAC_QUIT_GAME_REQ = 0xD722; // 退出桌请求
    public static final int MSGCODE_CAC_QUIT_GAME_RESP = 0xD723; // 退出桌响应

    /**
     * 斗地主开始玩牌。
     */

    public static final int MSGCODE_CAC_RAISE_HAND_REQ = 0xD732; // 请求准备
    public static final int MSGCODE_CAC_RAISE_HAND_RESP = 0xD737; // 请求准备响应

    public static final int MSGCODE_CAC_RAISE_HAND_NOTIFY = 0xD734; // 其它玩家进入游戏并举手
    public static final int MSGCODE_CAC_STANDUP_NOTIFY = 0xD710; // 其它玩家退出游戏

    public static final int MSGCODE_CAC_DDZ_DISPATCH_CARD_NOTIFY = 0xD101; // 发牌

    public static final int MSGCODE_CAC_DDZ_START_BID_NOTIFY = 0xD104; // 开始叫分消息
    public static final int MSGCODE_CAC_DDZ_BID_REQ = 0xD105; // 请求叫分
    public static final int MSGCODE_CAC_DDZ_BID_RESP = 0xD106; // 叫分响应
    public static final int MSGCODE_CAC_DDZ_BID_NOTIFY = 0xD107; // 叫分信息变更通知
    public static final int MSGCODE_CAC_DDZ_BID_FINISH_NOTIFY = 0xD108; // 叫分结束通知

    public static final int MSGCODE_CAC_DDZ_PLAY_CARD_REQ = 0xD112; // 玩家出牌请求
    public static final int MSGCODE_CAC_DDZ_PLAY_CARD_RESP = 0xD113; // 玩家出牌响应
    public static final int MSGCODE_CAC_DDZ_PLAY_CARD_NOTIFY = 0xD114; // 其它玩家出牌通知

    public static final int MSGCODE_CAC_DDZ_VICTORY_NOTIFY = 0xD135; // 结算通知

    public static final int MSGCODE_CAC_DDZ_SHOWCARD_NOTIFY = 0xD131;//

    public static final int MSGCODE_CAC_TRUSTEESHIP_REQ = 0xD735;
    public static final int MSGCODE_CAC_TRUSTEESHIP_NOTIFY = 0xD736;

    /**
     * 游戏中技能模块
     */

    public static final int MSGCODE_CAC_DDZ_SHOW_BACK_CARD_REQ = 0xD102;
    public static final int MSGCODE_CAC_DDZ_SHOW_BACK_CARD_RESP = 0xD103;
    public static final int MSGCODE_CAC_DDZ_DOUBLE_REQ = 0xD125;
    public static final int MSGCODE_CAC_DDZ_DOUBLE_RESP = 0xD126;
    public static final int MSGCODE_CAC_DDZ_DOUBLE_NOTIFY = 0xD127;
    public static final int MSGCODE_CAC_DDZ_HALF_REQ = 0xD128;
    public static final int MSGCODE_CAC_DDZ_HALF_RESP = 0xD129;
    public static final int MSGCODE_CAC_DDZ_HALF_NOTIFY = 0xD130;

    public static final int MSGCODE_CAC_DDZ_SWITCH_PLAYER_CARD_REQ = 0xD115;
    public static final int MSGCODE_CAC_DDZ_SWITCH_PLAYER_CARD_NOTIFY = 0xD116;
    public static final int MSGCODE_CAC_DDZ_ANTI_SWITCH_PLAYER_CARD_NOTIFY = 0xD117;
    public static final int MSGCODE_CAC_DDZ_ANTI_SWITCH_PLAYER_CARD_REQ = 0xD118;
    public static final int MSGCODE_CAC_DDZ_ANTI_SWITCH_PLAYER_CARD_RESP = 0xD119;
    public static final int MSGCODE_CAC_DDZ_SHOW_PLAYER_CARD_REQ = 0xD120;
    public static final int MSGCODE_CAC_DDZ_SHOW_PLAYER_CARD_NOTIFY = 0xD121;
    public static final int MSGCODE_CAC_DDZ_ANTI_SHOW_PLAYER_CARD_NOTIFY = 0xD122;
    public static final int MSGCODE_CAC_DDZ_ANTI_SHOW_PLAYER_CARD_REQ = 0xD123;
    public static final int MSGCODE_CAC_DDZ_ANTI_SHOW_PLAYER_CARD_RESP = 0xD124;

    /**
     * 支付模块
     */
    public static final int SMOD_ID_CAC_PAY = 0xDD00;
    public static final int MSGCODE_CAC_CARD_PAY_INFO_LIST_REQ = 0xC2B5; // 获取支付列表请求
    public static final int MSGCODE_CAC_CARD_PAY_INFO_LIST_RESP = 0xC2B6; // 获取支付列表响应
    public static final int MSGCODE_CAC_SMS_PAY_ACCESS_REQ = 0xC21D; // 短信支付请求
    public static final int MSGCODE_CAC_SMS_PAY_ACCESS_RESP = 0xC21E; // 短信支付响应
    public static final int MSGCODE_CAC_CARD_PAY_REQ = 0xC211; // 卡密支付请求
    public static final int MSGCODE_CAC_CARD_PAY_RESP = 0xC212; // 卡密支付响应
    public static final int MSGCODE_CAC_BILL_PAY_SWITCH_REQ = 0xD214; // 支付开关请求
    public static final int MSGCODE_CAC_BILL_PAY_SWITCH_RESP = 0xD215; // 支付开关响应

    /**
     * 任务
     */
    public static final int SMOD_ID_CAC_TASK = 0xD100;
    public static final int MSGCODE_CAC_GET_PLAYER_CURRENT_CHALLENGE_TASK_REQ = 0xD901; // 获取挑战任务列表请求
    public static final int MSGCODE_CAC_GET_PLAYER_CURRENT_CHALLENGE_TASK_RESP = 0xD902; // 获取挑战任务列表响应
    public static final int MSGCODE_CAC_ACQUIRE_CHALLENGE_TASK_REQ = 0xD903; // 接受任务请求
    public static final int MSGCODE_CAC_ACQUIRE_CHALLENGE_TASK_RESP = 0xd904; // 接收任务响应
    public static final int MSGCODE_CAC_GET_PLAYER_DAILY_TASK_REQ = 0xD905; // 日常任务请求
    public static final int MSGCODE_CAC_GET_PLAYER_DAILY_TASK_RESP = 0xD906;// 日常任务响应
    public static final int MSGCODE_CAC_GET_PLAYER_TASK_REQ = 0xD907; // 已接任务请求
    public static final int MSGCODE_CAC_GET_PLAYER_TASK_RESP = 0xD908; // 已接任务响应

    /**
     * 女宠
     */
    public static final int SMOD_ID_CAC_PET = 0xDE00; // 女宠模块ID

    public static final int MSGCODE_CAC_DDZ_PET_TYPE_REQ = 0xDE01; // 基础模块请求
    public static final int MSGCODE_CAC_DDZ_PET_TYPE_RESP = 0xDE02;
    public static final int MSGCODE_CAC_DDZ_PET_ADD_REQ = 0xDE03; // 女宠任务获得新增请求
    public static final int MSGCODE_CAC_DDZ_PET_ADD_RESP = 0xDE04;
    public static final int MSGCODE_CAC_DDZ_PET_SHOW_REQ = 0xDE05; // 女宠查看请求
    public static final int MSGCODE_CAC_DDZ_PET_SHOW_RESP = 0xDE06;
    public static final int MSGCODE_CAC_DDZ_PET_DEL_REQ = 0xDE07; // 女宠丢弃请求
    public static final int MSGCODE_CAC_DDZ_PET_DEL_RESP = 0xDE08;
    public static final int MSGCODE_CAC_DDZ_PET_CLOTHES_CHANGE_REQ = 0xDE09;// 女宠换装请求
    public static final int MSGCODE_CAC_DDZ_PET_CLOTHES_CHANGE_RESP = 0xDE0A;
    public static final int MSGCODE_CAC_DDZ_PET_DROP_PRICE_CHANGE_REQ = 0xDE0B;// 女宠掉落物品定价请求
    public static final int MSGCODE_CAC_DDZ_PET_DROP_PRICE_CHANGE_RESP = 0xDE0C;
    public static final int MSGCODE_CAC_DDZ_PET_BATTLE_BOUDOIR_CHANGE_REQ = 0xDE0E;// 女宠出战闺房状态更改请求
    public static final int MSGCODE_CAC_DDZ_PET_BATTLE_BOUDOIR_CHANGE_RESP = 0xDE0F;
    public static final int MSGCODE_DATA_DDZ_PLAYER_PET_SEARCH_REQ = 0xDE10;// 数据处理
    public static final int MSGCODE_DATA_DDZ_PLAYER_PET_SEARCH_RESP = 0xDE11;
    public static final int MSGCODE_DATA_DDZ_PLAYER_PET_RANKING_REQ = 0xDE12;// 数据处理
    public static final int MSGCODE_DATA_DDZ_PLAYER_PET_RANKING_RESP = 0xDE13; // 宠物排行榜请求
    public static final int MSGCODE_DATA_DDZ_PLAYER_PET_INCOME_OUTLAY_REQ = 0xDE14; // 数据处理
    public static final int MSGCODE_DATA_DDZ_PLAYER_PET_INCOME_OUTLAY_RESP = 0xDE15;// 宠物收益支出请求
    public static final int MSGCODE_CAC_DDZ_PET_OPER_REQ = 0xDE16; // 女宠操作请求
    public static final int MSGCODE_CAC_DDZ_PET_OPER_RESP = 0xDE17;
    public static final int MSGCODE_CAC_DDZ_PET_SKILL_STUDY_REQ = 0xDE18;// 女宠技能学习请求
    public static final int MSGCODE_CAC_DDZ_PET_SKILL_STUDY_RESP = 0xDE19;
    public static final int MSGCODE_CAC_DDZ_PET_SKILL_REQ = 0xDE1A;// 女宠技能查看请求
    public static final int MSGCODE_CAC_DDZ_PET_SKILL_RESP = 0xDE1B;

    // 修改密码
    public static final int MSGCODE_CAC_MODIFY_PASS_WORD_REQ = 0xDA0B;
    public static final int MSGCODE_CAC_MODIFY_PASS_WORD_RESP = 0xDA0C;

    // 获取用户信息
    public static final int MSGCODE_CAC_GET_PLAYER_BASE_INFO_REQ = 0xDA0F;
    public static final int MSGCODE_CAC_GET_PLAYER_BASE_INFO_RESP = 0xDA10;

    // 绑定手机
    public static final int MSGCODE_CAC_BIND_PHONE_REQ = 0xDA11;
    public static final int MSGCODE_CAC_BIND_PHONE_RESP = 0xDA12;

    // 忘记密码
    public static final int MSGCODE_CAC_FIND_PASSWORD_REQ = 0xDA13;
    public static final int MSGCODE_CAC_FIND_PASSWORD_RESP = 0xDA14;

    // 切换帐号
    public static final int MSGCODE_CAC_SWITCH_ACCOUNT_REQ = 0xDA15;
    public static final int MSGCODE_CAC_SWITCH_ACCOUNT_RESP = 0xDA16;

    /**
     * 商城模块
     */
    public static final int CMOD_ID_CAC_MALL = 0xDF00; // 商城模块号
    public static final int MSGCODE_CAC_MALL_LIST_REQ = 0xDF03; // 获取商城列表请求
    public static final int MSGCODE_CAC_MALL_LIST_RESP = 0xDF04; // 获取商城列表响应

    public static final int MSGCODE_CAC_MALL_PAY_REQ = 0xDF05; // 商城购买请求
    public static final int MSGCODE_CAC_MALL_PAY_RESP = 0xDF06; // 商城购买响应

    /**
     * 背包
     */
    public static final int CMOD_ID_CAC_PROP = 0xDC00;
    public static final int MSGCODE_CAC_PAY_PROP_REQ = 0xDC03;
    public static final int MSGCODE_CAC_PAY_PROP_RESP = 0xDC04;

    public static final int MSGCODE_CAC_PAY_COMPOUND_REQ = 0xDC05;
    public static final int MSGCODE_CAC_PAY_COMPOUND_RESP = 0xDC06;

    public static final int MSGCODE_BP_PROP_LIST_REQ = 0xDC07;
    public static final int MSGCODE_BP_PROP_LIST_RESQ = 0xDC08;

    public static final int MSGCODE_BP_COMPOUND_LIST_REQ = 0xDC09;
    public static final int MSGCODE_BP_COMPOUND_LIST_RESQ = 0xDC0A;

    public static final int MSGCODE_SORT_BP_PROP_LIST_REQ = 0xDC0B;
    public static final int MSGCODE_SORT_BP_PROP_LIST_RESQ = 0xDC0C;

    public static final int MSGCODE_SORT_BP_COMPOUND_LIST_REQ = 0xDC0D;
    public static final int MSGCODE_SORT_BP_COMPOUND_LIST_RESQ = 0xDC0E;

    public static final int MSGCODE_DIY_BP_PROP_LIST_REQ = 0xDC0F;
    public static final int MSGCODE_DIY_BP_PROP_LIST_RESQ = 0xDC10;

    public static final int MSGCODE_DIY_BP_COMPOUND_LIST_REQ = 0xDC11;
    public static final int MSGCODE_DIY_BP_COMPOUND_LIST_RESQ = 0xDC12;

    public static final int MSGCODE_ADD_PROP_REQ = 0xDC13;
    public static final int MSGCODE_ADD_PROP_RESQ = 0xDC14;

    public static final int MSGCODE_ADD_COMPOUND_REQ = 0xDC15;
    public static final int MSGCODE_ADD_COMPOUND_RESQ = 0xDC16;

    public static final int MSGCODE_CONSUME_PROP_REQ = 0xDC17;
    public static final int MSGCODE_CONSUME_PROP_RESQ = 0xDC18;

    public static final int MSGCODE_CONSUME_COMPOUND_REQ = 0xDC19;
    public static final int MSGCODE_CONSUME_COMPOUND_RESQ = 0xDC1A;

    public static final int MSGCODE_CONPOSE_COMPOUND_REQ = 0xDC1B;
    public static final int MSGCODE_CONPOSE_COMPOUND_RESQ = 0xDC1C;

    /**
     * 站内信
     */
    public static final int SMOD_ID_CAC_IMS = 0xD300; // 站内信模块号

    public static final int MSGCODE_CAC_SITEMAIL_INBOX_REQ = 0xD301;
    public static final int MSGCODE_CAC_SITEMAIL_INBOX_RESP = 0xD302;
    public static final int MSGCODE_CAC_SITEMAIL_NEW_COUNTS_REQ = 0xD303;
    public static final int MSGCODE_CAC_SITEMAIL_NEW_COUNTS_RESP = 0xD304;
    public static final int MSGCODE_CAC_SITEMAIL_CONTENT_REQ = 0xD305;
    public static final int MSGCODE_CAC_SITEMAIL_CONTENT_RESP = 0xD306;
    public static final int MSGCODE_CAC_SITEMAIL_SEND_REQ = 0xD307;
    public static final int MSGCODE_CAC_SITEMAIL_SEND_RESP = 0xD308;
    public static final int MSGCODE_CAC_PUBLICMAIL_SEND_REQ = 0xD309;
    public static final int MSGCODE_CAC_PUBLICMAIL_SEND_RESP = 0xD310;

    /**
     * 聊天消息
     */
    public static final int SMOD_ID_CAC_CHAT = 0xD700; // 聊天模块号
    public static final int MSGCODE_CAC_CHAT_REQ = 0xD757;
    public static final int MSGCODE_CAC_CHAT_NOTIFY = 0xD758;

    /**
     * 获取用户游戏信息
     */
    public static final int MSGCODE_CAC_GET_PLAYER_GAMEINFO_REQ = 0xD609;
    public static final int MSGCODE_CAC_GET_PLAYER_GAMEINFO_RESP = 0xD610;

    /**
     * 获取图片服务器地址
     */
    public static final int MSGCODE_CAC_GET_FILE_SERVER_ADDR_REQ = 0xC623;
    public static final int MSGCODE_CAC_GET_FILE_SERVER_ADDR_RESP = 0xC624;

}
