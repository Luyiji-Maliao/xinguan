package com.lims.core.orm.hibernate;

import com.lims.core.orm.OrderBySqlFormula;
import com.lims.core.orm.Page;
import com.lims.core.orm.PageOrder;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.orm.PropertyFilter.MatchType;
import com.lims.core.utils.reflection.ReflectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装扩展功能的Hibernat DAO泛型基类.
 * 
 * 扩展功能包括分页查询,按属性过滤条件列表查询.
 * 可在Service层直接使用,也可以扩展泛型DAO子类使用,见两个构造函数的注释.
 * 
 * @param <T> DAO操作的对象类型
 * @param <PK> 主键类型
 * 
 * @author USCILIMS
 */
public class SqlHibernateDao<T, PK extends Serializable> extends SqlSimpleHibernateDao<T, PK> {
	/**
	 * 用于Dao层子类的构造函数.
	 * 通过子类的泛型定义取得对象类型Class.
	 * eg.
	 * public class UserDao extends HibernateDao<User, Integer>{
	 * }
	 */
	public SqlHibernateDao() {
		super();
	}

	/**
	 * 用于省略Dao层, Service层直接使用通用HibernateDao的构造函数.
	 * 在构造函数中定义对象类型Class.
	 * eg.
	 * HibernateDao<User, Integer> userDao = new HibernateDao<User, Integer>(sessionFactory, User.class);
	 */
	public SqlHibernateDao(final SessionFactory sessionFactory, final Class<T> entityClass) {
		super(sessionFactory, entityClass);
	}

	//-- 分页查询函数 --//

	/**
	 * 分页获取全部对象.
	 */
	public Page<T> getAll(final Page<T> page) {
		return findPage(page);
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数. 注意不支持其中的orderBy参数.
	 * @param hql hql语句.
	 * @param values 数量可变的查询参数,按顺序绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final String hql, final Object... values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			Long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数. 注意不支持其中的orderBy参数.
	 * @param hql hql语句.
	 * @param values 命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final String hql, final Map<String, ?> values) {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			Long totalCount = countHqlResult(hql, values);
			page.setTotalCount(totalCount);
		}

		setPageParameterToQuery(q, page);

		List result = q.list();
		page.setResult(result);
		return page;
	}

	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page 分页参数.
	 * @param criterions 数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);
		if (page.isAutoCount()) {
			Long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriteria(c, page);
		List result = c.setCacheable(true).list();
		page.setResult(result);
		return page;
	}
	
	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page 分页参数.
	 * @param criterions 数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPageNotCache(final Page<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);
		if (page.isAutoCount()) {
			Long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriteria(c, page);
		List result = c.list();
		page.setResult(result);
		return page;
	}
	/**
	 * 调整【多对一查询 ：不是主键的属性】
	 * @param page
	 * @param criterions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> findPage1(final Page<T> page, List<Criterion>  criterions,Map<String, String> m) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria1(criterions,m);
		if (page.isAutoCount()) {
			Long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriteria(c, page);
		List result = c.setCacheable(true).list();
		page.setResult(result);
		return page;
	}
	@SuppressWarnings("unchecked")
	public PageOrder<T> findPageorder(final PageOrder<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);
		if (page.isAutoCount()) {
			Long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriteriaorder(c, page);
		List result = c.setCacheable(true).list();
		page.setResult(result);
		return page;
	}

	
	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameterToQuery(final Query q, final Page<T> page) {

		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		//hibernate的firstResult的序号从0开始
		q.setFirstResult(page.getFirst() - 1);
		q.setMaxResults(page.getPageSize());
		return q;
	}

	/**
	 * 设置分页参数到Criteria对象,辅助函数.
	 */
	protected Criteria setPageParameterToCriteria(final Criteria c, final Page<T> page) {

		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		//hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getFirst() - 1);
		c.setMaxResults(page.getPageSize());

		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');
			
			Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

			for (int i = 0; i < orderByArray.length; i++) {
				if (Page.ASC.equals(orderArray[i])) {
					c.addOrder(Order.asc(orderByArray[i]));
				} else {
					c.addOrder(Order.desc(orderByArray[i]));
				}
			}
		}
		return c;
	}

	protected Criteria setPageParameterToCriteriaorder(final Criteria c, final PageOrder<T> page) {

		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		//hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getFirst() - 1);
		c.setMaxResults(page.getPageSize());

		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');
			
			Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

			for (int i = 0; i < orderByArray.length; i++) {
				if (Page.ASC.equals(orderArray[i])) {
					c.addOrder(Order.asc(orderByArray[i]));
				} else {
					c.addOrder(Order.desc(orderByArray[i]));
				}
			}
			//c.addOrder(OrderBySqlFormula.sqlFormula(" leaveState='[结束]',(managerSignName='黄金龙' OR manpowerSignName='黄金龙' OR bossSignName='黄金龙') and leaveState!='[结束]', leaveName='黄金龙' AND leaveState!='[结束]',nextass='黄金龙' AND leaveState!='[结束]' "));
		}

		return c;
	}
	
	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected Long countHqlResult(final String hql, final Object... values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected Long countHqlResult(final String hql, final Map<String, ?> values) {
		String countHql = prepareCountHql(hql);

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		//select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	/**
	 * 执行count查询获得本次Criteria查询所能获得的对象总数.
	 */
	@SuppressWarnings("unchecked")
	protected Long countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
			ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		Long totalCountObject = (Long) c.setProjection(Projections.rowCount()).uniqueResult();
		Long totalCount = (totalCountObject != null) ? totalCountObject : 0;

		// 将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

	//-- 属性过滤条件(PropertyFilter)查询函数 --//

	/**
	 * 按属性查找对象列表,支持多种匹配方式.
	 * 
	 * @param matchType 匹配方式,目前支持的取值见PropertyFilter的MatcheType enum.
	 */
	public List<T> findBy(final String propertyName, final Object value, final MatchType matchType) {
		Criterion criterion = buildCriterion(propertyName, value, matchType);
		return find(criterion);
	}

	/**
	 * 按属性过滤条件列表查找对象列表.
	 */
	public List<T> find(List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return find(criterions);
	}

	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public Page<T> findPage(final Page<T> page, final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPage(page, criterions);
	}
	
	/**
	 * 按属性过滤条件列表分页查找对象.
	 */
	public Page<T> findPageNotcache(final Page<T> page, final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPageNotCache(page, criterions);
	}
	/**
	 * 按属性过滤条件列表分页查找对象.【多对一查询 ：不是主键的属性】
	 */
	public Page<T> findPage1(final Page<T> page, final List<PropertyFilter> filters) {
		List cs = buildCriterionByPropertyFilter1(filters);
		return findPage1(page,(List)cs.get(0) ,(Map)cs.get(1));
	}
	public PageOrder<T> findPageorder(final PageOrder<T> page, final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPageorder(page, criterions);
	}

	
	/**
	 * 按属性条件参数创建Criterion,辅助函数.
	 */
	@SuppressWarnings("null")
	protected Criterion buildCriterion(final String propertyName, final Object propertyValue, final MatchType matchType) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = null;
		//根据MatchType构造criterion

		switch (matchType) {
		case EE:
			String s=propertyName.substring(0,propertyName.length()-2)+"."+propertyName.substring(propertyName.length()-2,propertyName.length());
			criterion = Restrictions.eq(s, propertyValue);
			break;
		case EQ:
			criterion = Restrictions.eq(propertyName, propertyValue);
			break;
		case IN:
			String [] pv =propertyValue.toString().split(",");
			
			criterion = Restrictions.in(propertyName, pv);
			break;
		case NIN:
			String [] pvs =propertyValue.toString().split(",");
			
			criterion =Restrictions.not(Restrictions.in(propertyName, pvs));
			break;
		case LIKE:
			criterion = Restrictions.like(propertyName, (String) propertyValue, MatchMode.ANYWHERE);
			break;
		case ISLIKE:
			criterion = Restrictions.sqlRestriction(" {alias}."+propertyName+" like '%" + propertyValue + "%' or {alias}."+propertyName+" is null");
			break;
		case UNLIKE:
			criterion = Restrictions.sqlRestriction(" {alias}."+propertyName+" not like '%" + propertyValue + "%' or {alias}."+propertyName+" is null");
			break;
		case NE:
			criterion = Restrictions.ne(propertyName, propertyValue);
			break;	
		case LE:
			criterion = Restrictions.le(propertyName, propertyValue);
			break;
		case LT:
			criterion = Restrictions.lt(propertyName, propertyValue);
			break;
		case GE:
			criterion = Restrictions.ge(propertyName, propertyValue);
			break;
		case GT:
			criterion = Restrictions.gt(propertyName, propertyValue);
			break;
		case SQL:
			//待解决
			StringBuilder sb = new StringBuilder();
		      if (propertyValue.toString().substring(0, 1).equals("1")) {//是否有一录权限
		        sb.append(" ({alias}.yl='否' or {alias}.yl='未完成') or");//一录保存不显示
		      }
		      if (propertyValue.toString().substring(1, 2).equals("1")) {//是否有二录权限
		        sb.append(" ({alias}.yl='是' and {alias}.el='否' or {alias}.el='未完成') or");//二录保存不显示
		      }
		      if (propertyValue.toString().substring(2, 3).equals("1")) {//是否有一审权限
		        sb.append(" ({alias}.el='是' and {alias}.ys='否') or");
		      }
		      if (propertyValue.toString().substring(3, 4).equals("1")) {//是否有二审权限
		        sb.append(" ({alias}.ys='是' and {alias}.es='否') or"); 
		        }
		    if(sb.length()>2){
		    	criterion = Restrictions.sqlRestriction(sb.substring(0,sb.length()-2));
		    }else{
		    	criterion = Restrictions.eq("yl", null);
		    }
		    break;
		case CKPRO:
			String [] pvc =propertyValue.toString().split(",");
			
			StringBuilder sb1 = new StringBuilder();
			for(String pvci:pvc){
				 sb1.append(" ({alias}."+propertyName+" like '%"+pvci+"%') or");//循环检测项目
			}
		    if(sb1.length()>2){
		    	criterion = Restrictions.sqlRestriction(sb1.substring(0,sb1.length()-2));
		    }else{
		    	criterion = Restrictions.eq(propertyName, null);
		    }
			break;
		case GETIME:
			
			StringBuilder sb2 = new StringBuilder();
			sb2.append(" (RIGHT(REPLACE({alias}."+propertyName+",';',''),19) >= '"+propertyValue.toString()+"')");
			criterion = Restrictions.sqlRestriction(sb2.toString());
			break;
		case LETIME:
			
			StringBuilder sb3 = new StringBuilder();
			sb3.append(" (LEFT(REPLACE({alias}."+propertyName+",';',''),19) <= '"+propertyValue.toString()+"')");
			criterion = Restrictions.sqlRestriction(sb3.toString());
			break;
		}
		return criterion;
	}

	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.
	 */
	protected Criterion[] buildCriterionByPropertyFilter(final List<PropertyFilter> filters) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (PropertyFilter filter : filters) {
			if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
				Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(), filter
						.getMatchType());
				criterionList.add(criterion);
			} else {//包含多个属性需要比较的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				for (String param : filter.getPropertyNames()) {
					Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
					disjunction.add(criterion);
				}
				criterionList.add(disjunction);
			}
		}
		return criterionList.toArray(new Criterion[criterionList.size()]);
	}
	
	/**
	 * 按属性条件列表创建Criterion数组,辅助函数.【多对一查询 ：不是主键的属性】
	 */
	protected List buildCriterionByPropertyFilter1(final List<PropertyFilter> filters) {
		List l=new ArrayList();
		Map<String, String> m=new HashMap<String, String>();
		List<Criterion> criterionList = new ArrayList<Criterion>();
		for (PropertyFilter filter : filters) {
			if (!filter.hasMultiProperties()) { //只有一个属性需要比较的情况.
				if(filter.getPropertyName().indexOf("00")==-1){//查自身属性
					
					Criterion criterion = buildCriterion(filter.getPropertyName(), filter.getMatchValue(), filter.getMatchType());
					criterionList.add(criterion);
				}else{//关联表查询
					m.put(filter.getPropertyName(), filter.getMatchValue().toString());
				}
				
			} else {//包含多个属性需要比较的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				for (String param : filter.getPropertyNames()) {
					if(filter.getPropertyName().indexOf("00")==-1){//查自身属性
						Criterion criterion = buildCriterion(param, filter.getMatchValue(), filter.getMatchType());
						disjunction.add(criterion);
					}else{//关联表查询
						m.put(filter.getPropertyName(), filter.getMatchValue().toString());
					}
					
				}
				criterionList.add(disjunction);
			}
		}
		l.add(criterionList);
		l.add(m);
		return l;
	}
	
	/**
	 * 按属性过滤条件列表分页查找对象.
	 * 排序条件按分号分割
	 */
	public Page<T> findPages(final Page<T> page, final List<PropertyFilter> filters) {
		Criterion[] criterions = buildCriterionByPropertyFilter(filters);
		return findPages(page, criterions);
	}
	
	/**
	 * 按Criteria分页查询.
	 * 排序条件以分号分割
	 * @param page 分页参数.
	 * @param criterions 数量可变的Criterion.
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询输入参数.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<T> findPages(final Page<T> page, final Criterion... criterions) {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(criterions);
		if (page.isAutoCount()) {
			Long totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameterToCriterias(c, page);
		List result = c.setCacheable(true).list();
		
		page.setResult(result);
		return page;
	}
	/**
	 * 设置分页参数到Criteria对象,辅助函数.参数以“;”拆分
	 */
	protected Criteria setPageParameterToCriterias(final Criteria c, final Page<T> page) {

		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		//hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getFirst() - 1);
		c.setMaxResults(page.getPageSize());

		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ';');
			String[] orderArray = StringUtils.split(page.getOrder(), ';');
			
			Assert.isTrue(orderByArray.length == orderArray.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");

			for (int i = 0; i < orderByArray.length; i++) {
				if (Page.ASC.equals(orderArray[i])) {
					c.addOrder(OrderBySqlFormula.sqlFormula(orderByArray[i]+" ASC"));
				} else {
					c.addOrder(OrderBySqlFormula.sqlFormula(orderByArray[i]+" DESC"));
				}
			}
		}
		return c;
	}
}
