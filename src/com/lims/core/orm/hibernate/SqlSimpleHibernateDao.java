package com.lims.core.orm.hibernate;

import com.lims.core.utils.reflection.ReflectionUtils;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 封装Hibernate原生API的DAO泛型基类.
 * 
 * 可在Service层直接使用, 也可以扩展泛型DAO子类使用, 见两个构造函数的注释.
 * 参考Spring2.5自带的Petlinc例子, 取消了HibernateTemplate, 直接使用Hibernate原生API.
 * 
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 * 
 * @author niemengxiao
 */
@SuppressWarnings("unchecked")
public class SqlSimpleHibernateDao<T, PK extends Serializable> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected SessionFactory sqlsessionFactory;

	protected Class<T> entityClass;

	/**
	 * 用于Dao层子类使用的构造函数.
	 * 通过子类的泛型定义取得对象类型Class.
	 * eg.
	 * public class UserDao extends SimpleHibernateDao<User, Integer>
	 */
	public SqlSimpleHibernateDao() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	/**
	 * 用于用于省略Dao层, 在Service层直接使用通用SimpleHibernateDao的构造函数.
	 * 在构造函数中定义对象类型Class.
	 * eg.
	 * SimpleHibernateDao<User, Integer> userDao = new SimpleHibernateDao<User, Integer>(sqlsessionFactory, User.class);
	 */
	public SqlSimpleHibernateDao(final SessionFactory sqlsessionFactory, final Class<T> entityClass) {
		this.sqlsessionFactory = sqlsessionFactory;
		this.entityClass = entityClass;
	}

	/**
	 * 取得sqlsessionFactory.
	 */
	public SessionFactory getSqlsessionFactory() {
		return sqlsessionFactory;
	}

	/**
	 * 采用@Resource按名称注入SessionFactory, 当有多个SesionFactory的时候在子类重载本函数.
	 */
	@Resource(name = "sqlsessionFactory")
	//@Autowired
	public void setSqlSessionFactory(final SessionFactory sqlsessionFactory) {
		this.sqlsessionFactory = sqlsessionFactory;
	}

	/**
	 * 取得当前Session.
	 */
	public Session getSession() {
		return sqlsessionFactory.getCurrentSession();
	}

	/**
	 * 保存新增或修改的对象.
	 */
	public void save(final T entity) {
		Assert.notNull(entity, "entity不能为空");
			getSession().saveOrUpdate(entity);
		getSession().merge(entity);
		logger.debug("--------------------save entity: {}", entity);
		/*if(Struts2Utils.getSessionUser()!=null){
			logger.info(Struts2Utils.getSessionUser().getRealName()+":save entity:{}",entity);
		}else{
			logger.info("save entity:{}",entity);
		}*/
		
	}
	/**
	 * 保存新增对象
	 */
	public void saveOnly(final T entity) {
		Assert.notNull(entity, "entity不能为空");
			getSession().save(entity);
		getSession().merge(entity);
		logger.debug("--------------------save entity: {}", entity);
		/*if(Struts2Utils.getSessionUser()!=null){
			logger.info(Struts2Utils.getSessionUser().getRealName()+":save entity:{}",entity);
		}else{
			logger.info("save entity:{}",entity);
		}*/
		
	}
	public void merge(final T entity){
		getSession().merge(entity);
	}
	/**
	 * 保存修改的对象.
	 */
	public void update(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().update(entity);
	//	logger.info(Struts2Utils.getSessionUser().getName()+"update entity: {}", entity);
	}
	/**
	 * 保存新增或修改的对象.(返回id)
	 */
	public Integer returnsave(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		Serializable id=getSession().save(entity);
		Integer returnsaveid=Integer.parseInt(id.toString());	
		//logger.debug(Struts2Utils.getSessionUser().getName()+"save entity: {}", entity);
		return returnsaveid;
	}
	
	/**
	 * 删除对象.
	 * @param entity 对象必须是session中的对象或含id属性的transient对象.
	 */
	public void delete(final T entity) {
		Assert.notNull(entity, "entity不能为空");
		getSession().delete(entity);
		//logger.debug(Struts2Utils.getSessionUser().getRealName()+"delete entity: {}", entity);
	}

	/**
	 * 按id删除对象.
	 */
	public void delete(final PK id) {
		Assert.notNull(id, "id不能为空");
		delete(get(id));
		//logger.debug(Struts2Utils.getSessionUser().getRealName()+"delete entity {},id is {}", entityClass.getSimpleName(), id);
	}

	/**
	 * 按id获取对象.
	 */
	public T get(final PK id) {
		Assert.notNull(id, "id不能为空");
		return (T) getSession().get(entityClass, id);
	}
	/**
	 * 按id获取对象.
	 */
	public T load(final PK id) {
		Assert.notNull(id, "id不能为空");
		return (T) getSession().load(entityClass, id);
	}

	/**
	 * 按id列表获取对象列表.
	 */
	public List<T> get(final Collection<PK> ids) {
		return find(Restrictions.in(getIdName(), ids));
	}

	/**
	 *	获取全部对象.
	 */
	public List<T> getAll() {
		return find();
	}

	/**
	 *	获取全部对象, 支持按属性行序.
	 */
	public List<T> getAll(String orderByProperty, boolean isAsc) {
		Criteria c = createCriteria();
		if (isAsc) {
			c.addOrder(Order.asc(orderByProperty));
		} else {
			c.addOrder(Order.desc(orderByProperty));
		}
		return c.list();
	}

	/**
	 * 按属性查找对象列表, 匹配方式为相等.
	 */
	public List<T> findBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		
		return find(criterion);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	public T findUniqueBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = Restrictions.eq(propertyName, value);
		return (T) createCriteria(criterion).uniqueResult();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public <X> List<X> find(final String hql, final Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public <X> List<X> find(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public <X> X findUnique(final String hql, final Object... values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询唯一对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public <X> X findUnique(final String hql, final Map<String, ?> values) {
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 执行HQL进行批量修改/删除操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 * @return 更新记录数.
	 */
	public int batchExecute(final String hql, final Map<String, ?> values) {
		return createQuery(hql, values).executeUpdate();
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 数量可变的参数,按顺序绑定.
	 */
	public Query createQuery(final String queryString, final Object... values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	public Query createQuery(final String queryString, final Map<String, ?> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 按Criteria查询对象列表.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public List<T> find(final Criterion... criterions) {
		return createCriteria(criterions).list();
	}

	/**
	 * 按Criteria查询唯一对象.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public T findUnique(final Criterion... criterions) {
		return (T) createCriteria(criterions).uniqueResult();
	}

	/**
	 * 根据Criterion条件创建Criteria.
	 * 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions 数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			if(c!=null)
			criteria.add(c);
		}
		return criteria;
	}
	/**
	 * 解决多对一时 根据某个字段查询【不是主键】
	 */
	public Criteria createCriteria(List<Criterion> criterions ,Map<String,String> m) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		for(Map.Entry<String ,String> e:m.entrySet()){
			criteria.createCriteria(e.getKey().substring(0,e.getKey().indexOf("."))).add(Restrictions.eq(e.getKey().substring(e.getKey().indexOf(".")+1), e.getValue()));
		}
		return criteria;
	}
	
	/**
	 * 解决多对一时 根据某个字段查询【不是主键】findpage 特殊处理
	 */
	public Criteria createCriteria1(List<Criterion> criterions ,Map<String,String> m) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		for(Map.Entry<String ,String> e:m.entrySet()){
			criteria.createCriteria(e.getKey().substring(0,e.getKey().indexOf("00"))).add(Restrictions.eq(e.getKey().substring(e.getKey().indexOf("00")+2), e.getValue()));
		}
		return criteria;
	}
	
	/**
	 * 初始化对象.
	 * 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化.
	 * 如果传入entity, 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性.
	 * 如需初始化关联属性,需执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
	 * Hibernate.initialize(user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initProxyObject(Object proxy) {
		Hibernate.initialize(proxy);
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 为Query添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 为Criteria添加distinct transformer.
	 * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Criteria distinct(Criteria criteria) {
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	/**
	 * 取得对象的主键名.
	 */
	public String getIdName() {
		ClassMetadata meta = getSqlsessionFactory().getClassMetadata(entityClass);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
	 */
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
		if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);
		return (object == null);
	}
	/**
	 * 原生sql
	 * @param sql
	 * @return
	 */
	public List queryBySql(String sql) {  
        List<Object[]> list = getSession().createSQLQuery(sql).list();    
        return list;    
    }  
	
	/**
	 * 原生sql 转实体  注意字段的转换类型
	 * @param sql
	 * @return
	 */
	public  <T> List<T> entityBySql(String sql,Class c) {    
		return getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(c)).list();
    }
	/**
	 * 原生sql删除
	 * @param delsql
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void deleteBySql(String delsql) throws HibernateException, SQLException{  
        this.getSession().createSQLQuery(delsql).executeUpdate();  
        this.getSession().flush(); //清理缓存，执行批量插入  
        this.getSession().clear(); //清空缓存中的 对象  
          
    }  
	/**
	 * 原生sql添加/修改
	 * @param delsql
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public int updateBySql(String delsql) throws HibernateException, SQLException{  
        int id = this.getSession().createSQLQuery(delsql).executeUpdate();  
        this.getSession().flush(); //清理缓存，执行批量插入  
        this.getSession().clear(); //清空缓存中的 对象  
        return id;
          
    }  
}