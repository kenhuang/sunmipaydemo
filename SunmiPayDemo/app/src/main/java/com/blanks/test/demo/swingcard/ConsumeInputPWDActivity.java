package com.blanks.test.demo.swingcard;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blanks.test.demo.R;

/**
 * Created by xurong on 2017/3/2.
 */

public class ConsumeInputPWDActivity extends BaseInputPasswordActivity{
    private PasswordInputView passwordInputView;
    private FixPasswordKeyboard fixPasswordkeyboard;
    private RelativeLayout backLayout;
    int amount = 1000 ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume_inputpwd);
        initView();
    }

    private void initView() {

        backLayout = (RelativeLayout) findViewById(R.id.back_rel);
        TextView tvMoney = (TextView) findViewById(R.id.tv_money);
        tvMoney.setText(String.format(getString(R.string.amount), amount/100.00 + ""));
        TextView tvCard = (TextView) findViewById(R.id.tv_card_number);
        tvCard.setText(""+payDetail.getCardNo());
        passwordInputView = (PasswordInputView) findViewById(R.id.passwordInputView);
        fixPasswordkeyboard = (FixPasswordKeyboard) findViewById(R.id.fixPasswordKeyboard);
    }


    @Override
    public void handleTransactionProcess() {
//
//        ConsumptionTask task = new ConsumptionTask(this) {
//
//            @Override
//            protected void onPostExecute(TradeData tradeData) {
//
//                dismissCanChangeDialog();
//
//                PayDetail pay = tradeData.getPayDetail();
//                String code = pay.getTradeAnswerCode();
//                Intent intent = new Intent();
//                if (code.equals("00")) {
//                    intent.setClass(ConsumeInputPWDActivity.this, WaitPrintActivity.class);
//                    intent.putExtra("type", StateView.StateType.PRINT);
//                    intent.putExtra(ResultActivity.EXTRA_TITLE, R.string.consume_im_title);
//                    intent.putExtra(ResultActivity.EXTRA_ICON, R.drawable.state_print_success);
//                    intent.putExtra(ResultActivity.EXTRA_TEXT_MESSAGE, R.string.print_success);
//                    intent.putExtra(ResultActivity.EXTRA_RESULT_MESSAGE, getString(R.string.keeping_credentials));
//                    intent.putExtra(ResultActivity.EXTRA_IS_SUCCESS, true);
//                } else {
//                    intent.setClass(ConsumeInputPWDActivity.this, ResultActivity.class);
//                    intent.putExtra(ResultActivity.EXTRA_TITLE, R.string.consume_im_title);
//                    intent.putExtra(ResultActivity.EXTRA_ICON, R.drawable.state_print_failed);
//                    intent.putExtra(ResultActivity.EXTRA_TEXT_MESSAGE, R.string.consume_result_deal);
//                    intent.putExtra(ResultActivity.EXTRA_RESULT_MESSAGE, ErrorCode.getCode(code));
//                    intent.putExtra(ResultActivity.EXTRA_IS_SUCCESS, false);
//                }
//                startActivity(intent);
//                finish();
//            }
//
//        };
//
//        task.execute(tradeData);
    }

    @Override
    public PasswordInputView getPasswordInputView() {
        return passwordInputView;
    }

    @Override
    public FixPasswordKeyboard getFixPasswordKeyboard() {
        return fixPasswordkeyboard;
    }

    @Override
    public View getBackLayout() {
        return backLayout;
    }

    @Override
    protected String getAmount() {
        return String.valueOf(amount);
    }
}
