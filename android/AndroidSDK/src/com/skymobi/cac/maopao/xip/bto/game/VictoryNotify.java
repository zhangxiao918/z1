package com.skymobi.cac.maopao.xip.bto.game;


import java.util.Arrays;
import java.util.List;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_VICTORY_NOTIFY)
public class VictoryNotify extends XipBody {

    @ByteField(index = 0)
    private byte playerNum;

    @ByteField(index = 1, length = 0)
    private PlayerRoundResultInfo[] playerInfos;
    
    @ByteField(index = 2, description="是否换桌,0不换")
    private byte switchTable;
    
    @ByteField(index = 3, bytes = 1, description = "玩家自己是否地主,0不是")
    private int isLord;

    @ByteField(index = 4, bytes = 1, description = "玩家自己是否胜,0不是")
    private int isWinner;
    
    @ByteField(index = 5, bytes = 1, description = "地主座位号")
    private int lordSeatId;
    
    @ByteField(index = 6, bytes = 2, description = "底分")
    private int baseMoney;

    public byte getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(byte playerNum) {
        this.playerNum = playerNum;
    }

    public PlayerRoundResultInfo[] getPlayerRoundResults() {
        if (playerInfos == null) {
            playerInfos = new PlayerRoundResultInfo[0];
        }
        return playerInfos;
    }

    public void setPlayerRoundResults(PlayerRoundResultInfo[] playerInfos) {
        this.playerInfos = playerInfos;
        setPlayerNum((byte) getPlayerRoundResults().length);
    }

    public void addPlayerRoundResult(PlayerRoundResultInfo playerInfo) {
        List<PlayerRoundResultInfo> playerInfos = Arrays.asList(getPlayerRoundResults());
        playerInfos.add(playerInfo);
        setPlayerRoundResults(playerInfos.toArray(new PlayerRoundResultInfo[] {}));
    }

    
    public byte getSwitchTable() {
        return switchTable;
    }

    
    public void setSwitchTable(byte switchTable) {
        this.switchTable = switchTable;
    }
    
    public int getIsLord() {
        return isLord;
    }

    public void setIsLord(int isLord) {
        this.isLord = isLord;
    }

    public int getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(int isWinner) {
        this.isWinner = isWinner;
    }
    
    public int getLordSeatId() {
        return lordSeatId;
    }

    public void setLordSeatId(int lordSeatId) {
        this.lordSeatId = lordSeatId;
    }
    
    public int getBaseMoney() {
        return baseMoney;
    }

    public void setBaseMoney(int baseMoney) {
        this.baseMoney = baseMoney;
    }
}
