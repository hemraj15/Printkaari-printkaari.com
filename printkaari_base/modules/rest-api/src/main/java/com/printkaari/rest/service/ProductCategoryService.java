/**
 * 
 */
package com.printkaari.rest.service;

import java.util.List;

import com.printkaari.data.dto.ProductCategoryDto;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.EmptyListException;

/**
 * @author Hemraj
 *
 */
public interface ProductCategoryService {

	List<ProductCategoryDto> fetchAllProductsCategories(String status) throws DatabaseException, EmptyListException;
}
