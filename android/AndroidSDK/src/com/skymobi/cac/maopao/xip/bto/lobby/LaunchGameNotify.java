package com.skymobi.cac.maopao.xip.bto.lobby;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.basis.PlayerInfo;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_LAUNCH_GAME_NOTIFY)
public class LaunchGameNotify extends XipBody {

    @ByteField(index = 0, bytes = 2, description = "服务器模块id")
    private int svrModId;

    @ByteField(index = 1, bytes = 1, description = "玩家自己的座位号")
    private int mySeatId;

    @ByteField(index = 2, bytes = 1, description = "桌子ID")
    private int tableId;

    @ByteField(index = 3, bytes = 1, description = "玩家数量")
    private byte playerNum;

    @ByteField(index = 4, length = 3, description = "玩家信息")
    private PlayerInfo[] playerList;

    @ByteField(index = 5, bytes = 2, description = "提示消息长度")
    private int gameMsgLen;

    @ByteField(index = 6, length = 5, description = "提示消息内容")
    private String gameMsg;

    @ByteField(index = 7, bytes = 2, description = "分区公告长度")
    private int zoneMsgLen;

    @ByteField(index = 8, length = 7, description = "分区公告内容")
    private String zoneMsg;

    public int getSvrModId() {
        return svrModId;
    }

    public void setSvrModId(int svrModId) {
        this.svrModId = svrModId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public byte getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(byte playerNum) {
        this.playerNum = playerNum;
    }

    public PlayerInfo[] getPlayerList() {
        if (playerList == null) {
            playerList = new PlayerInfo[0];
        }
        return playerList;
    }

    public void setPlayerList(PlayerInfo[] playerList) {
        this.playerList = playerList;
        this.setPlayerNum((byte) getPlayerList().length);
    }

    public String getZoneMsg() {
        return zoneMsg;
    }

    public void setZoneMsg(String zoneMsg) {
        this.zoneMsg = zoneMsg;
        this.zoneMsgLen = (zoneMsg == null) ? 0 : (zoneMsg.length() * 2);
    }

    public int getGameMsgLen() {
        return gameMsgLen;
    }

    public String getGameMsg() {
        return gameMsg;
    }

    public int getZoneMsgLen() {
        return zoneMsgLen;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setGameMsg(String gameMsg) {
        this.gameMsg = gameMsg;
        this.gameMsgLen = (gameMsg == null) ? 0 : (gameMsg.length() * 2);
    }

    public int getMySeatId() {
        return mySeatId;
    }

    public void setMySeatId(int mySeatId) {
        this.mySeatId = mySeatId;
    }
}
