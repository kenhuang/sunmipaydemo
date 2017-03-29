package com.blanks.test.demo.swingcard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanks.test.demo.MyApplication;
import com.blanks.test.demo.R;
import com.blanks.test.demo.utils.StringByteUtils;
import com.blanks.test.demo.widget.ProcessResult;
import com.sunmi.pay.hardware.aidl.EMVProvider;
import com.sunmi.pay.hardware.aidl.PinPadProvider;
import com.sunmi.pay.hardware.aidl.TransactionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.blanks.test.demo.MyApplication.deviceProvide;

/**
 * 输入密码界面的基类
 *
 * @author Leeshenzhou on 2017/2/7.
 */
public class ConsumeInputPWDActivity extends Activity {
    private static final String TAG = "ConsumeInputPWDActivity";
    int amount = 1000;

    public ThreadPoolManager mThreadPoolManager;
    private LoadDialog mLoadDialog;
    private TransactionCallback transactionCallback = new TransactionCallback();

    private View backLayout;
    private PasswordInputView passwordInputView;
    private FixPasswordKeyboard fixPasswordkeyboard;
    EMVProvider mTransactionProcess;
    PinPadProvider mPinPadProvider;

    // 密码键盘单个item宽度
    private int width = 240;
    // 密码键盘单个item高度
    private int height = 130;

    // 密码键盘第一个button左顶点位置（绝对位置）
    private int[] keyboardCoordinate = new int[]{0, 661};
    // 取消键左顶点位置（绝对位置）
    private static int[] cancelCoordinate = new int[]{0, 48};

    // 线间隔
    private static int interval = 1;

    // 密码键盘单个item宽度
    private static int cancelWidth = 112;
    // 密码键盘单个item高度
    private static int cancelHeight = 112;

    // 第一次初始化键盘
    public boolean isInitKeyBoard;

    protected int userBehavior = -1; //记录用户点击键盘的哪个按键

    private static final int REQUEST_LAYOUT = 101;
    private static final int REQUEST_DISPLAY = 102;
    private static final int REQUEST_CLEAR = 103;
    private static final int REQUEST_DISMISS = 104;
    private static final int MESSAGE_TOAST = 105;
    private static final int TRANSACTION_FINISHED = 106;
    private static final int READFINISH_FINISHED = 107;
    public PayDetail payDetail = null;
    private MAGCardInfo magCardInfo = new MAGCardInfo();


    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_DISMISS:
                    // 4表示用户按下取消键，需要应用关闭密码键盘界面
                    // 5表示用户按下确定键，需要应用关闭密码键盘界面
                    // 6表示用户输入超时，需要应用关闭密码键盘界面
                    showCanChangeLoadingDialog(getString(R.string.base_pwd_connecting));
                    passwordInputView.setVisibility(View.GONE);
                    fixPasswordkeyboard.setVisibility(View.GONE);
                    break;
                case REQUEST_CLEAR:
                    // 3表示用户按下删除键，需要应用清除界面上的密码输入栏显示
                    passwordInputView.clearText();
                    break;
                case REQUEST_DISPLAY:
                    // 2表示用户按下一个数字键，需要应用在界面上的密码输入栏显示一个星号
                    passwordInputView.addText("*");
                    break;
                case REQUEST_LAYOUT:
                    dismissCanChangeDialog();
                    passwordInputView.setVisibility(View.VISIBLE);
                    fixPasswordkeyboard.setVisibility(View.VISIBLE);
                    String key = (String) msg.obj;
                    passwordInputView.clearText();
                    fixPasswordkeyboard.setKeepScreenOn(true);
                    fixPasswordkeyboard.setKeyBoard(key);
                    isInitKeyBoard = true;
                    break;
                case MESSAGE_TOAST:
                    Log.i(TAG, msg.obj.toString());
                    break;
                case TRANSACTION_FINISHED:
                    showToast(getString(R.string.consume_result_deal));
                    Log.i(TAG, msg.obj.toString());
                    finish();
                    break;
                case READFINISH_FINISHED:
                    showToast(getString(R.string.consume_result_success));
                    Log.i(TAG, msg.obj.toString());
                    finish();
                    break;
            }
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume_inputpwd);
        initView();

        mThreadPoolManager = ThreadPoolManager.getInstance();
        try {
            if (MyApplication.deviceProvide != null && (MyApplication.deviceProvide.getPinPadProvider() != null)) {
                MyApplication.deviceProvide.registerTransactionCallback(transactionCallback);
                mTransactionProcess = MyApplication.deviceProvide.getEMVProvider();
                mPinPadProvider = MyApplication.deviceProvide.getPinPadProvider();
            } else {
                ((MyApplication) getApplication()).conn();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        payDetail = (PayDetail) getIntent().getSerializableExtra("mPayDetail");
        backLayout =findViewById(R.id.back_rel);
        TextView tvMoney = (TextView) findViewById(R.id.tv_money);
        tvMoney.setText(String.format(getString(R.string.amount), amount / 100.00 + ""));
        TextView tvCard = (TextView) findViewById(R.id.tv_card_number);
        tvCard.setText("" + payDetail.getCardNo());
        passwordInputView = (PasswordInputView) findViewById(R.id.passwordInputView);
        fixPasswordkeyboard = (FixPasswordKeyboard) findViewById(R.id.fixPasswordKeyboard);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //添加默认AID和RID
        try {
            for (String aid : getAIDList()) {
                mTransactionProcess.saveAID(StringByteUtils.hexStringToBytes(aid));
            }

            for (String rid : getRIDList()) {
                mTransactionProcess.saveRID(StringByteUtils.hexStringToBytes(rid));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        startTransactionProcess();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        try {
            deviceProvide.unRegisterTransactionCallback(transactionCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TextView textView = fixPasswordkeyboard.getKey_0();
        textView.getLocationOnScreen(keyboardCoordinate);
        width = textView.getWidth();
        height = textView.getHeight();
        interval = 1;
        backLayout.getLocationOnScreen(cancelCoordinate);
        cancelWidth = backLayout.getWidth();
        cancelHeight = backLayout.getHeight();

        Log.i("", width + "==" + height);
        Log.i("", cancelWidth + "==" + cancelHeight);
        isInitKeyBoard = true;
    }


    /**
     * 获取卡号
     */
    public String getCardNumber() {
        return payDetail.CardNo;
    }

    private void requestKeyBoard() {
        if (!isInitKeyBoard) {
            PINThread pin = new PINThread();
            pin.start();
        }
    }

    private class PINThread extends Thread {

        @Override
        public void run() {
            byte[] panBlock = getPanBlock(getCardNumber());
            byte[] pinBlock = new byte[64];
            int ret = 0;
            try {
                ret = mPinPadProvider.inputOnlinePIN(panBlock, panBlock.length, pinBlock);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "ret:" + ret + " card number:" + getCardNumber());
            if (ret > 0) {
                // 输入密码,点击确定按钮,ret为密码长度
                payDetail.setPINCipher(Utils.hexEncode(pinBlock, 0, ret));
                mHandler.obtainMessage(READFINISH_FINISHED, 0, 0, new Object()).sendToTarget();
            } else if (ret == 0) {
                // 用户直接按下确定,为免密支付预留
                mHandler.obtainMessage(READFINISH_FINISHED, 0, 0, new Object()).sendToTarget();
            } else {
                setResult(ret);
                finish();
            }
        }

    }

    /**
     * 从卡号的倒数第二位开始截取
     */
    private byte[] getPanBlock(String cardNumber) {
        int length = cardNumber.length();
        String panStr = cardNumber.substring(length - 13, length - 1);
        return Utils.str2Bcd(panStr);
    }

    /**
     * 开始交易
     */
    private void startTransactionProcess() {
        int cardType = payDetail.getCardType();
        boolean easyProcess = false;//撤销类交易、磁条卡交易 走简易流程 开发者自行调整
        if (easyProcess) {
            requestKeyBoard();
            return;
        }

        if (cardType == CARDTYPE.IC.getValue() || cardType == CARDTYPE.CONTACTLESS.getValue()) {
            showCanChangeLoadingDialog(getString(R.string.base_loadding));
            passwordInputView.setVisibility(View.GONE);
            fixPasswordkeyboard.setVisibility(View.GONE);
            executeTask(new Runnable() {

                @Override
                public void run() {
                    chipCardProcess();
                }

            });
        } else {
            requestKeyBoard();
        }

    }

    /**
     * 芯片卡处理
     */
    private void chipCardProcess() {
        byte[] buffer = new byte[1024];
        int ret = 0;
        try {
            ret = mTransactionProcess.transactProcess(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // data[ret] is field 55.
        Log.d(TAG, "transactProcess ret:" + StringByteUtils.bytesToHexString(buffer));
        if (ret > 0 && userBehavior == 5) {
            // has return data.
            if (buffer[0] == (byte) 0xFB) {
                // Successful. package 8583.
                byte[] na = new byte[ret - 3];
                System.arraycopy(buffer, 3, na, 0, ret - 3);
                F55Data(na);
                mHandler.obtainMessage(READFINISH_FINISHED, 0, 0, ProcessResult.getResult(buffer[1])).sendToTarget();
//                handleTransactionProcess();
            } else if (buffer[0] == 0x00) {
                mHandler.obtainMessage(TRANSACTION_FINISHED, 0, 0, ProcessResult.getResult(buffer[1])).sendToTarget();
            } else {
                mHandler.obtainMessage(TRANSACTION_FINISHED, 0, 0, ProcessResult.EMVResult(buffer[0] & 0xFF)).sendToTarget();
            }
        } else {
            mHandler.obtainMessage(TRANSACTION_FINISHED, 0, 0, ProcessResult.EMVResult(buffer[0] & 0xFF)).sendToTarget();
        }
    }

    /**
     * 解析55域数据
     * @param na
     * @return
     */
    private String F55Data(byte[] na) {
        PayDetail payDetail = new PayDetail();
        String icData = Utils.Bcd2String(na);
        Map<String, TLV> tags = TLVUtils.builderTLVMap(icData);
        Log.d(TAG, "DF02:" + tags.toString());
        // 密码密文
        TLV df02 = tags.get("DF02");
        if (df02 != null) {
            Log.d(TAG, "pinblock:" + df02.getValue());
            payDetail.setPINCipher(df02.getValue());
        }

        // 主账号序号（针对IC 卡）
        String PANSubNo = "000";
        if (tags.get("5F34") != null) {
            PANSubNo = String.format(Locale.getDefault(), "%03d", Integer.parseInt(tags.get("5F34").getValue()));
        }
        payDetail.setCardSerialNo(PANSubNo);

        // AID
        if (tags.get("9F06") != null) {
            payDetail.setAID(tags.get("9F06").getValue());
        }

        // 应用标签
        if (tags.get("FF30") != null) {
            TLV ff30 = tags.get("FF30");
            payDetail.setAppLabel(ff30.getValue());
        }

        // 应用首选名称
        if (tags.get("FF31") != null) {
            TLV ff31 = tags.get("FF30");
            payDetail.setAppName(ff31.getValue());
        }

        // TVR
        if (tags.get("95") != null) {
            payDetail.setTVR(tags.get("95").getValue());
        }

        // TSI
        if (tags.get("9B") != null) {
            payDetail.setTSI(tags.get("9B").getValue());
        }

        // ATC
        if (tags.get("9F36") != null) {
            payDetail.setATC(tags.get("9F36").getValue());
        }

        // TC
        if (tags.get("9F26") != null) {
            payDetail.setTC(tags.get("9F26").getValue());
        }

        // 脚本处理结果
        if (tags.get("DF31") != null) {
            payDetail.setScriptResult(tags.get("DF31").getValue());
        }

        // 密文信息数据
        if (tags.get("9F27") != null) {
            payDetail.setCID(tags.get("9F27").getValue());
        }

        // 二磁
        if (tags.get("57") != null) {
            int idxF = tags.get("57").getValue().indexOf("F");
            if (idxF > 0) {
                magCardInfo.setTrack2(tags.get("57").getValue().substring(0, idxF));
            } else {
                magCardInfo.setTrack2(tags.get("57").getValue());
            }
            // 加密二磁道
            String track2Encrypt = TrackEncrypt.trackEncrypt(magCardInfo.getTrack2());
        } else {
            Log.e(TAG, "CARD_TYPE_NFC: no Track2");
        }

        // PAN
        TLV tlv5A = tags.get("5A");
        if (tlv5A != null && (!tlv5A.getValue().substring(0, 2).equals("00"))) {
            payDetail.setCardNo(tags.get("5A").getValue());
            // mHandler.obtainMessage(MESSAGE_TOAST, 0, 0, "卡号(IC):" + CardInformation.PAN).sendToTarget();
        } else if (magCardInfo.getTrack2() != null) {
            if (magCardInfo.getTrack2().length() >= 16) {
                int idx = magCardInfo.getTrack2().indexOf("D");
                payDetail.setCardNo(magCardInfo.getTrack2().substring(0, idx));
                // mHandler.obtainMessage(MESSAGE_TOAST, 0, 0, "卡号(QP):" + CardInformation.PAN).sendToTarget();
            }
        }

        // 卡有效期
        String Validity = "";
        if (tags.get("5F24") != null) {
            Validity = tags.get("5F24").getValue();
            if (Validity.equals("000000")) {
                int idx = magCardInfo.getTrack2().indexOf("D");
                Validity = magCardInfo.getTrack2().substring(idx + 1, idx + 5);
            }
        } else {
            if (magCardInfo.getTrack2() != null) {
                if (magCardInfo.getTrack2().length() >= 16) {
                    int idx = magCardInfo.getTrack2().indexOf("D");
                    Validity = magCardInfo.getTrack2().substring(idx + 1, idx + 5);
                }
            }
        }
        payDetail.EXPDate = Validity.substring(0, 4);

        // 55 域
        String F55 = "";
        if (tags.get("9F26") != null) {
            F55 += "9F2608" + tags.get("9F26").getValue();
        }
        if (tags.get("9F27") != null) {
            F55 += "9F2701" + tags.get("9F27").getValue();
        }
        if (tags.get("9F10") != null) {
            F55 += "9F10" + String.format("%02X", tags.get("9F10").getLength()) + tags.get("9F10").getValue();
        }
        if (tags.get("9F37") != null) {
            F55 += "9F3704" + tags.get("9F37").getValue();
        }
        if (tags.get("9F36") != null) {
            F55 += "9F3602" + tags.get("9F36").getValue();
        }
        if (tags.get("95") != null) {
            F55 += "9505" + tags.get("95").getValue();
        }
        if (tags.get("9A") != null) {
            F55 += "9A03" + tags.get("9A").getValue();
        }
        if (tags.get("9C") != null) {
            F55 += "9C01" + tags.get("9C").getValue();
        } else {
            F55 += "9C0100";
        }
        if (tags.get("9F02") != null) {
            F55 += "9F0206" + tags.get("9F02").getValue();
        }
        if (tags.get("5F2A") != null) {
            F55 += "5F2A02" + tags.get("5F2A").getValue();
        } else {
            F55 += "5F2A020156";
        }
        if (tags.get("82") != null) {
            F55 += "8202" + tags.get("82").getValue();
        }
        if (tags.get("9F1A") != null) {
            F55 += "9F1A02" + tags.get("9F1A").getValue();
        } else {
            F55 += "9F1A020156";
        }
        if (tags.get("9F03") != null) {
            F55 += "9F0306" + tags.get("9F03").getValue();
        }
        if (tags.get("9F33") != null) {
            F55 += "9F3303" + tags.get("9F33").getValue();
        } else {
            F55 += "9F330360F800";
        }
        if (tags.get("9F34") != null) {
            F55 += "9F3403" + tags.get("9F34").getValue();
        }
        if (tags.get("9F35") != null) {
            F55 += "9F3501" + tags.get("9F35").getValue();
        } else {
            F55 += "9F350121";
        }
        if (tags.get("9F1E") != null) {
            F55 += "9F1E08" + tags.get("9F1E").getValue();
        } else {
            F55 += "9F1E083730303030303034";
        }
        if (tags.get("84") != null) {
            F55 += "8408" + tags.get("84").getValue();
        } else {
            if (tags.get("9F06") != null) {
                F55 += "8408" + tags.get("9F06").getValue();
            } else {
                if (tags.get("4F") != null) {
                    F55 += "8408" + tags.get("4F").getValue();
                } else {
                    F55 += "8408A000000333010102";
                }
            }
        }
        if (tags.get("9F09") != null) {
            F55 += "9F0902" + tags.get("9F09").getValue();
        } else {
            F55 += "9F09020030";
        }
        if (tags.get("9F41") != null) {
            String str = String.format(Locale.getDefault(), "%02d", tags.get("9F41").getLength());
            F55 += "9F41" + str + tags.get("9F41").getValue();
        } else {
            String BatchNum = "000001";//使用POS当前批次号
            F55 += "9F4103" + String.format(Locale.getDefault(), "%06d", Integer.parseInt(BatchNum));
        }
        if (tags.get("9F63") != null) {
            F55 += "9F6310" + tags.get("9F63").getValue();
        } else {
            F55 += "9F631030313035303030300000000000000000";
        }
        Log.e(TAG, "field 55:" + F55);
        payDetail.setICC55(F55);
        return F55;
    }

    class TransactionCallback extends TransactionListener.Stub {

        @Override
        public IBinder asBinder() {
            return this;
        }

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
        public int onInputPIN(int type, byte[] data) {
            Log.e(TAG, "onInputPIN type: " + type);
            userBehavior = type;
            if (type == 1) {
                String keySerialNumber = "";
                for (int i = 0; i < 10; i++) {
                    keySerialNumber += String.format("%c", data[i]);
                }
                isInitKeyBoard = false;
                mHandler.obtainMessage(REQUEST_LAYOUT, 0, 0, keySerialNumber).sendToTarget();
                while (!isInitKeyBoard) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isInitKeyBoard = false;
                int index = 0;
                // start X
                data[index++] = (byte) ((keyboardCoordinate[0] >> 8) & 0xFF);
                data[index++] = (byte) ((keyboardCoordinate[0]) & 0xFF);
                // start Y
                data[index++] = (byte) ((keyboardCoordinate[1] >> 8) & 0xFF);
                data[index++] = (byte) ((keyboardCoordinate[1]) & 0xFF);
                // width
                data[index++] = (byte) ((width >> 8) & 0xFF);
                data[index++] = (byte) ((width) & 0xFF);
                // interval X
                data[index++] = (byte) ((interval >> 8) & 0xFF);
                data[index++] = (byte) ((interval) & 0xFF);
                // height
                data[index++] = (byte) ((height >> 8) & 0xFF);
                data[index++] = (byte) ((height) & 0xFF);
                // interval Y
                data[index++] = (byte) ((interval >> 8) & 0xFF);
                data[index++] = (byte) ((interval) & 0xFF);
                // cancel X
                data[index++] = (byte) ((cancelCoordinate[0] >> 8) & 0xFF);
                data[index++] = (byte) ((cancelCoordinate[0]) & 0xFF);
                // cancel Y
                data[index++] = (byte) ((cancelCoordinate[1] >> 8) & 0xFF);
                data[index++] = (byte) ((cancelCoordinate[1]) & 0xFF);
                // cancel width
                data[index++] = (byte) ((cancelWidth >> 8) & 0xFF);
                data[index++] = (byte) ((cancelWidth) & 0xFF);
                // cancel height
                data[index++] = (byte) ((cancelHeight >> 8) & 0xFF);
                data[index++] = (byte) ((cancelHeight) & 0xFF);

                data[index++] = 6;
                data[index++] = 6;
                data[index++] = 1;
                // data[index++] = 3;
                data[index++] = (byte) MyApplication.PIKIndex;
                data[index++] = 1;

                int timeout = 30;
                // timeout
                data[index++] = (byte) ((timeout >> 8) & 0xFF);
                data[index++] = (byte) ((timeout) & 0xFF);
            } else if (type == 2) {
                // 2表示用户按下一个数字键,需要应用在界面上的密码输入栏显示一个星号
                mHandler.obtainMessage(REQUEST_DISPLAY, 0, 0, 0).sendToTarget();
            } else if (type == 3) {
                // 3表示用户按下删除键,需要应用清除界面上的密码输入栏显示
                mHandler.obtainMessage(REQUEST_CLEAR, 0, 0, 0).sendToTarget();
            }
            // else if (type == 4) {
            // 4表示用户按下取消键,需要应用关闭密码键盘界面
            // mHandler.obtainMessage(REQUEST_DISMISS, 0, 0, 0).sendToTarget();
            // }
            else if (type == 5) {
                // 只有输入六位密码之后才有效或者不输入
                // 5表示用户按下确定键,需要应用关闭密码键盘界面
                mHandler.obtainMessage(REQUEST_DISMISS, 0, 0, 0).sendToTarget();
            }
            // else if (type == 6) {
            // 6表示用户输入超时,需要应用关闭密码键盘界面
            // mHandler.obtainMessage(REQUEST_DISMISS, 0, 0, 0).sendToTarget();
            // }
            return 0;
        }

        @Override
        public int onGetECC(byte[] bytes, int i, int i1, byte[] bytes1, byte[] bytes2) throws RemoteException {
            return 0;
        }

        @Override
        public int onGetTerminalTag(byte[] tag) {
            Log.e(TAG, "onGetTerminalTag()");
            byte[] tags;
            int cardType = payDetail.getCardType();
            if (cardType == CARDTYPE.IC.getValue()) {
                tags = Utils.hexDecode("9F3501219F3303E0E8C89F40050300C000005F280201569F1E0830303030303930359F4E14C9CFBAA3D0D0D5DCD0C5CFA2BCBCCAF5B9ABCBBEDF1906000000100000DF2006001000000000DF21060000000000019F6604360000805F2A0201569F1A020156");
            } else {
                tags = Utils.hexDecode("9F6604348000809F3501219F3303E0E8C89F40050300C000005F280201569F1E0830303030303930359F4E14C9CFBAA3D0D0D5DCD0C5CFA2BCBCCAF5B9ABCBBEDF140101DF1906000000100000DF2006001000000000DF21060000000000015F2A0201569F1A020156");
            }
            System.arraycopy(tags, 0, tag, 0, tags.length);
            return tags.length;
        }

        @Override
        public int onKernelMessage(int type, int messageID, byte[] messageData) throws RemoteException {
            if (Constant.EMVL2MSGID_TRACK2INFO == messageID) {
                final String PAN = new String(messageData);
                payDetail.setCardNo(PAN);
                Log.e(TAG, "卡号：" + PAN);
                showToastOnUI("onKernelMessage 卡号：" + PAN);
            } else if (Constant.EMVL2MSGID_REMOVECARD == messageID) {
                showToastOnUI("onKernelMessage 卡片信息已读取完成,请移开卡片。");
            }
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

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastOnUI(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        });
    }

    public void showCanChangeLoadingDialog(String msg) {
        mLoadDialog = new LoadDialog(this, msg);
        try {
            mLoadDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissCanChangeDialog() {
        if (mLoadDialog != null) {
            try {
                mLoadDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void executeTask(Runnable run) {
        mThreadPoolManager.executeTask(run);
    }
}
