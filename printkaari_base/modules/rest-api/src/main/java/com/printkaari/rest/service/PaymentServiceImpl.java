/**
 * 
 */
package com.printkaari.rest.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.printkaari.data.dao.OrderDao;
import com.printkaari.data.dao.PaymentDao;
import com.printkaari.data.dao.entity.Customer;
import com.printkaari.data.dao.entity.CustomerTransaction;
import com.printkaari.data.dao.entity.Order;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.constant.PaymentConstants;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.OrderStatusException;
import com.printkaari.rest.exception.UserNotFoundException;

/**
 * @author Hemraj
 *
 */
@Service
public class PaymentServiceImpl implements PaymentService {

	private Logger		LOGGER	= LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	private OrderDao	ordDao;

	@Autowired
	private PaymentDao	paymentDao;

	@Override
	@Transactional
	public Map<String, Object> initiateTransaction(Long orderId) throws DatabaseException {

		Map<String, Object> map = new HashMap<>();
		Order ord = new Order();
		Customer cust = null;
		Long trxId = null;
		CustomerTransaction trx = new CustomerTransaction();
		try {

			ord = getOrderByOrderId(orderId);

			if (ord != null) {

				cust = ord.getCustomer();

				if (cust != null) {

					map.put("merchantKey", PaymentConstants.merchantKey);
					map.put("merchantId", PaymentConstants.merchantId);
					map.put("authHeader", PaymentConstants.authHeader);
					map.put("merchantSalt", PaymentConstants.merchantSalt);
					map.put("customerEmail", cust.getEmail());
					map.put("customerId", cust.getId());
					map.put("customerFirstName", cust.getFirstName());
					map.put("custLastName", cust.getLastName());
					map.put("orderPrice", ord.getOrderPrice());
					map.put("orderId", ord.getId());

					trx.setAmount(ord.getOrderPrice());
					trx.setCustEmailId(cust.getEmail());
					trx.setCustFirstName(cust.getFirstName());
					trx.setOrderId(ord.getId());

					LOGGER.info("saving transaction for order id ::" + orderId);
					trxId = paymentDao.save(trx);
					LOGGER.info("saved transaction for order id ::" + orderId +" generated transaction id is "+trxId);

					map.put("tansactionId", trxId);

				}
				else{
					
					LOGGER.info("Customer not found for the request order id "+orderId);
					throw new UserNotFoundException("customer not found for the request order id",ErrorCodes.USER_NOT_FOUND_ERROR);
				}

			}

		} catch (Exception e) {
			LOGGER.error("Error occured while initiating transaction for order" + orderId
			        + " in database", e);
			throw new DatabaseException("Error occured while initiating transaction",
			        ErrorCodes.DATABASE_ERROR);
		}

		return map;
	}

	private Order getOrderByOrderId(Long orderId)
	        throws DatabaseException, InstanceNotFoundException {
		Order ord = null;
		try {

			ord = ordDao.find(orderId);

			if (ord != null) {

				if (!ord.getStatus().equalsIgnoreCase(CommonStatus.INITIATED.toString())) {

					throw new OrderStatusException("Order status in not initiated ",
					        ErrorCodes.INVALID_ORDER_STATUS);

				}
			}

		} catch (InstanceNotFoundException e) {

			LOGGER.info("fetch order to initiate transaction >> instance not found for order id ::"
			        + orderId);

			throw new InstanceNotFoundException("Order Not Found to initiate transaction",
			        ErrorCodes.ORDER_NOT_FOUND_ERROR);
		} catch (Exception e) {
			LOGGER.error("Error occured while fetching order for initiating transaction for order"
			        + orderId + " in database", e);
			throw new DatabaseException("Error occured while fetching order from database",
			        ErrorCodes.DATABASE_ERROR);
		}
		return ord;
	}

	@Override
	public Map<String, Object> transactionComplete(Long trxId) throws DatabaseException {
		 Map<String, Object> map=new HashMap<>();
		 
		 CustomerTransaction trxObj= new CustomerTransaction();
		try {
			
			
			trxObj=paymentDao.find(trxId);
			
			if (trxObj !=null) {
				
				
			}
			
			paymentDao.saveOrUpdate(trxObj);
			
		} catch (Exception e) {
			LOGGER.error("Error occured while updating transaction for transaction ::"
			        + trxId + " in database", e);
			throw new DatabaseException("Error occured while fetching order from database",
			        ErrorCodes.DATABASE_ERROR);
		}
		return map;
	}

}