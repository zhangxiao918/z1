package com.skymobi.cac.maopao.xip.bto.basis;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class PetInfo implements ByteBean{

    @ByteField(index = 0, bytes = 1, description = "头像ID")
    private int portraitId;

    @ByteField(index = 1, bytes = 1, description = "昵称长度")
    private int nicknameLen;

    @ByteField(index = 2, length = 1, description = "昵称")
    private String nickName;

    @ByteField(index = 3, bytes = 1, description = "技能数量")
    private int skillNum;

    @ByteField(index = 4, length = 3, description = "技能")
    private SkillInfo[] skills;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public int getNicknameLen() {
        return nicknameLen;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nicknameLen = (null == nickName) ? 0 : nickName.length() * 2;
    }

    public int getSkillNum() {
        return skillNum;
    }

    public SkillInfo[] getSkills() {
        return skills;
    }

    public void setSkills(SkillInfo[] skills) {
        this.skills = skills;
        this.skillNum = (null == skills) ? 0 : skills.length;
    }
}
