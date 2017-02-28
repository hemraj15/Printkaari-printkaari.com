/**
 * 
 */
package com.printkaari.data.dao;

import java.util.List;

import com.printkaari.data.dao.entity.Order;
import com.printkaari.data.dto.OrderDto;

/**
 * @author Hemraj
 *
 */
public interface OrderDao extends GenericDao<Order, Long> {

	List<Order> fetchAllOrdersByCustomerId(Long customerId);

	List<Order> fetchAllActiveOrdersByCustomerId(Long customerId , String status);

}
