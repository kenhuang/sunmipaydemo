package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunmi.pay.hardware.aidl.DeviceProvide;
import com.sunmi.pay.hardware.aidl.TransactionListener;

import java.util.Map;

/**
 * Created by sunmi on 2017/1/14.
 */

public class StandardEncryptionPinActivity extends Activity {

    private EditText edit_express;
    private TextView text_result;
    private Button btn_conn;
    private ConnectPayService connectPayService;
    private DeviceProvide deviceProvide;
    private TransactionCallback callback = new TransactionCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_encryption_pin);
        text_result = (TextView) findViewById(R.id.text_result);
        edit_express = (EditText) findViewById(R.id.edit_express);
        btn_conn = (Button) findViewById(R.id.btn_conn);
        conn(null);
    }

    public void encryption(View view) {

    }

    public void conn(View view) {
        connectPayService = ConnectPayService.Init(this.getApplicationContext());
        connectPayService.connectPayService(mPayServiceConnected);
    }

    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    btn_conn.setText(getText(R.string.isconnected));
                    break;
                case -1:
                    btn_conn.setText(getText(R.string.no_connected));
                    break;
            }
            return false;
        }
    });


    private class TransactionCallback extends TransactionListener.Stub {

        @Override
        public void onWaitingForCardSwipe() throws RemoteException {

        }

        @Override
        public int onCheckCardCompleted(int i, int i1, String s, String s1, Map hashMap) throws RemoteException {
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
    }

    private ConnectPayService.PayServiceConnected mPayServiceConnected = new ConnectPayService.PayServiceConnected() {

        @Override
        public void onServiceConnected(DeviceProvide mDeviceProvide) {
            deviceProvide = mDeviceProvide;
            try {
                mDeviceProvide.registerTransactionCallback(callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mhandler.sendEmptyMessage(1);
        }

        @Override
        public void onServiceDisconnected() {
            try {
                if (deviceProvide != null) {
                    deviceProvide.registerTransactionCallback(callback);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mhandler.sendEmptyMessage(-1);
        }
    };
}
