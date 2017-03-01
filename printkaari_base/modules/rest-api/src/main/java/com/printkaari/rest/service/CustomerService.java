package com.printkaari.rest.service;

import java.util.List;

import com.printkaari.data.dto.CustomerDto;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.InvalidProductException;
import com.printkaari.rest.exception.StatusException;
import com.printkaari.rest.exception.UserNotFoundException;

public interface CustomerService {

	List<CustomerDto> fetchAllCandidatesByModifiedDate(Integer records) throws DatabaseException;

	Object fetchAllOrdersByCustomerId(Long customerId)throws DatabaseException;

	Object fetchAllCustomerByModifyDate(Integer records)throws DatabaseException;

	Object fetchLoggedinCustomer() throws DatabaseException;

	Object fetchCustomerByEmail(String email)throws DatabaseException, UserNotFoundException, InstanceNotFoundException, StatusException;

	Object getLoggedinUser();

	Object fetchAllActiveOrdersByCustomerId(Long customerId, String string)throws DatabaseException;

	Long placeOrder(Integer glossyColorPages, Integer nonGlossyColorPages, String anyOtherRequest, Integer totalPages, String bindingType,Long fileId)throws DatabaseException,InvalidProductException;

}
