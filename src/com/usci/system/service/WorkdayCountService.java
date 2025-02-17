package com.usci.system.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.usci.system.dao.WorkdayCountDao;

@Component
@Transactional(readOnly=true)
public class WorkdayCountService {
	@Autowired
	private WorkdayCountDao workdayCountDao;
	
	/**
	 * 获取指定时间减一天的日期
	 * @param calendar
	 * @return
	 */
	public String getYesterday(String calendar){
		Calendar ca = Calendar.getInstance();
		Date d=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = df.parse(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	    ca.setTime(d);
	    ca.add(Calendar.DAY_OF_MONTH, -1);
	    return (new SimpleDateFormat("yyyy-MM-dd")).format(ca.getTime());  
	}
	
	/**
	 * 获取指定时间加一天的日期
	 * @param calendar
	 * @return
	 */
	public String getTomorrow(String calendar){
		Calendar ca = Calendar.getInstance();
		Date d=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = df.parse(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	    ca.setTime(d);
	    ca.add(Calendar.DAY_OF_MONTH, 1);
	    return (new SimpleDateFormat("yyyy-MM-dd")).format(ca.getTime());  
	}
	/**
	 * 获取指定时间加n天的日期（自然日）
	 * @param calendar
	 * @return
	 */
	public String getdayzr(String calendar,int days){
		Calendar ca = Calendar.getInstance();
		Date d=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = df.parse(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	    ca.setTime(d);
	    ca.add(Calendar.DAY_OF_MONTH, days);
	    return (new SimpleDateFormat("yyyy-MM-dd")).format(ca.getTime());  
	}
	/**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 查询某个日期前的 days个 工作日的日期
	  * @param calendar  当前的日期  
	  * @param days 相加天数  
	  * @return     
	  * return String   返回类型   返回相加day天，并且排除节假日和周末后的日期  
	 * 
	  */  
	 @SuppressWarnings("unchecked")
	public String getWorkDateByDays(String calendar,int days) {  
		Calendar ca = Calendar.getInstance();
		Date d=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = df.parse(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	    ca.setTime(d);//设置当前时间  
		String sql="select a.days from wk_daycheck as a where a.days<=DATE_FORMAT('"+calendar+"','%Y%m%d') ORDER BY a.days DESC LIMIT 0,"+10*days+"";  
		List<String> ss = workdayCountDao.queryBySql(sql);
		   
		for(int i=0;i<days;i++){
		    ca.add(Calendar.DAY_OF_MONTH, -1);
		    
			for(int j=0;j<ss.size();j++ ){
			    if(ss.get(j).equals((new SimpleDateFormat("yyyyMMdd")).format(ca.getTime()))){
					i--;
					ss.remove(j);
				}
			}
		 }
		 return (new SimpleDateFormat("yyyy-MM-dd")).format(ca.getTime());  
	}
	
	/**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 查询某个日期后的 days个 工作日之后的日期
	  * @param calendar  当前的日期  
	  * @param days 相加天数  
	  * @return     
	  * return String   返回类型   返回相加day天，并且排除节假日和周末后的日期  
	 * 
	  */  
	 @SuppressWarnings("unchecked")
	public String addDateByWorkDay(String calendar,int days) {  
		Calendar ca = Calendar.getInstance();
		Date d=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = df.parse(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	    ca.setTime(d);//设置当前时间  
		String sql="select a.days from wk_daycheck as a where a.days>=DATE_FORMAT('"+calendar+"','%Y%m%d') ORDER BY a.days ASC LIMIT 0,"+10*days+"";  
		List<String> ss = workdayCountDao.queryBySql(sql);
		   
		for(int i=0;i<days;i++){
		    ca.add(Calendar.DAY_OF_MONTH, 1); 
			for(int j=0;j<ss.size();j++ ){
			    if(ss.get(j).equals((new SimpleDateFormat("yyyyMMdd")).format(ca.getTime()))){
					i--;
					ss.remove(j);
				}
			}
		 }
		 return (new SimpleDateFormat("yyyy-MM-dd")).format(ca.getTime());  
	}  
	 /**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 查询某个日期后的 days个 工作日之后的日期(工作日指排除周末的所有日期)
	  * @param calendar  当前的日期  
	  * @param days 相加天数  
	  * @return     
	  * return String   返回类型   返回相加day天，并且排除周末后的日期  
	 * 
	  */  
	public String addDateByWorkDayWeek(String calendar,int days) {  
		int flags=getWeekOfDate(calendar);
		Calendar ca = Calendar.getInstance();
		Date d=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = df.parse(calendar);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	    ca.setTime(d);//设置当前时间  
	    if(flags==6){
	    	ca.add(Calendar.DAY_OF_MONTH, 1); 
	    	flags=1;
	    }else if(flags==7){
	    	flags=1;
	    }else if(flags==5){
	    	ca.add(Calendar.DAY_OF_MONTH, 2); 
	    	flags=1;
	    }
		 for(int i=0;i<days;i++){
		    ca.add(Calendar.DAY_OF_MONTH, 1);
		    if(flags==6||flags==7){
				i--;
			}
		    if(flags<7){
		    	 flags++;
		    }else{
		    	flags=1;
		    }
		 }
		 int flag2=getWeekOfDate((new SimpleDateFormat("yyyy-MM-dd")).format(ca.getTime()));
		 if(flag2==6){
			 ca.add(Calendar.DAY_OF_MONTH, 2); 
		 }
		 if(flag2==7){
			 ca.add(Calendar.DAY_OF_MONTH, 1); 
		 }
		 return (new SimpleDateFormat("yyyy-MM-dd")).format(ca.getTime());  
	}  
	 /**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 查询某个日期是否工作日
	  * @param calendar  当前的日期  
	  * @return     
	  * return String    返回类型   yes表示是工作日
	  */  
	 @SuppressWarnings("unchecked")
	public String isWorkDay(String calendar) {  
		
		String sql="select a.days from wk_daycheck as a where a.days=DATE_FORMAT('"+calendar+"','%Y%m%d')";  
		List<String> ss = workdayCountDao.queryBySql(sql);
		String result="";
		if(ss.size()==0){
			result="yes";
		}
		   
		return result;  
	 }  
	 
	 /**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 两个日期之间的工作日天数
	  * @param calendar  起始日期  
	  * @param calendar2  截止日期
	  * @return     
	  * return int    返回类型   两个日期之间的工作日天数
	  */  
	 @SuppressWarnings("unchecked")
	public int workDayCount(String calendar,String calendar2) {  
			
			String sql="select a.days from wk_daycheck as a where a.day>='DATE_FORMAT('"+calendar+"','%Y%m%d')' and a.day<='DATE_FORMAT('"+calendar2+"','%Y%m%d')'";  
		    List<String> ss = workdayCountDao.queryBySql(sql);
		   
		    return ss.size();  
		 } 
	
	 /**  
	  *   
	  * <p>Title: addDateByWorkDay </P> 
	  * 两个日期之间的工作日天数(只排除周末)
	  * @param calendar  起始日期  
	  * @param calendar2  截止日期
	  * @return     
	  * return int    返回类型   两个日期之间的工作日天数
	  */  
	public int workDayCountWeek(String calendar,String calendar2) {  
			
		int resultday=0;
		  
		Date d=new Date();
		Date d2=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = df.parse(calendar);
			d2 = df.parse(calendar2);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		int wd=getWeekOfDate(calendar);
		int days = daysBetween(d2,d);
		if(wd+days>7){
			resultday=2;
		}
	   switch(wd){
	    case 7:
	    	resultday=(days/7)*2+1+resultday;
	    	break;
	    case 6:
	    	resultday=(days/7)*2+2+resultday;
	    	break;
	    case 5:
	    case 4:
	    case 3:
	    case 2:
	    case 1:
	    	resultday=(days/7)*2+resultday;
	    	break;
	    }
	   if(wd+days==6){
		  resultday=days-1;
	  }else if(wd+days==7){
		  resultday=days-2;
	  }else if(wd+days<6){
		  resultday=days;
	  }else{
		  resultday=(days-resultday);
	  }
	    return resultday;  
	 }  
	 /**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public int getWeekOfDate(String dt) {
		Date dtf=new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dtf=df.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dtf);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    /**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public int daysBetween(Date smdate,Date bdate){    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        try {
			smdate=sdf.parse(sdf.format(smdate));  
			bdate=sdf.parse(sdf.format(bdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
}
