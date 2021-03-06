package com.printkaari.data.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.printkaari.data.dao.entity.UrlType;

@Repository
public class UrlTypeDaoImpl extends GenericDaoImpl<UrlType, Long> implements UrlTypeDao {

	@Override
	public Criteria getListByStatusCriteria(String status) {
		return getCriteria().add(Restrictions.eq("status", status));
	}

}
