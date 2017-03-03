/**
 * 
 */
package com.printkaari.data.dao.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Hemraj
 *
 */
@Entity
public class CustomerTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getSuccessCode() {
		return successCode;
	}
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}
	public Long getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(Long transactionNo) {
		this.transactionNo = transactionNo;
	}
	public String getCustEmailId() {
		return custEmailId;
	}
	public void setCustEmailId(String custEmailId) {
		this.custEmailId = custEmailId;
	}
	public String getCustFirstName() {
		return custFirstName;
	}
	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}
	public String getCustLastName() {
		return custLastName;
	}
	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}
	@Temporal(TemporalType.DATE)
	@PrePersist
	public Date getTransactonDate() {
		return transactonDate == null ? new Date() :transactonDate ;
	}
	public void setTransactonDate(Date transactonDate) {
		this.transactonDate = transactonDate;
	}
	
	@Temporal(TemporalType.DATE)
	@PrePersist
	@PostUpdate
	public Date getTransactionUpdateDate() {
		return transactionUpdateDate ==null ?new Date() : transactionUpdateDate;
	}
	public void setTransactionUpdateDate(Date transactionUpdateDate) {
		this.transactionUpdateDate = transactionUpdateDate;
	}
	public Long getPaymentGatewayTrxId() {
		return paymentGatewayTrxId;
	}
	public void setPaymentGatewayTrxId(Long paymentGatewayTrxId) {
		this.paymentGatewayTrxId = paymentGatewayTrxId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Long getBankRefNum() {
		return bankRefNum;
	}
	public void setBankRefNum(Long bankRefNum) {
		this.bankRefNum = bankRefNum;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Long getPayYouMoneyId() {
		return payYouMoneyId;
	}
	public void setPayYouMoneyId(Long payYouMoneyId) {
		this.payYouMoneyId = payYouMoneyId;
	}
	
	
	

}
