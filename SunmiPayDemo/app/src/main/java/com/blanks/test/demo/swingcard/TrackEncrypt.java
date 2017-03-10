package com.blanks.test.demo.swingcard;

import com.blanks.test.demo.MyApplication;
import com.blanks.test.demo.utils.StringByteUtils;

/**
 * Created by xinle on 1/19/17.
 */

public class TrackEncrypt {


    public static String trackEncrypt(String track) {
        String _track = new String(track.replaceAll("=", "D"));
        byte[] track2BytesReturn = new byte[8];
        int TDKIndex = 71;
        byte[] keyIndex = new byte[2];
        keyIndex[0] = (byte)TDKIndex;
        keyIndex[1] = 5;
        try{
        if(16 == getTrackSubString(_track).length()){
            byte[] track2Bytes = StringByteUtils.hexStringToBytes(getTrackSubString(_track));
            int track2EncryptStatus = MyApplication.deviceProvide.getSecurityProvider().dataEncrypt(keyIndex, (byte) 3, track2Bytes, track2Bytes.length, track2BytesReturn);
            if (8 == track2EncryptStatus) {
                _track = insert(_track, StringByteUtils.bytesToHexString(track2BytesReturn));
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return _track;
    }


    private static String getTrackSubString(String s) {
        return s.length() >= 17 ? s.substring(s.length() - 17, s.length() - 17 + 16) : "";
    }


    private static String insert(String s, String s2) {
        return s.substring(0, s.length() - 17) + s2 + s.substring(s.length() - 17 + 16, s.length());
    }

}

