package com.usci.system.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.orm.Page;
import com.lims.core.orm.PageOrder;
import com.lims.core.orm.PropertyFilter;
import com.usci.system.dao.AttendanceDao;
import com.usci.system.entity.Attendance;

@Component
@Transactional(readOnly=true)
public class AttendanceService {
  @Autowired
  private AttendanceDao attendanceDao;
    @Transactional
    public void save(Attendance entity) { 
    	List<Attendance> l=findByDeptNoDate(entity.getAttDeptName(),entity.getAttJobNumber(),entity.getAttDate());
    	if(l.size()==0){
    		attendanceDao.save(entity);
    	}else{
    		
    	}
    
    } 
	public void findPage(PageOrder<Attendance> page,List<PropertyFilter> filters) {
		attendanceDao.findPageorder(page, filters);		
	}
    public Page<Attendance> list(int limit){
		return attendanceDao.findPage(new Page<Attendance>(limit));
	}
	
	public Attendance get(Integer id){
		return attendanceDao.get(id);
	}
	/**
	 * 根据部门，卡号，日期定义唯一
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Attendance> findByDeptNoDate(String attDeptName,String attJobNumber,String attDate )  {

		List<Attendance> list = attendanceDao.createCriteria(Restrictions.eq("attDeptName", attDeptName),Restrictions.eq("attJobNumber", attJobNumber),Restrictions.eq("attDate", attDate)).list();
		return list;
	}
	/**
	 * 查看考勤
	 */
	public Page<Attendance> showAttend(String attDate){
		String sql="SELECT	attUserName,COUNT(attUserName) FROM	sys_attendance WHERE	attDate LIKE '%"+attDate+"%' AND HOUR (attSignin) < HOUR ('09:00:00') AND IF (HOUR (attSignout) > HOUR ('17:30:00'),HOUR (attSignout) > HOUR ('17:30:00'),HOUR (attSignout) > HOUR ('16:30:00') AND MINUTE (attSignout) > MINUTE ('16:29:00')) GROUP BY attUserName";
		List<Object[]> sa = attendanceDao.queryBySql(sql);
		List<Attendance> la=new ArrayList<Attendance>();
		for (Object[] o: sa) {
			Attendance a=new Attendance();
			a.setAttUserName(o[0].toString());
			a.setAttCount(o[1].toString());
			la.add(a);
		
		}
		Page<Attendance> pa=new Page<Attendance>();
		pa.setResult(la);
		return pa;
	}
	
}
