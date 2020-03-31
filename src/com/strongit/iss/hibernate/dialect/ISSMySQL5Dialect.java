package com.strongit.iss.hibernate.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;

public class ISSMySQL5Dialect extends MySQL5Dialect {
	
	public ISSMySQL5Dialect() {
		super();

		registerHibernateType(-1, Hibernate.STRING.getName());
	}
}
