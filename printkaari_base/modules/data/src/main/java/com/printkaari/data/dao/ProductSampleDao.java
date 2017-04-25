/**
 * 
 */
package com.printkaari.data.dao;

import java.util.List;

import com.printkaari.data.dao.entity.ProductSamples;

/**
 * @author Hemraj
 *
 */
public interface ProductSampleDao extends GenericDao<ProductSamples, Long>{

	List<ProductSamples> getSampleRecordByProductId(Long id);

}
