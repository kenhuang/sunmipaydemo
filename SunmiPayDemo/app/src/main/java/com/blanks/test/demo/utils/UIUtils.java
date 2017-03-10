package com.blanks.test.demo.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by tomcat on 2017/1/15.
 */
public class UIUtils {

    public static TextWatcher getHexTextWatcher(final TextView tagetTextView){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                byte[] bytes = StringByteUtils.HexString2Bytes(key);
                if(bytes.length>0){
                    String bytesToHexString = StringByteUtils.bytesToHexString(bytes);
                    String hexStringFotmat = StringByteUtils.hexStringFotmat(bytesToHexString);
                    tagetTextView.setText("hexStr:"+hexStringFotmat+"\nbyte arr:"+ Arrays.toString(bytes));
                }else {
                    tagetTextView.setText("");
                }
            }
        };
    }

    public static TextWatcher getNumTextWatcher(final TextView tagetTextView){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString();
                if(TextUtils.isEmpty(key)){
                    tagetTextView.setText("");
                    return;
                }
//                byte[] bytes = StringByteUtils.HexString2Bytes(key);
                try{
                    int intStr = Integer.parseInt(key);
                    byte[] bytes = new byte[]{(byte) intStr};
                    if(bytes.length>0){
                        String bytesToHexString = StringByteUtils.bytesToHexString(bytes);
                        String hexStringFotmat = StringByteUtils.hexStringFotmat(bytesToHexString);
                        tagetTextView.setText("hexStr:"+hexStringFotmat+"\nbyte arr:"+ Arrays.toString(bytes));
                    }else {
                        tagetTextView.setText("");
                    }
                }catch (Exception e){
                    tagetTextView.setText("非法数据.....");
                }

            }
        };
    }
}
