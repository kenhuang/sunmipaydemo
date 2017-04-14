package com.blanks.test.demo;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;

import com.blanks.test.demo.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.DeviceProvide;
import com.sunmi.pay.hardware.aidl.SecurityProvider;
import com.sunmi.pay.hardware.aidl.emv.EMVProvider;
import com.sunmi.pay.hardware.aidl.pinpad.PinPadProvider;
import com.sunmi.pay.hardware.aidl.readcard.ReadCardProvider;
import com.sunmi.pay.hardware.aidl.system.BasicProvider;

import sunmi.paylib.SunmiPayKernel;

/**
 * Created by sunmi on 2017/1/14.
 */

public class MyApplication extends Application {
    public static SunmiPayKernel mSunmiPayKernel;
    public static PinPadProvider mPinPadProvider;
    public static BasicProvider mBasicProvider;
    public static ReadCardProvider mReadCardProvider;
    public static EMVProvider mEMVProvider;

    public static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static int KEKIndex = 81;
    private static int TMKIndex = 41;
    private static int PIKIndex = 51;
    private static int MAKIndex = 61;
    private static int TDKIndex = 71;

    public static void initSecretKey() throws RemoteException {
        byte[] KEK = {(byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33,
                (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33};
        byte[] eTMK = StringByteUtils.HexString2Bytes("B784118946137E571C9DA668C2D790E2");
        byte[] workKey = {(byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31,
                (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31};

        //测试时请将主密钥和工作密钥存入以下索引
        // KEK 81
        mPinPadProvider.loadKEK(KEKIndex, KEK);
        // TMK 41
        mPinPadProvider.loadTMK(TMKIndex, 1, KEKIndex, eTMK);
        // PIK 51
        mPinPadProvider.loadPIK(PIKIndex, 2, TMKIndex, workKey, null);
        // MAK 61
        mPinPadProvider.loadWKEY(MAKIndex, 5, TMKIndex, workKey, null);
        // TDK 71
        mPinPadProvider.loadWKEY(TDKIndex, 5, TMKIndex, workKey, null);
    }
}
