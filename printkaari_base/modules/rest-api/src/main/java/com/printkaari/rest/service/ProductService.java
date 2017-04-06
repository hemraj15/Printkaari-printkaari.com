/**
 * 
 */
package com.printkaari.rest.service;

import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.EmptyListException;

/**
 * @author Hemraj
 *
 */
public interface ProductService {

	Object fetchAllProducts(String status) throws DatabaseException ,EmptyListException;

	Object fetchAllProductsByCategoryId(Long catId)throws DatabaseException ,EmptyListException;

	Object fetchAllProductsWithCatagory(String status) throws EmptyListException;

	

	//List<ProductCategoryDto> fetchAllProductsByCategoryIdNew(Long catId) throws DatabaseException, EmptyListException;

}
