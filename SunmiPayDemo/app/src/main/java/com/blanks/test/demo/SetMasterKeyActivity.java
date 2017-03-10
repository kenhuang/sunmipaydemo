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

public class SetMasterKeyActivity extends Activity implements View.OnClickListener {

    private EditText mEtKey;
    private EditText mEtLength;
    private Button mBtn;
    private TextView mTvResult;

    private TextView mTvKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_master);
        initView();
    }


    private void initView() {
        mEtKey = (EditText) findViewById(R.id.et_key);
        mEtLength = (EditText) findViewById(R.id.et_length);
        mBtn = (Button) findViewById(R.id.btn_ok);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mBtn.setOnClickListener(this);
        mTvKey = (TextView) findViewById(R.id.tv_key);
        mEtKey.addTextChangedListener(UIUtils.getHexTextWatcher(mTvKey));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                String key = mEtKey.getText().toString().trim();
                byte[] bytes = StringByteUtils.HexString2Bytes(key);
                String length = mEtLength.getText().toString().trim();
                int keyLength = 0;
                if (!TextUtils.isEmpty(length)) {
                    keyLength =  Integer.parseInt(length);
                }
                try {
                    int i = MyApplication.deviceProvide.getSecurityProvider().setMasterKey(bytes, keyLength);
                    mTvResult.setText(i + "");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

}
