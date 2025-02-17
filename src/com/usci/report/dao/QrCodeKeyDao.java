package com.usci.report.dao;

import com.lims.core.orm.hibernate.HibernateDao;
import com.usci.report.entity.QrCodeKey;
import org.springframework.stereotype.Component;

@Component
public class QrCodeKeyDao extends HibernateDao<QrCodeKey,Integer> {
}
