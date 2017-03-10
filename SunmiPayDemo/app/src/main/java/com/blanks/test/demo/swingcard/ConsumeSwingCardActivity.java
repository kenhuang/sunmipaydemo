package com.blanks.test.demo.swingcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.blanks.test.demo.MyApplication;
import com.blanks.test.demo.R;
import com.blanks.test.demo.TransactionCallback;
import com.blanks.test.demo.bean.CardInfo;

import static com.blanks.test.demo.MainActivity.CARDTYPE_IC;
import static com.blanks.test.demo.MainActivity.CARDTYPE_MAG;
import static com.blanks.test.demo.MainActivity.CARDTYPE_NFC;
import static com.blanks.test.demo.MainActivity.SECURE_READER_DEVICE;


/**
 * 刷卡界面的基类
 *
 * @author Leeshenzhou on 2017/2/6.
 */
public class ConsumeSwingCardActivity extends ParentActivity {

    private static final String TAG = "ConsumeSwingCard";
    private TextView textView1, textView2, textView3, textView4, textView5;
    private PayDetail mPayDetail = new PayDetail();


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (4 == msg.what) {
                textView5.setText(R.string.card_timeout);
                return true;
            }
            if (1 == msg.what) {
                textView5.setText(R.string.check_card_error);
                Toast.makeText(getApplicationContext(), getString(R.string.check_card_error), Toast.LENGTH_SHORT).show();
                finish();
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
                textView1.setText(getString(R.string.card_type) + cardInfo.cardType);
                textView2.setText(getString(R.string.pan) + cardInfo.cardStatus);
                textView3.setText(getString(R.string.track2) + cardInfo.track2);
                if (null != cardInfo.hashMap)
                    textView4.setText(getString(R.string.additonal) + cardInfo.hashMap.toString());
            }
            mPayDetail.setCardType(cardInfo.cardType);
            mPayDetail.setCardNo(cardInfo.PAN);

            Intent intent = new Intent(ConsumeSwingCardActivity.this, ConsumeInputPWDActivity.class);
            intent.putExtra("mPayDetail", mPayDetail);
            startActivity(intent);
            finish();
            return true;
        }
    });
    private TransactionCallback mCallback = new TransactionCallback(mHandler);


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //注意!stopCheckCard只有在人为中断刷卡流程时才可以调用
        stopCheckCard();
    }

    /**
     * 交易预处理
     */
    private boolean prepareTransaction() {
        boolean flag = true;
        try {
            String amount = "1000";
            // 175为非接脱机超限，对磁条卡和IC卡不处理
            int iPOSPrecessStatus = MyApplication.deviceProvide.getEMVProvider().prepareTransaction(SECURE_READER_DEVICE, 1, amount, "0");
            Log.d(TAG, "amount:" + amount + " iPOSPrecessStatus:" + iPOSPrecessStatus);
            if (iPOSPrecessStatus != 175 && iPOSPrecessStatus != 0) {
                showToast(getResources().getString(R.string.base_init_error));
                flag = false;
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consume_swing);
        findView();
        if (null != MyApplication.deviceProvide) {
            try {
                MyApplication.deviceProvide.registerTransactionCallback(mCallback);
                textView5.setText(R.string.initial_success);
                textView5.setText("等待检卡......");
                if(prepareTransaction()){
                    startCheckCard();
                }else {
                    finish();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, R.string.initial_fail, Toast.LENGTH_LONG).show();
        }


    }

    private void findView() {
        textView1 = (TextView) findViewById(R.id.tv1);
        textView2 = (TextView) findViewById(R.id.tv2);
        textView3 = (TextView) findViewById(R.id.tv3);
        textView4 = (TextView) findViewById(R.id.tv4);
        textView5 = (TextView) findViewById(R.id.checkstatus);
    }

    public void startCheckCard() {
        int cardType = 0;
        cardType += CARDTYPE_MAG;
        cardType += CARDTYPE_IC;
        cardType += CARDTYPE_NFC;
        byte[] appendData = new byte[16];
        try {
            MyApplication.deviceProvide.getReadCardProvider().checkCard(cardType, SECURE_READER_DEVICE, appendData, appendData.length, 60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注意!stopCheckCard只有在人为中断刷卡流程时才可以调用
     */
    public void stopCheckCard() {
        try {
            MyApplication.deviceProvide.getReadCardProvider().abortCheckCard();//注意!stopCheckCard只有在人为中断刷卡流程时才可以调用
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

