package com.blanks.test.demo.swingcard;

import java.io.Serializable;

/**
 * 订单详情
 */
@SuppressWarnings("serial")
public class PayDetail implements Serializable {

    // 自增ID
    public Long PDID;

    // 发送包
    public byte[] sendBag;
    // 接收包
    public byte[] recvBag;

    /**
     * ICCInfo
     */
    // 应用ID
    public String AID;
    // 应用标签
    public String appLabel;
    // 应用首选名称
    public String appName;
    // TVR
    public String TVR;
    // TSI
    public String TSI;
    // ATC
    public String ATC;
    // TC
    public String TC;
    // 发卡行认证数据
    public String issuerVeriData;
    //脚本处理结果
    public String ScriptResult;
    //密文信息数据
    public String CID;
    // 脚本1长度
    public int script1Length;
    // 脚本2长度
    public int script2Length;
    // 脚本1内容(用于二磁授权处理)71
    public String script1Content;
    // 脚本2内容(用于二磁授权处理)72
    public String script2Content;
    // ic卡55域数据
    public String ICC55;
    // ARPC状态
    public String ARPCStatus;
    // 发卡行应用数据
    public String bankAppDate;

    // 商户名
    public String merchantName;
    // 商户编号
    public String merchantNo;
    // 终端号
    public String terminalNo;
    // 收单机构代码（域 44）
    public String ACQcode;
    // 收单机构标识码
    public String AIICODE;
    // 发卡机构代码（域 44）
    public String IssuerCode;
    // PIN加密密文
    public String PINCipher;
    // 卡号
    public String CardNo;
    // 主账号序号（针对IC 卡）
    public String cardSerialNo;
    // 交易处理码（域 3）
    public String processCode;
    //附加金额，以分为单位
    public long additionalAmount;
    // 交易金额 ，以分为单位
    public long amount = 0;
    // 卡类型
    public int cardType;
    // 操作员代码（域 63.1）
    public String operatorNo;
    // 交易类型 (22:消费 23::消费撤销 25:退货)
    public String transType;
    //消息类型  (0800：终端签到请求)
    public String msgType;
    // 网络管理信息码
    public String netManageCode;
    // 卡有效期
    public String EXPDate;
    // 交易批次号
    public String BathNo;
    // 凭证号(终端/POS流水号)
    public String voucherNo;
    // 清算日期
    public String settleDate;
    // 交易日期(MMDD)（主机返回）
    public String TradeDate;
    // 交易时间(HHmmss)(主机返回）
    public String TradeTime;
    // 本地交易时间戳（Unix时间戳）
    public Long TerminalDate;
    // 授权号（主机返回）
    public String authNo;
    // 交易参考号
    public String referNo;
    // 原授权号（主机返回）
    public String originalAuthNo;
    // 原交易参考号
    public String originalReferNo;
    // 货币代码
    public String currencyCode;
    // 国际信用卡公司代码 （域 63.1）
    public String cardOrgCode;
    // 备注
    public String reference;
    // 交易应答码（域 39）
    public String tradeAnswerCode;
    // 是否需要冲正
    public boolean isNeedReversal;
    // 是否是外卡
    public boolean isForeignCard;
    // 是否离线交易
    public boolean isOffLine;
    // 是否IC卡交易
    public boolean isICCardTrans;
    // 是否已撤销
    public boolean isCanceled;
    // 是否已调整
    public boolean isAdjust;
    // 是否已冲正
    public boolean isReversaled;
    // 是否已上送(结算不平时,需要批上送)
    public boolean isUploaded;
    // 是否已打印
    public boolean isPrinted;
    // 原交易日期
    public String originalTransDate;
    // 原交易流水号
    public String originalPOSNum;
    // 冲正原因
    public String reversalReason;
    // 冲正授权码
    public String reversalAuthorize;

    public PayDetail() {
    }
    public Long getPDID() {
        return this.PDID;
    }
    public void setPDID(Long PDID) {
        this.PDID = PDID;
    }
    public byte[] getSendBag() {
        return this.sendBag;
    }
    public void setSendBag(byte[] sendBag) {
        this.sendBag = sendBag;
    }
    public byte[] getRecvBag() {
        return this.recvBag;
    }
    public void setRecvBag(byte[] recvBag) {
        this.recvBag = recvBag;
    }
    public String getAID() {
        return this.AID;
    }
    public void setAID(String AID) {
        this.AID = AID;
    }
    public String getAppLabel() {
        return this.appLabel;
    }
    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }
    public String getAppName() {
        return this.appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getTVR() {
        return this.TVR;
    }
    public void setTVR(String TVR) {
        this.TVR = TVR;
    }
    public String getTSI() {
        return this.TSI;
    }
    public void setTSI(String TSI) {
        this.TSI = TSI;
    }
    public String getATC() {
        return this.ATC;
    }
    public void setATC(String ATC) {
        this.ATC = ATC;
    }
    public String getTC() {
        return this.TC;
    }
    public void setTC(String TC) {
        this.TC = TC;
    }
    public String getIssuerVeriData() {
        return this.issuerVeriData;
    }
    public void setIssuerVeriData(String issuerVeriData) {
        this.issuerVeriData = issuerVeriData;
    }
    public String getScriptResult() {
        return this.ScriptResult;
    }
    public void setScriptResult(String ScriptResult) {
        this.ScriptResult = ScriptResult;
    }
    public String getCID() {
        return this.CID;
    }
    public void setCID(String CID) {
        this.CID = CID;
    }
    public int getScript1Length() {
        return this.script1Length;
    }
    public void setScript1Length(int script1Length) {
        this.script1Length = script1Length;
    }
    public int getScript2Length() {
        return this.script2Length;
    }
    public void setScript2Length(int script2Length) {
        this.script2Length = script2Length;
    }
    public String getScript1Content() {
        return this.script1Content;
    }
    public void setScript1Content(String script1Content) {
        this.script1Content = script1Content;
    }
    public String getScript2Content() {
        return this.script2Content;
    }
    public void setScript2Content(String script2Content) {
        this.script2Content = script2Content;
    }
    public String getICC55() {
        return this.ICC55;
    }
    public void setICC55(String ICC55) {
        this.ICC55 = ICC55;
    }
    public String getARPCStatus() {
        return this.ARPCStatus;
    }
    public void setARPCStatus(String ARPCStatus) {
        this.ARPCStatus = ARPCStatus;
    }
    public String getBankAppDate() {
        return this.bankAppDate;
    }
    public void setBankAppDate(String bankAppDate) {
        this.bankAppDate = bankAppDate;
    }
    public String getMerchantName() {
        return this.merchantName;
    }
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    public String getMerchantNo() {
        return this.merchantNo;
    }
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }
    public String getTerminalNo() {
        return this.terminalNo;
    }
    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }
    public String getACQcode() {
        return this.ACQcode;
    }
    public void setACQcode(String ACQcode) {
        this.ACQcode = ACQcode;
    }
    public String getAIICODE() {
        return this.AIICODE;
    }
    public void setAIICODE(String AIICODE) {
        this.AIICODE = AIICODE;
    }
    public String getIssuerCode() {
        return this.IssuerCode;
    }
    public void setIssuerCode(String IssuerCode) {
        this.IssuerCode = IssuerCode;
    }
    public String getPINCipher() {
        return this.PINCipher;
    }
    public void setPINCipher(String PINCipher) {
        this.PINCipher = PINCipher;
    }
    public String getCardNo() {
        return this.CardNo;
    }
    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }
    public String getCardSerialNo() {
        return this.cardSerialNo;
    }
    public void setCardSerialNo(String cardSerialNo) {
        this.cardSerialNo = cardSerialNo;
    }
    public String getProcessCode() {
        return this.processCode;
    }
    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }
    public long getAdditionalAmount() {
        return this.additionalAmount;
    }
    public void setAdditionalAmount(long additionalAmount) {
        this.additionalAmount = additionalAmount;
    }
    public long getAmount() {
        return this.amount;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
    public int getCardType() {
        return this.cardType;
    }
    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
    public String getOperatorNo() {
        return this.operatorNo;
    }
    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }
    public String getTransType() {
        return this.transType;
    }
    public void setTransType(String transType) {
        this.transType = transType;
    }
    public String getMsgType() {
        return this.msgType;
    }
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
    public String getNetManageCode() {
        return this.netManageCode;
    }
    public void setNetManageCode(String netManageCode) {
        this.netManageCode = netManageCode;
    }
    public String getEXPDate() {
        return this.EXPDate;
    }
    public void setEXPDate(String EXPDate) {
        this.EXPDate = EXPDate;
    }
    public String getBathNo() {
        return this.BathNo;
    }
    public void setBathNo(String BathNo) {
        this.BathNo = BathNo;
    }
    public String getVoucherNo() {
        return this.voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getSettleDate() {
        return this.settleDate;
    }
    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }
    public String getTradeDate() {
        return this.TradeDate;
    }
    public void setTradeDate(String TradeDate) {
        this.TradeDate = TradeDate;
    }
    public String getTradeTime() {
        return this.TradeTime;
    }
    public void setTradeTime(String TradeTime) {
        this.TradeTime = TradeTime;
    }
    public Long getTerminalDate() {
        return this.TerminalDate;
    }
    public void setTerminalDate(Long TerminalDate) {
        this.TerminalDate = TerminalDate;
    }
    public String getAuthNo() {
        return this.authNo;
    }
    public void setAuthNo(String authNo) {
        this.authNo = authNo;
    }
    public String getReferNo() {
        return this.referNo;
    }
    public void setReferNo(String referNo) {
        this.referNo = referNo;
    }
    public String getOriginalAuthNo() {
        return this.originalAuthNo;
    }
    public void setOriginalAuthNo(String originalAuthNo) {
        this.originalAuthNo = originalAuthNo;
    }
    public String getOriginalReferNo() {
        return this.originalReferNo;
    }
    public void setOriginalReferNo(String originalReferNo) {
        this.originalReferNo = originalReferNo;
    }
    public String getCurrencyCode() {
        return this.currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public String getCardOrgCode() {
        return this.cardOrgCode;
    }
    public void setCardOrgCode(String cardOrgCode) {
        this.cardOrgCode = cardOrgCode;
    }
    public String getReference() {
        return this.reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getTradeAnswerCode() {
        return this.tradeAnswerCode;
    }
    public void setTradeAnswerCode(String tradeAnswerCode) {
        this.tradeAnswerCode = tradeAnswerCode;
    }
    public boolean getIsNeedReversal() {
        return this.isNeedReversal;
    }
    public void setIsNeedReversal(boolean isNeedReversal) {
        this.isNeedReversal = isNeedReversal;
    }
    public boolean getIsForeignCard() {
        return this.isForeignCard;
    }
    public void setIsForeignCard(boolean isForeignCard) {
        this.isForeignCard = isForeignCard;
    }
    public boolean getIsOffLine() {
        return this.isOffLine;
    }
    public void setIsOffLine(boolean isOffLine) {
        this.isOffLine = isOffLine;
    }
    public boolean getIsICCardTrans() {
        return this.isICCardTrans;
    }
    public void setIsICCardTrans(boolean isICCardTrans) {
        this.isICCardTrans = isICCardTrans;
    }
    public boolean getIsCanceled() {
        return this.isCanceled;
    }
    public void setIsCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }
    public boolean getIsAdjust() {
        return this.isAdjust;
    }
    public void setIsAdjust(boolean isAdjust) {
        this.isAdjust = isAdjust;
    }
    public boolean getIsReversaled() {
        return this.isReversaled;
    }
    public void setIsReversaled(boolean isReversaled) {
        this.isReversaled = isReversaled;
    }
    public boolean getIsUploaded() {
        return this.isUploaded;
    }
    public void setIsUploaded(boolean isUploaded) {
        this.isUploaded = isUploaded;
    }
    public boolean getIsPrinted() {
        return this.isPrinted;
    }
    public void setIsPrinted(boolean isPrinted) {
        this.isPrinted = isPrinted;
    }
    public String getOriginalTransDate() {
        return this.originalTransDate;
    }
    public void setOriginalTransDate(String originalTransDate) {
        this.originalTransDate = originalTransDate;
    }
    public String getOriginalPOSNum() {
        return this.originalPOSNum;
    }
    public void setOriginalPOSNum(String originalPOSNum) {
        this.originalPOSNum = originalPOSNum;
    }
    public String getReversalReason() {
        return this.reversalReason;
    }
    public void setReversalReason(String reversalReason) {
        this.reversalReason = reversalReason;
    }
    public String getReversalAuthorize() {
        return this.reversalAuthorize;
    }
    public void setReversalAuthorize(String reversalAuthorize) {
        this.reversalAuthorize = reversalAuthorize;
    }

}
