package com.usci.system.action;


import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.persistence.Transient;
import javax.servlet.ServletContext;


import com.usci.system.entity.*;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.lims.core.utils.web.CrudActionSupport;
import com.lims.core.utils.web.Struts2Utils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ndktools.javamd5.Mademd5;
import com.usci.system.service.DepartmentService;
import com.usci.system.service.PositionService;
import com.usci.system.service.UserService;

@SuppressWarnings("serial")
@Component
@Scope("prototype")
@Results({
        @Result(name = "userlose", location = "/WEB-INF/content/systemmanage/leave/userlose.jsp"),
        @Result(name = "randomlose", location = "/WEB-INF/content/systemmanage/leave/randomlose.jsp"),
        @Result(name = "jietu", location = "/jietu.jsp"),
        @Result(name = "relogin", type = "redirect", location = "/welcome.jsp"),
        @Result(name = "bb", location = "/bootstrap.jsp"),
        @Result(name = "resignlist", type = "redirect",location = "/sign!modulepage.action"),
        @Result(name = "signlist", location = "/WEB-INF/content/norovirus/signlist.jsp"),
        @Result(name = "signedlist", location = "/WEB-INF/content/norovirus/signedlist.jsp"),
        @Result(name = "saleperformance", location = "/WEB-INF/content/norovirus/saleperformance.jsp"),
        @Result(name = "tongji", location = "/WEB-INF/content/norovirus/tongji.jsp"),
        @Result(name = "dangtian", location = "/WEB-INF/content/norovirus/dangtian.jsp")
})
/**
 * @author nimengxiao
 * @date 2016
 */
public class LoginAction extends CrudActionSupport<User> {
    private User entity;

    private String openid;
    private String type;
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PositionService positionService;
    private static final Logger log=LoggerFactory.getLogger(LoginAction.class);
    /**
     * 浏览器类型
     */
    private String browsertype;

    private String mobileRandom;
    private Integer leaveId;
    private Integer moduleId;
    private String str;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getBrowsertype() {
        return browsertype;
    }

    public void setBrowsertype(String browsertype) {
        this.browsertype = browsertype;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public String getMobileRandom() {
        return mobileRandom;
    }

    public void setMobileRandom(String mobileRandom) {
        this.mobileRandom = mobileRandom;
    }

    @Override
    protected void prepareModel() throws Exception {

        entity = new User();

    }

    @Override
    public User getModel() {
        return entity;
    }


    @Override
    public String input() throws Exception {
        return null;
    }

    @Override
    public String list() throws Exception {
        return null;
    }

    @Override
    public String save() throws Exception {
        return null;
    }

    /**
     * 微信用户登录
     * 绑定微信id
     *   openid 微信id
     *   type  进入哪个页面
     * @return
     */
    public String wxlogin(){
        String sql="select * from wx_loginuser where openid='"+openid+"' and perstr like '%"+type+";%'";
        List<WxLoginUser>  us=userService.wxuser(sql);
        if(us.size()>0){
            WxLoginUser wxuser=new WxLoginUser();
            ServletContext ap = Struts2Utils.getSession().getServletContext();
            Map<String, String> s = (Map) ap.getAttribute("loginMaps");
            s.put(wxuser.getUsername(), Struts2Utils.getSession().getId());
            ap.setAttribute("loginMaps", s);
            User user = userService.checkAccount(us.get(0).getUsername());
            if(user!=null){
                Struts2Utils.getSession().setAttribute("loginUser", user);
            }else{
                user=new User();
                user.setUsername(us.get(0).getUsername());
                user.setPassword(us.get(0).getPassword());
                Struts2Utils.getSession().setAttribute("loginUser", user);
            }
            Struts2Utils.getSession().setAttribute("userName", us.get(0).getUsername());
            //浏览器类型
            Struts2Utils.getSession().setAttribute("browsertype", browsertype);
            //浏览器类型
            Struts2Utils.getSession().setAttribute("tiaozhuaninit", "1");
            //Struts2Utils.getSession().setMaxInactiveInterval(60*30);
            msg.setSuccess(true);
            Struts2Utils.getSession().setAttribute("type", type);
            return returnurl();
        }else{
            Struts2Utils.getSession().setAttribute("openId", openid);
        }
//        System.out.println("openid: "+openid);
        return "relogin";
    }
    public String returnurl(){
        if(type==null){
            return "";
        }else if(type.equals("待签到列表")){
            return "signlist";
        }else if(type.equals("已签到列表")){
            return "signedlist";
        }else if(type.equals("销售分销查询")){
            return "saleperformance";
        }else if(type.equals("统计")){
            return "tongji";
        }else if(type.equals("采样点当日统计")){
            return "dangtian";
        }
        return "";
    }

    /**
     * 登陆密码为默认密码时的验证
     *
     * @return null
     */
    public String checkPwd() {

        User user = userService.checkAccount(entity.getUsername());
        Mademd5 md = new Mademd5();
        if (user != null) {
            //密码为默认密码
            String initpwd = "0000";
            if (user.getPassword().equals(md.toMd5(initpwd))) {
                msg.setMsg("为了账号安全，请修改初始密码");
            } else {
                //已经修改过密码
                msg.setSuccess(false);
                msg.setMsg("密码不正确，请填写正确的密码");
            }
        } else {
            msg.setSuccess(false);
            msg.setMsg("账号不存在，请向管理员索取");
        }

        Struts2Utils.renderJson(msg);
        return null;
    }

    /**
     * 登陆
     *
     * @return null
     * @throws Exception
     */
    public String login() throws Exception {
        log.info("ipipip:{}" , Struts2Utils.getIpAddress());
        User user = userService.checkAccount(entity.getUsername());

        if (user == null) {
            msg.setSuccess(false);
            msg.setMsg("账号不存在，请向管理员索取");
        } else {
            //默认可以正常登录
            boolean flag = true;
            //确认是否可以登录
            ControlUser cu1 = userService.findControlUser("other");
            //不能正常登录
            if (cu1 != null && cu1.getLoginstate() == 0) {
                ControlUser cu2 = userService.findControlUser(entity.getUsername());
                if (cu2 == null) {
                    flag = false;
                }
            }
            if (flag) {
                Mademd5 md = new Mademd5();
                if (user.getPassword().equals(md.toMd5(entity.getPassword()))) {
                    //失效
                    if (user.getLoginStatus() == 0) {
                        msg.setSuccess(false);
                        msg.setMsg("当前账号已失效，如有疑问请联系管理员");
                    } else {
                        Department d = departmentService.get(user.getDepartmentId());
                        Position p = positionService.get(user.getPositionId());
                        user.setDepartment(d);
                        user.setPosition(p);
                        ServletContext ap = Struts2Utils.getSession().getServletContext();
                        Map<String, String> s = (Map) ap.getAttribute("loginMaps");
                        if (s == null) {
                            s = new HashMap<String, String>();
                            msg.setMsg("登陆成功");
                        } else {
                            //存在此账号，且sessionid不同
                            if (s.containsKey(user.getUsername()) && !s.get(user.getUsername()).equals(Struts2Utils.getSession().getId())) {
                                msg.setMsg("此账号已登陆，如有问题请及时修改密码");
                            } else {
                                msg.setMsg("登陆成功");
                            }
                        }
                        s.put(user.getUsername(), Struts2Utils.getSession().getId());
                        ap.setAttribute("loginMaps", s);
                        Struts2Utils.getSession().setAttribute("loginUser", user);
                        Struts2Utils.getSession().setAttribute("userName", entity.getUsername());
                        //浏览器类型
                        Struts2Utils.getSession().setAttribute("browsertype", browsertype);
                        //浏览器类型
                        Struts2Utils.getSession().setAttribute("tiaozhuaninit", "1");
                        //Struts2Utils.getSession().setMaxInactiveInterval(60*30);
                        msg.setSuccess(true);
                        String oid=(String)Struts2Utils.getSession().getAttribute("openId");
                        String type=(String)Struts2Utils.getSession().getAttribute("type");
                        if(oid!=null && !"".equals(oid)){
                            String sql="select * from wx_loginuser where openid='"+oid+"'";
                            List<WxLoginUser>  us=userService.wxuser(sql);
                            if(us.size()<1){
                                WxLoginUser wxLoginUser=new WxLoginUser();
                                wxLoginUser.setOpenid(oid);
                                wxLoginUser.setUsername(entity.getUsername());
                                wxLoginUser.setPassword(user.getPassword());
                                userService.savewxuser(wxLoginUser);
                                if(type!=null && type.equals("待签到列表")){
                                    msg.setMsg("sign!modulepage.action");
                                }else if(type!=null && type.equals("已签到列表")){
                                    msg.setMsg("sign!signed.action");
                                }else if(type!=null && type.equals("销售分销查询")){
                                    msg.setMsg("sign!signed.action");
                                }else if(type!=null && type.equals("统计")){
                                    msg.setMsg("sign!signed.action");
                                }else if(type!=null && type.equals("采样点当日统计")){
                                    msg.setMsg("sign!signed.action");
                                }
                            }
                        }

                    }
                } else {
                    msg.setSuccess(false);
                    msg.setMsg("密码不正确，请填写正确的密码");
                }
            } else {
                //不能登录
                msg.setSuccess(false);
                msg.setMsg(cu1.getLoginInfo());
            }
        }


        Struts2Utils.renderJson(msg);
        return null;
    }

    /**
     * 修改初始密码并且登陆
     *
     * @return
     */
    public String updatePwd() {
        Mademd5 md = new Mademd5();
        //去空加密
        entity.setPassword(md.toMd5(entity.getPassword().trim()));
        //修改密码
        userService.updatePwd(entity);

        User user = userService.checkAccount(entity.getUsername());
        Department d = departmentService.get(user.getDepartmentId());
        Position p = positionService.get(user.getPositionId());
        user.setDepartment(d);
        user.setPosition(p);

        Struts2Utils.getSession().setAttribute("loginUser", user);
        //浏览器类型
        Struts2Utils.getSession().setAttribute("browsertype", browsertype);
        //浏览器类型
        Struts2Utils.getSession().setAttribute("tiaozhuaninit", "1");
        //Struts2Utils.getSession().setMaxInactiveInterval(60*1);

        msg.setSuccess(true);
        msg.setMsg("登陆成功");

        Struts2Utils.renderJson(msg);
        return null;
    }

    @Override
    public String modulepage() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public String online() {
        ServletContext ap = Struts2Utils.getSession().getServletContext();
        Map<String, String> s = (Map<String, String>) ap.getAttribute("loginMaps");
        StringBuffer sb = new StringBuffer();
        if (s != null) {
            for (String key : s.keySet()) {
                sb.append(key + ",");
            }
        }

        Struts2Utils.renderJson(sb);
        return null;
    }

    public String outlogin() {
        ServletContext application = Struts2Utils.getSession().getServletContext();
        User u = (User) Struts2Utils.getSession().getAttribute("loginUser");
        Map s = (Map) application.getAttribute("loginMaps");
        //存在此账号
        if (s != null && u != null && s.get(u.getUsername()) != null) {
            s.remove(u.getUsername());
            application.setAttribute("loginMaps", s);
        }
        //注销
        Struts2Utils.getSession().removeAttribute("loginUser");
        return "relogin";
    }

    public String ss() {
        ComboPooledDataSource ds = new ComboPooledDataSource();

        try {
            // 最大连接数
            System.out.println("最大连接数:" + ds.getMaxPoolSize());
            // 最小连接数
            System.out.println("最小连接数:" + ds.getMinPoolSize());
            // 正在使用连接数
            System.out.println("正在使用连接数:" + ds.getNumBusyConnections());
            // 空闲连接数
            System.out.println("空闲连接数:" + ds.getNumIdleConnections());
            // 总连接数
            System.out.println("总连接数:" + ds.getNumConnections());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     *  PDF 格式
      */
    static final int WORD_FORMAT_PDF = 17;

    public String wordoPdf() {
        System.out.println(System.getProperty("java.library.path"));
        System.out.println("启动Word...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", new Variant(false));
            Dispatch docs = app.getProperty("Documents").toDispatch();

            // String path =  this.getSession().getServletContext().getRealPath("/")+"attachment/";
            String sfileName = "d:\\" + "123" + ".doc";
            String toFileName = "d:\\" + "123" + ".pdf";

            doc = Dispatch.call(docs, "Open", sfileName).toDispatch();
            System.out.println("打开文档..." + sfileName);
            System.out.println("转换文档到PDF..." + toFileName);
            File tofile = new File(toFileName);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc,"SaveAs",toFileName,WORD_FORMAT_PDF);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");


        } catch (Exception e) {
            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            Dispatch.call(doc, "Close", false);
            System.out.println("关闭文档");
            if (app != null) {
                app.invoke("Quit", new Variant[]{});
            }
        }
        //如果没有这句话,winword.exe进程将不会关闭
        ComThread.Release();
        Struts2Utils.renderJson("123");
        return null;
    }

    public String bs() {
        Struts2Utils.renderJson("{\"current\":1,\"total\":2,\"rowCount\":25,\"rows\":[{\"type1\":\"123\",\"sender\":\"1234\"}]}");
        return null;
    }

    /**
     *  密码校验
     */
    public void checkPassword() {
        if (str != null && !str.equals("")) {
            String pwdLength="^.{8,}$";
            if (str.matches(pwdLength)) {
                String num = "^.*[0-9]+.*$";
                String az="^.*[a-zA-Z]+.*$";
                String zzts="^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$";
                if (str.matches(az) && str.matches(num) && str.matches(zzts)) {
                    String threeRepeat="^.*(.)\\1{2,}+.*$";
                    if (!str.matches(threeRepeat)) {
                        String threesRepeat="^.*(.{3})(.*)\\1+.*$";
                        if (!str.matches(threesRepeat)) {
                            String space = "^.*[\\s]+.*$";
                            if (!str.matches(space)) {
                                Struts2Utils.renderJson("ok");
                            } else {
                                Struts2Utils.renderJson("不能包含空格、制表符、换页符等空白字符");
                            }
                        } else {
                            Struts2Utils.renderJson("不能包含3位及以上字符组合的重复【eg：ab!23ab!】");
                        }
                    } else {
                        Struts2Utils.renderJson("不能包含3位及以上相同字符的重复【eg：x111@q& xxxx@q&1】");
                    }
                } else {
                    Struts2Utils.renderJson("必须包含数字、字母、特殊字符 三种");
                }
            } else {
                Struts2Utils.renderJson("密码 长度至少8位");
            }
        } else {
            Struts2Utils.renderJson("密码 不能为空！");
        }

    }
    
    public String threadtest(){
    	for(int i=0;i<5;i++){
    		Thread t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						System.out.println("new thread"+Thread.currentThread().getName());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			},"线程i_"+i);
    		t.start();
    	}
    	return null;
    }
    public String threadpooltest(){
    	ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(2,4,200,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(2));
    	for(int i=0;i<5;i++){
    		poolExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						System.out.println("threadpool:"+Thread.currentThread().getName());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
    		
    	}
    	return null;
    }
}
