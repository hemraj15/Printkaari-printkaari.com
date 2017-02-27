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
@Table(name="customer_files")
public class CustomerFiles extends PrintkaariBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String filaPath;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilaPath() {
		return filaPath;
	}
	public void setFilaPath(String filaPath) {
		this.filaPath = filaPath;
	}
	
	
	

}
