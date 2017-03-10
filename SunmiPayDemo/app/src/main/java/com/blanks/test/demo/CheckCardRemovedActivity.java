package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.sunmi.pay.hardware.aidl.DeviceProvide;


/**
 * Created by sunmi on 2017/1/14.
 */

public class CheckCardRemovedActivity extends Activity {

    private TextView text_result;
    private DeviceProvide deviceProvide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_card_removed);
        text_result = (TextView) this.findViewById(R.id.text_result);
        this.deviceProvide = MyApplication.deviceProvide;
        if(deviceProvide==null){
            mhandler.sendEmptyMessage(-1);
        }else{
            mhandler.sendEmptyMessage(1);
        }
    }

    public void nfc(View view) {
        try {
            boolean icRemoved = deviceProvide.getReadCardProvider().isICRemoved();
            if (icRemoved) {
                mhandler.sendEmptyMessage(-2);
            } else {
                mhandler.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            Message message = new Message();
            message.what=0;
            message.obj = e.getMessage();
            mhandler.handleMessage(message);
            e.printStackTrace();
        }
    }

    public void conn(View view){
        ((MyApplication)getApplication()).conn();
    }

    public void ic(View view) {
        try {
            boolean icRemoved = deviceProvide.getReadCardProvider().isICRemoved();
            if (icRemoved) {
                mhandler.sendEmptyMessage(-3);
            } else {
                mhandler.sendEmptyMessage(3);
            }
        }catch (Exception e){
            Message message = new Message();
            message.what=0;
            message.obj = e.getMessage();
            mhandler.handleMessage(message);
            e.printStackTrace();
        }
    }

    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    text_result.setText(getText(R.string.isconnected));
                    break;
                case -1:
                    text_result.setText(getText(R.string.no_connected));
                    break;
                case 2:
                    text_result.setText(getText(R.string.nfc_removed));
                    break;
                case -2:
                    text_result.setText(getText(R.string.nfc_unremoved));
                    break;
                case 3:
                    text_result.setText(getText(R.string.ic_removed));
                    break;
                case -3:
                    text_result.setText(getText(R.string.ic_unremoved));
                    break;
                case 0:
                    text_result.setText(getText(R.string.error)+msg.obj.toString());
                    break;
            }
            return false;
        }
    });

}
