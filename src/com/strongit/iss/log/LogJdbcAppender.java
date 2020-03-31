package com.strongit.iss.log;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.apache.log4j.jdbc.JDBCAppender;

import com.strongit.iss.common.SpringContextUtil;
import com.strongit.iss.dao.DaoContext;

public class LogJdbcAppender extends JDBCAppender {
	protected final Logger logger = Logger.getLogger(getClass());	

	public LogJdbcAppender() {
		super();
	}

	@Override
	protected Connection getConnection() {
		try {
			connection = ((DaoContext) SpringContextUtil.getBean("daoContext"))
					.getSession().connection();

			return connection;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void closeConnection(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
				con = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
