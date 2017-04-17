package com.blanks.test.demo;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;

import com.blanks.test.demo.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.DeviceProvide;
import com.sunmi.pay.hardware.aidl.SecurityProvider;

import sunmi.paylib.SunmiPayKernel;

/**
 * Created by sunmi on 2017/1/14.
 */

public class MyApplication extends Application {
    public static SunmiPayKernel mSunmiPayKernel;
    public static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void conn() {
        mSunmiPayKernel = SunmiPayKernel.getInstance();
        mSunmiPayKernel.connectPayService(getApplicationContext(), mConnCallback);
    }

    private SunmiPayKernel.ConnCallback mConnCallback = new SunmiPayKernel.ConnCallback() {
        @Override
        public void onServiceConnected() {
            try {
                initSecretKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected() {
        }
    };

    public static int KEKIndex = 81;
    public static int TMKIndex = 41;
    public static int PIKIndex = 51;
    public static int MAKIndex = 61;
    public static int TDKIndex = 71;


    private void initSecretKey() throws RemoteException {
        byte[] KEK = {(byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33,
                (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33};
        byte[] eTMK = StringByteUtils.HexString2Bytes("B784118946137E571C9DA668C2D790E2");
        byte[] workKey = {(byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31,
                (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31};

        //测试时请将主密钥和工作密钥存入以下索引
        // KEK 81
        mSunmiPayKernel.mPinPadProvider.loadKEK(KEKIndex, KEK);
        // TMK 41
        mSunmiPayKernel.mPinPadProvider.loadTMK(TMKIndex, 1, KEKIndex, eTMK);
        // PIK 51
        mSunmiPayKernel.mPinPadProvider.loadPIK(PIKIndex, 2, TMKIndex, workKey, null);
        // MAK 61
        mSunmiPayKernel.mPinPadProvider.loadWKEY(MAKIndex, 5, TMKIndex, workKey, null);
        // TDK 71
        mSunmiPayKernel.mPinPadProvider.loadWKEY(TDKIndex, 5, TMKIndex, workKey, null);
    }
}
