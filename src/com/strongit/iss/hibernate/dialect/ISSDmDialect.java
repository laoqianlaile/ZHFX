package com.strongit.iss.hibernate.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.DmDialect;

public class ISSDmDialect extends DmDialect {

	public ISSDmDialect() {
		super();

		registerHibernateType(-1, Hibernate.STRING.getName());
	}
}
