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
import org.springframework.util.CollectionUtils;

import com.printkaari.data.dao.OrderDao;
import com.printkaari.data.dao.entity.CustOrder;
import com.printkaari.data.dao.entity.TransacationOrder;
import com.printkaari.data.dto.GenericDTO;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.EmptyListException;
import com.printkaari.rest.exception.VaidationException;

/**
 * @author Hemraj
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	private Logger				LOGGER	= LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderDao ordDao;

	@Override
	@Transactional
	public Object fetchAllOrders(int pageNum, int count, String sortField, String order)
	        throws VaidationException, DatabaseException, EmptyListException {

		if (pageNum == 0 || count == 0) {
			throw new VaidationException("Invalid PageNumber or Job Count",
			        ErrorCodes.INVALID_PAGENUM_OR_COUNT);
		}
		List<CustOrder> trxList = null;
		
		GenericDTO result=null;
		try {

			result = ordDao.fetchAllOrders(pageNum, count, sortField, order);
		} catch (Exception e) {
			LOGGER.error("Error occured while getting  CustOrder list through database", e);
			throw new DatabaseException("Error occured while getting  CustOrder  list through database",
			        ErrorCodes.DATABASE_ERROR);
		}
		
		if (CollectionUtils.isEmpty(result.getResult())) {
			throw new EmptyListException(" CustOrder List is empty", ErrorCodes.TRANSACTION_ORDER_LIST_EMPTY);
		}

		LOGGER.info("OrderServiceImpl.fetchAllOrders <<");
		return result;
	}

	@Override
	@Transactional
	public Object fetchAllOrdersByStatus(int pageNum, int count, String sortField, String order,String status)
	        throws VaidationException, DatabaseException, EmptyListException {

		if (pageNum == 0 || count == 0) {
			throw new VaidationException("Invalid PageNumber or Job Count",
			        ErrorCodes.INVALID_PAGENUM_OR_COUNT);
		}
		List<CustOrder> ordList = null;
		
		GenericDTO result=null;
		try {

			result = ordDao.fetchAllOrdersByStatus(pageNum, count, sortField, order,status);
		} catch (Exception e) {
			LOGGER.error("Error occured while getting  CustOrder list through database", e);
			throw new DatabaseException("Error occured while getting  CustOrder  list through database",
			        ErrorCodes.DATABASE_ERROR);
		}
		
		if (CollectionUtils.isEmpty(result.getResult())) {
			throw new EmptyListException(" CustOrder List is empty", ErrorCodes.TRANSACTION_ORDER_LIST_EMPTY);
		}

		LOGGER.info("OrderServiceImpl.fetchAllOrders <<");
		return result;
	}

}
