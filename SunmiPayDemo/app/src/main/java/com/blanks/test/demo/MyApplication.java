package com.blanks.test.demo;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;

import com.blanks.test.demo.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.DeviceProvide;
import com.sunmi.pay.hardware.aidl.SecurityProvider;

/**
 * Created by sunmi on 2017/1/14.
 */

public class MyApplication extends Application {

    private ConnectPayService connectPayService;
    public static DeviceProvide deviceProvide;
    SecurityProvider securityProvider;
    public static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
//        conn();
    }

    public void conn() {
        connectPayService = ConnectPayService.Init(this.getApplicationContext());
        connectPayService.connectPayService(mPayServiceConnected);
    }


    private ConnectPayService.PayServiceConnected mPayServiceConnected = new ConnectPayService.PayServiceConnected() {

        @Override
        public void onServiceConnected(DeviceProvide mDeviceProvide) {
            deviceProvide = mDeviceProvide;
            try {
                securityProvider = MyApplication.deviceProvide.getSecurityProvider();
                initSecretKey();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected() {
            deviceProvide=null;
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
        saveKey(KEKIndex, 5, 1, 1, KEK, KEK.length);
        // TMK 41
        saveKey(TMKIndex, 1, KEKIndex, 3, eTMK, eTMK.length);
        // PIK 51
        saveKey(PIKIndex, 2, TMKIndex, 3, workKey, workKey.length);
        // MAK 61
        saveKey(MAKIndex, 5, TMKIndex, 3, workKey, workKey.length);
        // TDK 71
        saveKey(TDKIndex, 5, TMKIndex, 3, workKey, workKey.length);
    }

    /**
     * 保存密钥到芯片
     *
     * @param index1      需要保存的密钥索引，1~99
     * @param index2      需要保存的密钥类型索引，1为主密钥；2为PIN密钥；3为MAC密钥；4为PAN密钥；5为自定义密钥
     * @param index3      指定解密密文的密钥索引，1~99；
     * @param encryptType 密钥密文的加密方式，1表示没加密；2表示DES；3表示TDES；4表示RSA公钥
     * @param key         密钥数据
     * @param keyLength   密钥数据长度
     * @return 等于0：密钥保存成功；等于4：密钥重复；其他：密钥保存错误。
     */
    public synchronized int saveKey(int index1, int index2, int index3, int encryptType, byte[] key, int keyLength) throws RemoteException {
        byte[] keyIndex = new byte[3];
        keyIndex[0] = (byte) index1;
        keyIndex[1] = (byte) index2;
        keyIndex[2] = (byte) index3;
        int result = securityProvider.saveKey(keyIndex, (byte) encryptType, key, keyLength);
        Log.e(TAG, "saveKey result:" + result);
        return result;
    }
}
