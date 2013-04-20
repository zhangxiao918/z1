package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.basis.PlayerInfo;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_ADD_PLAYER_NOTIFY)
public class AddPlayerNotify extends XipBody {

	@ByteField(index = 0)
	private PlayerInfo playerInfo;

	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}

}
