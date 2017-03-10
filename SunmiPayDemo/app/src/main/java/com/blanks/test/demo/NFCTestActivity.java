package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blanks.test.demo.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.DeviceProvide;
import com.sunmi.pay.hardware.aidl.TransactionListener;

import java.util.Map;

/**
 * Created by WL on 2017/2/21.
 */

public class NFCTestActivity extends Activity {


    public static final int CARDTYPE_MAG = 1;
    public static final int CARDTYPE_IC = 2;
    public static final int CARDTYPE_NFC = 4;
    public static final int GENERAL_READER_DEVICE = 269484034;

    private Button mBtnTest;
    private TextView mTvResult;

    private DeviceProvide mDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_test);
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        mDevice = MyApplication.deviceProvide;
        try {
            mDevice.registerTransactionCallback(mCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] b = new byte[4];
                try {
                    mDevice.getReadCardProvider().checkCard(CARDTYPE_IC, GENERAL_READER_DEVICE, b, b.length, 30);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private TransactionListener mCallback = new TransactionListener.Stub() {
        @Override
        public void onWaitingForCardSwipe() throws RemoteException {

        }

        @Override
        public int onCheckCardCompleted(int i, int i1, String s, String s1, Map hashMap) throws RemoteException {

            if (i1 == 0) {
                if (i == CARDTYPE_IC) {
                    mHandle.obtainMessage(0).sendToTarget();
                } else if (i == CARDTYPE_NFC) {
                    mHandle.obtainMessage(1).sendToTarget();
                }
            }

            return 0;
        }

        @Override
        public void onTimeout() throws RemoteException {

        }

        @Override
        public void onUpdateProcess(int i) throws RemoteException {

        }

        @Override
        public int onInputPIN(int i, byte[] data) throws RemoteException {
            return 0;
        }

        @Override
        public int onGetECC(byte[] bytes, int i, int i1, byte[] bytes1, byte[] bytes2) throws RemoteException {
            return 0;
        }

        @Override
        public int onGetTerminalTag(byte[] tag) throws RemoteException {
            return 0;
        }

        @Override
        public int onKernelMessage(int type, int messageID, byte[] messageData) throws RemoteException {
            return 0;
        }

        @Override
        public int onAppSelect(int i, String[] strings) throws RemoteException {
            return 0;
        }

        @Override
        public void onDebug(String info) throws RemoteException {

        }

        @Override
        public int onCheckCRL(String RID, String Index, String SN) throws RemoteException {
            return 0;
        }
    };

    byte[] apdu1 = StringByteUtils.HexString2Bytes("00A40000023F00");
    byte[] apdu2 = StringByteUtils.HexString2Bytes("00B0960000");
    byte[] apdu3 = StringByteUtils.HexString2Bytes("00A404000853484D454353414D");
    byte[] apdu4 = StringByteUtils.HexString2Bytes("002000010412345678");
    byte[] apdu5 = StringByteUtils.HexString2Bytes("00A4040008D1A7C9FABFA8303100");
    byte[] apdu6 = StringByteUtils.HexString2Bytes("00A4040008DF53484D4543000100");
    byte[] apdu7 = StringByteUtils.HexString2Bytes("00A4040008AD53484D4543010100");
    byte[] apdu8 = StringByteUtils.HexString2Bytes("0084000008");
    byte[] apdu9 = StringByteUtils.HexString2Bytes("801A250D08B523468000000000");
    String apdu10 = "80FA050010";
    String apdu11 = "04DC013C06";

    byte[] apdu_test = StringByteUtils.HexString2Bytes("00B2013D00");
    private Handler mHandle = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    byte[] b1 = new byte[512];
                    try {
                        mTvResult.setText(mTvResult.getText().toString() + "第1步指令:" + StringByteUtils.bytesToHexString(apdu1) + "\n");
                        int i1 = mDevice.getReadCardProvider().ICCSendRecv(apdu1, apdu1.length, b1);
                        if (i1 > 0) {
                            byte[] r1 = new byte[i1];
                            System.arraycopy(b1, 0, r1, 0, r1.length);
                            mTvResult.setText(mTvResult.getText().toString() + "第一步PSAM指令结果:" + StringByteUtils.bytesToHexString(r1) + "\n");
                            if (parseResult(r1).equals("9000")) {
                                byte[] b2 = new byte[512];
                                mTvResult.setText(mTvResult.getText().toString() + "第2步指令:" + StringByteUtils.bytesToHexString(apdu2) + "\n");
                                int i2 = mDevice.getReadCardProvider().ICCSendRecv(apdu2, apdu2.length, b2);
                                if (i2 > 0) {
                                    byte[] r2 = new byte[i2];
                                    System.arraycopy(b2, 0, r2, 0, r2.length);
                                    mTvResult.setText(mTvResult.getText().toString() + "第二步PSAM指令结果:" + StringByteUtils.bytesToHexString(r2) + "\n");
                                    if (parseResult(r2).equals("9000")) {
                                        byte[] b3 = new byte[512];
                                        mTvResult.setText(mTvResult.getText().toString() + "第3步指令:" + StringByteUtils.bytesToHexString(apdu3) + "\n");
                                        int i3 = mDevice.getReadCardProvider().ICCSendRecv(apdu3, apdu3.length, b3);
                                        if (i3 > 0) {
                                            byte[] r3 = new byte[i3];
                                            System.arraycopy(b3, 0, r3, 0, r3.length);
                                            mTvResult.setText(mTvResult.getText().toString() + "第三步PSAM指令结果:" + StringByteUtils.bytesToHexString(r3) + "\n");
                                            if (parseResult(r3).equals("9000")) {
                                                byte[] b4 = new byte[512];
                                                mTvResult.setText(mTvResult.getText().toString() + "第4步指令:" + StringByteUtils.bytesToHexString(apdu4) + "\n");
                                                int i4 = mDevice.getReadCardProvider().ICCSendRecv(apdu4, apdu4.length, b4);
                                                if (i4 > 0) {
                                                    byte[] r4 = new byte[i4];
                                                    System.arraycopy(b4, 0, r4, 0, r4.length);
                                                    mTvResult.setText(mTvResult.getText().toString() + "第四步PSAM指令结果:" + StringByteUtils.bytesToHexString(r4) + "\n");
                                                    if (parseResult(r4).equals("9000")) {
                                                        byte[] b = new byte[4];
                                                        mDevice.getReadCardProvider().checkCard(CARDTYPE_NFC, GENERAL_READER_DEVICE, b, b.length, 30);
                                                    } else {
                                                        mTvResult.setText(mTvResult.getText().toString() + " 第四步PSAM指令结果结果不为9000！\n");
                                                    }
                                                } else {
                                                    mTvResult.setText(mTvResult.getText().toString() + " 第四步PSAM指令结果失败！\n");
                                                }
                                            } else {
                                                mTvResult.setText(mTvResult.getText().toString() + " 第三步PSAM指令结果结果不为9000！\n");
                                            }
                                        } else {
                                            mTvResult.setText(mTvResult.getText().toString() + " 第三步PSAM指令结果失败！\n");
                                        }
                                    } else {
                                        mTvResult.setText(mTvResult.getText().toString() + " 第二步PSAM指令结果结果不为9000！\n");
                                    }
                                } else {
                                    mTvResult.setText(mTvResult.getText().toString() + " 第二步PSAM指令结果失败！\n");
                                }
                            } else {
                                mTvResult.setText(mTvResult.getText().toString() + " 第一步PSAM指令结果不为9000！\n");
                            }
                        } else {
                            mTvResult.setText("第一步PSAM指令结果失败！");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    try {
                        byte[] b5 = new byte[512];
                        mTvResult.setText(mTvResult.getText().toString() + "第5步指令:" + StringByteUtils.bytesToHexString(apdu5) + "\n");
                        int i5 = mDevice.getReadCardProvider().NFCSendRecv(apdu5, apdu5.length, b5);
                        if (i5 > 0) {
                            byte[] r5 = new byte[i5];
                            System.arraycopy(b5, 0, r5, 0, r5.length);
                            mTvResult.setText(mTvResult.getText().toString() + "第五步CPU指令结果:" + StringByteUtils.bytesToHexString(r5) + "\n");
                            if (parseResult(r5).equals("9000")) {
                                byte[] b6 = new byte[512];
                                mTvResult.setText(mTvResult.getText().toString() + "第6步指令:" + StringByteUtils.bytesToHexString(apdu6) + "\n");
                                int i6 = mDevice.getReadCardProvider().NFCSendRecv(apdu6, apdu6.length, b6);
                                if (i6 > 0) {
                                    byte[] r6 = new byte[i6];
                                    System.arraycopy(b6, 0, r6, 0, r6.length);
                                    mTvResult.setText(mTvResult.getText().toString() + "第六步CPU指令结果:" + StringByteUtils.bytesToHexString(r6) + "\n");
                                    if (parseResult(r6).equals("9000")) {
                                        byte[] b7 = new byte[512];
                                        mTvResult.setText(mTvResult.getText().toString() + "第7步指令:" + StringByteUtils.bytesToHexString(apdu7) + "\n");
                                        int i7 = mDevice.getReadCardProvider().NFCSendRecv(apdu7, apdu7.length, b7);
                                        if (i7 > 0) {
                                            byte[] r7 = new byte[i7];
                                            System.arraycopy(b7, 0, r7, 0, r7.length);
                                            mTvResult.setText(mTvResult.getText().toString() + "第七步CPU指令结果:" + StringByteUtils.bytesToHexString(r7) + "\n");
                                            if (parseResult(r7).equals("9000")) {
                                                byte[] b8 = new byte[512];
                                                mTvResult.setText(mTvResult.getText().toString() + "第8步指令:" + StringByteUtils.bytesToHexString(apdu8) + "\n");
                                                int i8 = mDevice.getReadCardProvider().NFCSendRecv(apdu8, apdu8.length, b8);
                                                if (i8 > 0) {
                                                    byte[] r8 = new byte[i8];
                                                    System.arraycopy(b8, 0, r8, 0, r8.length);
                                                    mTvResult.setText(mTvResult.getText().toString() + "第八步CPU指令结果:" + StringByteUtils.bytesToHexString(r8) + "\n");
                                                    if (parseResult(r8).equals("9000")) {
                                                        byte[] randomb = new byte[i8 - 3];
                                                        System.arraycopy(b8, 1, randomb, 0, randomb.length);
                                                        random1 = StringByteUtils.bytesToHexString(randomb);
                                                        mTvResult.setText(mTvResult.getText().toString() + "第八步CPU指令执行完生成的随机数:" + random1 + "\n");
//                                                        byte[] w_b = new byte[1024];
//                                                        int w1 = mDevice.getReadCardProvider().NFCSendRecv(apdu_test, apdu_test.length, w_b);
//                                                        byte[] r_b = new byte[w1];
//                                                        System.arraycopy(w_b, 0, r_b, 0, r_b.length);
//                                                        mTvResult.setText(mTvResult.getText().toString() + "测试指令执行完结果:" + StringByteUtils.bytesToHexString(r_b) + "\n");
                                                        mHandle.obtainMessage(3).sendToTarget();
                                                    } else {
                                                        mTvResult.setText(mTvResult.getText().toString() + " 第八步CPU指令结果失败！\n");
                                                    }
                                                }
                                            } else {
                                                mTvResult.setText(mTvResult.getText().toString() + " 第七步CPU指令结果失败！\n");
                                            }
                                        }
                                    } else {
                                        mTvResult.setText(mTvResult.getText().toString() + " 第六步CPU指令结果失败！\n");
                                    }
                                }
                            } else {
                                mTvResult.setText(mTvResult.getText().toString() + " 第五步CPU指令结果失败！\n");
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case 3:
                    try {
                        byte[] b9 = new byte[512];
                        mTvResult.setText(mTvResult.getText().toString() + "第9步指令:" + StringByteUtils.bytesToHexString(apdu9) + "\n");
                        int i9 = mDevice.getReadCardProvider().ICCSendRecv(apdu9, apdu9.length, b9);
                        if (i9 > 0) {
                            byte[] r9 = new byte[i9];
                            System.arraycopy(b9, 0, r9, 0, r9.length);
                            mTvResult.setText(mTvResult.getText().toString() + "第九步PSAM指令结果:" + StringByteUtils.bytesToHexString(r9) + "\n");
                            if (parseResult(r9).equals("9000")) {
                                String str10 = apdu10 + random1 + "04DC013C06" + writeData + "80";
                                mTvResult.setText(mTvResult.getText().toString() + "第10步指令:" + str10 + "\n");
                                byte[] t_apdu10 = StringByteUtils.HexString2Bytes(str10);
                                byte[] b10 = new byte[512];
                                int i10 = mDevice.getReadCardProvider().ICCSendRecv(t_apdu10, t_apdu10.length, b10);
                                if (i10 > 0) {
                                    byte[] r10 = new byte[i10];
                                    System.arraycopy(b10, 0, r10, 0, r10.length);
                                    mTvResult.setText(mTvResult.getText().toString() + "第十步PSAM指令结果:" + StringByteUtils.bytesToHexString(r10) + "\n");
                                    if (parseResult(r10).equals("9000")) {
                                        byte[] mac = new byte[i10 - 3];
                                        System.arraycopy(b10, 1, mac, 0, mac.length);
                                        strmac = StringByteUtils.bytesToHexString(mac);
                                        mTvResult.setText(mTvResult.getText().toString() + "第十步PSAM指令执行完计算出来的MAC:" + strmac + "\n");
//                                        byte[] w_b = new byte[1024];
//                                        int w1 = mDevice.getReadCardProvider().NFCSendRecv(apdu_test, apdu_test.length, w_b);
//                                        byte[] r_b = new byte[w1];
//                                        System.arraycopy(w_b, 0, r_b, 0, r_b.length);
//                                        mTvResult.setText(mTvResult.getText().toString() + "测试指令执行完结果:" + StringByteUtils.bytesToHexString(r_b) + "\n");

                                        //最后一步的操作，对CPU卡写数据
                                        String str11 = apdu11 + writeData + strmac;
                                        mTvResult.setText(mTvResult.getText().toString() + "第11步指令:" + str11 + "\n");
                                        byte[] t_apdu11 = StringByteUtils.HexString2Bytes(str11);
                                        byte[] b11 = new byte[512];
                                        int i11 = mDevice.getReadCardProvider().NFCSendRecv(t_apdu11, t_apdu11.length, b11);
                                        if (i11 > 0) {
                                            byte[] r11 = new byte[i11];
                                            System.arraycopy(b11, 0, r11, 0, r11.length);
                                            mTvResult.setText(mTvResult.getText().toString() + "第十一步CPU指令结果:" + StringByteUtils.bytesToHexString(r11) + "\n");
                                            if (parseResult(r11).equals("9000")) {
                                                mTvResult.setText(mTvResult.getText().toString() + " 所有测试指令已经全部执行完成！\n");
                                            } else {
                                                mTvResult.setText(mTvResult.getText().toString() + " 第十一步CPU指令结果结果不为9000！\n");
                                            }
                                        } else {
                                            mTvResult.setText(mTvResult.getText().toString() + " 第十一步CPU指令结果失败！\n");
                                        }


                                    } else

                                    {
                                        mTvResult.setText(mTvResult.getText().toString() + " 第十步PSAM指令结果结果不为9000！\n");
                                    }
                                } else

                                {
                                    mTvResult.setText(mTvResult.getText().toString() + " 第十步PSAM指令结果失败！\n");
                                }
                            } else {
                                mTvResult.setText(mTvResult.getText().toString() + " 第九步PSAM指令结果结果不为9000！\n");
                            }
                        } else {
                            mTvResult.setText(mTvResult.getText().toString() + " 第九步PSAM指令结果失败！\n");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return false;
        }
    }
    );


    private String strmac = "";
    private String random1 = "0A824CAC268AAFC9";
    //    private String writeData = "B52346804D4754540002201701231046280A824CAC268AAFC9";414180
    private String writeData = "4141";

    private String parseResult(byte[] r2) {
        if (r2.length < 2) {
            return "";
        }
        byte[] tb = new byte[2];
        tb[0] = r2[r2.length - 2];
        tb[1] = r2[r2.length - 1];
        return StringByteUtils.bytesToHexString(tb);
    }
}