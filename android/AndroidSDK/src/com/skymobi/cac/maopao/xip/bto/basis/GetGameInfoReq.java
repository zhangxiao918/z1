
package com.skymobi.cac.maopao.xip.bto.basis;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_GAME_INFO_REQ)
public class GetGameInfoReq extends XipBody {

    @ByteField(index = 0, bytes = 2, description = "游戏ID")
    private int gameId;

    @ByteField(index = 1, bytes = 1, description = "1标识从主入口登入，0标识从其他入口进入")
    private int firstLogIn;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(short gameId) {
        this.gameId = gameId;
    }

    public int getFirstLogIn() {
        return firstLogIn;
    }

    public void setFirstLogIn(short firstLogIn) {
        this.firstLogIn = firstLogIn;
    }

}
