package com.blanks.test.demo.swingcard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.blanks.test.demo.R;

public abstract class ParentActivity extends Activity implements View.OnClickListener {

    public static final String ACTION_CLOSE_ACTIVITY = "payment.ACTION_CLOSE_ACTIVITY";
    public static final String ACTION_CHANGE_DIALOG_TEXT = "payment.ACTION_CHANGE_DIALOG_TEXT";
    public static final String ACTION_TEXT_EXTRA = "payment.ACTION_TEXT_EXTRA";

    public ThreadPoolManager mThreadPoolManager;

    private String clazzName;
    private LoadDialog mLoadDialog;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
        mThreadPoolManager = ThreadPoolManager.getInstance();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        // 新建intentFilter并给其action标签赋值
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CLOSE_ACTIVITY);
        filter.addAction(ACTION_CHANGE_DIALOG_TEXT);
        // 创建广播接收器实例,并注册.将其接收器与action标签进行绑定.
        localBroadcastManager.registerReceiver(receiver, filter);

        clazzName = getClass().getSimpleName();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            // 取消注册调用的是unregisterReceiver()方法,并传入接收器实例
            localBroadcastManager.unregisterReceiver(receiver);
        }
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
            mLoadDialog.onDestroy();
            mLoadDialog = null;
        }
    }

    /**
     * 设置通知栏背景色
     */
    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    public void showCanChangeLoadingDialog(String msg) {
        mLoadDialog = new LoadDialog(this, msg);
        try {
            mLoadDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissCanChangeDialog() {
        if (mLoadDialog != null) {
            try {
                mLoadDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void changeText(String msg) {
        if (mLoadDialog != null) {
            mLoadDialog.changeText(msg);
        }
    }

    public void sendCloseActivityBroadcast() {
        Intent intent = new Intent(ParentActivity.ACTION_CLOSE_ACTIVITY);
        localBroadcastManager.sendBroadcast(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_CHANGE_DIALOG_TEXT)) {
                String extra = intent.getStringExtra(ACTION_TEXT_EXTRA);
                changeText(extra);
            } else if (action.equals(ACTION_CLOSE_ACTIVITY)) {
                Log.i("nsz", "activity:" + clazzName);
                if (!clazzName.equals("OrdinaryUserHomePageActivity")) {
                    finish();
                }
            }
        }

    };

    public void runOnUI(Runnable run) {
        runOnUiThread(run);
    }

    public void executeTask(Runnable run) {
        mThreadPoolManager.executeTask(run);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastOnUI(int resID) {
        showToastOnUI(getString(resID));
    }

    public void showToastOnUI(final String msg) {

        runOnUI(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    public void onClick(View v) {

    }

    public boolean checkEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            showToastOnUI(getResources().getString(R.string.error_input));
        }
        return !TextUtils.isEmpty(str);
    }

    public void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public boolean checkInt(String str, int upValue, int downValue) {
        int value;
        try {
            value = Integer.parseInt(str);
            if (value > upValue || value < downValue) {
                showToastOnUI(getResources().getString(R.string.int_check_error));
                return false;
            } else
                return true;
        } catch (NumberFormatException e) {
            showToastOnUI(getResources().getString(R.string.error_input));
            return false;
        }
    }
}
