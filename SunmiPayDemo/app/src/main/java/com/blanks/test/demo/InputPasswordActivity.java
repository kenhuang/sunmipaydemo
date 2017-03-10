package com.blanks.test.demo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blanks.test.demo.utils.StringByteUtils;
import com.blanks.test.demo.widget.CustomerKeyboard;
import com.blanks.test.demo.widget.dialog.KeyBoardDialog;
import com.sunmi.pay.hardware.aidl.TransactionListener;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class InputPasswordActivity extends Activity {

    private TextView show_pin;

    private EditText pan_block,index_block;
    /**
     * 密码键盘第一个button左顶点位置（绝对位置）
     */
    private static int[] keyboardleCoord = new int[2];
    /**
     * 密码键盘单个item宽度
     */
    private static int width;
    /**
     * 密码键盘单个item高度
     */
    private static int height;

    /**
     * 线间隔
     */
    private static int interval;

    /**
     * 取消键左顶点位置（绝对位置）
     */
    private static int[] cancelCoord = new int[2];
    /**
     * 取消键宽度
     */
    private static int cancelWidth;
    /**
     * 取消键高度
     */
    private static int cancelHeight;

    KeyBoardDialog.Builder builder;
    CustomerKeyboard mCustomerKeyboard;
    TextView mPasswordEditText;
    ImageView mImgCancel;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);
        show_pin = (TextView) findViewById(R.id.show_pin);
        pan_block = (EditText) findViewById(R.id.pan_block);
        index_block = (EditText) findViewById(R.id.pan_index);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    boolean isInit = false;

    public void init(View view) {
        try {
            if (MyApplication.deviceProvide != null && (MyApplication.deviceProvide.getPinPadProvider() != null)) {
                isInit = true;
                show_pin.setText(getText(R.string.initial_success));
                MyApplication.deviceProvide.registerTransactionCallback(mCallback);
            } else {
                ((MyApplication) getApplication()).conn();
                show_pin.setText(getText(R.string.initial_fail));
                isInit = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 点击输入密码
     *
     * @param view
     */
    public void inputPin(View view) {
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        if (!isInit) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }
        if (isLayout == false) {
            Log.e("wl", "客户端点击输入密码！");
            PINThread pin = new PINThread();
            pin.start();
            Log.e(TAG, "PINThread start");
        }
    }

    private TransactionCallback mCallback = new TransactionCallback();

    private boolean isLayout = false;

    private static final int REQUEST_LAYOUT = 0;
    private static final int MESSAGE_TOAST = 1;
    private static final int REQUEST_DISPLAY = 2;
    private static final int REQUEST_CLEAR = 3;
    private static final int REQUEST_DISMISS = 4;
    public static String pin = "";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TOAST:
                    show_pin.setText((String) msg.obj);
                    break;
                case REQUEST_CLEAR:
                    pin = "";
                    mPasswordEditText.setText(pin);
                    break;
                case REQUEST_DISMISS:
                    pin = "";
                    builder.dismiss();
                    break;
                case REQUEST_DISPLAY:
                    pin += "*";
                    mPasswordEditText.setText(pin);
                    break;
                case REQUEST_LAYOUT:
                    String key = (String) msg.obj;

                    Log.e("wl", "handle in ");
                    pin = "";

                    builder = new KeyBoardDialog.Builder(InputPasswordActivity.this).fullWidth().fromBottom().setView(R.layout.dialog_customer_keyboard);
                    builder.setOnClickListener(R.id.delete_dialog, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });
                    KeyBoardDialog dialog = builder.create();
                    mCustomerKeyboard = builder.getView(R.id.custom_key_board);
                    /**
                     * 初始化键盘数字
                     */
                    mCustomerKeyboard.initKeyBoard(key);
                    mPasswordEditText = builder.getView(R.id.tv_password);
                    mPasswordEditText.setText("");
                    mImgCancel = builder.getView(R.id.delete_dialog);
                    builder.setShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            // TODO Auto-generated method stub
                            TextView textView = (TextView) mCustomerKeyboard.findViewById(R.id.tv_1);
                            textView.getLocationOnScreen(keyboardleCoord);
                            width = textView.getWidth();
                            height = textView.getHeight();
                            interval = ((ViewGroup.MarginLayoutParams) textView.getLayoutParams()).rightMargin;
                            mImgCancel.getLocationOnScreen(cancelCoord);
                            cancelWidth = mImgCancel.getWidth();
                            cancelHeight = mImgCancel.getHeight();
                            StringBuffer buffer = new StringBuffer();
                            buffer.append("密码键盘第一个按钮坐标：x=" + keyboardleCoord[0] + "    y=" + keyboardleCoord[1] + "\n");
                            buffer.append("密码键盘第一个长度：width=" + width + "宽度：height=" + height + "\n");
                            buffer.append("密码键盘间隔线宽度：interval=" + interval + "\n");
                            buffer.append("取消按钮坐标：x=" + cancelCoord[0] + "    y=" + cancelCoord[1] + "\n");
                            buffer.append("取消按钮长度：width=" + cancelWidth + "宽度：height=" + cancelHeight + "\n");
                            buffer.append("屏幕长度：width=" + getScreenWidth(getBaseContext()) + "屏幕宽度：height="
                                    + getScreenHeight(getBaseContext()) + "\n");
                            show_pin.setText(buffer.toString());

                            isLayout = true;

                            Log.e("wl", "handle layout end: ");

                        }
                    });
                    dialog.show();
                    Log.e("wl", "handle out ");
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(builder!=null){
            builder.dismiss();
        }
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

    public class PINThread extends Thread {
        @Override
        public void run() {
            String input = pan_block.getText().toString();
            if (TextUtils.isEmpty(input)) {
                mHandler.obtainMessage(MESSAGE_TOAST, 0, 0,getText(R.string.input_tip)).sendToTarget();
                return;
            }
            byte[] panBlock = StringByteUtils.HexString2Bytes(input);
//            byte[] panBlock = {0x12, 0x34, 0x56, 0x78, (byte) 0x90};
            byte[] pinBlock = new byte[8];

            int ret = 0;
            try {
                ret = MyApplication.deviceProvide.getPinPadProvider().inputOnlinePIN(panBlock, panBlock.length, pinBlock);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Log.e(TAG, "inputOnlinePIN end: " + ret);

            String pin = "用户输入的PIN :";

            if (ret > 0) {
//                for (int i = 0; i < ret; i++) {
//                    Log.e(TAG, "pinBlock " + i + " " + String.format("%s", pinBlock[i]));
//
//                    pin += String.format("%s", pinBlock[i]);
//                }
                pin = StringByteUtils.bytesToHexString(pinBlock);
                pin = StringByteUtils.hexStringFotmat(pin);
            } else if (ret == 0) {
                pin = getResources().getString(R.string.custom_enter_ok);
            } else if (ret == -13) {
                pin = getResources().getString(R.string.input_overtime);
            } else if (ret == -12) {
                pin = getResources().getString(R.string.custom_cancel);
            } else {
                pin = getResources().getString(R.string.input_pin_error) + String.format("%s", ret);
            }

            mHandler.obtainMessage(MESSAGE_TOAST, 0, 0, pin).sendToTarget();
        }
    }

    class TransactionCallback extends TransactionListener.Stub {

        @Override
        public void onWaitingForCardSwipe() throws RemoteException {
        }

        @Override
        public int onCheckCardCompleted(int i, int i1, String s, String s1, Map hashMap) throws RemoteException {
            return 0;
        }

        @Override
        public void onTimeout() throws RemoteException {
            mHandler.obtainMessage(4).sendToTarget();
        }

        @Override
        public void onUpdateProcess(int i) throws RemoteException {
        }

        @Override
        public int onInputPIN(int i, byte[] data) throws RemoteException {
            if (i == 1) {
                String layout = "";

                for (int n = 0; n < 10; n++) {
                    layout += String.format("%c", data[n]);
                }

                Log.e("wl", "onInputPIN REQUEST_LAYOUT: " + layout);


                isLayout = false;

                mHandler.obtainMessage(REQUEST_LAYOUT, 0, 0, layout).sendToTarget();

                while (isLayout == false) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                isLayout = false;

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                int index = 0;
                //start X
                data[index++] = (byte) ((keyboardleCoord[0] >> 8) & 0xFF);
                data[index++] = (byte) ((keyboardleCoord[0]) & 0xFF);
                //start Y
                data[index++] = (byte) ((keyboardleCoord[1] >> 8) & 0xFF);
                data[index++] = (byte) ((keyboardleCoord[1]) & 0xFF);
                //width
                data[index++] = (byte) ((width >> 8) & 0xFF);
                data[index++] = (byte) ((width) & 0xFF);
                //interval X
                data[index++] = (byte) ((interval >> 8) & 0xFF);
                data[index++] = (byte) ((interval) & 0xFF);
                //height
                data[index++] = (byte) ((height >> 8) & 0xFF);
                data[index++] = (byte) ((height) & 0xFF);
                //interval Y
                data[index++] = (byte) ((interval >> 8) & 0xFF);
                data[index++] = (byte) ((interval) & 0xFF);
                //cancel X
                data[index++] = (byte) ((cancelCoord[0] >> 8) & 0xFF);
                data[index++] = (byte) ((cancelCoord[0]) & 0xFF);
                //cancel Y
                data[index++] = (byte) ((cancelCoord[1] >> 8) & 0xFF);
                data[index++] = (byte) ((cancelCoord[1]) & 0xFF);
                //cancel width
                data[index++] = (byte) ((cancelWidth >> 8) & 0xFF);
                data[index++] = (byte) ((cancelWidth) & 0xFF);
                //cancel height
                data[index++] = (byte) ((cancelHeight >> 8) & 0xFF);
                data[index++] = (byte) ((cancelHeight) & 0xFF);

                data[index++] = 6;
                data[index++] = 6;
                data[index++] = 1;
                data[index++] = (byte)Integer.parseInt(index_block.getText().toString());
                data[index++] = 1;
                Log.e(TAG,"PIN密码索引:"+(byte)Integer.parseInt(index_block.getText().toString()));
                int timeout = 30;
                //timeout
                data[index++] = (byte) ((timeout >> 8) & 0xFF);
                data[index++] = (byte) ((timeout) & 0xFF);
            } else if (i == 2) {
                mHandler.obtainMessage(REQUEST_DISPLAY, 0, 0, 0).sendToTarget();
            } else if (i == 3) {
                mHandler.obtainMessage(REQUEST_CLEAR, 0, 0, 0).sendToTarget();
            } else if (i == 4) {
                mHandler.obtainMessage(REQUEST_DISMISS, 0, 0, 0).sendToTarget();
            } else if (i == 5) {
                mHandler.obtainMessage(REQUEST_DISMISS, 0, 0, 0).sendToTarget();
            } else if (i == 6) {
                mHandler.obtainMessage(REQUEST_DISMISS, 0, 0, 0).sendToTarget();
            }

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
}
