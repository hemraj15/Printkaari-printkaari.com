/**
 * 
 */
package com.printkaari.rest.form;

import java.util.Date;

/**
 * @author Hemraj
 *
 */
public class TransactionResponseForm {
	private Long transactionNo;
	private String custEmailId;
	private String custFirstName;
	private String custLastName;
	private Date transactonDate;
	protected Date transactionUpdateDate;
	private Long paymentGatewayTrxId;
	private Double amount;
	private Long bankRefNum;
	private String bankCode;
	private String cardNumber;
	private String cardType;
	private Double discount;
	private Long payYouMoneyId;
	private String errorMessage;
	private String errorCode;
	private String successMessage;
	private String successCode;
	private Long orderId;
	private String trxMessage;
	private String trxStatus;
	private String trxStatusCode;
	private Long netAmountPaid;
	private String paymentMode;
	private String custTrxAction;
	/**
	 * @return the transactionNo
	 */
	public Long getTransactionNo() {
		return transactionNo;
	}
	/**
	 * @param transactionNo the transactionNo to set
	 */
	public void setTransactionNo(Long transactionNo) {
		this.transactionNo = transactionNo;
	}
	/**
	 * @return the custEmailId
	 */
	public String getCustEmailId() {
		return custEmailId;
	}
	/**
	 * @param custEmailId the custEmailId to set
	 */
	public void setCustEmailId(String custEmailId) {
		this.custEmailId = custEmailId;
	}
	/**
	 * @return the custFirstName
	 */
	public String getCustFirstName() {
		return custFirstName;
	}
	/**
	 * @param custFirstName the custFirstName to set
	 */
	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}
	/**
	 * @return the custLastName
	 */
	public String getCustLastName() {
		return custLastName;
	}
	/**
	 * @param custLastName the custLastName to set
	 */
	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}
	/**
	 * @return the transactonDate
	 */
	public Date getTransactonDate() {
		return transactonDate;
	}
	/**
	 * @param transactonDate the transactonDate to set
	 */
	public void setTransactonDate(Date transactonDate) {
		this.transactonDate = transactonDate;
	}
	/**
	 * @return the transactionUpdateDate
	 */
	public Date getTransactionUpdateDate() {
		return transactionUpdateDate;
	}
	/**
	 * @param transactionUpdateDate the transactionUpdateDate to set
	 */
	public void setTransactionUpdateDate(Date transactionUpdateDate) {
		this.transactionUpdateDate = transactionUpdateDate;
	}
	/**
	 * @return the paymentGatewayTrxId
	 */
	public Long getPaymentGatewayTrxId() {
		return paymentGatewayTrxId;
	}
	/**
	 * @param paymentGatewayTrxId the paymentGatewayTrxId to set
	 */
	public void setPaymentGatewayTrxId(Long paymentGatewayTrxId) {
		this.paymentGatewayTrxId = paymentGatewayTrxId;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the bankRefNum
	 */
	public Long getBankRefNum() {
		return bankRefNum;
	}
	/**
	 * @param bankRefNum the bankRefNum to set
	 */
	public void setBankRefNum(Long bankRefNum) {
		this.bankRefNum = bankRefNum;
	}
	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}
	/**
	 * @param cardNumber the cardNumber to set
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}
	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	/**
	 * @return the discount
	 */
	public Double getDiscount() {
		return discount;
	}
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	/**
	 * @return the payYouMoneyId
	 */
	public Long getPayYouMoneyId() {
		return payYouMoneyId;
	}
	/**
	 * @param payYouMoneyId the payYouMoneyId to set
	 */
	public void setPayYouMoneyId(Long payYouMoneyId) {
		this.payYouMoneyId = payYouMoneyId;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the successMessage
	 */
	public String getSuccessMessage() {
		return successMessage;
	}
	/**
	 * @param successMessage the successMessage to set
	 */
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	/**
	 * @return the successCode
	 */
	public String getSuccessCode() {
		return successCode;
	}
	/**
	 * @param successCode the successCode to set
	 */
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}
	/**
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the trxMessage
	 */
	public String getTrxMessage() {
		return trxMessage;
	}
	/**
	 * @param trxMessage the trxMessage to set
	 */
	public void setTrxMessage(String trxMessage) {
		this.trxMessage = trxMessage;
	}
	/**
	 * @return the trxStatus
	 */
	public String getTrxStatus() {
		return trxStatus;
	}
	/**
	 * @param trxStatus the trxStatus to set
	 */
	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}
	/**
	 * @return the trxStatusCode
	 */
	public String getTrxStatusCode() {
		return trxStatusCode;
	}
	/**
	 * @param trxStatusCode the trxStatusCode to set
	 */
	public void setTrxStatusCode(String trxStatusCode) {
		this.trxStatusCode = trxStatusCode;
	}
	/**
	 * @return the netAmountPaid
	 */
	public Long getNetAmountPaid() {
		return netAmountPaid;
	}
	/**
	 * @param netAmountPaid the netAmountPaid to set
	 */
	public void setNetAmountPaid(Long netAmountPaid) {
		this.netAmountPaid = netAmountPaid;
	}
	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode() {
		return paymentMode;
	}
	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	/**
	 * @return the custTrxAction
	 */
	public String getCustTrxAction() {
		return custTrxAction;
	}
	/**
	 * @param custTrxAction the custTrxAction to set
	 */
	public void setCustTrxAction(String custTrxAction) {
		this.custTrxAction = custTrxAction;
	}
	
	
}
