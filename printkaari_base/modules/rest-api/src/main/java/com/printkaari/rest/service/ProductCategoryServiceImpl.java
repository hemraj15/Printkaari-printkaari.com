/**
 * 
 */
package com.printkaari.rest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.printkaari.data.dao.ProductCategaryDao;
import com.printkaari.data.dao.ProductCategaryDaoImpl;
import com.printkaari.data.dto.ProductCategoryDto;
import com.printkaari.data.dto.ProductDto;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.EmptyListException;

/**
 * @author Hemraj
 *
 */

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	private Logger				LOGGER	= LoggerFactory.getLogger(ProductCategoryServiceImpl.class);
	
    @Autowired	
	private ProductCategaryDao prodCatDao;
	
	@Override
	@Transactional
	public List<ProductCategoryDto> fetchAllProductsCategories(String status) throws DatabaseException, EmptyListException {
		 
		List<ProductCategoryDto> prodCatDtos=null;
		try {

			prodCatDtos = prodCatDao.fetchAllProductsCategories(status);
			
			

		} catch (Exception e) {
			LOGGER.error("Error occured while getting ProductCategoryDto list through database", e);
			throw new EmptyListException("Error occured while getting ProductCategoryDto list through database",
			        ErrorCodes.DATABASE_ERROR);
		}

		if (prodCatDtos == null || prodCatDtos.isEmpty()) {
			throw new EmptyListException("ProductCategoryDto List is empty  ", ErrorCodes.PRODUCT_LIST_EMPTY);
		}

		LOGGER.info("fetchAllProductsCategories <<"+prodCatDtos.toString());
				
		return prodCatDtos;
	}
/*	@Override
	public List<ProductCategoryDto> fetchAllProductsCategories(String status)
			throws DatabaseException, EmptyListException {
		
		return null;
	}*/

}
