package com.strongit.iss.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.strongit.iss.common.ResultToMap;
import com.strongit.iss.exception.DAOException;
import com.strongit.iss.orm.hibernate.Page;
import com.strongit.iss.orm.hibernate.ProjectPage;
import com.strongit.iss.util.BeanUtils;
@Repository
public class DaoContext {
	protected final Logger logger = Logger.getLogger(getClass());
	protected SessionFactory sessionFactory;
	protected Criteria criteria;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}

	public Session getSession() throws DAOException {
		try {
			return this.getSessionFactory().getCurrentSession();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public Connection getConnection() throws DAOException {
		try {
			return this.getSession().connection();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public <T> void save(T entity) throws DAOException {
		try {
			this.getSession().save(entity);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public <T> void save(List<T> entities) throws DAOException {
		if (entities != null && entities.size() > 0) {
			for (Object entity : entities) {
				this.save(entity);
			}
		}
	}

	public <T> void saveOrUpdate(T entity) throws DAOException {
		try {
			getSession().saveOrUpdate(entity);
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	public <T> void delete(T entity) throws DAOException {
		try {
			this.getSession().delete(entity);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public <T> void deleteById(Class<T> clazz, Serializable id)
			throws DAOException {
		this.delete(get(clazz, id));
	}

	public <T> void delete(Class<T> clazz, Serializable[] ids)
			throws DAOException {
		if (ids != null && ids.length > 0) {
			for (Serializable id : ids) {
				this.deleteById(clazz, id);
			}
		}
	}

	public <T> void delete(List<T> entities) throws DAOException {
		if (entities != null && entities.size() > 0) {
			for (T entity : entities) {
				this.delete(entity);
			}
		}
	}

	public <T> void update(T entity) throws DAOException {
		try {
			this.getSession().update(entity);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public <T> void update(List<T> entities) throws DAOException {
		if (entities != null && entities.size() > 0) {
			for (T entity : entities) {
				this.update(entity);
			}
		}
	}

	public <T> List<T> findAll(Class<T> clazz) throws DAOException {
		return find(clazz, new Criterion[0]);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> find(Class<T> clazz, Criterion... criterions) {
		return createCriteria(clazz, criterions).list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> find(String hql, Object... values) {
		return (List<T>) createQuery(hql, values).list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> find(String hql, Map<String, ?> values) {
		return (List<T>) createQuery(hql, values).list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findSql(Class clazz, String sql, Object... values) {
		return (List<T>) createSqlQuery(sql, values).addEntity(clazz).list();
	}

	@SuppressWarnings("unchecked")
	public <T> T findByUnique(Class<T> clazz, String propertyName, Object value) {
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(clazz, new Criterion[] { criterion })
				.uniqueResult();
	}

	public <T> Page<T> findAll(Class<T> clazz, Page<T> page)
			throws DAOException {
		return findByCriteria(clazz, page, new Criterion[0]);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz, Serializable id) throws DAOException {
		try {
			return (T) getSession().get(clazz, id);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T load(Class<T> clazz, Serializable id) throws DAOException {
		try {
			return (T) getSession().load(clazz, id);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T findById(Class<T> clazz, Serializable id, boolean lock)
			throws DAOException {
		try {
			T entity;
			if (lock) {
				entity = (T) getSession().load(clazz, id, LockMode.UPGRADE);
			} else {
				entity = (T) getSession().load(clazz, id);
			}
			return (T) entity;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public Object findUnique(String hql, Object... values) throws DAOException {
		try {
			return createQuery(hql, values).uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	public Object findUniqueSql(String sql, Object... values)
			throws DAOException {
		try {
			return createSqlQuery(sql, values).uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public Object findUniqueSql(String sql, Map<String, ?> values)
			throws DAOException {
		try {
			return createSqlQuery(sql, values).uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public Integer findInt(String hql, Object... values) throws DAOException {
		return (Integer) findUnique(hql, values);
	}

	public Long findLong(String hql, Object... values) throws DAOException {
		return (Long) findUnique(hql, values);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByCriteria(Class<T> clazz, Criterion... criterion)
			throws DAOException {
		try {
			return this.createCriteria(clazz, criterion).list();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public <T> List<T> findByProperty(Class<T> clazz, String propertyName,
			Object value) {
		Criterion criterion = Restrictions.eq(propertyName, value);
		return find(clazz, new Criterion[] { criterion });
	}

	@SuppressWarnings("unchecked")
	public <T> T findUniqueByProperty(Class<T> clazz, String propertyName,
			Object value) throws DAOException {
		try {
			return (T) createCriteria(clazz,
					new Criterion[] { Restrictions.eq(propertyName, value) })
					.uniqueResult();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T findSqlUnique(Class<T> clazz, String sql, Object... values) {
		try {
			return (T) createSqlQuery(sql, values).addEntity(clazz)
					.uniqueResult();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findBySql(String sql, Object... values) {
		try {
			SQLQuery q = createSqlQuery(sql, values);
			List<Map<String, Object>> list = q.setResultTransformer(
					new ResultToMap()).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findBySql(String sql,
			Map<String, Object> values) {
		try {
			SQLQuery q = createSqlQuery(sql, values);
			List<Map<String, Object>> list = q.setResultTransformer(
					new ResultToMap()).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		}
	}

	public <T> List<T> findBySql(Class<T> clazz, String sql, Object... values) {
		try {
			return createSqlQuery(sql, values).addEntity(clazz).list();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	public <T> List<T> findBySql(Class<T> clazz, String sql, Map<String, ?> values) {
		try {
			return createSqlQuery(sql, values).addEntity(clazz).list();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}
	
	
	public <T> List<T> findBySqlToBean(Class<T> clazz, String sql, Object... values) {
		try {
			return createSqlQuery(sql, values).setResultTransformer(Transformers.aliasToBean(clazz)).list();
		} catch (Exception e) {
			throw new DAOException(e);
		}
	}

	/**
	 * 通过sql查询数据库，返回object数组
	 * 
	 * @author 肖红飞
	 * @createDate 2014-11-4下午08:49:44
	 * @arithMetic
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Object[]> findBySqlObj(String sql, Object... values) {

		return createSqlQuery(sql, values).list();
	}

	/**
	 * 通过sql查询数据库，返回object数组
	 * 
	 * @author 肖红飞
	 * @createDate 2014-11-4下午08:50:28
	 * @arithMetic
	 * @param sql
	 * @param values
	 * @return
	 */
	public List<Object[]> findBySqlObj(String sql, Map<String, Object> values) {

		return createSqlQuery(sql, values).list();
	}

	public SQLQuery createSqlQuery(String queryString, Object... values) {
		SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (values[i] != null) {
					sqlQuery.setParameter(i, values[i]);
				}
			}
		}
		return sqlQuery;
	}

	protected SQLQuery createSqlQuery(String queryString, Map<String, ?> values) {
		SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
		if (values != null) {
			sqlQuery.setProperties(values);
		}
		return sqlQuery;
	}

	public Query createQuery(String queryString, Object... values)
			throws DAOException {
		try {
			Query queryObject = this.getSession().createQuery(queryString);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					if (values[i] != null) {
						queryObject.setParameter(i, values[i]);
					}
				}
			}
			return queryObject;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString,
			final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	public <T> Criteria createCriteria(Class<T> clazz, Criterion... criterions)
			throws DAOException {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	public <T> boolean isPropertyUnique(Class<T> clazz, String propertyName,
			Object newValue, Object orgValue) throws DAOException {
		if ((newValue == null) || (newValue.equals(orgValue))) {
			return true;
		}
		Object object = findUniqueByProperty(clazz, propertyName, newValue);
		return object == null;
	}

	public <T> String getIdName(Class<T> clazz) {
		ClassMetadata meta = getSessionFactory().getClassMetadata(clazz);
		return meta.getIdentifierPropertyName();
	}

	public void executeJdbcUpdate(String sql) throws DAOException {
		try {
			this.getConnection().prepareStatement(sql).execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public ResultSet executeJdbcQuery(String sql) throws DAOException {
		try {
			return this.getConnection().prepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Page<T> find(Page<T> page, String hql, Object... values)
			throws DAOException {
		try {
			String countQueryString = " select count(*) "
					+ removeSelect(removeOrders(hql));

			if (page.isAutoCount()) {
				try {
					Query cq = createQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			Query q = createQuery(hql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<T> result = q.list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public <T> Page<T> find(Page<T> page, String hql, Map<String, ?> values)
			throws DAOException {
		try {
			String countQueryString = " select count(*) "
					+ removeSelect(removeOrders(hql));

			if (page.isAutoCount()) {
				try {
					Query cq = createQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			Query q = createQuery(hql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<T> result = q.list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Page<T> findBySql(Class<T> clazz, Page<T> page, String sql,
			Object... values) throws DAOException {
		try {
			String countQueryString = " select count(1) "
					+ removeSelect(removeOrders(sql));

			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			q.addEntity(clazz);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<T> result = q.list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public <T> Page<T> findBySql(Class<T> clazz, Page<T> page, String sql,
			Map<String, ?> values) throws DAOException {
		try {
			String countQueryString = " select count(1) "
					+ removeSelect(removeOrders(sql));

			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					//Long count = ((BigInteger) cq.uniqueResult()).longValue();
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			q.addEntity(clazz);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<T> result = q.list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> int countQueryResult(Page<T> page, Criteria c)
			throws DAOException {
		try {
			CriteriaImpl impl = (CriteriaImpl) c;

			Projection projection = impl.getProjection();
			ResultTransformer transformer = impl.getResultTransformer();

			List<T> orderEntries = null;
			try {
				orderEntries = (List<T>) BeanUtils.getFieldValue(impl,
						"orderEntries");
				BeanUtils.setFieldValue(impl, "orderEntries",
						new ArrayList<T>());
			} catch (Exception e) {
				this.logger.error("不可能抛出的异常");
			}

			int totalCount = ((Integer) c.setProjection(Projections.rowCount())
					.uniqueResult()).intValue();

			if (totalCount < 1) {
				totalCount = -1;
			}

			c.setProjection(projection);

			if (projection == null) {
				c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			}
			if (transformer != null) {
				c.setResultTransformer(transformer);
			}
			try {
				BeanUtils.setFieldValue(impl, "orderEntries", orderEntries);
			} catch (Exception e) {
				this.logger.error("不可能抛出的异常");
			}

			return totalCount;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Page<T> findByCriteria(Class<T> clazz, Page<T> page,
			Criterion[] criterion) throws DAOException {

		try {
			Criteria c = createCriteria(clazz, criterion);

			if (page.isAutoCount()) {
				try {
					page.setTotalCount(countQueryResult(page, c));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (page.isFirstSetted()) {
				c.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				c.setMaxResults(page.getPageSize());
			}

			if (page.isOrderBySetted()) {
				if (page.getOrder().endsWith("asc"))
					c.addOrder(Order.asc(page.getOrderBy()));
				else {
					c.addOrder(Order.desc(page.getOrderBy()));
				}
			}

			List<T> result = c.list();

			page.setResult(result);

			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	private String removeSelectWithMulSelect(String hql){
		String subString = hql;
		do{
			subString = removeSelect(subString);
			int a = subString.toLowerCase().indexOf(" as ");
			if(a>-1){
				subString = subString.substring(a);
			}
		}while(subString.toLowerCase().indexOf(" as ")>-1);
		return subString;
	}
	private String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}

	private static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", 2);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		/*if(hql.trim().endsWith(")")){
			sb.append(")");
		}*/
		return sb.toString();
	}

	public void setFlushMode(FlushMode mode) {
		this.getSession().setFlushMode(mode);
	}

	public void flush() throws DAOException {
		try {
			this.getSession().flush();
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public <T> void merge(T entity) throws DAOException {
		try {
			this.getSession().merge(entity);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	
	
	public <T> void merge(List<T> entities) throws DAOException {
		if (entities != null && entities.size() > 0) {
			for (Object entity : entities) {
				this.merge(entity);
			}
		}
	}

	public void clear() throws DAOException {
		this.getSession().clear();
	}

	public <T> void evict(T entity) throws DAOException {
		try {
			this.getSession().evict(entity);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	public void refresh(Object entity) throws DAOException {
		try {
			this.getSession().refresh(entity);
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行SQL进行修改/删除操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int executeSql(final String sql, final Object... values) {
		return createSqlQuery(sql, values).executeUpdate();
	}

	/**
	 * 执行SQL进行修改/删除操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int executeSql(final String sql, final Map<String, ?> values) {
		return createSqlQuery(sql, values).executeUpdate();
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param sql
	 * @param values
	 * @return
	 */
	public Page<Map<String, Object>> findBySql(Page<Map<String, Object>> page,
			String sql, Map<String, Object> values) {

		try {
			String countQueryString = " select count(1) "
					+ removeSelect(removeOrders(sql));

			if (page.isOrderBySetted()) {
				countQueryString += "order by " + page.getOrder();
			}

			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);

			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	
	
	public Page<Map<String, Object>> findBySql1(Page<Map<String, Object>> page,
			String sql, Map<String, Object> values) {

		try {
			String countQueryString = " select count(1) "
					+ removeSelectWithMulSelect(removeOrders(sql));

			if (page.isOrderBySetted()) {
				countQueryString += "order by " + page.getOrder();
			}

			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);

			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	
	
	public ProjectPage<Map<String, Object>> findProjectBySql(ProjectPage<Map<String, Object>> page,
			String sql, Object... values) {

		try {
			String countQueryString = " select count(1), NVL(sum(to_number(X013)),0) " + removeSelect(removeOrders(sql));

			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object[] objects = (Object[]) cq.uniqueResult();
					Long count = 0l;
					Object o = objects[0];
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
					page.setTotalInvestment(Double.valueOf(objects[1].toString()));
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
					page.setTotalInvestment(0D);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	/**
	 * 查询字段中含有select子句
	 * @author			李奥伟
	 * @createDate		2016年4月29日上午09:44:40
	 * @arithMetic                                                                           
	 * @param page
	 * @param sql
	 * @param values
	 * @return
	 */
	public ProjectPage<Map<String, Object>> findProjectBySqlWithMulSelect(ProjectPage<Map<String, Object>> page,
			String sql, Object... values) {

		try {
			String countQueryString = " select count(1), NVL(sum(to_number(X013)),0) " + removeSelectWithMulSelect(removeOrders(sql));

			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object[] objects = (Object[]) cq.uniqueResult();
					Long count = 0l;
					Object o = objects[0];
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
					page.setTotalInvestment(Double.valueOf(objects[1].toString()));
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
					page.setTotalInvestment(0D);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	/**
	 * 使用D01统计所有项目总金额
	 * @author			王曙华
	 * @createDate		2016-3-9下午4:46:05
	 * @arithMetic                                                                           
	 * @param page
	 * @param sql
	 * @param values
	 * @return
	 */
	public ProjectPage<Map<String, Object>> findProjectBySqlD01(ProjectPage<Map<String, Object>> page,
			String sql, Object... values) {
		
		try {
			String countQueryString = " select count(1), NVL(sum(to_number(D01)),0) " + removeSelect(removeOrders(sql));
			countQueryString = countQueryString + ")";
			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object[] objects = (Object[]) cq.uniqueResult();
					Long count = 0l;
					Object o = objects[0];
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
					page.setTotalInvestment(Double.valueOf(objects[1].toString()));
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
					page.setTotalInvestment(0D);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}
			
			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	public ProjectPage<Map<String, Object>> findDispatchProjectBySqlD01(ProjectPage<Map<String, Object>> page,
			String sql, Object... values) {
		
		try {
			String countQueryString = " select count(1), NVL(sum(to_number(D01)),0) from ( " + removeOrders(sql);
			countQueryString = countQueryString + ")";
			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object[] objects = (Object[]) cq.uniqueResult();
					Long count = 0l;
					Object o = objects[0];
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
					page.setTotalInvestment(Double.valueOf(objects[1].toString()));
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
					page.setTotalInvestment(0D);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}
			
			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	
	public ProjectPage<Map<String, Object>> findBySql(ProjectPage<Map<String, Object>> page,
			String sql, Object... values) {

		try {
			String countQueryString = " select count(1) "
					+ removeSelect(removeOrders(sql));

			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param sql
	 * @param values
	 * @return
	 */
	public Page<Map<String, Object>> findBySql(Page<Map<String, Object>> page,
			String sql, Object... values) {

		try {
			String countQueryString = " select count(1) "
					+ removeSelect(removeOrders(sql));
			countQueryString = countQueryString.replace("left join  v_early_warning yp  on p.id=yp.project_id", " ");
			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}

			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	public Page<Map<String, Object>> findBySqlDispatch(Page<Map<String, Object>> page,
			String sql, Object... values) {
		
		try {
			String countQueryString = " select count(1)  "
					+ removeSelectWithMulSelect(removeSelect(removeOrders(sql)));
			
			if (page.isAutoCount()) {
				try {
					SQLQuery cq = createSqlQuery(countQueryString, values);
					Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
					page.setTotalCount(count.intValue());
				} catch (Exception e) {
					e.printStackTrace();
					page.setTotalCount(0);
				}
			}
			if (page.getPageNo() > page.getTotalPages()) {
				page.setPageNo(1);
			}
			
			SQLQuery q = createSqlQuery(sql, values);
			if (page.isFirstSetted()) {
				q.setFirstResult(page.getFirst());
			}
			if (page.isPageSizeSetted()) {
				q.setMaxResults(page.getPageSize());
			}
			List<Map<String, Object>> result = q.setResultTransformer(
					new ResultToMap()).list();
			page.setResult(result);
			return page;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	
	/**
	 * 分页查询-稽察审核专用
	 * @author			胡军
	 * @createDate		2015年10月24日上午10:37:36
	 * @arithMetic                                                                           
	 * @param page
	 * @param sql
	 * @param values
	 * @return
	 */
    public Page<Map<String, Object>> findBySqlToAudit(Page<Map<String, Object>> page,
            String sql, Object... values) {

        try {
            String countQueryString = " select count(1) "
                    + removeSelect(sql);

            if (page.isAutoCount()) {
                try {
                    SQLQuery cq = createSqlQuery(countQueryString, values);
                    Object o = cq.uniqueResult();
					Long count = 0l;
					if(o instanceof Long){
						count = (Long) o;
					}else if(o instanceof BigDecimal){
						BigDecimal b = (BigDecimal) o;
						count = b.longValue();
					}else if(o instanceof BigInteger){
						BigInteger b = (BigInteger) o;
						count = b.longValue();
					}
                    page.setTotalCount(count.intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    page.setTotalCount(0);
                }
            }
            if (page.getPageNo() > page.getTotalPages()) {
                page.setPageNo(1);
            }

            SQLQuery q = createSqlQuery(sql, values);
            if (page.isFirstSetted()) {
                q.setFirstResult(page.getFirst());
            }
            if (page.isPageSizeSetted()) {
                q.setMaxResults(page.getPageSize());
            }
            List<Map<String, Object>> result = q.setResultTransformer(
                    new ResultToMap()).list();
            page.setResult(result);
            return page;
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
    }
    
    /**
     * 计算总投资-稽察审核专用
     * @author          胡军
     * @createDate      2015年10月23日下午3:27:48
     * @arithMetic                                                                           
     * @param sql
     * @param values
     * @return
     */
    public Double countInvestmentToAudit(String sql, Object... values) {

        try {
            String countQueryString = " select sum(to_number(X013)) " + removeSelect(sql);
            Double count = 0D;
            try {
                SQLQuery cq = createSqlQuery(countQueryString, values);
                count =Double.valueOf(cq.uniqueResult().toString());
            } catch (Exception e) {
                return 0D;
            }
            return count;
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
    }

	/**
	 * 计算总投资   (查询时弃用)合并到查询总数中
	 * @author			肖红飞
	 * @createDate		2015年10月23日下午3:27:48
	 * @arithMetic                                                                           
	 * @param sql
	 * @param values
	 * @return
	 */
    @Deprecated
	public Double countInvestment(String sql, Object... values) {

		try {
			String countQueryString = " select sum(to_number(X013)) " + removeSelect(removeOrders(sql));
			Double count = 0D;
			try {
				SQLQuery cq = createSqlQuery(countQueryString, values);
				count =Double.valueOf(cq.uniqueResult().toString());
			} catch (Exception e) {
				return 0D;
			}
			return count;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
	/**
	 * 统计项目前期工作各项投资金额合计，用于项目报送、提交审核时候，投资金额的比较
	 * @author			朱方文
	 * @createDate		2015年10月20日下午1:16:47
	 * @arithMetic                                                                           
	 * @param sql
	 * @param values
	 * @return
	 */
	public Double countEInvestment(String sql, Object... values) {

		try {
			String countQueryString = " select sum(to_number(project_value)) " + removeSelect(removeOrders(sql));
			Double count = 0D;
			try {
				SQLQuery cq = createSqlQuery(countQueryString, values);
				count =Double.valueOf(cq.uniqueResult().toString());
			} catch (Exception e) {
				return 0D;
			}
			return count;
		} catch (HibernateException e) {
			throw new DAOException(e);
		}
	}
    public <X> List<X> findInCache(String hql, Object... values) {
        return createQuery(hql, values).setCacheable(true).list();
    }
}
