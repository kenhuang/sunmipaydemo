package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.pay.hardware.aidl.DeviceProvide;

/**
 * Created by sunmi on 2017/1/14.
 */

public class Calculation8583MacActivity extends Activity {

    private byte[] tagIn = {0x08, 0x30, 0x00, 0x18, 0x00, 0x00, 0x02, (byte) 0xC0, 0x00, 0x14, 0x11, 0x27, 0x05, 0x01, 0x11, 0x30, 0x30, 0x32, 0x39, 0x30, 0x30, 0x30, 0x32, 0x38, 0x35, 0x38, 0x34, 0x38, 0x32, 0x39, 0x30, 0x30, 0x35, 0x31, 0x39, 0x38, 0x36, 0x30, 0x30, 0x32, 0x00, 0x11, 0x00, 0x00, 0x00, 0x01, 0x38, 0x20, 0x00, 0x45, 0x31, (byte) 0x9F, 0x06, 0x08, (byte) 0xA0, 0x00, 0x00, 0x03, 0x33, 0x01, 0x01, 0x01, (byte) 0x9F, 0x06, 0x08, (byte) 0xA0, 0x00, 0x00, 0x03, 0x33, 0x01, 0x01, 0x02, (byte) 0x9F, 0x06, 0x08, (byte) 0xA0, 0x00, 0x00, 0x03, 0x33, 0x01, 0x01, 0x03, (byte) 0x9F, 0x06, 0x08, (byte) 0xA0, 0x00, 0x00, 0x03, 0x33, 0x01, 0x01, 0x06, 0x00};
    private TextView textView2, t1;
    private EditText editText;
    private DeviceProvide deviceProvide;
    private EditText editTextKeyIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation8583_mac);
        textView2 = (TextView) this.findViewById(R.id.textView2);
        t1 = (TextView) this.findViewById(R.id.t1);
        editText = (EditText) this.findViewById(R.id.edit_type);
        editTextKeyIndex = (EditText) findViewById(R.id.edit_index);
        t1.setText(t1.getText().toString() + "\n\n\n" + R.string.tag_length + tagIn.length);
        this.deviceProvide = MyApplication.deviceProvide;
        if (deviceProvide == null) {
            mhandler.sendEmptyMessage(-1);
        } else {
            mhandler.sendEmptyMessage(1);
        }
    }

    public void conn(View view) {
        ((MyApplication) getApplication()).conn();
    }

    public void calculation(View view) {
        try {
            byte[] tagOut = new byte[8];
            String editString = editText.getText().toString().trim();
            String editIndex = editTextKeyIndex.getText().toString().trim();
            if (TextUtils.isEmpty(editString)) {
                Toast.makeText(getBaseContext(), "Type not allow null!", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(editIndex)) {
                Toast.makeText(getBaseContext(), "Index not allow null!", Toast.LENGTH_SHORT).show();
            }
            int type = Integer.parseInt(editString);
            int result = deviceProvide.getSecurityProvider().calc8583MAC(Integer.parseInt(editIndex), type, tagIn, tagIn.length, tagOut);
            Message message = new Message();
            message.what = 2;
            message.obj = tagOut;
            message.arg1 = result;
            mhandler.handleMessage(message);
        } catch (Exception e) {
            Message message = new Message();
            message.what = 0;
            message.obj = e.getMessage();
            mhandler.sendMessage(message);
            e.printStackTrace();
        }
    }

    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    textView2.setText(R.string.connected);
                    break;
                case -1:
                    textView2.setText(R.string.unconnected);
                    break;
                case 2:
                    textView2.setText(R.string.return_result + msg.arg1 + "\n" + getResources().getString(R.string.return_MAC_ciphertext) + msg.obj.toString());
                    break;
                case 0:
                    textView2.setText(getResources().getString(R.string.exception) + msg.obj.toString());
                    break;
            }
            return false;
        }
    });

}
