/**
 * 
 */
package com.printkaari.data.dao.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Hemraj
 *
 */
@Entity
@Table(name="product_samples")
public class ProductSamples extends PrintkaariBaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6030975521334358688L;
	
	private Product product;

	@ManyToOne(cascade=CascadeType.ALL,targetEntity=Product.class)
	@JoinColumn(name="product_id")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
