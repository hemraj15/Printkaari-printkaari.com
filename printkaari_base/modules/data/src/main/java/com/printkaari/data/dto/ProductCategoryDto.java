/**
 * 
 */
package com.printkaari.data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hemraj
 *
 */
public class ProductCategoryDto {
	
	private Long id;
	private String name;
	private List<ProductDto> prodDtos= new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ProductDto> getProdDtos() {
		return prodDtos;
	}
	public void setProdDtos(List<ProductDto> prodDtos) {
		this.prodDtos = prodDtos;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProductCategoryDto [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", prodDtos=");
		builder.append(prodDtos);
		builder.append("]");
		return builder.toString();
	}
	
	

}
