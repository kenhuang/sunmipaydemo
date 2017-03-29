package com.blanks.test.demo.swingcard;

import java.io.Serializable;

/**
 * 订单详情
 */
public class PayDetail implements Serializable {

    /**
     * ICCInfo
     */
    // 应用ID
    public String AID;
    // 应用标签
    public String appLabel;
    // 应用首选名称
    public String appName;
    // TVR
    public String TVR;
    // TSI
    public String TSI;
    // ATC
    public String ATC;
    // TC
    public String TC;
    //脚本处理结果
    public String ScriptResult;
    //密文信息数据
    public String CID;
    // ic卡55域数据
    public String ICC55;
    // PIN加密密文
    public String PINCipher;
    // 卡号
    public String CardNo;
    // 主账号序号（针对IC 卡）
    public String cardSerialNo;
    // 卡类型
    public int cardType;
    // 卡有效期
    public String EXPDate;


    public void setAID(String AID) {
        this.AID = AID;
    }
    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public void setTVR(String TVR) {
        this.TVR = TVR;
    }
    public void setTSI(String TSI) {
        this.TSI = TSI;
    }
    public void setATC(String ATC) {
        this.ATC = ATC;
    }
    public void setTC(String TC) {
        this.TC = TC;
    }
    public void setScriptResult(String ScriptResult) {
        this.ScriptResult = ScriptResult;
    }
    public void setCID(String CID) {
        this.CID = CID;
    }
    public void setICC55(String ICC55) {
        this.ICC55 = ICC55;
    }
    public void setPINCipher(String PINCipher) {
        this.PINCipher = PINCipher;
    }
    public String getCardNo() {
        return this.CardNo;
    }
    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }
    public void setCardSerialNo(String cardSerialNo) {
        this.cardSerialNo = cardSerialNo;
    }
    public int getCardType() {
        return this.cardType;
    }
    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

}
