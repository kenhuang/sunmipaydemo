package com.blanks.test.demo.swingcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blanks.test.demo.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;


/**
 * @author longtao.li on 2016/11/19.
 */
public class PasswordInputView extends TextView {

    public interface InputCallback {
        /**
         * 密码正确
         */
        void onSuccess();
    }

    private InputCallback mInputCallback;

    public void setInputCallback(InputCallback callback) {
        this.mInputCallback = callback;
    }

    private int textLength = 0;
    private int borderColor;

    private int passwordLength = 6;
    private int passwordColor;

    private Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);

    private final int defaultContMargin = 1;
    private final int defaultSplitLineWidth = 1;

    private final int space = 10;

    Bitmap passwordInputed;
    Bitmap passwordDef;


    //缓存输入的密码
    private StringBuilder inputSB;

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final Resources res = getResources();
        passwordInputed = BitmapFactory.decodeResource(res, R.drawable.password_inputed);
        passwordDef = BitmapFactory.decodeResource(res, R.drawable.password_def);

        passwordColor = getResources().getColor(R.color.FD5A52);
        passwordPaint.setTextSize(35);
        passwordPaint.setColor(passwordColor);

        borderColor = getResources().getColor(R.color.CE6E6E6);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        borderPaint.setColor(borderColor);

        inputSB = new StringBuilder();
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth() - space * passwordLength;
        int height = getHeight();
        int unit = width / passwordLength;

        RectF rectF = new RectF();
        int _space = 0;
        for (int i = 1; i <= passwordLength; i++) {
            if (i <= textLength) {
                borderColor = getResources().getColor(R.color.white);
                borderPaint.setColor(borderColor);
            } else {
                borderColor = getResources().getColor(R.color.CE6E6E6);
                borderPaint.setColor(borderColor);
            }
            float xLeft = 0;
            rectF.left = xLeft;
            rectF.top = 0;
            rectF.right = xLeft + unit;
            rectF.bottom = 118;
            if (i > 1) {
                _space = space;
                xLeft = width * (i - 1) / passwordLength + _space * (i - 1);
                rectF.left = xLeft;
                rectF.top = 0;
                rectF.right = xLeft + unit;
                rectF.bottom = 118;
            }
//            canvas.drawLine(x, 0, x, height, borderPaint);
            canvas.drawRoundRect(rectF, 10, 10, borderPaint);
        }

        // 密码
        float cx, cy = height / 2;
        float half = width / passwordLength / 2;
        for (int i = 0; i < textLength; i++) {
            int __space = space;
            if (i == 0) {
                __space = 0;
            }
            cx = width * i / passwordLength + half + __space * i;
            canvas.drawText("*", cx - 5, cy + 15, passwordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (inputSB == null)
            return;
        this.textLength = text.toString().length();
        if (this.textLength == passwordLength) {
            //输满了6个字符
            if (mInputCallback != null) {
                mInputCallback.onSuccess();
            }
        }
        invalidate();
    }

    /**
     * 输入一个字符
     *
     * @param text
     */
    public synchronized void addText(String text) {
        if (inputSB.length() == passwordLength) {
            return;
        }
        inputSB.append(text);
        setText(inputSB.toString());
    }

    /**
     * 删除所有
     */
    public void clearText() {
        if (inputSB.length() == 0)
            return;
        inputSB.delete(0, inputSB.length());
        setText(inputSB.toString());
    }

    /**
     * 删除最后一个密码
     */
    public void delLast() {
        if (inputSB.length() == 0)
            return;
        inputSB.deleteCharAt(inputSB.length() - 1);
        setText(inputSB.toString());
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }


    public void setBorderWidth(float borderWidth) {
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }


    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public boolean isCompleteInput() {
        boolean b = false;
        if (textLength == 0 || textLength == passwordLength) {
            b = true;
        }
        return b;
    }

    public String getPassword() {
        if (inputSB == null) {
            return null;
        }
        return inputSB.toString();
    }

}
