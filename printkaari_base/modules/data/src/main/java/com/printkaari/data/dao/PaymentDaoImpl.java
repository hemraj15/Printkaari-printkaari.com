/**
 * 
 */
package com.printkaari.data.dao;

import org.springframework.stereotype.Repository;

import com.printkaari.data.dao.entity.CustomerTransaction;

/**
 * @author Hemraj
 *
 */
@Repository
public class PaymentDaoImpl extends GenericDaoImpl<CustomerTransaction, Long> implements PaymentDao{

}
