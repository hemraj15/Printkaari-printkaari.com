package com.printkaari.rest.service;

import java.util.List;
import java.util.Map;

import com.printkaari.data.dao.entity.User;
import com.printkaari.data.dto.CustomerDto;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.InvalidNumberOfPagesException;
import com.printkaari.rest.exception.InvalidProductException;
import com.printkaari.rest.exception.InvalidQuantiryException;
import com.printkaari.rest.exception.MailNotSendException;
import com.printkaari.rest.exception.StatusException;
import com.printkaari.rest.exception.UserNotFoundException;

public interface CustomerService {

	List<CustomerDto> fetchAllCandidatesByModifiedDate(Integer records) throws DatabaseException;

	Object fetchAllOrdersByCustomerId(String mailId)throws DatabaseException;

	Object fetchAllCustomerByModifyDate(Integer records)throws DatabaseException;

	String fetchLoggedinCustomer() throws DatabaseException, UserNotFoundException;

	Object fetchCustomerByEmail(String email)throws DatabaseException, UserNotFoundException, InstanceNotFoundException, StatusException;

	User getLoggedinUser();

	Object fetchAllActiveOrdersByCustomerId(String string2, String string)throws DatabaseException;

	Map<String,Object> placeOrder(Integer glossyColorPages, Integer nonGlossyColorPages, String anyOtherRequest, Integer totalPages, String bindingType,Long fileId, Integer totalColorPage, Integer quantity, String colorPages)throws DatabaseException,InvalidProductException, MailNotSendException, InvalidNumberOfPagesException, InvalidQuantiryException;

	void confirmOrder(Long orderId, String string)throws DatabaseException;

	void changeOrderStatus(String status, Long orderId) throws DatabaseException;

}
