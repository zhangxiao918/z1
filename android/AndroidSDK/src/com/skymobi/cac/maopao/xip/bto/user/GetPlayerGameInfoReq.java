/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename: GetPlayerGameInfoReq.java
 * Creator: yangtong_fang
 * Version: 1.0
 * Date: 2010-5-26 下午08:54:13
 * Description:
 *******************************************************************************/

package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_GAMEINFO_REQ)
public class GetPlayerGameInfoReq extends XipBody {

    @ByteField(index = 0, description = "游戏ID")
    private short gameId;

    public short getGameId() {
        return gameId;
    }

    public void setGameId(short gameId) {
        this.gameId = gameId;
    }

}
