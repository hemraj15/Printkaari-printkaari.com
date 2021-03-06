/**
 * 
 */
package com.printkaari.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.constant.PaymentConstants;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.InvalidTransactionException;
import com.printkaari.rest.exception.OrderStatusException;
import com.printkaari.rest.form.SignUpStep1Form;
import com.printkaari.rest.form.TransacationOrderForm;
import com.printkaari.rest.form.TransactionResponseForm;
import com.printkaari.rest.model.ErrorResponse;
import com.printkaari.rest.service.CustomerService;
import com.printkaari.rest.service.PaymentService;
import com.printkaari.rest.utils.ErrorUtils;

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
	
	@Autowired
	private CustomerService custService;
	
	@ResponseBody
	@RequestMapping(value="/trxInitiate/{orderId}" ,method = RequestMethod.GET)
	public Object getMerchantCreds(@PathVariable Long orderId ,  HttpServletResponse response) {
		Object data=null;
		try {
			
			Map<String ,Object> map=new HashMap<>();
			
			map=paymentService.initiateTransaction(orderId);
			//map.put("test", "test");			
			data=map;
			
		} 
		
		catch (DatabaseException e) {
			data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
          catch (InstanceNotFoundException e) {
        	  data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.ORDER_NOT_FOUND_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
       catch (OrderStatusException e) {
    	   data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.ORDER_STATUS_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		catch (Exception e) {
			data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(ErrorCodes.VALIDATION_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage() );
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return data;
		
	}

	@ResponseBody
	@RequestMapping(value="/trxComplete" ,method = RequestMethod.POST, consumes = "application/json")
	public Object transactionComplete(@RequestBody TransactionResponseForm completTrxForm,
	        BindingResult result, HttpServletResponse response) {
		
		Object data=null;
		Long trxOrderId=null;
		if (result.hasErrors()) {
			String message = ErrorUtils.getTextValidationErrorMessage(result.getAllErrors());
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(message);
			((ErrorResponse) data).setMessage("Form validation failed!");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		    }
		else{
			
		try {
			
			Map<String ,Object> map=new HashMap<>();
			
			trxOrderId=paymentService.transactionComplete(completTrxForm);
			map.put("transactionId", completTrxForm.getTransactionNo());
			map.put("transactioOrderId", trxOrderId);
			map.put("transactioMessage", "payment successfull - transaction completed for transaction order id :"+trxOrderId);
			
			LOGGER.info("transaction order id to confirm ::"+trxOrderId);
			LOGGER.info("Placing order >>");
		   custService.confirmOrder(trxOrderId,completTrxForm.getTrxStatus());
	
			map.put("message", "order has been confirmed succssfully for transaction order id :"+trxOrderId);
			data=map;
			
		} 
		 catch (InstanceNotFoundException e) {
       	  data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.INVALID_TRANSACTION_NUMBER_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}		
		 catch (InvalidTransactionException e) {
	       	  data = new ErrorResponse();
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.INVALID_TRANSACTION_NUMBER_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		catch (DatabaseException e) {
			data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(ErrorCodes.VALIDATION_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage() );
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		}
		return data;
		
	}
	
	@ResponseBody
	@RequestMapping(value="/cartTrxInitiate" ,method = RequestMethod.POST,consumes="application/json")
	public Object initiateTransaction(@RequestBody TransacationOrderForm form ,BindingResult result,   HttpServletResponse response) {
		
		Object data=null;
		if (result.hasErrors()) {
			String message = ErrorUtils.getTextValidationErrorMessage(result.getAllErrors());
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(message);
			((ErrorResponse) data).setMessage("Form validation failed!");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		    }
		
		else{
		
		try {
			
			LOGGER.info("cart transaction initiate list of orders "+form.getOrderIdList());
			
			Map<String ,Object> map=new HashMap<>();
			
			map=paymentService.initiateCartTransaction(form);
			//map.put("test", "test");			
			data=map;
			
		} 
		
		catch (DatabaseException e) {
			data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.DATABASE_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
          catch (InstanceNotFoundException e) {
        	  data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.ORDER_NOT_FOUND_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
       catch (OrderStatusException e) {
    	   data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			((ErrorResponse) data).setErrorCode(ErrorCodes.ORDER_STATUS_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		catch (Exception e) {
			data = new ErrorResponse();
			LOGGER.error(e.getMessage(), e);
			data = new ErrorResponse();
			((ErrorResponse) data).setErrorCode(ErrorCodes.VALIDATION_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage() );
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		}
		return data;
		
	}

}
