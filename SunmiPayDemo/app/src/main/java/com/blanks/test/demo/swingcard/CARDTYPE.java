package com.blanks.test.demo.swingcard;

/**
 * Created by admin on 2016/12/24.
 */

public enum CARDTYPE {
    MAGNETIC(1, "磁卡"),
    IC(2, "IC卡"),
    CONTACTLESS(4, "QuickPass");

    private int value;
    private String text;

    CARDTYPE(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public static CARDTYPE get(int value) {
        CARDTYPE[] elements = CARDTYPE.values();

        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getValue() == value) {
                return elements[i];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
