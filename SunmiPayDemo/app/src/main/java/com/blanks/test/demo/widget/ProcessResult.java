package com.blanks.test.demo.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */

public final class ProcessResult {
    static final Map errorcode = new HashMap<Integer, String>();
    static final Map offlineResult = new HashMap<Integer, String>();
    static final Map EMVResult = new HashMap<Integer, String>();

    public static String getCode(int code) {
        if(errorcode.isEmpty()) {
            initCode();
        }

        if(errorcode.containsKey(code)) {
            return (String) errorcode.get(code);
        } else {
            return String.valueOf(code);
        }
    }

    public static String getResult(int code) {
        if(offlineResult.isEmpty()) {
            initResult();
        }

        if(offlineResult.containsKey(code)) {
            return (String) offlineResult.get(code);
        } else {
            return String.valueOf(code);
        }
    }

    public static String EMVResult(int code) {
        if(EMVResult.isEmpty()) {
            initEMVResult();
        }

        if(EMVResult.containsKey(code)) {
            return (String) EMVResult.get(code);
        } else {
            return String.valueOf(code);
        }
    }

    private static void initCode() {
        errorcode.put(0, "交易拒绝");
        errorcode.put(1, "脱机交易成功");
        errorcode.put(2, "脱机交易拒绝");
        errorcode.put(3, "联机交易成功");
        errorcode.put(4, "联机交易拒绝");
        errorcode.put(5, "未知类型交易拒绝");
        errorcode.put(6, "未知类型交易成功");
    }

    private static void initResult() {
        offlineResult.put(0, "交易终止");
        offlineResult.put(1, "脱机交易成功");
        offlineResult.put(2, "脱机交易拒绝");
        offlineResult.put(3, "--");
        offlineResult.put(4, "--");
        offlineResult.put(5, "未知类型交易成功");
        offlineResult.put(6, "未知类型交易拒绝");
    }

    private static void initEMVResult() {
        EMVResult.put(0, "EMVERR_SUCCESS");
        EMVResult.put(1, "EMVERR_CANCEL");
        EMVResult.put(2, "EMVERR_DEVERR");
        EMVResult.put(3, "EMVERR_DATAERR");
        EMVResult.put(4, "EMVERR_OVERBUF");
        EMVResult.put(5, "EMVERR_NOTAG");
        EMVResult.put(6, "EMVERR_TAGUNPACK");
        EMVResult.put(7, "EMVERR_TAGLEN");
        EMVResult.put(8, "EMVERR_TAGNOLENBYTE");
        EMVResult.put(9, "EMVERR_TAGNOENOUGHDATA");
        EMVResult.put(10, "EMVERR_TAGNOBUF");
        EMVResult.put(11, "EMVERR_TAGDATAEXIST");
        EMVResult.put(12, "EMVERR_TAGNODATA");
        EMVResult.put(13, "EMVERR_CFGUPDATE");
        EMVResult.put(14, "EMVERR_CFGMISS");
        EMVResult.put(15, "EMVERR_CARDERR");
        EMVResult.put(16, "EMVERR_APDUFIRST");
        EMVResult.put(16, "EMVERR_APDUINVALIDINS");
        EMVResult.put(17, "EMVERR_APDUBADSW");
        EMVResult.put(18, "EMVERR_APDUINVALIDFILE");
        EMVResult.put(19, "EMVERR_APDUAUTHFAILED");
        EMVResult.put(20, "EMVERR_APDUPINFAILED");
        EMVResult.put(21, "EMVERR_APDUAUTHBLOCKED");
        EMVResult.put(22, "EMVERR_APDUINVALIDREFDATA");
        EMVResult.put(23, "EMVERR_APDUCONDITIONS");
        EMVResult.put(24, "EMVERR_APDUFUNCTIONNOTSUPPORT");
        EMVResult.put(25, "EMVERR_APDUFILENOTFOUND");
        EMVResult.put(26, "EMVERR_APDURECORDNOTFOUND");
        EMVResult.put(27, "EMVERR_APDUREFDATANOTFOUND");

        EMVResult.put(31, "EMVERR_APDUREFDATANOTFOUND");
        EMVResult.put(32, "EMVERR_APPNOTEXIST");

        EMVResult.put(48, "EMVERR_AUTHFAILED");
        EMVResult.put(49, "EMVERR_INVALIDPKTYPE");
        EMVResult.put(50, "EMVERR_PKNOTFOUND");
        EMVResult.put(51, "EMVERR_CAPKINDEXNOTFOUND");
        EMVResult.put(52, "EMVERR_CAMISSDATA");
        EMVResult.put(53, "EMVERR_CALENNOTMATCH");
        EMVResult.put(54, "EMVERR_CAHASH");
        EMVResult.put(55, "EMVERR_CAEXPIRED");
        EMVResult.put(56, "EMVERR_CAINCRL");
        EMVResult.put(57, "EMVERR_AUTHPANNOTMATCH");
        EMVResult.put(58, "EMVERR_AUTHCALENNOTMATCH");
        EMVResult.put(59, "EMVERR_AUTHCALENNOTMATCH");

        EMVResult.put(64, "EMVERR_CVVERIFY");
        EMVResult.put(65, "EMVERR_CVCONDITION");
        EMVResult.put(66, "EMVERR_VERIFYFAIL");
        EMVResult.put(67, "EMVERR_VERIFYPLAINPIN");
        EMVResult.put(68, "EMVERR_VERIFYONLINEENPIN");
        EMVResult.put(69, "EMVERR_VERIFYPLAINPINANDSIGN");
        EMVResult.put(70, "EMVERR_VERIFYENCIPHERPIN");
        EMVResult.put(71, "EMVERR_VERIFYENCIPHERPINANDSIGN");
        EMVResult.put(72, "EMVERR_VERIFYSIGN");
        EMVResult.put(73, "EMVERR_VERIFYNOCVM");
        EMVResult.put(74, "EMVERR_VERIFYNOTMATCH");
        EMVResult.put(75, "EMVERR_VERIFYPARAM");
        EMVResult.put(76, "EMVERR_VERIFYMALFUNCTION");
        EMVResult.put(77, "EMVERR_VERIFYBYPASS");
        EMVResult.put(78, "EMVERR_VERIFYPINBLOCKED");
        EMVResult.put(79, "EMVERR_VERIFYLIMITEXCEEDED");
        EMVResult.put(80, "EMVERR_VERIFYCANCEL");
        EMVResult.put(81, "EMVERR_VERIFYPINNOTPRESENT");
        EMVResult.put(82, "EMVERR_VERIFYREVERIFY");
        EMVResult.put(83, "EMVERR_VERIFYICCCOMMAND");
        EMVResult.put(84, "EMVERR_VERIFYUNRECOGNISED");
        EMVResult.put(85, "EMVERR_VERIFYICCDATA");
        EMVResult.put(86, "EMVERR_VERIFYCONDITION");
        EMVResult.put(87, "EMVERR_VERIFYIDCARD");
        EMVResult.put(88, "EMVERR_VERIFYCVMNOTSUPPORT");

        EMVResult.put(128, "EMVERR_UNKNOWNTRANSTYPE");
        EMVResult.put(160, "EMVERR_UNKNOWNTRANSTYPE");

        EMVResult.put(224, "EMVERR_KERNELINIT");
        EMVResult.put(237, "EMVERR_UNABLEONLINE");
        EMVResult.put(238, "EMVERR_FALLBACK");
        EMVResult.put(239, "EMVERR_RESELECT");
        EMVResult.put(240, "EMVERR_INVALIDPARAM");
        EMVResult.put(241, "EMVERR_INVALIDDATE");
        EMVResult.put(242, "EMVERR_INVALIDCARDDATA");
        EMVResult.put(243, "EMVERR_DOLPROCESS");
        EMVResult.put(244, "EMVERR_CARDMISSDATA");
        EMVResult.put(245, "EMVERR_TERMINALMISSDATA");
        EMVResult.put(246, "EMVERR_MALFUNCTION");
        EMVResult.put(247, "EMVERR_SKIP");
        EMVResult.put(248, "EMVERR_NOTIMPLEMENT");
        EMVResult.put(249, "EMVERR_INVALIDSERVICE");
        EMVResult.put(250, "EMVERR_CID");
        EMVResult.put(251, "EMVERR_NEEDARQC");
        EMVResult.put(252, "EMVERR_NOLICNESE");
        EMVResult.put(253, "EMVERR_USECONTACT");
        EMVResult.put(254, "EMVERR_TRANSFINISHED");
        EMVResult.put(255, "EMVERR_CHANGEINTERFACE");
    }
}
