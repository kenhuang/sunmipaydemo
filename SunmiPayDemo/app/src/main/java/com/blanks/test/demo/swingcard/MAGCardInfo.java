package com.blanks.test.demo.swingcard;

import java.io.Serializable;

/**
 * 磁条卡相关信息
 * Created by xurong on 2017/2/12.
 */

public class MAGCardInfo implements Serializable {

    //卡片一磁
    private String track1;
    //卡片二磁
    private String track2;
    //二磁加密密文
    private String track2Cipher;
    //卡片三磁
    private String track3;
    //三磁加密密文
    private String track3Cipher;
    //卡有效期 YYMM
    private String expireDate;
    //是否为IC卡
    private boolean isIccCard;
    //国家代码
    private String countryCode;
    //持卡人姓名
    private String cardHolder;

    public String getTrack1() {
        return track1;
    }

    public void setTrack1(String track1) {
        this.track1 = track1;
    }

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }

    public String getTrack2Cipher() {
        return track2Cipher;
    }

    public void setTrack2Cipher(String track2Cipher) {
        this.track2Cipher = track2Cipher;
    }

    public String getTrack3() {
        return track3;
    }

    public void setTrack3(String track3) {
        this.track3 = track3;
    }

    public String getTrack3Cipher() {
        return track3Cipher;
    }

    public void setTrack3Cipher(String track3Cipher) {
        this.track3Cipher = track3Cipher;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isIccCard() {
        return isIccCard;
    }

    public void setIccCard(boolean iccCard) {
        isIccCard = iccCard;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    @Override
    public String toString() {
        return "MAGCardInfo{" +
                "track1='" + track1 + '\'' +
                ", track2='" + track2 + '\'' +
                ", track2Cipher='" + track2Cipher + '\'' +
                ", track3='" + track3 + '\'' +
                ", track3Cipher='" + track3Cipher + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", isIccCard=" + isIccCard +
                ", countryCode='" + countryCode + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                '}';
    }
}
