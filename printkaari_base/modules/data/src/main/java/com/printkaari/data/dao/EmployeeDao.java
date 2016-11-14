/**
 * 
 */
package com.printkaari.data.dao;

import org.hibernate.Criteria;

import com.printkaari.data.dao.entity.Employee;

/**
 * @author Hemraj
 *
 */
public interface EmployeeDao extends GenericDao<Employee, Long>{

	Criteria getFindByEmailCriteria(String email);

}
