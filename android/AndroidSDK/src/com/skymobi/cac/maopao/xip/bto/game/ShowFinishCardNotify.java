package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_SHOWCARD_NOTIFY)
public class ShowFinishCardNotify extends XipBody {

	@ByteField(index = 0, bytes = 1)
	private int cardInfoLen;

	@ByteField(index = 1, length=0)
	private CardInfo[] cardInfos;

	public CardInfo[] getCardInfos() {
		if (cardInfos == null) {
			cardInfos = new CardInfo[0];
		}
		return cardInfos;
	}

	public void setCardInfos(CardInfo[] cardInfos) {
		this.cardInfos = cardInfos;
		this.cardInfoLen = getCardInfos().length;
	}

	public int getCardInfoLen() {
		return cardInfoLen;
	}

}
