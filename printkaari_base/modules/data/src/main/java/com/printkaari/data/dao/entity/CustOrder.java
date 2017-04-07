/**
 * 
 */
package com.printkaari.data.dao.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Hemraj
 *
 */

@Entity
@Table(name="cust_order")
public class CustOrder extends PrintkaariBaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8995896702674504742L;
	
	private Double orderPrice;
	private Double paidAmount;
	private Set<Product> products=new HashSet<>();
	private Customer customer;
	private Set<CustomerFiles> fileId;
	private String colorPages;
	private Integer totalPages;
	private Integer totalColorPages;
    private Integer printQuantity;
    private Integer discount;
    private Double discountAmount;
//@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER,targetEntity=Product.class)
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "order_products", joinColumns = {
	        @JoinColumn(name = "order_id", nullable = false, updatable = false) }, inverseJoinColumns = {
	                @JoinColumn(name = "product_id", nullable = false, updatable = false) })

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}



	@Column(name="price")
	public Double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "Orde_Documents", joinColumns = {
	        @JoinColumn(name = "ord_id", nullable = false, updatable = false) }, inverseJoinColumns = {
	                @JoinColumn(name = "file_id", nullable = false, updatable = false) })
	public Set<CustomerFiles> getFileId() {
		return fileId;
	}

	public void setFileId(Set<CustomerFiles> fileId) {
		this.fileId = fileId;
	}

	public String getColorPages() {
		return colorPages;
	}

	public void setColorPages(String colorPages) {
		this.colorPages = colorPages;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getTotalColorPages() {
		return totalColorPages;
	}

	public void setTotalColorPages(Integer totalColorPages) {
		this.totalColorPages = totalColorPages;
	}

	public Integer getPrintQuantity() {
		return printQuantity;
	}

	public void setPrintQuantity(Integer printQuantity) {
		this.printQuantity = printQuantity;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	

}
