package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blanks.test.demo.utils.StringByteUtils;
import com.blanks.test.demo.utils.UIUtils;

/**
 * Created by bps .
 */

public class UpdatePINKeyActivity extends Activity implements View.OnClickListener {

    private EditText mEtKey;
    private EditText mEtLength;
    private Button mBtn;
    private TextView mTvResult;
    private EditText mEtType;
    private TextView mTvKey;
    private EditText mEtIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pin);
        initView();
    }

    private void initView() {
        mEtType = (EditText) findViewById(R.id.et_type);
        mEtKey = (EditText) findViewById(R.id.et_key);
        mEtLength = (EditText) findViewById(R.id.et_length);
        mBtn = (Button) findViewById(R.id.btn_ok);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mBtn.setOnClickListener(this);
        mTvKey = (TextView) findViewById(R.id.tv_key);
        mEtKey.addTextChangedListener(UIUtils.getHexTextWatcher(mTvKey));
        mEtIndex = (EditText) findViewById(R.id.et_index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                String type = mEtType.getText().toString().trim();
                int encryptType = Integer.parseInt(type);
                String key = mEtKey.getText().toString().trim();
                byte[] bytes = StringByteUtils.HexString2Bytes(key);
                String length = mEtLength.getText().toString().trim();
                int keyLength = 0;
                if (!TextUtils.isEmpty(length)) {
                    keyLength = Integer.parseInt(length);
                }
                String keyIndex = mEtIndex.getText().toString().trim();
                byte[] bytekeyIndex = new byte[2];
                bytekeyIndex[0] = (byte) Integer.parseInt(keyIndex);
                bytekeyIndex[1] = 5;
                bytekeyIndex[2] = 0;
                try {
                    MyApplication.deviceProvide.getSecurityProvider().saveKey(bytekeyIndex, (byte) encryptType, bytes, bytes.length);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
