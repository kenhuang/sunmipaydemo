package com.blanks.test.demo.swingcard;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.blanks.test.demo.R;

/**
 * @author Lee64 on 2017/2/23.
 */
public class LoadDialog extends Dialog {

    private TextView tvMessage;

    public LoadDialog(Context context, String text) {
        this(context, R.style.defaultDialogStyle, text);
    }

    public LoadDialog(Context context, int theme, String text) {
        super(context, theme);
        init(text);
    }

    private void init(String text) {
        setContentView(R.layout.dialog_loading);
        if (getWindow() != null) {
            // 居中
            getWindow().getAttributes().gravity = Gravity.CENTER;
        }
        // 点击空白不取消
        setCanceledOnTouchOutside(false);
        // 点击返回按钮不取消
        setCancelable(false);

        tvMessage = (TextView) findViewById(R.id.tv_msg);
        if (TextUtils.isEmpty(text)) {
            tvMessage.setText(getContext().getString(R.string.base_loadding));
        } else {
            tvMessage.setText(text);
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            tvMessage.setText((String) msg.obj);
        }

    };

    public void changeText(String msg) {
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.obj = msg;
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

}
