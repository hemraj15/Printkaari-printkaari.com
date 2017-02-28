package com.printkaari.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prinktaakri.auth.util.AuthorizationUtil;
import com.printkaari.auth.service.SystemRoles;
import com.printkaari.data.dao.entity.User;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.exception.CompanyFileUploadException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.StatusException;
import com.printkaari.rest.exception.UserNotFoundException;
import com.printkaari.rest.model.ErrorResponse;
import com.printkaari.rest.security.AuthenticateUtil;
import com.printkaari.rest.service.CustomerService;
import com.printkaari.rest.service.PrintStoreService;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

	private Logger				LOGGER	= LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService	customerService;
	
	@Autowired
	private PrintStoreService printStoreService;

	//@Secured(value = { SystemRoles.ADMIN})
	@RequestMapping(value = "/recent", method = RequestMethod.GET)
	public Object fetchAllCustomerByModifyDate(
	        @RequestParam(value = "records", required = true) Integer records,
	        HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info(">> fetchAllCandidatesByModifiedDate");
		Object data = null;
		try {
			LOGGER.info("fetchCandidates <<");
			data = customerService.fetchAllCustomerByModifyDate(records);
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
	@RequestMapping(value = "/{customerId}/my-orders", method = RequestMethod.GET)
	public Object fetchAllOrdersByCustomerId(@PathVariable Long customerId
	        , HttpServletResponse response) {
		LOGGER.info(">> fetchAllOrdersByCustomerId");
		
		LOGGER.info(">> fetchAllOrdersByCustomerId for customerId "+customerId);
		Object data = null;
		try {
			LOGGER.info("fetchOrders <<");
			data = customerService.fetchAllOrdersByCustomerId(customerId);
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
			((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return data;
	}
	
	//@Secured(value = { SystemRoles.ADMIN,SystemRoles.CUSTOMER})
	@RequestMapping(value = "/{customerId}/my-active-orders", method = RequestMethod.GET)
	public Object fetchAllActiveOrdersByCustomerId(@PathVariable Long customerId
	        , HttpServletResponse response) {
		LOGGER.info(">> fetchAllOrdersByCustomerId");
		
		LOGGER.info(">> fetchAllOrdersByCustomerId for customerId "+customerId);
		Object data = null;
		try {
			LOGGER.info("fetchOrders <<");
			data = customerService.fetchAllActiveOrdersByCustomerId(customerId ,CommonStatus.ACTIVE.toString());
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
			((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
			((ErrorResponse) data).setMessage(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return data;
	}
	
	    //@Secured(value = { SystemRoles.ADMIN,SystemRoles.CUSTOMER})
		@RequestMapping(value = "/profile", method = RequestMethod.GET)
		public Object fetchLoggedinUser(@RequestParam String emailId, HttpServletResponse response) {
			LOGGER.info(">> fetchLoggedinUser");
			
			LOGGER.info(">> fetchLoggedinUser for customerId ");
			Object data = null;
			try {
				LOGGER.info("fetchOrders <<");
				//data = customerService.fetchAllOrdersByCustomerId();
				//data=customerService.fetchLoggedinCustomer();
				data=customerService.fetchCustomerByEmail(emailId);
				
				
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
				((ErrorResponse) data).setErrorCode(ErrorCodes.USER_NOT_FOUND_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				
			}
			catch (StatusException  e) {
				data = new ErrorResponse();
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.VALIDATION_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
			
			catch (UserNotFoundException e) {
				data = new ErrorResponse();
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.USER_NOT_FOUND_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				
			}
			
			catch (Exception e) {
				data = new ErrorResponse();
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			return data;
		}

		@ResponseBody
		//@Secured(value = {SystemRoles.ROLE_CUSTOMER})
		@Secured(value = {SystemRoles.CUSTOMER})
		@RequestMapping(value = "/email", method = RequestMethod.GET)
		public Object getLoggedinUser( HttpServletResponse response) {
			LOGGER.info(">> getLoggedinUser");
			
			
			Object data = null;
			try {
				LOGGER.info("fetchOrders <<");
			   //data = customerService.fetchAllOrdersByCustomerId(customerId);
				
				//data=(User) AuthorizationUtil.getLoggedInUser();
				data=customerService.getLoggedinUser();
			}
			
			
			catch (Exception e) {
				data = new ErrorResponse();
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			return data;
		}
		
		@ResponseBody
		@RequestMapping(value = "/upload-files", method = RequestMethod.POST, consumes = "multipart/form-data")
		public Object uploadCompanyFiles( @RequestParam String fileType, @RequestParam String bindingType, @RequestParam Integer totalPrice,
				 @RequestParam String colorPageNo,  @RequestParam MultipartFile file, HttpServletRequest request,
		        HttpServletResponse response) {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			Map<String ,Object> map=new HashMap<>();
			Long fileId=null;
			Long orderId=null;
			
			Object data = null;
			if (!isMultipart || fileType == null) {
				data = new ErrorResponse();
				((ErrorResponse) data).setErrorCode(ErrorCodes.SIGNUP_REQUEST_NOT_MULTIPART);
				((ErrorResponse) data).setMessage("Form validation failed!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				try {					
					
					fileId=printStoreService.uploadFile( fileType, file);
					orderId=customerService.placeOrder();
					map.put("fileId", fileId);
					map.put("orderId", orderId);
					
				} catch (CompanyFileUploadException e) {
					data = new ErrorResponse();
					((ErrorResponse) data).setErrorCode(e.getErrorCode());
					((ErrorResponse) data).setMessage(e.getMessage());
					switch (e.getErrorCode()) {
					case ErrorCodes.SIGNUP_COMPANY_NOT_FOUND:
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						break;
					default:
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						break;
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
					data = new ErrorResponse();
					((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
					((ErrorResponse) data).setMessage(e.getMessage());
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
			return data;
		}
	
}
