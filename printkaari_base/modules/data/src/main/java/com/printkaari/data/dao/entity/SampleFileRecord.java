/**
 * 
 */
package com.printkaari.data.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Hemraj
 *
 */
@Entity
@Table(name="product_sample_file")
public class SampleFileRecord extends PrintkaariBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name ;
	private String filePath;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/*@OneToOne(fetch = FetchType.LAZY)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
*/
}
