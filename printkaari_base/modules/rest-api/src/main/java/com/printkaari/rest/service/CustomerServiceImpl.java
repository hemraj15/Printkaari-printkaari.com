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
import com.printkaari.data.exception.InstanceNotFoundException;
import com.printkaari.message.exception.MailNotSentException;
import com.printkaari.message.model.MailMessage;
import com.printkaari.message.service.MailService;
import com.printkaari.rest.constant.CommonStatus;
import com.printkaari.rest.constant.CostConstant;
import com.printkaari.rest.constant.ErrorCodes;
import com.printkaari.rest.constant.ProductCodes;
import com.printkaari.rest.constant.UserTypes;
import com.printkaari.rest.exception.DatabaseException;
import com.printkaari.rest.exception.InvalidProductException;
import com.printkaari.rest.exception.MailNotSendException;
import com.printkaari.rest.exception.ProductNotFoundException;
import com.printkaari.rest.exception.StatusException;
import com.printkaari.rest.exception.UserNotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService {

	private Logger			LOGGER	= LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private UserDao			userDao;

	@Autowired
	private ProductDao		prodDao;

	@Autowired
	private OrderDao		ordDao;

	@Autowired
	private CustomerFileDao	custFileDao;

	@Autowired
	private CustomerDao		custDao;

	@Autowired
	private MailService		mailService;

	@Override
	@Transactional
	public List<CustomerDto> fetchAllCustomerByModifyDate(Integer records)
	        throws DatabaseException {
		List<CustomerDto> customerDtos = null;
		Integer toIndex = records;

		try {
			customerDtos = custDao.fetchAllCustomerByModifyDate(0, toIndex,
			        CommonStatus.ACTIVE.toString());

		} catch (Exception e) {
			LOGGER.error("Error occured while getting candidate list through database", e);
			throw new DatabaseException(
			        "Error occured while getting candidate list through database",
			        ErrorCodes.DATABASE_ERROR);
		}
		return customerDtos;

	}

	@Override
	@Transactional
	public Object fetchAllOrdersByCustomerId(String customerMailId) throws DatabaseException {

		List<Order> orders = null;
		Customer customer = null;

		try {

			customer = getCustomerByEmailId(customerMailId);

			if (customer != null) {
				orders = ordDao.fetchAllOrdersByCustomerId(customer.getId());
			} else {

				throw new UserNotFoundException(
				        " Customer not found Error occured while getting all orders for a customer through database",
				        ErrorCodes.DATABASE_ERROR);
			}

		} catch (Exception e) {
			LOGGER.error("Error occured while getting candidate list through database", e);
			e.printStackTrace();
			throw new DatabaseException(
			        "Error occured while getting all orders for a customer through database",
			        ErrorCodes.DATABASE_ERROR);
		}

		return orders;
	}

	/**
	 * @param customerMailId
	 * @return
	 * @throws InstanceNotFoundException
	 */
	private Customer getCustomerByEmailId(String customerMailId) throws InstanceNotFoundException {
		Customer customer;
		customer = (Customer) custDao.getByCriteria(custDao.getFindByEmailCriteria(customerMailId));
		return customer;
	}

	@Override
	public List<CustomerDto> fetchAllCandidatesByModifiedDate(Integer records)
	        throws DatabaseException {

		return null;
	}

	@Override
	public String fetchLoggedinCustomer() throws DatabaseException, UserNotFoundException {
		String email = "";
		User user = (User) AuthorizationUtil.getLoggedInUser();
		if (user.getUserType().equals(UserTypes.CUSTOMER.toString())) {
			email = user.getEmailId();
		} else {

			throw new UserNotFoundException("Logged in user is not a customer",
			        ErrorCodes.USER_NOT_FOUND_ERROR);
		}

		return email;
	}

	@Override
	@Transactional
	public Object fetchCustomerByEmail(String email) throws DatabaseException,
	        UserNotFoundException, InstanceNotFoundException, StatusException {
		Customer cust = null;
		User user = (User) userDao.getByCriteria(userDao.getFindByEmailCriteria(email));
		if (user == null) {

			System.out.println("user is null");
			throw new UserNotFoundException("No user found with this Email",
			        ErrorCodes.USER_NOT_FOUND_ERROR);
		} else {

			System.out.println("user found " + user.getUserType());
			System.out.println("user status :" + user.getStatus());

			if (user.getUserType().equals(UserTypes.CUSTOMER.toString())
			        && user.getStatus().equals(CommonStatus.ACTIVE.toString())) {

				cust = getCustomerByEmailId(user.getEmailId());

			}
			if (cust != null) {

				System.out.println("Customer found +" + cust.getFirstName());
				System.out.println("customer status" + cust.getStatus());
				System.out.println("customer email" + cust.getEmail());
			} else {

				throw new StatusException("User is not a customer or Inactive Customer");
			}

		}

		return cust;
	}

	@Override
	@Transactional
	public User getLoggedinUser() {

		System.out.println("Calling get logged in user from customer service impl ");
		return AuthorizationUtil.getLoggedInUser();
	}

	@Override
	@Transactional
	public Object fetchAllActiveOrdersByCustomerId(String userEmail, String status)
	        throws DatabaseException {
		List<Order> orders = null;
		Customer customer = null;

		// Map<String ,Object> data =new HashMap<>();

		try {

			customer = getCustomerByEmailId(userEmail);
			if (customer != null) {

				orders = ordDao.fetchAllActiveOrdersByCustomerId(customer.getId(), status);
			} else {

				LOGGER.info("getCustomerByeEmailId returned null cust object");
			}

		} catch (Exception e) {
			LOGGER.error("Error occured while getting candidate list through database", e);
			e.printStackTrace();
			throw new DatabaseException(
			        "Error occured while getting all orders for a customer through database",
			        ErrorCodes.DATABASE_ERROR);
		}

		return orders;
	}

	@Override
	@Transactional
	public Map<String, Object> placeOrder(Integer glossyColorPages, Integer nonGlossyColorPages,
	        String anyOtherRequest, Integer totalPages, String bindingType, Long fileId)
	        throws DatabaseException, InvalidProductException, MailNotSendException {
		Long orderId = null;
		String productCode = null;
		Order order = new Order();
		Product product = null;
		User user = null;
		Customer cust = null;
		Double basePrice = 0.0;
		Map<String, Object> map = new HashMap<>();
		Integer blackPage = totalPages - (glossyColorPages + nonGlossyColorPages);
		try {

			LOGGER.info("Place Order Customer Service Impl");
			LOGGER.info("Place Order for Bindig type ::" + bindingType);
			if (bindingType.equalsIgnoreCase("hard")) {
				productCode = ProductCodes.hard_binding.toString();
				basePrice = CostConstant.hard_binnding_base_tate;
			} else if (bindingType.equalsIgnoreCase("spiral")) {
				productCode = ProductCodes.spiral_binding.toString();
				basePrice = CostConstant.spiral_binding_base_rate;
			} else {

				LOGGER.info("Invalid Product type for College Projects Catagory");

				throw new InvalidProductException("Invalid Product for College Projects Catagory !",
				        ErrorCodes.INVALID_PRODUCT_ERROR);
			}
			user = (User) getLoggedinUser();

			LOGGER.info("Product code for College Projects Catagory " + productCode);

			if (user != null && user.getUserType().equals(UserTypes.CUSTOMER.toString())) {
				LOGGER.info("Initiate order Logged in user is " + user.getFirstName() + " user id  "
				        + user.getId());
				cust = getCustomerByEmailId(user.getEmailId().trim());
				if (cust != null) {

					LOGGER.info("Initiate order customer is " + cust.getFirstName() + " user id  "
					        + cust.getId());
					Double totalPrice = basePrice
					        + (glossyColorPages * CostConstant.color_glossy_page)
					        + (nonGlossyColorPages * CostConstant.color_non_glossy_page)
					        + (blackPage * CostConstant.simple_black_page);

					product = fetchProductCodeFromDB(productCode);

					if (product == null) {

						LOGGER.info("no product found for product code " + productCode);
						 throw new ProductNotFoundException("Product not found in the data base while placing order ",ErrorCodes.PRODUCT_NOT_FOUND_IN_DATABASE);
						
					} else {

						order.setCustomer(cust);
						order.setDescription(anyOtherRequest);
						order.setStatus(CommonStatus.INITIATED.toString());
						Set<Product> sets = new HashSet<>();
						sets.add(product);
						order.setProducts(sets);
						order.setOrderPrice(totalPrice);

						Set<CustomerFiles> fileSet = new HashSet<>();

						fileSet.add(custFileDao.find(fileId));

						order.setFileId(fileSet);

						order.setCreatedBy(cust.getFirstName());

						orderId = ordDao.save(order);
						map.put("order_id", orderId);
						map.put("total_price", order.getOrderPrice());

						LOGGER.info("OrderDao Save Order --> initiated with Order Id " + orderId);

						sendOrderStatusMailToCustomer(orderId, order.getStatus(), cust);
						sendOrderStatusMailToAdmin(orderId, cust);

					}

					
				}

				LOGGER.info("Customer object  is null or is not a customer  while placing order ");
			}

			LOGGER.info("User is null or is not a customer  while placing order ");
		} catch (Exception e) {
			LOGGER.error("Error occured while getting candidate list through database", e);
			e.printStackTrace();
			throw new DatabaseException(
			        "Error occured while getting all orders for a customer through database",
			        ErrorCodes.DATABASE_ERROR);
		}
		return map;
	}

	/**
	 * @param productCode
	 * @return
	 * @throws InstanceNotFoundException
	 */
	private Product fetchProductCodeFromDB(String productCode) throws InstanceNotFoundException {
		Product product;
		product = (Product) prodDao
		        .getByCriteria(prodDao.getByProductCode(productCode));
		return product;
	}

	private void sendOrderStatusMailToCustomer(Long orderId, String ordStatus, Customer cust)
	        throws MailNotSendException {
		MailMessage mailHtmlMessage = new MailMessage();
		String email = cust.getEmail();
		mailHtmlMessage.setSubject("Your Order Status here  !! ");
		mailHtmlMessage.setContent("<h2>Hello " + cust.getFirstName() + " " + cust.getLastName()
		        + "!</h2><h3>Your Order Status is " + ordStatus + " you can track your order  "
		        + "<a href=www.printkaari.com/#!/auth/login > here  </a></h3>");
		mailHtmlMessage.setToAddresses(new String[] { email });
		try {
			mailService.sendHtmlMail(mailHtmlMessage);
		} catch (MailNotSentException e) {
			LOGGER.error(e.getMessage(), e);
			throw new MailNotSendException("Error occurred while sending Order Status Email!",
			        ErrorCodes.EMAIL_ERROR);
		}
		LOGGER.info("HTML Email Sent to Customer");

	}

	private void sendOrderStatusMailToAdmin(Long orderId, Customer cust)
	        throws InstanceNotFoundException, MailNotSendException {

		try {
			User user = getAdminUser();
			String email = user.getEmailId();
			MailMessage mailHtmlMessage = new MailMessage();
			mailHtmlMessage.setSubject("Order Status mail !! ");
			mailHtmlMessage.setContent("<h2>Hello " + user.getFirstName()
			        + "</h2> <h3>One of customer " + cust.getFirstName() + " " + cust.getLastName()
			        + " has updated/placed order track order  "
			        + " <a href=www.printkaari.com/#!/auth/login >here</a> </h3>");
			mailHtmlMessage.setToAddresses(new String[] { email });

			mailService.sendHtmlMail(mailHtmlMessage);
		} catch (MailNotSentException e) {
			LOGGER.error(e.getMessage(), e);
			throw new MailNotSendException("Error occurred while sending order Status Email!",
			        ErrorCodes.EMAIL_ERROR);
		}
		LOGGER.info("HTML Email Sent to ADMIN");

	}

	/**
	 * @return
	 * @throws InstanceNotFoundException
	 */
	private User getAdminUser() throws InstanceNotFoundException {
		User user = (User) userDao.getByCriteria(userDao.getFingByeUserRole(SystemRoles.ADMIN));
		return user;
	}

	@Override
	@Transactional
	public void confirmOrder(Long orderId , String successCode) throws DatabaseException {
		try {
			LOGGER.info("Order about to confirm :" + orderId);
			LOGGER.info("Success Code to confirm order :" + successCode);
			Order ord = getOrderByOrderId(orderId);
			if (ord != null && successCode !=null) {
				LOGGER.info("Order " + orderId + " found");
				ord.setStatus(CommonStatus.ACTIVE.toString());
				ordDao.update(ord);
				LOGGER.info("Order " + orderId + " is confirmed ");
				Customer cust = ord.getCustomer();
				if (cust != null) {
					LOGGER.error("Customer associated with Order " + orderId
					        + " is found >> sending mails");
					sendOrderStatusMailToCustomer(ord.getId(), CommonStatus.ACTIVE.toString(),
					        cust);
					sendOrderStatusMailToAdmin(ord.getId(), cust);
					LOGGER.error("Mail sent to customer and admin ");
				} else {
					LOGGER.error("Customer associated with Order " + orderId + " is null");
				}

			}
			else{
				LOGGER.error(" Success Code is null !!");
			}
			
		} catch (Exception e) {
			LOGGER.error("Error occured while updating order in database", e);
			e.printStackTrace();
			throw new DatabaseException("Error occured while updating order in database",
			        ErrorCodes.DATABASE_ERROR);
		}

	}

	/**
	 * @param orderId
	 * @return
	 * @throws InstanceNotFoundException
	 */
	private Order getOrderByOrderId(Long orderId) throws InstanceNotFoundException {
		Order ord = ordDao.find(orderId);
		return ord;
	}

	@Override
	@Transactional
	public void changeOrderStatus(String status, Long orderId) throws DatabaseException {
		try {
			LOGGER.info("Order status about to change :" + orderId);
			Order ord = getOrderByOrderId(orderId);
			if (ord != null) {
				ord.setStatus(status);
				ordDao.update(ord);
				LOGGER.info("Status of Order " + orderId + " is updated to  " + status);

				Customer cust = ord.getCustomer();
				if (cust != null) {
					sendOrderStatusMailToCustomer(ord.getId(), status, cust);
					sendOrderStatusMailToAdmin(ord.getId(), cust);
				}

			}
		} catch (Exception e) {
			LOGGER.error("Error occured while getting candidate list through database", e);
			e.printStackTrace();
			throw new DatabaseException(
			        "Error occured while getting all orders for a customer through database",
			        ErrorCodes.DATABASE_ERROR);
		}

	}

}
