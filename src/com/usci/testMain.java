package com.usci;

import com.lims.core.utils.web.QrCodeUtils;
import com.lims.core.utils.web.Struts2Utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Date;

public class testMain {
    public static void main(String[] args) throws Exception {
      /*  int s= 0;
        try {
            //s = Struts2Utils.sendSMS("19831818983","公众号填写个人信息，并完成采样点和日期的预约。完成预约后，我们将为您生成预约码，请您携带好身份证件及预约码及时前往采样点采样，感谢您的配合。【预约流程：微信搜索并关注优迅医学公众号——新冠检测——预约检测——填写信息】", Struts2Utils.getStringDatehmss(new Date()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(",,,,,"+s);*/
        QrCodeUtils.createQRCode("wozuishui","F:\\qr.png",500,500,false);
    }
}
