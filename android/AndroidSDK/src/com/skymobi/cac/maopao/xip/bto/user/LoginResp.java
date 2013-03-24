package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_USER_LOGIN_RESP)
public class LoginResp extends XipResp {

    @ByteField(index = 3, description = "玩家基本信息")
    private PlayerBaseInfoBTO playerBaseInfo;

    public PlayerBaseInfoBTO getPlayerBaseInfo() {
        return playerBaseInfo;
    }

    public void setPlayerBaseInfo(PlayerBaseInfoBTO playerBaseInfo) {
        this.playerBaseInfo = playerBaseInfo;
    }

}
