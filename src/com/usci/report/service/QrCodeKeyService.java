package com.usci.report.service;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.lims.core.orm.Page;
import com.lims.core.orm.PropertyFilter;
import com.lims.core.utils.web.QrCodeUtils;
import com.lims.core.utils.web.Struts2Utils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.usci.report.dao.QrCodeKeyDao;
import com.usci.report.entity.QrCodeKey;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class QrCodeKeyService {
    @Autowired
    QrCodeKeyDao qrCodeKeyDao;
    /**
     * 返回页码
     */
    private static int i = 0;
    private static final String qrcodeurl="http://www.scisoon.cn/bgfw.html?code=";

    public void findPage(Page<QrCodeKey> page, List<PropertyFilter> filters){
        qrCodeKeyDao.findPage(page,filters);
    }

    @Transactional
    public void save(QrCodeKey qrCodeKey){
        qrCodeKeyDao.save(qrCodeKey);
    }


    /**
     * 创建二维码 颜色可选 白边可选
     * @param sampleNo 样本编号
     * @param width  宽
     * @param height 高
     * @param oncolor 二维码颜色（16进制）
     * @param isflag 是否去白边
     * @return
     */
    public String createQrCode(String sampleNo,String username,int width,int height,int oncolor,Boolean isflag){
        String QrcodeImages="QrcodeImages";
        String uuid= UUID.randomUUID().toString().replaceAll("-","");
        QrCodeKey qrCodeKey=new QrCodeKey();
        qrCodeKey.setQrkey(uuid);
        qrCodeKey.setSampleNo(sampleNo);
        qrCodeKey.setUserName(username);
        //根据uuid生成二维码
        String txtfile=Struts2Utils.getymd(new Date());
        String path= ServletActionContext.getServletContext().getRealPath("/"+QrcodeImages+"/"+txtfile+"/") + File.separator;
        File tempFile = new File(path);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        try {
            path=path+""+Struts2Utils.getStringDatehmss(new Date())+"_"+sampleNo+".png";
            qrCodeKey.setImageUrl("/"+QrcodeImages+"/"+txtfile+"/"+Struts2Utils.getStringDatehmss(new Date())+"_"+sampleNo+".png");
            uuid=qrcodeurl+""+uuid;
            QrCodeUtils.createQRCode(uuid,path,width,height,oncolor,isflag);
            qrCodeKey.setUpdateName("系统");
            qrCodeKey.setUpdateTime(Struts2Utils.getStringDate(new Date()));
            this.save(qrCodeKey);
            return qrCodeKey.getImageUrl();
        }catch(Exception e){

        }
        return "生成失败！";
    }

    /**
     * 创建二维码固定黑白 白边可选
     * @param sampleNo
     * @return
     */
    @Transactional
    public String createQrCode(String sampleNo, String username, Boolean isflag){
        String QrcodeImages="QrcodeImages";
        String uuid= UUID.randomUUID().toString().replaceAll("-","");
        QrCodeKey qrCodeKey=new QrCodeKey();
        qrCodeKey.setQrkey(uuid);
        qrCodeKey.setSampleNo(sampleNo);
        qrCodeKey.setUserName(username);
        //根据uuid生成二维码
        String txtfile=Struts2Utils.getymd(new Date());
        String path= ServletActionContext.getServletContext().getRealPath("/"+QrcodeImages+"/"+txtfile+"/") + File.separator;
        File tempFile = new File(path);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        try {
            path=path+""+Struts2Utils.getStringDatehmss(new Date())+"_"+sampleNo+".png";
            qrCodeKey.setImageUrl("/"+QrcodeImages+"/"+txtfile+"/"+Struts2Utils.getStringDatehmss(new Date())+"_"+sampleNo+".png");
            uuid=qrcodeurl+""+uuid;
            QrCodeUtils.createQRCode(uuid,path,500,500,isflag);
            qrCodeKey.setUpdateName("系统");
            qrCodeKey.setUpdateTime(Struts2Utils.getStringDate(new Date()));
            this.save(qrCodeKey);
            return qrCodeKey.getImageUrl();
        }catch(Exception e){

        }
        return "生成失败！";
    }


    /**
     * 向pdf中插入二维码
     * @param imgPath  图片路径(数据库的相对路径)
     * @param filePath 文件路径（pdf路径）
     * @param pageNum 页数（在第几页插入）
     * @param x x轴坐标
     * @param y y轴坐标
     * @param template 报告模板
     * @throws Exception
     */
    public void insertQrcodeImg(String imgPath,String filePath,int pageNum,float x,float y,String template)  {
        try {
//            System.out.println(imgPath);
//            System.out.println(filePath);
//            System.out.println(pageNum);
//            System.out.println(x);
//            System.out.println(y);
//            System.out.println(template);
        /*****************************插图s******************************/
        //imgPath转换为绝对路径  :
        imgPath=ServletActionContext.getServletContext().getRealPath("/"+imgPath);
        filePath=ServletActionContext.getServletContext().getRealPath("/"+filePath);
        // 读取模板文件
        InputStream input = new FileInputStream(new File(filePath));
        PdfReader reader = new PdfReader(input);
        //去掉pdf密码验证
        Field f = PdfReader.class.getDeclaredField("ownerPasswordUsed");
        f.setAccessible(true);
        f.set(reader, Boolean.TRUE);
        String newpath=filePath;
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(newpath));
        //去掉pdf加密（去掉不能复制）
        PdfReader.unethicalreading = true;
        // 提取pdf中的表单
        AcroFields form = stamper.getAcroFields();
        form.addSubstitutionFont(BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
        // 读图片
        Image image1 = Image.getInstance(imgPath);
        // 获取操作的页面
        PdfContentByte under = stamper.getOverContent(pageNum);
        // 根据域的大小缩放图片
        image1.scaleToFit(58, 58);
        // 添加图片
        image1.setAbsolutePosition(x+50, y-13);//审核人
        under.addImage(image1);
        stamper.close();
        reader.close();
        input.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
