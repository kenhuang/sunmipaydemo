package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blanks.test.demo.widget.ProcessResult;
import com.sunmi.pay.hardware.aidl.TransactionListener;

import java.util.Map;

public class TransactionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        transactType = (EditText) findViewById(R.id.transactType);
        amount = (EditText) findViewById(R.id.amount);
        show = (TextView) findViewById(R.id.show);
    }

    private EditText transactType;
    private EditText amount;
    private TextView show;
    StringBuilder sb = new StringBuilder();
    private boolean isInit = false;

    private TransactionCallback mCallback = new TransactionCallback();

    public void init(View view) {
        try {
            if (MyApplication.deviceProvide != null && (MyApplication.deviceProvide.getPinPadProvider() != null)) {
                isInit = true;
                show.setText(getText(R.string.initial_success));
                MyApplication.deviceProvide.registerTransactionCallback(mCallback);
            } else {
                ((MyApplication) getApplication()).conn();
                isInit = false;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void prepareTransaction(View view) {
        if (!isInit) {
            sb.delete(0, sb.length());
            show.setText(getText(R.string.initial_fail));
            return;
        }
        String transactTypeStr = transactType.getText().toString();
        String amountStr = amount.getText().toString();
        if (TextUtils.isEmpty(transactTypeStr) || TextUtils.isEmpty(amountStr)) {
            sb.delete(0, sb.length());
            show.setText(getText(R.string.input_tip));
            return;
        }
        if (MyApplication.deviceProvide == null) {
            sb.delete(0, sb.length());
            show.setText(getText(R.string.connect_server_failed));
            return;
        }
        int transactType = Integer.parseInt(transactTypeStr);
        try {
            int ret = MyApplication.deviceProvide.getEMVProvider().prepareTransaction(269484034, transactType, amountStr, "0");
            sb.append(getResources().getString(R.string.return_code) + ret);
            show.setText(sb.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void transactProcess(View view) {
        if (!isInit) {
            sb.delete(0, sb.length());
            show.setText(R.string.initial_fail);
            return;
        }
        if (MyApplication.deviceProvide == null) {
            sb.delete(0, sb.length());
            show.setText(getText(R.string.connect_server_failed));
            return;
        }
        show.setText("transactProcess");
        byte[] aret = new byte[1024];
        try {
            int iret = MyApplication.deviceProvide.getEMVProvider().transactProcess(aret);
//             aret[iret] is field 55.
            if (iret > 0) {
                // has return data.
                if (aret[0] == (byte) 0xFB) {
                    // Successful. package 8583.
                    show.setText(getResources().getString(R.string.back)+iret+"--"+ProcessResult.getResult(aret[1]));
                } else if (aret[0] == 0x00) {
                    show.setText(getResources().getString(R.string.back)+iret+"--"+ProcessResult.getResult(aret[1]));
                } else {
                    show.setText(getResources().getString(R.string.back)+iret+"--"+ProcessResult.EMVResult(aret[0] & 0xFF));
                }
            } else {
                show.setText(getResources().getString(R.string.back)+iret+"--"+ProcessResult.EMVResult(aret[0] & 0xFF));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
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

}
