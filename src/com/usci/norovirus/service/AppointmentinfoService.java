package com.usci.norovirus.service;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.lims.core.orm.Page;
import com.lims.core.utils.web.Struts2Utils;
import com.lowagie.text.pdf.PdfWriter;
import com.usci.norovirus.dao.AppointmentinfoDao;
import com.usci.norovirus.dao.NorovirusDao;
import com.usci.norovirus.entity.Appointmentinfo;
import com.usci.norovirus.entity.Norovirus;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional(readOnly=true)
public class AppointmentinfoService {

    @Autowired
    private AppointmentinfoDao appointmentinfoDao;

    public Appointmentinfo findByYuyuenum(String sql) {
        List<Appointmentinfo> lcs=appointmentinfoDao.entityBySql(sql, Appointmentinfo.class);
        if(lcs!=null && lcs.size()>0){
            return lcs.get(0);
        }
        return null;
    }

	public List<Appointmentinfo> findBySql(String sql) {
		// TODO Auto-generated method stub
		List<Appointmentinfo> lcs=appointmentinfoDao.entityBySql(sql, Appointmentinfo.class);
        return lcs;
	}


}
