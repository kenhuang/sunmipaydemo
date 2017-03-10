package com.sunmi.pay.hardware.aidl.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * AID实体类
 * Created by WL on 2017/1/11.
 */

public class AID implements Parcelable {

    public String aid_9F06;
    public String aid_DF01;
    public String aid_9F09;
    public String aid_DF11;
    public String aid_DF12;
    public String aid_DF13;
    public String aid_9F1B;
    public String aid_DF15;
    public String aid_DF16;
    public String aid_DF17;
    public String aid_DF14;
    public String aid_DF18;
    public String aid_9F7B;
    public String aid_DF19;
    public String aid_DF20;
    public String aid_DF21;
    public String aid_all;





    @Override
    public String toString() {
        return "AID{" +
                "aid_9F06='" + aid_9F06 + '\'' +
                ", aid_DF01='" + aid_DF01 + '\'' +
                ", aid_9F09='" + aid_9F09 + '\'' +
                ", aid_DF11='" + aid_DF11 + '\'' +
                ", aid_DF12='" + aid_DF12 + '\'' +
                ", aid_DF13='" + aid_DF13 + '\'' +
                ", aid_9F1B='" + aid_9F1B + '\'' +
                ", aid_DF15='" + aid_DF15 + '\'' +
                ", aid_DF16='" + aid_DF16 + '\'' +
                ", aid_DF17='" + aid_DF17 + '\'' +
                ", aid_DF14='" + aid_DF14 + '\'' +
                ", aid_DF18='" + aid_DF18 + '\'' +
                ", aid_9F7B='" + aid_9F7B + '\'' +
                ", aid_DF19='" + aid_DF19 + '\'' +
                ", aid_DF20='" + aid_DF20 + '\'' +
                ", aid_DF21='" + aid_DF21 + '\'' +
                '}';
    }

    public AID() {
    }

    private AID(Parcel in) {
        aid_9F1B = in.readString();
        aid_9F06 = in.readString();
        aid_9F7B = in.readString();
        aid_9F09 = in.readString();
        aid_DF01 = in.readString();
        aid_DF11 = in.readString();
        aid_DF12 = in.readString();
        aid_DF13 = in.readString();
        aid_DF14 = in.readString();
        aid_DF15 = in.readString();
        aid_DF16 = in.readString();
        aid_DF17 = in.readString();
        aid_DF18 = in.readString();
        aid_DF19 = in.readString();
        aid_DF20 = in.readString();
        aid_DF21 = in.readString();

    }

    public static final Creator<AID> CREATOR = new Creator<AID>() {
        @Override
        public AID createFromParcel(Parcel source) {
            return new AID(source);
        }

        @Override
        public AID[] newArray(int size) {
            return new AID[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(aid_9F1B);
        dest.writeString(aid_9F06);
        dest.writeString(aid_9F7B);
        dest.writeString(aid_9F09);
        dest.writeString(aid_DF01);
        dest.writeString(aid_DF11);
        dest.writeString(aid_DF12);
        dest.writeString(aid_DF13);
        dest.writeString(aid_DF14);
        dest.writeString(aid_DF15);
        dest.writeString(aid_DF16);
        dest.writeString(aid_DF17);
        dest.writeString(aid_DF18);
        dest.writeString(aid_DF19);
        dest.writeString(aid_DF20);
        dest.writeString(aid_DF21);
    }
}
