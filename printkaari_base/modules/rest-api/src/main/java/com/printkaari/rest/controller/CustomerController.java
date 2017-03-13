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

import com.printkaari.auth.service.SystemRoles;
import com.printkaari.data.dao.entity.Customer;
import com.printkaari.data.dao.entity.User;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.exception.CompanyFileUploadException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.InvalidProductException;
import com.printkaari.rest.exception.MailNotSendException;
import com.printkaari.rest.exception.StatusException;
import com.printkaari.rest.exception.UserNotFoundException;
import com.printkaari.rest.model.ErrorResponse;
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


	@Secured(value = { SystemRoles.ADMIN,SystemRoles.CUSTOMER})
	@RequestMapping(value = "/my-orders", method = RequestMethod.GET)
	public Object fetchAllOrdersByCustomerId( HttpServletResponse response) {
		LOGGER.info(">> fetchAllOrdersByCustomerId");		
		
		Object data = null;
		User user=null;
		try {
			user=customerService.getLoggedinUser();
			LOGGER.info(">> fetchAllOrdersByCustomerId for customerId "+user.getId());
			data = customerService.fetchAllOrdersByCustomerId(user.getEmailId().trim());
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
	
	@Secured(value = { SystemRoles.ADMIN,SystemRoles.CUSTOMER})
	@RequestMapping(value = "/my-active-orders", method = RequestMethod.GET)
	public Object fetchAllActiveOrdersByCustomerId( HttpServletResponse response) {
		LOGGER.info(">> fetchAllOrdersByCustomerId");
		
		Object data = null;
		User user=null;
		try {
			user=customerService.getLoggedinUser();
			
			
			LOGGER.info(">> fetchAllOrdersByCustomerId for customerId "+user.getId());
			data = customerService.fetchAllActiveOrdersByCustomerId(user.getEmailId() ,CommonStatus.ACTIVE.toString());
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
	
	    @Secured(value = {SystemRoles.CUSTOMER})
		@RequestMapping(value = "/profile", method = RequestMethod.GET)
		public Object fetchLoggedinUser( HttpServletResponse response) {
			LOGGER.info(">> fetchLoggedinUser");
			
			
			Object data = null;
			User user=null;
			try {
				LOGGER.info("fetchOrders <<");
				
				LOGGER.info(">> fetchLoggedinUser for my profile ");
				//data = customerService.fetchAllOrdersByCustomerId();
				String custEmail=customerService.fetchLoggedinCustomer();
				data=customerService.fetchCustomerByEmail(custEmail.trim());
				
				
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
		@Secured(value = {SystemRoles.CUSTOMER})
		@RequestMapping(value = "/email", method = RequestMethod.GET)
		public Object getLoggedinUser( HttpServletResponse response) {
			LOGGER.info(">> getLoggedinUser");
			
			
			Object data = null;
			User user=null;
			try {
				LOGGER.info("fetchOrders <<");
			   //data = customerService.fetchAllOrdersByCustomerId(customerId);
				
				//data=(User) AuthorizationUtil.getLoggedInUser();
				user=customerService.getLoggedinUser();
				System.out.println("");
			}
			
			
			catch (Exception e) {
				data = new ErrorResponse();
				LOGGER.error(e.getMessage(), e);
				((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
				((ErrorResponse) data).setMessage(e.getMessage());
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			return user.getEmailId();
		}
		
		@ResponseBody
		@RequestMapping(value = "/college-order-upload-files", method = RequestMethod.POST, consumes = "multipart/form-data")
		public Object uploadCompanyFiles( @RequestParam String fileType, @RequestParam String bindingType, @RequestParam Integer totalPages,
				 @RequestParam Integer glossyColorPages,@RequestParam Integer nonGlossyColorPages,@RequestParam String anyOtherRequest,  @RequestParam MultipartFile file, HttpServletRequest request,
		        HttpServletResponse response) {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			Map<String ,Object> map=new HashMap<>();
			Long fileId=null;
			Long orderId=null;
			
			Object data = null;
			if (!isMultipart || fileType == null) {
				data = new ErrorResponse();
				((ErrorResponse) data).setErrorCode(ErrorCodes.COLLEGE_SECTION_INITIATE_ORDER_NOT_MULTIPART);
				((ErrorResponse) data).setMessage("Form validation failed!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				try {					
					
					fileId=printStoreService.uploadFile( fileType, file);
					LOGGER.info("file saved successfully with file id ::"+fileId);
					LOGGER.info("Placing order >>");
					map=customerService.placeOrder(glossyColorPages,nonGlossyColorPages,anyOtherRequest,totalPages,bindingType,fileId);
					map.put("fileId", fileId);
					map.put("message", "order ahs been initiated succssfully please make payment to confirm order");
					data=map;
					LOGGER.info("order initiated");
				} catch (CompanyFileUploadException e) {
					data = new ErrorResponse();
					((ErrorResponse) data).setErrorCode(e.getErrorCode());
					((ErrorResponse) data).setMessage(e.getMessage());
					switch (e.getErrorCode()) {
					case ErrorCodes.CUSTOMER_NOT_FOUND_ERROR:
						response.setStatus(HttpServletResponse.SC_NOT_FOUND);
						break;
					default:
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						break;
					}
				}
				catch (UserNotFoundException e) {
					data = new ErrorResponse();
					LOGGER.error(e.getMessage(), e);
					((ErrorResponse) data).setErrorCode(ErrorCodes.USER_NOT_FOUND_ERROR);
					((ErrorResponse) data).setMessage(e.getMessage());
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					
				}
				catch (InvalidProductException e) {
					data = new ErrorResponse();
					LOGGER.error(e.getMessage(), e);
					((ErrorResponse) data).setErrorCode(ErrorCodes.INVALID_PRODUCT_ERROR);
					((ErrorResponse) data).setMessage(e.getMessage());
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
				catch (MailNotSendException e) {
					data =new ErrorResponse();
					LOGGER.error(e.getMessage(),e);
					((ErrorResponse)data).setErrorCode(ErrorCodes.MAIL_NOT_SENT_ERROR);
					((ErrorResponse)data).setMessage(e.getMessage());
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
				catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
					data = new ErrorResponse();
					((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
					((ErrorResponse) data).setMessage(e.getMessage());
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			}
			return data;
		}
		
		@ResponseBody
		@RequestMapping(value = "/confirm-college-order/{orderId}", method = RequestMethod.GET)
		public Object confirmOrder( @PathVariable Long  orderId, HttpServletRequest request,
		        HttpServletResponse response) {
			
			Map<String ,Object> map=new HashMap<>();			
			
			Object data = null;
			
				try {					
					LOGGER.info("order id to comfirm ::"+orderId);
					LOGGER.info("Placing order >>");
					 customerService.confirmOrder(orderId);
					
					map.put("orderId", orderId);
					map.put("message", "order has been confirmed succssfully !!");
					data=map;
					LOGGER.info("order initiated");
				} 				
				
				catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
					data = new ErrorResponse();
					((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
					((ErrorResponse) data).setMessage(e.getMessage());
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			
			return data;
		}
		
		@ResponseBody
		@RequestMapping(value = "/update-college-order/{orderId}/{ordStatus}", method = RequestMethod.GET)
		public Object changeOrderStatus( @PathVariable Long orderId, @PathVariable String ordStatus, HttpServletRequest request,
		        HttpServletResponse response) {
			
			Map<String ,Object> map=new HashMap<>();			
			
			Object data = null;
			
				try {					
					LOGGER.info("order id to comfirm ::"+orderId);
					LOGGER.info("Placing order >>");
					 customerService.changeOrderStatus(ordStatus, orderId);
					
					map.put("orderId", orderId);
					map.put("message", "order has been updated to "+ordStatus+" succssfully !!");
					data=map;
					LOGGER.info("order initiated");
				} 				
				
				catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
					data = new ErrorResponse();
					((ErrorResponse) data).setErrorCode(ErrorCodes.SERVER_ERROR);
					((ErrorResponse) data).setMessage(e.getMessage());
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				}
			
			return data;
		}
		/*@ResponseBody
		@RequestMapping(value = "/collegeSec-initiate-order", method = RequestMethod.POST)
		public Object collegeSectionInitiateOrder(@RequestBody @Valid CollegeSectionOrderForm collegeSectionOrderForm ,BindingResult result,   HttpServletRequest request,
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
			} 
			
			if (result.hasErrors()) {
				String message = ErrorUtils.getTextValidationErrorMessage(result.getAllErrors());
				data = new ErrorResponse();
				((ErrorResponse) data).setErrorCode(message);
				((ErrorResponse) data).setMessage("Form validation failed!");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				
			}
			
			else {
				try {					
					
					fileId=printStoreService.uploadFile( collegeSectionOrderForm.getFieType(), collegeSectionOrderForm.getFile());
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
		}*/
}
