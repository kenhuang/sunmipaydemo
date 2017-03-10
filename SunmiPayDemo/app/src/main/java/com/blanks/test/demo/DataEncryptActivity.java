package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 * Created by Xiho on 2017/1/14.
 * 数据加密
 */

public class DataEncryptActivity extends Activity {

    private static final String TAG = "DataEncryptActivity";
    private TextView mTvResult, mTv1, mTv2, mTv3, mTv4, mTv5;

    private EditText mEditKeyIndex, mEditEncryptType, mEditPlainText, mEditKeyIndexType,mEditPlainTextLength;

    private String ciphertext;  //加密结果

    String inputStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_encrypt);
        initView();
    }

    private void initView() {
        mEditKeyIndex = (EditText) findViewById(R.id.key_index);
        mEditKeyIndexType = (EditText) findViewById(R.id.key_index_type);
        mEditEncryptType = (EditText) findViewById(R.id.encrypt_type);
        mEditPlainText = (EditText) findViewById(R.id.plaintext);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mEditPlainTextLength = (EditText) findViewById(R.id.plaintext_length);

        mTv1 = (TextView) findViewById(R.id.tv_key1);
        mTv2 = (TextView) findViewById(R.id.tv_key2);
        mTv3 = (TextView) findViewById(R.id.tv_key3);
        mTv4 = (TextView) findViewById(R.id.tv_key4);
        mTv5 = (TextView) findViewById(R.id.tv_key5);

        mEditKeyIndex.addTextChangedListener(UIUtils.getNumTextWatcher(mTv1));
        mEditKeyIndexType.addTextChangedListener(UIUtils.getNumTextWatcher(mTv2));
        mEditEncryptType.addTextChangedListener(UIUtils.getNumTextWatcher(mTv3));
        mEditPlainText.addTextChangedListener(UIUtils.getHexTextWatcher(mTv4));
        mEditPlainTextLength.addTextChangedListener(UIUtils.getNumTextWatcher(mTv5));

    }

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mTvResult.setText(inputStr +"\n"+getText(R.string.return_data) + msg.arg1+"\n"+getText(R.string.encryption_result)+msg.obj);
                    break;
                case -1:
                    String error = (String) msg.obj;
                    mTvResult.setText("error:"+error);
                    break;
                 }
            return false;
        }
    });



    public void dataEncrypt(View v) {
        String keyIndex = mEditKeyIndex.getText().toString().trim();//密钥索引
        String keyIndexType = mEditKeyIndexType.getText().toString().trim();//密钥类型索引
        String encryType = mEditEncryptType.getText().toString().trim();//加密类型
        String plainText = mEditPlainText.getText().toString().trim();//待加密的明文数据
        String plainTextLength = mEditPlainTextLength.getText().toString().trim();//长度

        if (MyApplication.deviceProvide == null) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.server_no_init), Toast.LENGTH_SHORT).show();
            return;
        }
            try {
                byte[] keyIndexBytes = new byte[2];
                if(TextUtils.isEmpty(keyIndex)){
                    Toast.makeText(this,getResources().getString(R.string.secret_key_index),Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(keyIndexType)){
                    Toast.makeText(this,getResources().getString(R.string.secret_key_type_index),Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(encryType)){
                    Toast.makeText(this,getResources().getString(R.string.secret_key_type),Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(plainText)){
                    Toast.makeText(this,getResources().getString(R.string.no_encryption_data),Toast.LENGTH_LONG).show();
                } else {
                    keyIndexBytes[0] = Byte.parseByte(keyIndex, 10);
                    keyIndexBytes[1] = Byte.parseByte(keyIndexType, 10);
                    byte type = Byte.parseByte(encryType, 10);

                    byte[] plainTextBytes = StringByteUtils.HexString2Bytes(plainText);
//                    int b1Length = plainTextBytes.length;

                    //待加密的明文数据长度
                    int plainLength = Integer.parseInt(plainTextLength);

                    //加密的结果
                    byte[] outArr = new byte[32];
                    String str = "";
                    for(int idx=0; idx < 8; idx++){
                        str += String.format("%02X", plainTextBytes[idx]);
                    }
                    Log.e(TAG, "Source: " + str);

                    Log.e(TAG, "输入: keyIndexArr:"+ Arrays.toString(keyIndexBytes)+" encryptType:"+type + " key arr:"+Arrays.toString(plainTextBytes)+" length:"+plainLength);
                    int result = MyApplication.deviceProvide.getSecurityProvider().dataEncrypt(keyIndexBytes, type, plainTextBytes, plainLength, outArr);
                    Log.e(TAG, "输出:"+"result:"+result+" outArr:"+Arrays.toString(outArr));
                    str = "";
                    for(int idx=0; idx < 8; idx++){
                        str += String.format("%02X", outArr[idx]);
                    }
                    Log.e(TAG, "Encrypt: " + str);
                    inputStr = getText(R.string.input)+"keyIndexArr:"+ Arrays.toString(keyIndexBytes)+" encryptType:"+type + " key arr:"+Arrays.toString(plainTextBytes)+" length:"+plainLength;
                    Message msg= new Message();
                    msg.what=1;
                    String outStr = StringByteUtils.bytesToHexString(outArr);
                    msg.obj = StringByteUtils.hexStringFotmat(outStr);
                    msg.arg1 =result;
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message message = mHandler.obtainMessage();
                message.obj = e.getMessage();
                message.what = -1;
                mHandler.sendMessage(message);
            }

    }


}
