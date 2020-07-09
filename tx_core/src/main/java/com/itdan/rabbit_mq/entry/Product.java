package com.itdan.rabbit_mq.entry;

import java.io.Serializable;

/*
 * 商品实体类
 */
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private int productNo;//商品编号
	private String productName;//商品名称
	private int count;//商品总数

	public int getProductNo() {
		return productNo;
	}

	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Product{" +
				"productNo=" + productNo +
				", productName='" + productName + '\'' +
				", count=" + count +
				'}';
	}
}
