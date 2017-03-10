package com.blanks.test.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blanks.test.demo.utils.StringByteUtils;
import com.blanks.test.demo.widget.ProcessResult;

public class AuthorizationProcessActivity extends Activity {
    private TextView show;
    private EditText tagIn;

    private StringBuilder sb=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization_process);
        tagIn = (EditText) findViewById(R.id.tagIn);
        show = (TextView) findViewById(R.id.show);
    }

    public void authorizationProcess(View view) {
        if (MyApplication.deviceProvide == null) {
            show.setText(R.string.server_connection_failed);
            return;
        }
        String tagInStr = tagIn.getText().toString();
        if (TextUtils.isEmpty(tagInStr)) {
            show.setText(R.string.input_cannot_be_null);
            return;
        }
        byte[] resp = StringByteUtils.HexString2Bytes(tagInStr);
        try {
            byte[] byrecv = new byte[1024];
            int ret = MyApplication.deviceProvide.getEMVProvider().authorizationProcess(true, resp, resp.length, byrecv);
            if (ret > 0) {
                sb.append("return "+ret+"--"+ProcessResult.getCode(byrecv[1]));
                show.setText(sb.toString());
            }
            sb.append(byrecv.toString());
            show.setText(sb.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
