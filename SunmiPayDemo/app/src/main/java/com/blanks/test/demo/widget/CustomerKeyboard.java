package com.blanks.test.demo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blanks.test.demo.R;


public class CustomerKeyboard extends LinearLayout implements View.OnClickListener {
    private CustomerKeyboardClickListener mListener;


    View view;

    public CustomerKeyboard(Context context) {
        this(context, null);
    }

    public CustomerKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
	public CustomerKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = inflate(context, R.layout.ui_customer_keyboard, this);
        setChildViewOnclick(this);
    }


    /**
     * 设置键盘子View的点击事件
     */
    private void setChildViewOnclick(ViewGroup parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            // 不断的递归设置点击事件
            View view = parent.getChildAt(i);
            if (view instanceof ViewGroup) {
                setChildViewOnclick((ViewGroup) view);
                continue;
            }
            view.setOnClickListener(this);
        }
    }

    /**
     * 仅长度为10的0-9数字能设置
     *
     * @param str
     */
    public void initKeyBoard(String str) {
        if (str.length() == 10) {
            ((TextView) view.findViewById(R.id.tv_1)).setText(String.valueOf(str.charAt(0)));
            ((TextView) view.findViewById(R.id.tv_2)).setText(String.valueOf(str.charAt(1)));
            ((TextView) view.findViewById(R.id.tv_3)).setText(String.valueOf(str.charAt(2)));
            ((TextView) view.findViewById(R.id.tv_4)).setText(String.valueOf(str.charAt(3)));
            ((TextView) view.findViewById(R.id.tv_5)).setText(String.valueOf(str.charAt(4)));
            ((TextView) view.findViewById(R.id.tv_6)).setText(String.valueOf(str.charAt(5)));
            ((TextView) view.findViewById(R.id.tv_7)).setText(String.valueOf(str.charAt(6)));
            ((TextView) view.findViewById(R.id.tv_8)).setText(String.valueOf(str.charAt(7)));
            ((TextView) view.findViewById(R.id.tv_9)).setText(String.valueOf(str.charAt(8)));
            ((TextView) view.findViewById(R.id.tv_0)).setText(String.valueOf(str.charAt(9)));
        }
    }

    @Override
    public void onClick(View v) {
        View clickView = v;
        if (clickView instanceof TextView) {
            // 如果点击的是TextView
            String number = ((TextView) clickView).getText().toString();
            if (!TextUtils.isEmpty(number)) {
                if (mListener != null) {
                    // 回调
                    mListener.click(number);
                }
            }
        } else if (clickView instanceof ImageView) {
            if (mListener != null) {
                if (clickView.getId() == R.id.iv_deleted)
                    mListener.delete();
                else if (clickView.getId() == R.id.iv_confirm)
                    mListener.confirm();
            }
        }
    }

    /**
     * 设置键盘的点击回调监听
     */
    public void setOnCustomerKeyboardClickListener(CustomerKeyboardClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 点击键盘的回调监听
     */
    public interface CustomerKeyboardClickListener {
        public void click(String number);

        public void delete();

        public void confirm();
    }
}
