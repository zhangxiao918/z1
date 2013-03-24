package com.skymobi.cac.maopao.xip.bto.basis;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_ENTER_ROOM_STYLE_REQ)
public class EnterRoomStyleReq extends XipBody {

    @ByteField(index = 0, bytes = 2, description = "游戏ID")
    private int gameId;

    @ByteField(index = 1, bytes = 4, description = "房间分区ID")
    private int roomZoneId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(short gameId) {
        this.gameId = gameId;
    }

    public int getRoomZoneId() {
        return roomZoneId;
    }

    public void setRoomZoneId(int roomZoneId) {
        this.roomZoneId = roomZoneId;
    }

}
