package com.skymobi.cac.maopao.xip.bto.basis;

import java.util.Arrays;
import java.util.List;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_GET_GAME_INFO_RESP)
public class GetGameInfoResp extends XipResp {

    @ByteField(index = 3)
    private byte roomStylesNum;

    @ByteField(index = 4, length = 3, description = "房间类型")
    private RoomStyleInfo[] roomStyles;

    @ByteField(index = 5, bytes = 1, description = "欢迎语")
    private int messageLen;

    @ByteField(index = 6, length = 5)
    private String message;

    public byte getRoomStylesNum() {
        return roomStylesNum;
    }

    public void setRoomStylesNum(byte roomStylesNum) {
        this.roomStylesNum = roomStylesNum;
    }

    public RoomStyleInfo[] getRoomStyles() {
        if (roomStyles == null) {
            roomStyles = new RoomStyleInfo[0];
        }
        return roomStyles;
    }

    public void setRoomStyles(RoomStyleInfo[] roomStyles) {
        this.roomStyles = roomStyles;
        this.setRoomStylesNum((byte) getRoomStyles().length);
    }

    public void addRoomStyle(RoomStyleInfo roomStyle) {
        List<RoomStyleInfo> roomStyles = Arrays.asList(getRoomStyles());
        roomStyles.add(roomStyle);
        setRoomStyles(roomStyles.toArray(new RoomStyleInfo[] {}));
    }

    public int getMessageLen() {
        return messageLen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.messageLen = (message == null) ? 0 : (message.length() * 2);
    }
}
