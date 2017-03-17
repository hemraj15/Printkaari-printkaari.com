/**
 * 
 */
package com.printkaari.rest.service;

import java.util.Map;

import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.OrderStatusException;
import com.printkaari.rest.exception.UserNotFoundException;
import com.printkaari.rest.form.TransactionResponseForm;

/**
 * @author Hemraj
 *
 */
public interface PaymentService {

	Map<String ,Object> initiateTransaction(Long orderId) throws DatabaseException,UserNotFoundException,OrderStatusException,InstanceNotFoundException ;

	Map<String, Object> transactionComplete(TransactionResponseForm completTrxForm) throws DatabaseException;



}
