package com.itdan.rabbit_mq.entry;

import java.io.Serializable;

/**
 * 订单实体类
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	private int orderNo;//订单编号
	private int productNo;//商品编号
	private String customer;//客户名
	private int number;//数量

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getProductNo() {
		return productNo;
	}

	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderNo=" + orderNo +
				", productNo=" + productNo +
				", customer='" + customer + '\'' +
				", number=" + number +
				'}';
	}
}