package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blanks.test.demo.utils.StringByteUtils;
import com.sunmi.pay.hardware.aidl.DeviceProvide;
import com.sunmi.pay.hardware.aidl.TransactionListener;
import com.sunmi.pay.hardware.aidl.bean.AID;
import com.sunmi.pay.hardware.aidl.bean.RID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xurong on 2017/1/14.
 */

public class CRUDAidRidActivity extends Activity {
    private TextView info_tv;
    private TransactionCallback mCallback = new TransactionCallback();
    private DeviceProvide deviceProvide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_aidrid);
        info_tv = (TextView) findViewById(R.id.info_tv);
        deviceProvide = MyApplication.deviceProvide;
        if (deviceProvide != null) {
            try {
                deviceProvide.registerTransactionCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -2:
                    info_tv.setText(getText(R.string.tip));
                    break;
                case -1:
                    info_tv.setText(getText(R.string.unconnected));
                    break;
                case 0:
                    info_tv.setText(getText(R.string.connected));
                    break;
                case 1:
                    if ((long) msg.obj == -1) {
                        info_tv.setText(getText(R.string.set_aid_failed));
                    } else {
                        info_tv.setText(getText(R.string.set_aid_success));
                    }
                    break;
                case 2:
                    if ((long) msg.obj == -1) {
                        info_tv.setText(getText(R.string.set_rid_failed));
                    } else {
                        info_tv.setText(getText(R.string.set_rid_success));
                    }
                    break;

                case 3:
                    if ((boolean) msg.obj) {
                        info_tv.setText(getText(R.string.AID_delete_success));
                    } else {
                        info_tv.setText(getText(R.string.AID_delete_fail));
                    }
                    break;
                case 4:
                    if ((boolean) msg.obj) {
                        info_tv.setText(getText(R.string.RID_delete_success));
                    } else {
                        info_tv.setText(getText(R.string.RID_delete_fail));
                    }
                    break;
                case 5:
                    List<RID> ridList = (ArrayList) msg.obj;
                    if (ridList != null && !ridList.isEmpty()) {
                        StringBuffer sb = new StringBuffer();
                        for (RID rid : ridList) {
                            sb.append("rid_9F06==>").append(rid.rid_9F06).append("\n");
                            sb.append("rid_9F22==>").append(rid.rid_9F22).append("\n");
                            sb.append("rid_DF05==>").append(rid.rid_DF05).append("\n");
                            sb.append("rid_DF06==>").append(rid.rid_DF06).append("\n");
                            sb.append("rid_DF07==>").append(rid.rid_DF07).append("\n");
                            sb.append("rid_DF02==>").append(rid.rid_DF02).append("\n");
                            sb.append("rid_DF04==>").append(rid.rid_DF04).append("\n");
                            sb.append("rid_DF03==>").append(rid.rid_DF03).append("\n\n\n");
                        }
                        info_tv.setText(getText(R.string.check_all_aid_success) + "\n" + sb);
                    } else {
                        info_tv.setText(getText(R.string.no_data));
                    }
                    break;
                case 6:
                    List<AID> aidList = (ArrayList) msg.obj;
                    if (aidList != null && !aidList.isEmpty()) {
                        StringBuffer sb = new StringBuffer();
                        for (AID aid : aidList) {
                            sb.append("rid_9F06==>").append(aid.aid_9F06).append("\n");
                            sb.append("aid_DF01==>").append(aid.aid_DF01).append("\n");
                            sb.append("aid_9F09==>").append(aid.aid_9F09).append("\n");
                            sb.append("aid_DF11==>").append(aid.aid_DF11).append("\n");
                            sb.append("aid_DF12==>").append(aid.aid_DF12).append("\n");
                            sb.append("aid_DF13==>").append(aid.aid_DF13).append("\n");
                            sb.append("aid_DF15==>").append(aid.aid_DF15).append("\n");
                            sb.append("aid_DF16==>").append(aid.aid_DF16).append("\n");
                            sb.append("aid_DF17==>").append(aid.aid_DF17).append("\n");
                            sb.append("aid_DF14==>").append(aid.aid_DF14).append("\n");
                            sb.append("aid_DF18==>").append(aid.aid_DF18).append("\n");
                            sb.append("aid_9F7B==>").append(aid.aid_9F7B).append("\n");
                            sb.append("aid_DF19==>").append(aid.aid_DF19).append("\n");
                            sb.append("aid_DF20==>").append(aid.aid_DF20).append("\n");
                            sb.append("aid_DF21==>").append(aid.aid_DF21).append("\n\n\n");
                        }
                        info_tv.setText("查看全部AID成功！返回数据：\n" + sb);
                    } else {
                        info_tv.setText("没有记录！返回数据：null");
                    }
                    break;
                default:
                    break;
            }
        }
    };


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
        list.add("9F0605A0000003339F220103DF05083230323431323331DF060101DF070101DF0281B0B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33DDF040103DF031487F0CD7C0E86F38F89A66F8C47071A8B88586F26");
        return list;
    }

    /**
     * 设置一组AID
     *
     * @param view
     */
    public void setAID(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }
        long l = -1;
        try {
            for (String aid : getAIDList()) {
                l = deviceProvide.getEMVProvider().saveAID(StringByteUtils.HexString2Bytes(aid));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mHandler.obtainMessage(1, l).sendToTarget();
    }

    /**
     * 设置一组RID
     *
     * @param view
     */
    public void setRID(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }
        long l = -1;
        try {
            for (String rid : getRIDList()) {
                l = deviceProvide.getEMVProvider().saveRID(StringByteUtils.HexString2Bytes(rid));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mHandler.obtainMessage(2, l).sendToTarget();
    }

    /**
     * 删除全部AID
     *
     * @param view
     */
    public void deleteAllAID(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }
        boolean b = false;
        try {
            b = deviceProvide.getEMVProvider().deleteAllAID();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mHandler.obtainMessage(3, b).sendToTarget();

    }

    /**
     * 删除全部RID
     *
     * @param view
     */
    public void deleteAllRID(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }

        boolean b = false;
        try {
            b = deviceProvide.getEMVProvider().deleteAllRID();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mHandler.obtainMessage(4, b).sendToTarget();

    }

    /**
     * 查看RID列表
     *
     * @param view
     */
    public void showRIDList(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }

        List<RID> list;
        try {
            list = deviceProvide.getEMVProvider().getAllRID();
            if (list != null) {
                mHandler.obtainMessage(5, list).sendToTarget();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查看AID列表
     *
     * @param view
     */
    public void showAIDList(View view) {
        if (deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }
        List<AID> list;
        try {
            list = deviceProvide.getEMVProvider().getAllAID();
            if (list != null) {
                mHandler.obtainMessage(6, list).sendToTarget();
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
