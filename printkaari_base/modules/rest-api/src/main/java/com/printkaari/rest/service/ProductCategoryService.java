/**
 * 
 */
package com.printkaari.rest.service;

import java.util.List;

import com.printkaari.data.dao.entity.ProductCatagory;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.EmptyListException;

/**
 * @author Hemraj
 *
 */
public interface ProductCategoryService {

	List<ProductCatagory> fetchAllProductsCategories(String status) throws DatabaseException, EmptyListException;
}
