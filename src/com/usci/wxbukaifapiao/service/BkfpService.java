package com.usci.wxbukaifapiao.service;


import com.usci.wxbukaifapiao.dao.BkfpDao;
import com.usci.wxbukaifapiao.entity.Bkfp;
import com.usci.xgcollectinfo.entity.xgcollectinfo;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class BkfpService {
    @Autowired
    private BkfpDao bkfpDao;



    @Transactional(transactionManager = "sqltransactionManager")
    public String save(Bkfp bkfp) {
        bkfpDao.save(bkfp);
        return null;
    }




//    public List<Bkfp> selectFapiaotaitou(String fapiaotaitou) {
//        // TODO Auto-generated method stub
//        String sql ="FROM Bkfp WHERE fapiaotaitou='"+fapiaotaitou+"'";
//        Query query = bkfpDao.createQuery(sql);
//        List<Bkfp> lcs =query.list();
//        return lcs;
//    }
}
