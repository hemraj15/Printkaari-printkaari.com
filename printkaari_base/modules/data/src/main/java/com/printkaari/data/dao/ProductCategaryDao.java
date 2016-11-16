/**
 * 
 */
package com.printkaari.data.dao;

import java.util.List;

import com.printkaari.data.dao.entity.ProductCatagory;
import com.printkaari.data.dto.ProductCategoryDto;

/**
 * @author Hemraj
 *
 */
public interface ProductCategaryDao extends GenericDao<ProductCatagory, Long> {

	//List<ProductCategoryDto> fetchAllProductsByCategoryStatus(String status);

	List<ProductCategoryDto> fetchAllProductsCategories(String status);

}
