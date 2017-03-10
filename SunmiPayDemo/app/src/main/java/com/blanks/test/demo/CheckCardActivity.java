package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blanks.test.demo.bean.CardInfo;

import java.util.ArrayList;
import java.util.List;

import static com.blanks.test.demo.MainActivity.CARDTYPE_IC;
import static com.blanks.test.demo.MainActivity.CARDTYPE_MAG;
import static com.blanks.test.demo.MainActivity.CARDTYPE_NFC;
import static com.blanks.test.demo.MainActivity.SECURE_READER_DEVICE;

public class CheckCardActivity extends Activity {

    private CheckBox checkBox1, checkBox2, checkBox3;
    private TextView textView1, textView2, textView3, textView4, textView5;
    private EditText editText;
    private List<CheckBox> list = new ArrayList<>();

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (4 == msg.what) {
                textView5.setText(R.string.card_timeout);
                return true;
            }

            textView5.setText("");
            if (null == msg.obj) return false;
            CardInfo cardInfo = (CardInfo) msg.obj;
            if (7 == cardInfo.cardType) {
                textView1.setText(getString(R.string.card_type));
                textView2.setText(getString(R.string.pan));
                textView3.setText(getString(R.string.track2));
                textView4.setText(getString(R.string.additonal));
            } else {
                textView1.setText(getString(R.string.card_type)+ cardInfo.cardType);
                textView2.setText(getString(R.string.pan)+ cardInfo.cardStatus);
                textView3.setText(getString(R.string.track2) + cardInfo.track2);
                if (null != cardInfo.hashMap)
                    textView4.setText(getString(R.string.additonal) + cardInfo.hashMap.toString());
            }

            return true;
        }
    });
    private TransactionCallback mCallback = new TransactionCallback(mHandler);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_card);
        findView();
        if (null != MyApplication.deviceProvide) {
            try {
                MyApplication.deviceProvide.registerTransactionCallback(mCallback);
                textView5.setText(R.string.initial_success);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, R.string.initial_fail, Toast.LENGTH_LONG).show();
        }
    }

    public void initProvider(View view) {
        if (null != MyApplication.deviceProvide) {
            try {
                textView5.setText(R.string.initial_success);
                MyApplication.deviceProvide.registerTransactionCallback(mCallback);
                view.setEnabled(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            ((MyApplication) getApplication()).conn();
            Toast.makeText(this, R.string.initial_fail, Toast.LENGTH_LONG).show();
        }

    }


    private void findView() {
        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        textView1 = (TextView) findViewById(R.id.tv1);
        textView2 = (TextView) findViewById(R.id.tv2);
        textView3 = (TextView) findViewById(R.id.tv3);
        textView4 = (TextView) findViewById(R.id.tv4);
        textView5 = (TextView) findViewById(R.id.checkstatus);
        editText = (EditText) findViewById(R.id.edit_timeout);
        list.add(checkBox1);
        list.add(checkBox2);
        list.add(checkBox3);
    }

    public void startCheckCard(View view) {
        int cardType = 0;
        if (list.get(0).isChecked()) {
            cardType += CARDTYPE_MAG;
        }
        if (list.get(1).isChecked()) {
            cardType += CARDTYPE_IC;
        }
        if (list.get(2).isChecked()) {
            cardType += CARDTYPE_NFC;
        }

        byte[] b = new byte[4];
        try {
            MyApplication.deviceProvide.getReadCardProvider().checkCard(cardType, SECURE_READER_DEVICE, b, b.length, Integer.valueOf(editText.getText().toString().trim()));
            textView5.setText("等待检卡......");
        } catch (Exception e) {

        }
    }

    public void stopCheckCard(View view) {
        try {
            MyApplication.deviceProvide.getReadCardProvider().abortCheckCard();
            textView5.setText("");
        } catch (Exception e) {
            Toast.makeText(this, "程序异常！", Toast.LENGTH_LONG).show();
        }
        textView1.setText("卡片类型:  ");
        textView2.setText("PAN:  ");
        textView3.setText("track2: ");
        textView4.setText("additonal:  ");
    }


}
