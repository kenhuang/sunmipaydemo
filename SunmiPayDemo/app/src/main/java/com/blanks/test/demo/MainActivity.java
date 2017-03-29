package com.blanks.test.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blanks.test.demo.swingcard.ConsumeSwingCardActivity;
import com.blanks.test.demo.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.BasicProvider;
import com.sunmi.pay.hardware.aidl.DeviceProvide;
import com.sunmi.pay.hardware.aidl.TransactionListener;

import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView mTvResult;
    private TextView mTvState;

    private EditText mEtInput;
    private int flag;

    public static final int CARDTYPE_MAG = 1;
    public static final int CARDTYPE_IC = 2;
    public static final int CARDTYPE_NFC = 4;
    private static final int MESSAGE_TOAST = 15;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvState = (TextView) findViewById(R.id.tv_state);
        mButton = (Button) findViewById(R.id.button);
        mEtInput = (EditText) findViewById(R.id.mEtInput);
        mButton.setOnClickListener(this);
        flag = 0;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTvState.setText(getResources().getText(R.string.connected));
                    break;
                case 2:
                    mTvState.setText(getResources().getText(R.string.unconnected));
                    break;
                case 3:
                    mTvResult.setText(getResources().getText(R.string.wait_swipe_card));
                    break;
                case 4:
                    mTvResult.setText(getResources().getText(R.string.swipe_timeout));
                    break;
                case 5:
                    mTvResult.setText(getResources().getText(R.string.read_card_fail));
                    break;
                case 6:
                    mTvResult.setText(getResources().getText(R.string.read_card_success));
                    break;
                case 7:
                    mTvResult.setText(getResources().getText(R.string.encrypted_data_results) + "" + (int) msg.obj);
                    break;
                case 8:
                    if ((int) msg.obj == 100) {
                        mTvResult.setText(getResources().getText(R.string.update_completed));
                    } else
                        mTvResult.setText(getResources().getText(R.string.update_process) + "" + (int) msg.obj);
                    break;
                case 9:
                    mTvResult.setText(getResources().getText(R.string.update_error) + "" + (int) msg.obj);
                    break;
                case 10:
                    mTvResult.setText(getResources().getText(R.string.restart_devices));
                    break;
                case MESSAGE_TOAST:
                    mTvResult.setText((String) msg.obj);
                    break;
                case 16:
                    mTvResult.setText(getResources().getText(R.string.functions_error) + "" + (int) msg.obj);
                    break;
                case 17:
                    mTvResult.setText(getResources().getText(R.string.APDU_error) + "" + String.format("%02X", (byte) msg.obj));
                    break;
                case 18:
                    mTvResult.setText(getResources().getText(R.string.APDU_success) + "" + (String) msg.obj);
                    break;
                case 19:
                    if ((long) msg.obj == -1) {
                        mTvResult.setText(getText(R.string.AID_fail) + "" + (long) msg.obj);
                    } else {
                        mTvResult.setText(getResources().getText(R.string.AID_success) + "" + (long) msg.obj);
                    }
                    break;
                case 20:
                    if ((long) msg.obj == -1) {
                        mTvResult.setText(getText(R.string.RID_fail) + "" + (long) msg.obj);
                    } else {
                        mTvResult.setText(getText(R.string.RID_success) + "" + (long) msg.obj);
                    }
                    break;

                case 21:
                    if ((boolean) msg.obj) {
                        mTvResult.setText(getText(R.string.AID_delete_success) + "" + (boolean) msg.obj);
                    } else {
                        mTvResult.setText(getText(R.string.AID_delete_fail) + "" + (boolean) msg.obj);
                    }
                    break;
                case 22:
                    if ((boolean) msg.obj) {
                        mTvResult.setText(getText(R.string.RID_delete_success) + "" + (boolean) msg.obj);
                    } else {
                        mTvResult.setText(getText(R.string.RID_delete_fail) + "" + (boolean) msg.obj);
                    }
                    break;
                case 100:
                    mTvResult.setText(getText(R.string.update_process) + "" + (int) msg.obj);
                    break;

                case -10:
                    mTvResult.setText(getText(R.string.encrypted_data_results) + "\n" + (String) msg.obj);
                    break;
                case -11:
                    mTvResult.setText(getText(R.string.cal_8583_error) + "\n" + (String) msg.obj);
                    break;
                case -2:
                    mTvResult.setText(getText(R.string.tip));
                    break;
            }
            return false;
        }
    });

    private TransactionCallback mCallback = new TransactionCallback();
    private DeviceProvide deviceProvide;

    private ConnectPayService.PayServiceConnected mPayServiceConnected = new ConnectPayService.PayServiceConnected() {

        @Override
        public void onServiceConnected(DeviceProvide mDeviceProvide) {
            deviceProvide = mDeviceProvide;
            MyApplication.deviceProvide = mDeviceProvide;
            try {
                mDeviceProvide.registerTransactionCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(1);
        }

        @Override
        public void onServiceDisconnected() {
            try {
                MyApplication.deviceProvide = null;
                deviceProvide.unRegisterTransactionCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(2);
        }
    };

    private void setBasicInfo(BasicProvider mBasicProvider) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(getText(R.string.firmware_version) + "" + mBasicProvider.getFirmwareVer() + "\n");
            sb.append(getText(R.string.hardware_version) + "" + mBasicProvider.getHardwareVer() + "\n");
            sb.append(getText(R.string.check_version) + "" + mBasicProvider.getSDKVer() + "\n");
            new AlertDialog.Builder(this)
                    .setTitle(getText(R.string.version_code))
                    .setMessage(sb.toString())
                    .setPositiveButton(getText(R.string.confirm), new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            dialog.dismiss();
                        }

                    })
                    .show();
            //设置显示的内容
            mTvResult.setText(sb.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ConnectPayService connectPayService;

    public void init(View view) {
        connectPayService = ConnectPayService.Init(this.getApplicationContext());
        connectPayService.connectPayService(mPayServiceConnected);
    }

    /**
     * 打开LED灯
     *
     * @param view
     */
    public void openLed(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getText(R.string.tip), Toast.LENGTH_SHORT).show();
            return;
        }
        if (connectPayService != null && connectPayService.getDeviceProvide() != null) {
            try {
                if (flag == 0) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(1, 1);
                    flag++;
                } else if (flag == 1) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(2, 1);
                    flag++;
                } else if (flag == 2) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(3, 1);
                    flag++;
                } else if (flag == 3) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(4, 1);
                    flag++;
                } else if (flag == 4) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(4, 0);
                    flag++;
                } else if (flag == 5) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(3, 0);
                    flag++;
                } else if (flag == 6) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(2, 0);
                    flag++;
                } else if (flag == 7) {
                    connectPayService.getDeviceProvide().getBasicProvider().ledStatusOnDevice(1, 0);
                    flag = 0;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            mHandler.sendEmptyMessage(-2);
        }
    }

    /**
     * 蜂鸣器响
     *
     * @param view
     */
    public void openBuzzer(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getText(R.string.tip), Toast.LENGTH_SHORT).show();
            return;
        }
        if (connectPayService != null && connectPayService.getDeviceProvide() != null) {
            try {
                connectPayService.getDeviceProvide().getBasicProvider().buzzerOnDevice(2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            mHandler.sendEmptyMessage(-2);
        }
    }

    /**
     * 点击输入密码
     *
     * @param view
     */
    public void inputPin(View view) {
        startActivity(new Intent(this, InputPasswordActivity.class));
    }

    /**
     * 数据加密
     *
     * @param view
     */
    public void dataEncrypt(View view) {
        startActivity(new Intent(this, DataEncryptActivity.class));
//        if (deviceProvide == null) {
//            Toast.makeText(getBaseContext(), getResources().getText(R.string.tip), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (connectPayService != null && connectPayService.getDeviceProvide() != null) {
//            try {
//                String s = mEtInput.getText().toString();
//                if (!TextUtils.isEmpty(s)) {
//                    byte[] b1 = s.getBytes();
//                    int b1Length = b1.length;
//                    byte[] b2 = new byte[b1Length];
//                    int result = connectPayService.getDeviceProvide().getSecurityProvider().dataEncrypt(new byte[2], (byte) 2, b1, b1Length, b2);
//                    Message msg = mHandler.obtainMessage(7, result);
//                    mHandler.sendMessage(msg);
//                }
//            } catch (RemoteException e) {
//                Message msg = mHandler.obtainMessage(-10, e.toString());
//                mHandler.sendMessage(msg);
//                e.printStackTrace();
//            }
//        } else {
//            mHandler.sendEmptyMessage(-2);
//        }
    }

    /**
     * 检卡
     *
     * @param view
     */
    public void onCheckCard(View view) {
        startActivity(new Intent(this, CheckCardActivity.class));
    }


    /**
     * 更新固件
     *
     * @param view
     */
    public void updateFirmware(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getText(R.string.tip), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    /**
     * 更新PIN KEY
     *
     * @param view
     */
    public void updatePINKEY(View view) {
        startActivity(new Intent(this, UpdatePINKeyActivity.class));
    }

    /**
     * 更新PAN KEY
     *
     * @param view
     */
    public void updatePANKEY(View view) {

    }

    /**
     * 更新MAC KEY
     *
     * @param view
     */
    public void updateMACKEY(View view) {
        startActivity(new Intent(this, UpdateMACKeyActivity.class));
    }

    /**
     * 设置主密钥
     *
     * @param view
     */
    public void setMasterKEY(View view) {

    }

    /**
     * 设置一组AID
     *
     * @param view
     */
    public void setAID(View view) {
        startActivity(new Intent(this, CRUDAidRidActivity.class));
    }

    /**
     * 设置一组RID
     *
     * @param view
     */
    public void setRID(View view) {
        startActivity(new Intent(this, CRUDAidRidActivity.class));
    }

    /**
     * 删除全部AID
     *
     * @param view
     */
    public void deleteAllAID(View view) {
        startActivity(new Intent(this, CRUDAidRidActivity.class));
    }

    /**
     * 删除全部RID
     *
     * @param view
     */
    public void deleteAllRID(View view) {
        startActivity(new Intent(this, CRUDAidRidActivity.class));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String path = actualimagecursor.getString(actual_image_column_index);
                if (connectPayService != null && connectPayService.getDeviceProvide() != null) {
                    try {
                        connectPayService.getDeviceProvide().getUpdateProvider().updateFirmware(path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    mHandler.sendEmptyMessage(-2);
                }

            }
        }
    }

    /**
     * NFC卡APDU指令操作
     *
     * @param view
     */
    public void NFCSendRecv(View view) {
//        if (deviceProvide == null) {
//            Toast.makeText(getBaseContext(), getResources().getText(R.string.tip), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(mEtInput.getText().toString())) {
//            Toast.makeText(getBaseContext(), getText(R.string.input_tip), Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (connectPayService != null && connectPayService.getDeviceProvide() != null) {

            Intent i = new Intent(MainActivity.this, NFCTestActivity.class);
            startActivity(i);
//            try {
//                byte[] tagIn = StringByteUtils.HexString2Bytes(mEtInput.getText().toString());
//                byte[] tagOut = new byte[256];
//                int r = connectPayService.getDeviceProvide().getReadCardProvider().NFCSendRecv(tagIn, tagIn.length, tagOut);
//                if (r > 0) {
//                    if (tagOut[0] == 0) {
//                        String str = r + ":";
//                        for (int i = 1; i < tagOut.length; i++) {
//                            str += String.format("%02X", tagOut[i]);
//                        }
//                        mHandler.obtainMessage(18, str).sendToTarget();
//                    } else {
//                        mHandler.obtainMessage(17, tagIn[0]).sendToTarget();
//                    }
//                } else {
//                    mHandler.obtainMessage(16, r).sendToTarget();
//                }
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        } else {
            mHandler.sendEmptyMessage(-2);
        }
    }

    /**
     * IC卡APDU指令操作
     *
     * @param view
     */
    public void ICSendRecv(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getText(R.string.tip), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mEtInput.getText().toString())) {
            Toast.makeText(getBaseContext(), getText(R.string.input_tip), Toast.LENGTH_SHORT).show();
            return;
        }
        if (connectPayService != null && connectPayService.getDeviceProvide() != null) {
            try {
                byte[] tagIn = StringByteUtils.HexString2Bytes(mEtInput.getText().toString());
                byte[] tagOut = new byte[256];
                int r = connectPayService.getDeviceProvide().getReadCardProvider().ICCSendRecv(tagIn, tagIn.length, tagOut);
                if (r > 0) {
                    if (tagOut[0] == 0) {
                        String str = r + ":";
                        for (int i = 1; i < tagOut.length; i++) {
                            str += String.format("%02X", tagOut[i]);
                        }
                        mHandler.obtainMessage(18, str).sendToTarget();
                    } else {
                        mHandler.obtainMessage(17, tagIn[0]).sendToTarget();
                    }
                } else {
                    mHandler.obtainMessage(16, r).sendToTarget();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            mHandler.sendEmptyMessage(-2);
        }
    }

    byte[] b = new byte[4];


    /**
     * 查询版本信息
     *
     * @param view
     */
    public void querySystemInfo(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getText(R.string.tip), Toast.LENGTH_SHORT).show();
            return;
        }
        if (connectPayService != null && connectPayService.getDeviceProvide() != null) {
            try {
                setBasicInfo(connectPayService.getDeviceProvide().getBasicProvider());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            mHandler.sendEmptyMessage(-2);
        }
    }

    /**
     * 保存密钥
     *
     * @param view
     */
    public void saveKEY(View view) {
        Intent saveKeyIntent = new Intent(this, SaveKeyActivity.class);
        this.startActivity(saveKeyIntent);
    }

    /**
     * 交易预处理
     *
     * @param view
     */
    public void preTransaction(View view) {
        this.startActivity(new Intent(this, TransactionActivity.class));
    }

    /**
     * 二次授权处理
     *
     * @param view
     */
    public void authorizationProcess(View view) {
        this.startActivity(new Intent(this, AuthorizationProcessActivity.class));
    }

    /**
     * 刷卡
     *
     * @param view
     */
    public void swingcard(View view) {
        this.startActivity(new Intent(this, ConsumeSwingCardActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(this, SetMasterKeyActivity.class));
                break;
            default:
                break;
        }
    }

    class TransactionCallback extends TransactionListener.Stub {

        @Override
        public void onWaitingForCardSwipe() throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvResult.setText(getText(R.string.wait_swipe_card));
                }
            });
        }

        @Override
        public int onCheckCardCompleted(int i, int i1, String s, String s1, Map hashMap) throws RemoteException {
            if (i1 == 0) {
                mHandler.obtainMessage(6).sendToTarget();
            } else {
                mHandler.obtainMessage(5).sendToTarget();
            }
            return 0;
        }

        @Override
        public void onTimeout() throws RemoteException {
            mHandler.obtainMessage(4).sendToTarget();
        }

        @Override
        public void onUpdateProcess(int i) throws RemoteException {
            if (i < 0) {
                mHandler.obtainMessage(9, 0, 0, i).sendToTarget();
            } else if (i >= 0 && i <= 100) {
                mHandler.obtainMessage(8, 0, 0, i).sendToTarget();
            } else if (i == 10000) {
                mHandler.obtainMessage(10).sendToTarget();
            }
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
    }

    /**
     * 按银联标准加密PIN
     */
    public void standard_encryption_pin(View view) {
        Intent intent = new Intent(MainActivity.this, StandardEncryptionPinActivity.class);
        startActivity(intent);
    }

    public void calculation8583mac(View view) {
        Intent intent = new Intent(MainActivity.this, Calculation8583MacActivity.class);
        startActivity(intent);
    }

    /**
     * 卡是否移走了
     *
     * @param view
     */
    public void check_card_is_remove(View view) {
        Intent intent = new Intent(MainActivity.this, CheckCardRemovedActivity.class);
        startActivity(intent);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
