package com.printkaari.rest.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prinktaakri.auth.util.AuthorizationUtil;
import com.printkaari.auth.service.SystemRoles;
import com.printkaari.data.dao.CustomerDao;
import com.printkaari.data.dao.CustomerFileDao;
import com.printkaari.data.dao.OrderDao;
import com.printkaari.data.dao.ProductDao;
import com.printkaari.data.dao.UserDao;
import com.printkaari.data.dao.entity.Customer;
import com.printkaari.data.dao.entity.CustomerFiles;
import com.printkaari.data.dao.entity.Order;
import com.printkaari.data.dao.entity.Product;
import com.printkaari.data.dao.entity.User;
import com.printkaari.data.dto.CustomerDto;
import com.printkaari.data.dto.OrderDto;
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.CostConstant;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.constant.ProductCodes;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.InvalidProductException;
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
	private ProductDao prodDao;
	
	@Autowired
	private OrderDao ordDao;
	
	@Autowired
	private CustomerFileDao custFileDao;
	
	@Autowired
	private CustomerDao		custDao;
	
	@Autowired
	private CustomerService customerService;

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
		
		List<Order> orders=null;
		Customer customer=null;
		
		try {
			
			orders=ordDao.fetchAllOrdersByCustomerId(customerId);
			//customer=customerDao.find(customerId);
		} catch (Exception e) {
			   LOGGER.error("Error occured while getting candidate list through database", e);
			   e.printStackTrace();
			   throw new DatabaseException("Error occured while getting all orders for a customer through database",
			           ErrorCodes.DATABASE_ERROR);
			  }
		
		return orders;
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
				
			}if(cust!=null){
				
				System.out.println("customer status"+cust.getStatus());
				System.out.println("customer status"+cust.getEmail());
			}else {
				
				
				throw new StatusException("User is not a customer or Inactive Customer");
			}
			
		}
		
		return cust;
	}

	@Override
	@Transactional
	public Object getLoggedinUser() {
		return (User)AuthorizationUtil.getLoggedInUser();
	}

	@Override
	@Transactional
	public Object fetchAllActiveOrdersByCustomerId(Long customerId,String status) throws DatabaseException {
		List<Order> orders=null;
		Customer customer=null;
		
		Map<String ,Object> data =new HashMap<>();
		
		try {
			
			orders=ordDao.fetchAllActiveOrdersByCustomerId(customerId,status);
			
			//customer=customerDao.find(customerId);
		} catch (Exception e) {
			   LOGGER.error("Error occured while getting candidate list through database", e);
			   e.printStackTrace();
			   throw new DatabaseException("Error occured while getting all orders for a customer through database",
			           ErrorCodes.DATABASE_ERROR);
			  }
		
		return orders;
	}

	@Override
	@Transactional
	public Long placeOrder(Integer glossyColorPages,Integer nonGlossyColorPages, String anyOtherRequest, Integer totalPages,String bindingType,Long fileId) throws DatabaseException,InvalidProductException {
		Long orderId=null;
		String productCode=null;
		
		Order order=new Order();
		Product product=null;
		User user=null;
		Customer cust=null;
		Double basePrice=0.0;
		Integer blackPage=totalPages-(glossyColorPages+nonGlossyColorPages);
		try {			
			
			
			if(bindingType.equals("hard")){
				productCode=ProductCodes.hard_binding.toString();
				basePrice=CostConstant.hard_binnding_base_tate;
			}
			else if (bindingType.equals("spiral")){
				productCode=ProductCodes.spiral_binding.toString();
				basePrice=CostConstant.spiral_binding_base_rate;
			}
			else{
				
				throw new InvalidProductException("Invalid Product !",
				        ErrorCodes.INVALID_PRODUCT_ERROR);
			}
            user=(User)customerService.getLoggedinUser();
			
			
			if(user !=null && user.getUserType().equals(SystemRoles.CUSTOMER)){
				
				cust=(Customer)custDao.getByCriteria(custDao.getFindByEmailCriteria(user.getEmailId()));
				
				if(cust !=null){	
					
			
			Double totalPrice=basePrice+(glossyColorPages*CostConstant.color_glossy_page)+(nonGlossyColorPages*CostConstant.color_non_glossy_page)+(blackPage*CostConstant.simple_black_page);
			
			product=(Product)prodDao.getByCriteria(prodDao.getByProductCode(productCode));
			
			order.setCustomer(cust);
			order.setDescription(anyOtherRequest);
			order.setStatus(CommonStatus.INITIATED.toString());
			Set<Product> sets=new HashSet<>();
			sets.add(product);
			order.setProducts(sets);
			order.setOrderPrice(totalPrice);			
			
			Set<CustomerFiles> fileSet=new HashSet<>();
			
			fileSet.add(custFileDao.find(fileId));
			
			order.setFileId(fileSet);
			
			order.setCreatedBy(cust.getFirstName());
			
			
			orderId=ordDao.save(order);
			
				}
				
			}
			
		} catch (Exception e) {
			   LOGGER.error("Error occured while getting candidate list through database", e);
			   e.printStackTrace();
			   throw new DatabaseException("Error occured while getting all orders for a customer through database",
			           ErrorCodes.DATABASE_ERROR);
			  }
		return orderId;
	}

}
