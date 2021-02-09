package com.smartcontact.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class MyOrder {
	
	/*
	 * 
	 * id":"order_DaZlswtdcn9UNV",
  "entity":"order",
  "amount":50000,
  "amount_paid":0,
  "amount_due":50000,
  "currency":"INR",
  "receipt":"Receipt #20",
  "status":"created",
  "attempts":0,
  "notes":[],
  "created_at":1572502745
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long myOrderId;
	
	private String orderId;
	private String amount;
	private String receipt;
	private String currency;
	private String status;
	private String paymentId;
	@ManyToOne
	private User user;
	public Long getMyOrderId() {
		return myOrderId;
	}
	public void setMyOrderId(Long myOrderId) {
		this.myOrderId = myOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getReceipt() {
		return receipt;
	}
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
