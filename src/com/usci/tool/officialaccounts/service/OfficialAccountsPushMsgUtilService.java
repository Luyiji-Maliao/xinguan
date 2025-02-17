package com.usci.tool.officialaccounts.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.mail.MessagingException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lims.core.utils.web.Struts2Utils;
import com.usci.email.entity.EmailName;
import com.usci.email.entity.EmailOut;
import com.usci.system.entity.User;
import com.usci.system.service.UserService;
import com.usci.tool.officialaccounts.entity.OfficialAccountsPushMsg;
import com.usci.tool.officialaccounts.entity.OfficialAccountsPushName;
import com.usci.tool.url.service.InterfaceUrlUtilService;


@Service
@Transactional(readOnly=true)
public class OfficialAccountsPushMsgUtilService {
   
    private static final Logger log = LoggerFactory.getLogger(OfficialAccountsPushMsgUtilService.class);

}
