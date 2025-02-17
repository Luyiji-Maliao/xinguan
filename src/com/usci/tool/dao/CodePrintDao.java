package com.usci.tool.dao;

import org.springframework.stereotype.Component;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.tool.entity.CodePrint;
@Component
public class CodePrintDao extends HibernateDao<CodePrint, Integer> {

}
