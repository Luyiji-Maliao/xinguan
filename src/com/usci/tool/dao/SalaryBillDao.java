package com.usci.tool.dao;


import org.springframework.stereotype.Component;



import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.tool.entity.SalaryBill;

@Component
public class SalaryBillDao extends HibernateDao<SalaryBill, Integer>{

}
