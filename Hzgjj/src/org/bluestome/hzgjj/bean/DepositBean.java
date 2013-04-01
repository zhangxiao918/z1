
package org.bluestome.hzgjj.bean;

public class DepositBean {

    // 序号
    private int index;
    // 业务年月
    private String date;
    // 缴存类型
    private String type;
    // 企业
    private String company;
    // 个人
    private String self;
    // 合计
    private String total;
    // 补交金额
    private String pay;
    // 是否入账
    private String in;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

}
