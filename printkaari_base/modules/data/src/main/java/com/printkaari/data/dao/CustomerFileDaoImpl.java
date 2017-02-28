/**
 * 
 */
package com.printkaari.data.dao;

import org.springframework.stereotype.Repository;

import com.printkaari.data.dao.entity.CustomerFiles;

/**
 * @author Hemraj
 *
 */
@Repository
public class CustomerFileDaoImpl extends GenericDaoImpl<CustomerFiles, Long> implements CustomerFileDao  {

}
