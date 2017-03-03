/**
 * 
 */
package com.printkaari.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.constant.PaymentConstants;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.model.ErrorResponse;
import com.printkaari.rest.service.PaymentService;

/**
 * @author Hemraj
 *
 */
@RestController
@RequestMapping(value="/payment")
public class PaymentController {
	
	
	private static final Logger		LOGGER	= LoggerFactory.getLogger(PaymentController.class);
	
	@Autowired
	private PaymentService paymentService;
	
	@ResponseBody
	@RequestMapping(value="/getCreds" ,method = RequestMethod.GET)
	public Object getMerchantCreds(@PathVariable Long orderId ,  HttpServletResponse response) {
		Object data=null;
		try {
			
			Map<String ,Object> map=new HashMap<>();
			
			map=paymentService.initiateTransaction(orderId);
			//map.put("test", "test");
			
			
			data=map;
			
		} 
		
		catch (DatabaseException e) {
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(ErrorCodes.VALIDATION_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage() );
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return data;
		
	}

	@ResponseBody
	@RequestMapping(value="/trxComplete" ,method = RequestMethod.GET)
	public Object transactionComplete(@PathVariable Long trxId ,  HttpServletResponse response) {
		Object data=null;
		try {
			
			Map<String ,Object> map=new HashMap<>();
			
			map=paymentService.transactionComplete(trxId);
			map.put("test", "test");
			
			
			data=map;
			
		} 
		
		catch (DatabaseException e) {
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(ErrorCodes.VALIDATION_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage() );
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return data;
		
	}

}
