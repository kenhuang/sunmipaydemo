package com.blanks.test.demo.swingcard;

import java.io.Serializable;

/**
 * 磁条卡相关信息
 * Created by xurong on 2017/2/12.
 */

public class MAGCardInfo implements Serializable {

    //卡片二磁
    private String track2;

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }
}
