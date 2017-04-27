/**
 * 
 */
package com.printkaari.rest.service;

import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.EmptyListException;
import com.printkaari.rest.exception.VaidationException;

/**
 * @author Hemraj
 *
 */
public interface OrderService {

	Object fetchAllOrders(int pageNum, int count, String sortField, String order) throws VaidationException, DatabaseException, EmptyListException ;

	Object fetchAllOrdersByStatus(int pageNum, int count, String sortField, String order,
	        String status) throws VaidationException, DatabaseException, EmptyListException;

	Object fetchOrdersByOrderId(Long orderId) throws InstanceNotFoundException ,DatabaseException;

	
}
