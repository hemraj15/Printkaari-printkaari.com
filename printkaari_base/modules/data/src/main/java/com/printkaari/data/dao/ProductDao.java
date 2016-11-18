/**
 * 
 */
package com.printkaari.data.dao;

import java.util.List;

import com.printkaari.data.dao.entity.Product;
import com.printkaari.data.dto.ProductDto;

/**
 * @author Hemraj
 *
 */
public interface ProductDao extends GenericDao<Product, Long>{

	List<ProductDto> fetchAllProducts(String status);

	List<ProductDto> fetchAllProductsByCategoryId(Long catId);

}
