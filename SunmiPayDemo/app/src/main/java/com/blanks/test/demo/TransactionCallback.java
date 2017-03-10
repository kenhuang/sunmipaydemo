package com.blanks.test.demo;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.blanks.test.demo.bean.CardInfo;
import com.blanks.test.demo.swingcard.CARDTYPE;
import com.blanks.test.demo.swingcard.Utils;
import com.sunmi.pay.hardware.aidl.TransactionListener;

import java.util.Map;

/**
 * Created by xinle on 1/14/17.
 */


public class TransactionCallback extends TransactionListener.Stub {

    private static final String TAG = "TransactionCallback";
    private Handler mHandler;


    public TransactionCallback(Handler handler) {
        this.mHandler = handler;
    }


    @Override
    public void onWaitingForCardSwipe() throws RemoteException {

    }

    @Override
    public int onCheckCardCompleted(int cardType, int status, String PAN, String track2, Map additional) throws RemoteException {

        Message message = Message.obtain();
        if (status == 0) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.cardType = cardType;
            cardInfo.cardStatus = status;
            cardInfo.PAN = PAN;
            cardInfo.track2 = track2;
            cardInfo.hashMap = additional;
            message.obj = cardInfo;
            mHandler.sendMessage(message);
            Log.e(TAG, "onCheckCardCompleted: cardType:"+cardType + " status:"+status +" PAN:"+PAN + " track2:"+track2 );
        } else {
            message.what = 1;
            mHandler.sendMessage(message);
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
        Log.e(TAG, "onGetTerminalTag()");
        byte[] tags;
        int cardType = 4;
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
