package com.blanks.test.demo.swingcard;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class TLVUtils {


    /**
     * 将16进制字符串转换为TLV对象MAP
     *
     * @param hexString
     * @return
     */
    public static Map<String, TLV> builderTLVMap(String hexString) {

        //TLV文档连接 http://wenku.baidu.com/view/b31b26a13186bceb18e8bb53.html?re=view&qq-pf-to=pcqq.c2c
        Map<String, TLV> tlvs = new HashMap<String, TLV>();

        int position = 0;
        while (position < hexString.length()) {
            String _hexTag = getTag(hexString, position);
            if (_hexTag.equals("00")) {
                break;
            }
            position += _hexTag.length();

            LPositon l_position = getLengthAndPosition(hexString, position);

            int _vl = l_position.get_vL();
            position = l_position.get_position();
            String _value = hexString.substring(position, position + _vl * 2);
            position = position + _value.length();
            Log.e("TLV-builderTLVMap", _hexTag + ": " + _value);
            if (tlvs.get(_hexTag) == null) {
//                Log.e("TLV-builderTLVMap","true");
                tlvs.put(_hexTag, new TLV(_hexTag, _vl, _value));
            }
        }

        return tlvs;
    }

    /**
     * 返回最后的Value的长度
     *
     * @param hexString
     * @param position
     * @return
     */
    private static LPositon getLengthAndPosition(String hexString, int position) {
        String firstByteString = hexString.substring(position, position + 2);
        int i = Integer.parseInt(firstByteString, 16);
        String hexLength = "";
        //Length域的编码比较简单,最多有四个字节, 
        // 如果第一个字节的最高位b8为0, b7~b1的值就是value域的长度. 
        // 如果b8为1, b7~b1的值指示了下面有几个子字节. 下面子字节的值就是value域的长度.
        if (((i >> 7) & 1) == 0) {
            hexLength = hexString.substring(position, position + 2);
            position = position + 2;
        } else {
            // 当最左侧的bit位为1的时候，取得后7bit的值，
            int _L_Len = i & 127;//127的二进制 0111 1111
            position = position + 2;
            hexLength = hexString.substring(position, position + _L_Len * 2);
            // position表示第一个字节，后面的表示有多少个字节来表示后面的Value值
            position = position + _L_Len * 2;
        }

        return new LPositon(Integer.parseInt(hexLength, 16), position);
    }

    /**
     * 取得子域Tag标签
     *
     * @param hexString
     * @param position
     * @return
     */
    private static String getTag(String hexString, int position) {
        try {
            String firstByte = hexString.substring(position, position + 2);
            int i = Integer.parseInt(firstByte, 16);
            // b5~b1如果全为1，则说明这个tag下面还有一个子字节，PBOC/EMV里的tag最多占两个字节
            if ((i & 0x1F) == 0x1F) {
                return hexString.substring(position, position + 4);
            } else {
                return hexString.substring(position, position + 2);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
