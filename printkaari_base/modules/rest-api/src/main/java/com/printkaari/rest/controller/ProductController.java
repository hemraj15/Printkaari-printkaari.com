/**
 * 
 */
package com.printkaari.rest.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.model.ErrorResponse;
import com.printkaari.rest.service.ProductCategoryService;
import com.printkaari.rest.service.ProductService;

/**
 * @author Hemraj
 *
 */
@RestController
@RequestMapping(value="/product")
public class ProductController {
	
	private Logger				LOGGER	= LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService prodService;
	
	@Autowired
	private ProductCategoryService prodCatService;

	//@Secured(value = { SystemRoles.ADMIN,SystemRoles.CUSTOMER})
		@RequestMapping(value = "/all", method = RequestMethod.GET)
		public Object fetchAllProducts(@RequestParam String status
		        , HttpServletResponse response) {
			LOGGER.info(">> fetchAllProducts");
			
			LOGGER.info(">> fetchAllProducts for status : "+status);
			Object data = null;
			try {
				LOGGER.info("fetchOrders <<");
				data = prodService.fetchAllProducts(status);
				
				LOGGER.info("Get All Product data size "+data);
			}
			catch (DatabaseException e) {
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			
			catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			return data;
		}
		
		//@Secured(value = { SystemRoles.ADMIN,SystemRoles.CUSTOMER})
				@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
				public Object fetchAllProductsByCategoryId(@PathVariable Long categoryId
				        , HttpServletResponse response) {
					LOGGER.info(">> fetchAllProductsByCategoryId");
					
					LOGGER.info(">> fetchAllProductsByCategoryId for category : "+categoryId);
					Object data = null;
					try {
						LOGGER.info("fetchAllProductsByCategoryId <<");
						data = prodService.fetchAllProductsByCategoryId(categoryId);
						
						LOGGER.info("Get All Product data size "+data);
					}
					catch (DatabaseException e) {
						LOGGER.error(e.getMessage(), e);
						((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
						((ErrorResponse) data).setMessage(e.getMessage());
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					
					catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
						((ErrorResponse) data).setMessage(e.getMessage());
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					return data;
				}
				
				//@Secured(value = { SystemRoles.ADMIN,SystemRoles.CUSTOMER})
				@RequestMapping(value = "/categories", method = RequestMethod.GET)
				public Object fetchAllProductCategories(@RequestParam String status
				        , HttpServletResponse response) {
					LOGGER.info(">> fetchAllProductCategories");
					
					LOGGER.info(">> fetchAllProductsCategories for  status : "+status);
					Object data = null;
					try {
						LOGGER.info("fetchAllProductCategories <<");
						data = prodCatService.fetchAllProductsCategories(status);
						
						LOGGER.info("Get All Product Catagory data size "+data);
					}
					catch (DatabaseException e) {
						LOGGER.error(e.getMessage(), e);
						((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
						((ErrorResponse) data).setMessage(e.getMessage());
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					
					catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
						((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
						((ErrorResponse) data).setMessage(e.getMessage());
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					return data;
				}
}
