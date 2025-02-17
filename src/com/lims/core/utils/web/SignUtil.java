package com.lims.core.utils.web;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 接口加密解密工具类
 *
 * @author 菅志平
 */
public class SignUtil {
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 单例模式（饿汉模式）
     */
    private SignUtil() {
    }
    private static final class SignUtilHolder {
        private static final SignUtil instance = new SignUtil();
    }

    /**
     * 获取SignUtil实例对象
     * @return
     */
    public static SignUtil getInstance() {
        return SignUtilHolder.instance;
    }
    /**
     * @param params
     * 参数
     * @param signKey
     * 加密秘钥
     * @return
     */
    public String createSign(Map<String, Object> params, String signKey) {
        logger.info("createSign params:[{}]", params);
        SortedMap<String, Object> sortedMap = new TreeMap<String,Object>(params);
        StringBuilder toSign = new StringBuilder();
        toSign.append(signKey);
        for (String key : sortedMap.keySet()) {
            String value =(String) params.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)
                    && !"packageNm".equals(key)) {
                toSign.append(key);
                toSign.append(value);
            }
            if ("packageNm".equals(key)) {
                toSign.append("package="+ value + "&");
            }
        }
        toSign.append(signKey);
        logger.info("createSign sign:[{}]", toSign);
        return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
    }

}
