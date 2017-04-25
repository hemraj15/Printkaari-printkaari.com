/**
 * 
 */
package com.printkaari.rest.service;

import com.printkaari.data.dto.GenericDTO;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.EmptyListException;
import com.printkaari.rest.exception.VaidationException;

/**
 * @author Hemraj
 *
 */
public interface TransactionOrderService {

	GenericDTO fetchAllTrxOrders(int pageNum, int count, String sortField, String order) throws DatabaseException, VaidationException, EmptyListException;

	GenericDTO fetchAllTrxOrdersByStatus(int pageNum, int count, String sortField, String order,
	        String status) throws VaidationException, DatabaseException, EmptyListException;

	GenericDTO fetchAllOrdersByStatus(int pageNum, int count, String sortField, String order,
	        String status)throws VaidationException, DatabaseException, EmptyListException;


}
