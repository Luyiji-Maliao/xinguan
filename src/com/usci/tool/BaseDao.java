package com.usci.tool;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.util.List;  
  
  
public class BaseDao {  
      
    /**  
     * 数据库连接Connection  
     */  
    public Connection con;  
      
    /**  
     * 结果集ResultSet  
     */  
    public ResultSet rs;  
      
    /**  
     * JDBC连接数据库路径字符串  
     */  
    private String url;  
      
    /**  
     * JDBC连接数据库账户  
     */  
    private String user;  
      
    /**  
     * JDBC连接数据库密码  
     */  
    private String password;  
      
    /**  
     * 数据库驱动加载类  
     */  
    private String driver;  
      
      
    /**  
     * 无参构造函数  
     */  
    public BaseDao(){  
        //连接sqlserver数据库  
        //this.setBaseDao("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://127.0.0.1:1433;databaseName=registerManage","sa","1QA2ws3ed");  
    	//现场本地库
    	this.setBaseDao("com.mysql.jdbc.Driver","jdbc:mysql://10.232.83.35:3306/center?useUnicode=true&characterEncoding=utf-8&useOldAliasMetadataBehavior=true","root","root");
    	System.out.println("连接本地 sqlserver  数据库成功------------------------------");
    	//测试本地库
    	//this.setBaseDao("com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://127.0.0.1:1433;databaseName=registerManage","sa","sa");
    	//连接mysql数据库  
        //this.setBaseDao("com.mysql.jdbc.Driver","jdbc:sqlserver://192.168.1.80:1433;databaseName=registerManage","sa","sa");  
        //连接orcle数据库  
        //this.setBaseDao("oracle.jdbc.driver.OracleDriver","jdbc:oracle://127.0.0.1/webfilesystem","root","root");  
    }  
      
    /**  
     * 有参构造函数  
     */  
    public BaseDao(String driver, String url, String user, String password){  
        this.driver = driver;  
        this.url = url;  
        this.user = user;  
        this.password = password;  
    }  
      
    /**  
     * 设置JDBC连接数据库路径、账号、密码  
     */  
    public void setBaseDao(String driver, String url, String user, String password){  
        this.driver = driver;  
        this.url = url;  
        this.user = user;  
        this.password = password;  
    }  
      
    /**  
     * 获取数据库连接对象Connection  
     */  
    public Connection getConnection(){  
        try {  
            Class.forName(driver);  
            con = DriverManager.getConnection(url,user,password);  
            //con.setAutoCommit(false);
        }catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }catch (SQLException e) {  
            e.printStackTrace();  
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        return con;  
    }  
      
    /**  
     * 关闭资源的函数  
     */  
    public void closeAll(){  
        if(rs != null){  
            try {  
                rs.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        if(con != null){  
            try {  
                con.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
          
    }  
    /**  
     * 查询多行多列数据到成员ResultSet对象  
     */  
    public ResultSet executeQuery(String sql){  
        try{  
            getConnection();  
            PreparedStatement ps = con.prepareStatement(sql);  
            rs = ps.executeQuery();  
        }catch (SQLException e) {  
            e.printStackTrace();  
        }catch (Exception e) {  
              
        }  
        return rs;  
    }  

   
      
      
}  