/**
 * 
 */
package com.printkaari.rest.service;

import java.util.Map;

import javax.management.InstanceNotFoundException;

import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.OrderStatusException;
import com.printkaari.rest.exception.UserNotFoundException;

/**
 * @author Hemraj
 *
 */
public interface PaymentService {

	Map<String ,Object> initiateTransaction(Long orderId) throws DatabaseException,UserNotFoundException,InstanceNotFoundException,OrderStatusException;

	Map<String, Object> transactionComplete(Long trxId) throws DatabaseException;



}
