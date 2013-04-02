
package com.bluestome.hzti.bean;

/*
 号牌号码:浙A249NT
 号牌种类:小型汽车
 违法时间:2013/3/5 12:04:17
 违法地点:[景区]环城西路_环城西路白沙路口(环城西路白沙路口附近)
 违法行为:机动车违反禁止标线指示的
 处理标记:未处理
 缴款标记:未缴款
 */
public class TBean {

    private String carNum;
    private String carType;
    private String date;
    private String loc;
    private String content;
    private String dealResult;
    private String payResult;

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

}
