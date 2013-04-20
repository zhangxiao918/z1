package com.skymobi.cac.maopao.xip.bto.user;


import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class PlayerBaseInfoBTO implements ByteBean {

    @ByteField(index = 0, bytes = 4)
    private long skyId;

    @ByteField(index = 1, bytes = 1)
    private int accountNameLen;

    @ByteField(index = 2, length = 1)
    private String accountName;

    @ByteField(index = 3, bytes = 1)
    private int nickNameLen;

    @ByteField(index = 4, length = 3)
    private String nickName;

    @ByteField(index = 5, bytes = 1)
    private int passwordLen;

    @ByteField(index = 6, length = 5, description = "加密后的密码")
    private String password;
    
    @ByteField(index = 7, bytes = 2)
    private int portraitId;

    @ByteField(index = 8, bytes = 1)
    private int sex;

    @ByteField(index = 9, bytes = 1)
    private int age;

    @ByteField(index = 10, bytes = 4)
    private long gold;//金豆

    @ByteField(index = 11, bytes = 4)
    private long yuanbao;//元宝
    
    @ByteField(index = 12, bytes = 1)
    private int accountType;//用户类型：0:游客,1:平台注册账户,2:新浪,3:腾讯


    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public int getAccountNameLen() {
        return accountNameLen;
    }

    public void setAccountNameLen(int accountNameLen) {
        this.accountNameLen = accountNameLen;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
        this.accountNameLen = (accountName == null) ? 0 : accountName.length() * 2;
    }

    public int getNickNameLen() {
        return nickNameLen;
    }

    public void setNickNameLen(int nickNameLen) {
        this.nickNameLen = nickNameLen;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nickNameLen = (nickName == null) ? 0 : nickName.length() * 2;
    }

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getPoint() {
        return yuanbao;
    }

    public void setPoint(long point) {
        this.yuanbao = point;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordLen = (null == this.password ? 0 : this.password.length() * 2);
    }

    public int getPasswordLen() {
        return passwordLen;
    }
    
    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAccountType() {
        return accountType;
    }
}
