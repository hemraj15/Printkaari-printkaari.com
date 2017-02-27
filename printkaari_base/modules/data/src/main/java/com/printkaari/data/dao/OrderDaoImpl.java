/**
 * 
 */
package com.printkaari.data.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.printkaari.data.dao.entity.Order;
import com.printkaari.data.dto.OrderDto;

/**
 * @author Hemraj
 *
 */
@Repository
public class OrderDaoImpl extends GenericDaoImpl<Order, Long> implements OrderDao{

	
	@Override
	public List<Order> fetchAllOrdersByCustomerId(Long customerId) {
	
		List<OrderDto> dtos=null;
		List<Order> orders=null;
		
	/*	String orderQuerry="SELECT ord.id AS orderId,ord.status AS orderStatus,ord.price AS orderValue,ord.customer_id AS customerId ,"
		+"prd.id AS productId,prd.name AS productName "+
				" FROM cust_order ord,products prd where  ord.customer_id=?";
				//+  "SELECT ord.id,ord.status,ord.price,ord.customer_id AS customerId ,prd.id,prd.name "+
		//" FROM Order ord,Product prd where ord.customer_id=?";
		
		dtos=getSession().createSQLQuery(orderQuerry).setParameter(0, customerId).setResultTransformer(Transformers.aliasToBean(OrderDto.class)).list();
		*/
		/*Criteria crit=getSession().createCriteria(Order.class,"ord").createAlias("products","product").createAlias("customer","cust");
		
		crit.add(Restrictions.eq("cust.id", customerId));
		 crit.setProjection(Projections.projectionList().add(Projections.property("ord.id"),"id")
		.add(Projections.property("ord.dateCreated"),"dateCreated")
		 .add(Projections.property("ord.dateUpdated"),"dateUpdated")
		 .add(Projections.property("ord.lastModifiedBy"),"lastModified")
		 .add(Projections.property("ord.status"),"status")
		 .add(Projections.property("ord.orderPrice"),"orderPrice")
		 .add(Projections.property("product.id"),"id")
		 .add(Projections.property("product.name"),"name"))
		 .add(Restrictions.eq("cust.id", customerId))
	     .setResultTransformer(Transformers.aliasToBean(OrderDto.class));// here is the priblem
		 */
		
		Criteria crit=getCriteria().add(Restrictions.eq("customer.id", customerId));
				     /* .setProjection(Projections.projectionList().setResultTransformer(Transformers.aliasToBean(OrderDto.class));
				      .add(Projections.property("id"),"id")
				      .add(Projections.property("orderPrice"),"orderPrice")
				      .add(Projections.property("dateCreated"),"dateCreated")
				      .add(Projections.property("dateUpdated"),"dateUpdated")
				      .add(Projections.property("status"),"status"))
				      .setResultTransformer(Transformers.aliasToBean(OrderDto.class));*/
		         
		 
		// System.out.println("order dto list for customer ----- > "+dtos.size());
		orders=crit.list();
		
		System.out.println("order dto list for customer "+customerId);
		return orders;
	}

}
