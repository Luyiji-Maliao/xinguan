package com.usci.wxbukaifapiao.com.github.wxpay.sdk;


import com.github.wxpay.sdk.WXPayConfig;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 微信 支付 Java配置
 *
 */

@Component
@Scope("prototype")
@ParentPackage("base")
@InterceptorRefs( { @InterceptorRef("mystack") })
public class MyConfig implements WXPayConfig {



    public String getAppID() {
        return "wx3fc2f8e8dd6fb5a8";
    }

    public String getMchID() {
        return "1279529601";
    }

    public String getKey() {
        return "jiyinwuweixinzhifu20160908usci00";
    }

    public InputStream getCertStream() {
        return null;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
