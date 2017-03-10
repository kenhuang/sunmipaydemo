package com.sunmi.pay.hardware.aidl.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * RID实体类
 * Created by WL on 2017/1/11.
 */

public class RID implements Parcelable {

    public String rid_9F06;
    public String rid_9F22;
    public String rid_DF05;
    public String rid_DF06;
    public String rid_DF07;
    public String rid_DF02;
    public String rid_DF04;
    public String rid_DF03;
    public String rid_all;

    public RID() {
    }

    protected RID(Parcel in) {
        rid_9F06 = in.readString();
        rid_9F22 = in.readString();
        rid_DF05 = in.readString();
        rid_DF06 = in.readString();
        rid_DF07 = in.readString();
        rid_DF02 = in.readString();
        rid_DF04 = in.readString();
        rid_DF03 = in.readString();
    }

    public static final Creator<RID> CREATOR = new Creator<RID>() {
        @Override
        public RID createFromParcel(Parcel in) {
            return new RID(in);
        }

        @Override
        public RID[] newArray(int size) {
            return new RID[size];
        }
    };


    @Override
    public String toString() {
        return "RID{" +
                "rid_9F06='" + rid_9F06 + '\'' +
                ", rid_9F22='" + rid_9F22 + '\'' +
                ", rid_DF05='" + rid_DF05 + '\'' +
                ", rid_DF06='" + rid_DF06 + '\'' +
                ", rid_DF07='" + rid_DF07 + '\'' +
                ", rid_DF02='" + rid_DF02 + '\'' +
                ", rid_DF04='" + rid_DF04 + '\'' +
                ", rid_DF03='" + rid_DF03 + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rid_9F06);
        dest.writeString(rid_9F22);
        dest.writeString(rid_DF05);
        dest.writeString(rid_DF06);
        dest.writeString(rid_DF07);
        dest.writeString(rid_DF02);
        dest.writeString(rid_DF04);
        dest.writeString(rid_DF03);
    }
}
