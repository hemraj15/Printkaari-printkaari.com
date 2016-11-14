/**
 * 
 */
package com.printkaari.data.dao;

import org.springframework.stereotype.Repository;

import com.printkaari.data.dao.entity.Product;

/**
 * @author Hemraj
 *
 */
@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao{

}
