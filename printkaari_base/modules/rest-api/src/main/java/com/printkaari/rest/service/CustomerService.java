package com.printkaari.rest.service;

import java.util.List;
import java.util.Map;

import com.printkaari.data.dao.entity.User;
import com.printkaari.data.dto.CustomerDto;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.InvalidProductException;
import com.printkaari.rest.exception.MailNotSendException;
import com.printkaari.rest.exception.StatusException;
import com.printkaari.rest.exception.UserNotFoundException;

public interface CustomerService {

	List<CustomerDto> fetchAllCandidatesByModifiedDate(Integer records) throws DatabaseException;

	Object fetchAllOrdersByCustomerId(Long customerId)throws DatabaseException;

	Object fetchAllCustomerByModifyDate(Integer records)throws DatabaseException;

	String fetchLoggedinCustomer() throws DatabaseException, UserNotFoundException;

	Object fetchCustomerByEmail(String email)throws DatabaseException, UserNotFoundException, InstanceNotFoundException, StatusException;

	User getLoggedinUser();

	Object fetchAllActiveOrdersByCustomerId(Long customerId, String string)throws DatabaseException;

	Map<String,Object> placeOrder(Integer glossyColorPages, Integer nonGlossyColorPages, String anyOtherRequest, Integer totalPages, String bindingType,Long fileId)throws DatabaseException,InvalidProductException, MailNotSendException;

	void confirmOrder(Long orderId)throws DatabaseException;

	void changeOrderStatus(String status, Long orderId) throws DatabaseException;

}
