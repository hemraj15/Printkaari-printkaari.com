package com.printkaari.rest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prinktaakri.auth.util.AuthorizationUtil;
import com.printkaari.auth.service.SystemRoles;
import com.printkaari.data.dao.CustomerDao;
import com.printkaari.data.dao.OrderDao;
import com.printkaari.data.dao.UserDao;
import com.printkaari.data.dao.entity.Customer;
import com.printkaari.data.dao.entity.User;
import com.printkaari.data.dto.CustomerDto;
import com.printkaari.data.dto.OrderDto;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.SignUpException;
import com.printkaari.rest.exception.StatusException;
import com.printkaari.rest.exception.UserNotFoundException;


@Service
public class CustomerServiceImpl implements CustomerService {

	private Logger			LOGGER	= LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerDao	customerDao;
	
	@Autowired
	private UserDao		userDao;
	
	
	@Autowired
	private OrderDao orderDao;

	@Override
	@Transactional
	public List<CustomerDto> fetchAllCustomerByModifyDate(Integer records) throws DatabaseException {
		List<CustomerDto> customerDtos = null;
		Integer toIndex = records;

		try {
			customerDtos = customerDao.fetchAllCustomerByModifyDate(0, toIndex,
			        CommonStatus.ACTIVE.toString());

		} 
		catch (Exception e) {
			   LOGGER.error("Error occured while getting candidate list through database", e);
			   throw new DatabaseException("Error occured while getting candidate list through database",
			           ErrorCodes.DATABASE_ERROR);
			  }
		return customerDtos;

	}

	@Override
	@Transactional
	public Object fetchAllOrdersByCustomerId(Long customerId) throws DatabaseException {
		
		//Customer customer = null;
		
		List<OrderDto> orderDtos=null;
		
		try {
			
			orderDtos=orderDao.fetchAllOrdersByCustomerId(customerId);
			//customer=customerDao.find(customerId);
		} catch (Exception e) {
			   LOGGER.error("Error occured while getting candidate list through database", e);
			   e.printStackTrace();
			   throw new DatabaseException("Error occured while getting all orders for a customer through database",
			           ErrorCodes.DATABASE_ERROR);
			  }
		
		return orderDtos;
	}

	@Override
	public List<CustomerDto> fetchAllCandidatesByModifiedDate(Integer records) throws DatabaseException {


		return null;
	}

	@Override
  public Object fetchLoggedinCustomer() throws DatabaseException {
    
              
		
		return AuthorizationUtil.getLoggedInUser();  
	}

	@Override
	@Transactional
	public Object fetchCustomerByEmail(String email) throws DatabaseException, UserNotFoundException, InstanceNotFoundException, StatusException {
		Customer cust=null;
		User user = (User) userDao.getByCriteria(userDao.getFindByEmailCriteria(email));
		if (user == null) {
			
			System.out.println("user is null");
			throw new UserNotFoundException("No user found with this Email",
			        ErrorCodes.USER_NOT_FOUND_ERROR);
		}else{
			
			System.out.println("user found "+user.getUserType());
			System.out.println("user status :"+user.getStatus());
			
			if(user.getUserType().equals(SystemRoles.CUSTOMER) && user.getStatus().equals(CommonStatus.ACTIVE.toString())){
				
				 cust=(Customer)customerDao.getByCriteria(customerDao.getFindByEmailCriteria(email));
				 
				 System.out.println("Customer found +"+cust.getFirstName());
				
			}else {
				
				System.out.println("customer status"+cust.getStatus());
				System.out.println("customer status"+cust.getEmail());
				throw new StatusException("User is not a customer or Inactive Customer");
			}
			
		}
		
		return cust;
	}

}
