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
import com.blanks.test.demo.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.EMVProvider;

import java.util.ArrayList;
import java.util.List;

import static com.blanks.test.demo.MainActivity.CARDTYPE_IC;
import static com.blanks.test.demo.MainActivity.CARDTYPE_MAG;
import static com.blanks.test.demo.MainActivity.CARDTYPE_NFC;


/**
 * 刷卡界面的基类
 *
 * @author Leeshenzhou on 2017/2/6.
 */
public class ConsumeSwingCardActivity extends Activity {

    private static final String TAG = "ConsumeSwingCard";
    private TextView textView1, textView2, textView3, textView4, textView5;
    private PayDetail mPayDetail = new PayDetail();
    EMVProvider mTransactionProcess;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (4 == msg.what) {
                textView5.setText(R.string.card_timeout);
                return true;
            }
            if (1 == msg.what) {
                textView5.setText(R.string.check_card_error);
                showToast(getString(R.string.check_card_error));
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
            int iPOSPrecessStatus = MyApplication.deviceProvide.getEMVProvider().prepareTransaction(Constant.SECURE_READER_DEVICE, 1, amount, "0");
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
        //添加默认AID和RID
        try {
            mTransactionProcess = MyApplication.deviceProvide.getEMVProvider();
            for (String aid : getAIDList()) {
                mTransactionProcess.saveAID(StringByteUtils.hexStringToBytes(aid));
            }
            for (String rid : getRIDList()) {
                mTransactionProcess.saveRID(StringByteUtils.hexStringToBytes(rid));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_consume_swing);
        findView();
        if (null != MyApplication.deviceProvide) {
            try {
                MyApplication.deviceProvide.registerTransactionCallback(mCallback);
                textView5.setText(R.string.initial_success);
                textView5.setText("等待检卡......");
                if (prepareTransaction()) {
                    startCheckCard();
                } else {
                    finish();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            showToast(getString(R.string.initial_fail));
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
            MyApplication.deviceProvide.getReadCardProvider().checkCard(cardType, Constant.SECURE_READER_DEVICE, appendData, appendData.length, 60);
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

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取默认AID列表
     *
     * @return
     */
    private List<String> getAIDList() {
        List<String> list = new ArrayList<>();
        list.add("9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B040000C350DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06100000000000DF1906100000000000DF2006100000000000DF2106100000000000");
        list.add("9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B040000C350DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06100000000000DF1906100000000000DF2006100000000000DF2106100000000000");
        list.add("9F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B040000C350DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06100000000000DF1906100000000000DF2006100000000000DF2106100000000000");
        list.add("9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B040000C350DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06100000000000DF1906100000000000DF2006100000000000DF2106100000000000");
        return list;
    }

    /**
     * 获取默认RID列表
     *
     * @return
     */
    private List<String> getRIDList() {
        List<String> list = new ArrayList<>();
        list.add("9F0605A0000003339F220104DF05083230323531323331DF060101DF070101DF0281F8BC853E6B5365E89E7EE9317C94B02D0ABB0DBD91C05A224A2554AA29ED9FCB9D86EB9CCBB322A57811F86188AAC7351C72BD9EF196C5A01ACEF7A4EB0D2AD63D9E6AC2E7836547CB1595C68BCBAFD0F6728760F3A7CA7B97301B7E0220184EFC4F653008D93CE098C0D93B45201096D1ADFF4CF1F9FC02AF759DA27CD6DFD6D789B099F16F378B6100334E63F3D35F3251A5EC78693731F5233519CDB380F5AB8C0F02728E91D469ABD0EAE0D93B1CC66CE127B29C7D77441A49D09FCA5D6D9762FC74C31BB506C8BAE3C79AD6C2578775B95956B5370D1D0519E37906B384736233251E8F09AD79DFBE2C6ABFADAC8E4D8624318C27DAF1DF040103DF0314F527081CF371DD7E1FD4FA414A665036E0F5E6E5");
        list.add("9F0605A0000003339F220103DF05083230323431323331DF060101DF070101DF0281B0B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33DDF040103DF031487F0CD7C0E86F38F89A66F8C47071A8B88586F26");
        list.add("9F0605A0000003339F220102DF05083230323131323331DF060101DF070101DF028190A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57DF040103DF031403BB335A8549A03B87AB089D006F60852E4B8060");
        list.add("9F0605A0000003339F220101DF05083230313431323331DF060101DF070101DF028180BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93BDF040103DF0314E881E390675D44C2DD81234DCE29C3F5AB2297A0");
        return list;
    }
}

