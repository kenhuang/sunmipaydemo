package com.blanks.test.demo.swingcard;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanks.test.demo.R;

/**
 * @author KenMa on 2017/1/9.
 */
public class FixPasswordKeyboard extends LinearLayout {

    public FixPasswordKeyboard(Context context) {
        this(context, null);
    }

    public FixPasswordKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixPasswordKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private TextView key_0, key_1, key_2;
    private TextView key_3, key_4, key_5;
    private TextView key_6, key_7, key_8;
    private TextView key_9, key_ok;
    private RelativeLayout key_delete;

    private void initView(Context context) {
        inflate(context, R.layout.view_fix_password_keyboard, this);
        key_0 = (TextView) findViewById(R.id.text__0);
        key_1 = (TextView) findViewById(R.id.text__1);
        key_2 = (TextView) findViewById(R.id.text__2);
        key_3 = (TextView) findViewById(R.id.text__3);
        key_4 = (TextView) findViewById(R.id.text__4);
        key_5 = (TextView) findViewById(R.id.text__5);
        key_6 = (TextView) findViewById(R.id.text__6);
        key_7 = (TextView) findViewById(R.id.text__7);
        key_8 = (TextView) findViewById(R.id.text__8);
        key_9 = (TextView) findViewById(R.id.text__9);
        key_ok = (TextView) findViewById(R.id.key__ok);
        key_delete = (RelativeLayout) findViewById(R.id.key__delete);
    }

    public void setKeyBoard(String keys) {
        if (TextUtils.isEmpty(keys) || keys.length() != 10) return;
        key_0.setText(keys.substring(0, 1));
        key_1.setText(keys.substring(1, 2));
        key_2.setText(keys.substring(2, 3));
        key_3.setText(keys.substring(3, 4));
        key_4.setText(keys.substring(4, 5));
        key_5.setText(keys.substring(5, 6));
        key_6.setText(keys.substring(6, 7));
        key_7.setText(keys.substring(7, 8));
        key_8.setText(keys.substring(8, 9));
        key_9.setText(keys.substring(9, 10));
    }

    public TextView getKey_0() {
        return key_0;
    }

}
