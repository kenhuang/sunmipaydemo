package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blanks.test.demo.utils.StringByteUtils;
import com.blanks.test.demo.utils.UIUtils;

import java.util.Arrays;

/**
 * Created by LJ on 2017/1/14.
 */
public class SaveKeyActivity extends Activity {
    private TextView tvSaveKeyResult, mTv1, mTv2, mTv3, mTv4, mTv5, mTv6;
    private final String TAG = "SaveKeyActivity";
    EditText et_input_key_index, et_input_key_type, et_input_deciphering_key_index, et_input_encryptType, et_input_mkey,et_input_length;
    int key_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_key);
        init();
        tvSaveKeyResult = (TextView) findViewById(R.id.tv_save_key_result);
        et_input_key_index = (EditText) findViewById(R.id.et_input_key_index);
        et_input_key_type = (EditText) findViewById(R.id.et_input_key_type);
        et_input_deciphering_key_index = (EditText) findViewById(R.id.et_input_deciphering_key_index);
        et_input_encryptType = (EditText) findViewById(R.id.et_input_encryptType);
        et_input_mkey = (EditText) findViewById(R.id.et_input_mkey);
        et_input_length = (EditText) findViewById(R.id.et_input_length);

        mTv1 = (TextView) findViewById(R.id.tv_key1);
        mTv2 = (TextView) findViewById(R.id.tv_key2);
        mTv3 = (TextView) findViewById(R.id.tv_key3);
        mTv4 = (TextView) findViewById(R.id.tv_key4);
        mTv5 = (TextView) findViewById(R.id.tv_key5);
        mTv6 = (TextView) findViewById(R.id.tv_key6);

        et_input_key_index.addTextChangedListener(UIUtils.getNumTextWatcher(mTv1));
        et_input_key_type.addTextChangedListener(UIUtils.getNumTextWatcher(mTv2));
        et_input_deciphering_key_index.addTextChangedListener(UIUtils.getNumTextWatcher(mTv3));
        et_input_encryptType.addTextChangedListener(UIUtils.getNumTextWatcher(mTv4));
        et_input_mkey.addTextChangedListener(UIUtils.getHexTextWatcher(mTv5));
        et_input_length.addTextChangedListener(UIUtils.getNumTextWatcher(mTv6));
    }

    public void startSaveKey(View view) {
        byte[] keyIndex = new byte[3];
        byte[] key;
        byte encryptType = 0;
        if (TextUtils.isEmpty(et_input_key_index.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.secret_key_index), Toast.LENGTH_LONG).show();
        } else {
            keyIndex[0] = Byte.parseByte(et_input_key_index.getText().toString(), 10);
        }
        if (TextUtils.isEmpty(et_input_key_type.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.secret_key_type), Toast.LENGTH_LONG).show();
        } else {
            keyIndex[1] = Byte.parseByte(et_input_key_type.getText().toString(), 10);
        }
        if (TextUtils.isEmpty(et_input_deciphering_key_index.getText().toString())) {
            Toast.makeText(this,getResources().getString(R.string.secret_key_type_index), Toast.LENGTH_LONG).show();
        } else {
            keyIndex[2] = Byte.parseByte(et_input_deciphering_key_index.getText().toString(), 10);
        }

        if(TextUtils.isEmpty(et_input_encryptType.getText().toString())){
            Toast.makeText(this, getResources().getString(R.string.input_encryption_type), Toast.LENGTH_LONG).show();
        }else{
           encryptType = Byte.parseByte(et_input_encryptType.getText().toString(), 10);
        }
        if(TextUtils.isEmpty(et_input_mkey.getText().toString())){
            key = new byte[0];
        }else{
            key = StringByteUtils.HexString2Bytes(et_input_mkey.getText().toString());
        }


        key_length = Integer.parseInt(et_input_length.getText().toString());
//        String keyLengthStr = Integer.toHexString(key_length);
        if (connectPayService != null && connectPayService.getDeviceProvide() != null) {
            try {
                int result = MyApplication.deviceProvide.getSecurityProvider().saveKey(keyIndex, encryptType, key, key_length);
                Log.e(TAG, "savekey-- keyIndex arr:"+ Arrays.toString(keyIndex)+" encryptType:"+encryptType + " key arr:"+Arrays.toString(key)+" key_length:"+key_length);
                tvSaveKeyResult.setText(getText(R.string.input)+"keyIndex:"+ Arrays.toString(keyIndex)+" encryptType:"+encryptType + " key:"+Arrays.toString(key)+" keylength:"+key_length+"\n"
                        +getText(R.string.result)+":" + result);
                for(int i=0;i<key.length;i++){
                    Log.e(TAG,key[i]+"\n");
                }
                Log.e(TAG,key_length+"");
            } catch (RemoteException e) {
                e.printStackTrace();
                tvSaveKeyResult.setText("结果:" + e.toString());
            }
        }
    }

    private ConnectPayService connectPayService;

    public void init() {
        connectPayService = ConnectPayService.Init(this.getApplicationContext());
    }


}
