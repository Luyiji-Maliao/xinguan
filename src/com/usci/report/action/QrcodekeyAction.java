package com.usci.report.action;

import com.google.zxing.WriterException;
import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.QrCodeUtils;
import com.lims.core.utils.web.Struts2Utils;
import com.usci.report.entity.QrCodeKey;
import com.usci.report.service.QrCodeKeyService;
import org.apache.poi.hslf.record.Sound;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletOutputStream;
import java.io.*;
import java.util.*;

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs({ @InterceptorRef("mystack") })
/**
 * 报告防伪验证二维码
 * @author 菅志平
 *
 */
@Results({
        @Result(name = "logins", type = "redirect", location = "session.jsp"),
        @Result(name = "modulepage", location = "/WEB-INF/content/report/qrcodekey.jsp"),
})
public class QrcodekeyAction extends CrudActionSupport<QrCodeKey> {

    @Autowired
    private QrCodeKeyService qrCodeKeyService;


    private static String imageurl;
    private static String samplenos;


    private QrCodeKey entity;

    public QrCodeKey getEntity() {
        return entity;
    }

    public void setEntity(QrCodeKey entity) {
        this.entity = entity;
    }

    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    @Override
    public String list() throws Exception {
        Page<QrCodeKey> page = new Page<QrCodeKey>(limit);
        page.setPageNo((start / limit) + 1);
        if(sort==null){
            page.setOrderBy("updateTime");
            page.setOrder("DESC");
        }else{
            Map<String, String> mp= Struts2Utils.toMap(sort.substring(1,sort.length()-1));
            page.setOrderBy(mp.get("property"));
            page.setOrder(mp.get("direction"));
        }

        qrCodeKeyService.findPage(page, PropertyFilter.buildFromHttpRequest(Struts2Utils.getRequest()));
        Struts2Utils.renderJson(page);
        return null;
    }


    public void downloadimg() throws IOException{
        Struts2Utils.getResponse().addHeader("Content-Disposition", "attachment;filename=\""+samplenos+".png"+"\"");
        OutputStream os = Struts2Utils.getResponse().getOutputStream();
        FileInputStream is=new FileInputStream(ServletActionContext.getServletContext().getRealPath("/"+imageurl));
        byte[] buffer = new byte[400];
        int length = 0;
        while (-1 != (length = is.read(buffer))) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }





    @Override
    public String modulepage() throws Exception {
        return "modulepage";
    }

    @Override
    public String save() throws Exception {
        return null;
    }

    @Override
    protected void prepareModel() throws Exception {
        entity=new QrCodeKey();
    }

    @Override
    public QrCodeKey getModel() {
        return entity;
    }
}
