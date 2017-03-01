/**
 * 
 */
package com.printkaari.data.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.printkaari.data.dao.entity.Product;
import com.printkaari.data.dto.ProductDto;

/**
 * @author Hemraj
 *
 */
@Repository
public class ProductDaoImpl extends GenericDaoImpl<Product, Long> implements ProductDao{
	private Logger				LOGGER	= LoggerFactory.getLogger(ProductDaoImpl.class);
	
	
	@Override
	public List<ProductDto> fetchAllProducts(String status) {
		LOGGER.info("ProductDaoImpl fetchAllProducts <<<");
		List<ProductDto> prodDtos=null;
		LOGGER.info("ProductDaoImpl status : "+status);
	
		Criteria crit=getCriteria().add(Restrictions.eq("status", status))
				      .setProjection(Projections.projectionList().add(Projections.property("id"),"id")
				      .add(Projections.property("name"),"name")
				      .add(Projections.property("status"),"status"))
				      .setResultTransformer(Transformers.aliasToBean(ProductDto.class));
		prodDtos = crit.list();
				
		LOGGER.info("ProductDaoImpl prodDtos : "+prodDtos.size());
		return prodDtos;
	}
	@Override
	public List<ProductDto> fetchAllProductsByCategoryId(Long catId) {
		LOGGER.info("ProductDaoImpl fetchAllProducts <<<");
		List<ProductDto> prodDtos=null;
		LOGGER.info("ProductDaoImpl categoryId : "+catId);
	
		Criteria crit=getCriteria().createAlias("catagory", "cat").add(Restrictions.eq("cat.id", catId))
				      .setProjection(Projections.projectionList().add(Projections.property("id"),"id")
				      .add(Projections.property("name"),"name")
				      .add(Projections.property("status"),"status"))
				      .setResultTransformer(Transformers.aliasToBean(ProductDto.class));
		prodDtos = crit.list();
				
		LOGGER.info("ProductDaoImpl prodDtos size : "+prodDtos.size());
		return prodDtos;
	}
	@Override
	public Criteria getByProductCode(String productCode) {
		LOGGER.info("ProductDaoImpl : product code ::"+productCode);
		
		Criteria crit=getCriteria().add(Restrictions.eq("productCode", productCode));
		return crit;
	}

}
