/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename:  GetPlayerGameInfoResp.java
 * Creator:   yangtong_fang
 * Version:   1.0
 * Date: 	  2010-5-26 下午08:55:59
 * Description: 
 *******************************************************************************/

package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_PLAYER_GAMEINFO_RESP)
public class GetPlayerGameInfoResp extends XipResp {

    @ByteField(index = 3, description = "赢的局数")
    private int winRound;

    @ByteField(index = 4, description = "输的局数")
    private int loseRound;

    @ByteField(index = 5, description = "平的局数")
    private int equalRound;

    @ByteField(index = 6, description = "逃跑的局数")
    private int escapeRound;

    @ByteField(index = 7, description = "断线次数")
    private int disconnRound;

    public int getWinRound() {
        return winRound;
    }

    public void setWinRound(int winRound) {
        this.winRound = winRound;
    }

    public int getLoseRound() {
        return loseRound;
    }

    public void setLoseRound(int loseRound) {
        this.loseRound = loseRound;
    }

    public int getEqualRound() {
        return equalRound;
    }

    public void setEqualRound(int equalRound) {
        this.equalRound = equalRound;
    }

    public int getEscapeRound() {
        return escapeRound;
    }

    public void setEscapeRound(int escapeRound) {
        this.escapeRound = escapeRound;
    }

    public int getDisconnRound() {
        return disconnRound;
    }

    public void setDisconnRound(int disconnRound) {
        this.disconnRound = disconnRound;
    }
}
