package com.lims.core.utils.web;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
 /**
  * json数据输出，树grid
  * @author 聂梦肖
  *
  */
 public abstract class JsonUtils
 {
   protected static final ObjectMapper OM = new ObjectMapper();
 
   public static String toJsonString(Object obj)
     throws Exception
   {
     if (obj != null) {
       return OM.writeValueAsString(obj);
     }
     return "";
   }
 
   public static <T> T toObjectByJson(String json, Class<T> clazz)
     throws Exception
   {
     if ((json != null) && (!json.equals(""))) {
       return OM.readValue(json, clazz);
     }
     return null;
   }
   public static String formatStr(String str) {
     char[] c = str.toCharArray();
     for (int i = 0; i < c.length; i++)
       if (c[i] == ' ') {
         c[i] = '　';
       }
       else if ((c[i] <= '/') || (c[i] >= ':'))
       {
         if ((c[i] <= '@') || (c[i] >= '['))
         {
           if ((c[i] <= '`') || (c[i] >= '{'))
           {
             if (c[i] < '')
               c[i] = ((char)(c[i] + 65248)); 
           }
         }
       }
     return new String(c);
   }
// 原文件函数为以下注释内容。
//   public static <T> List<T> toListByJsonArray(String jsonArrya, Class<List<T> > clazz)
//   throws Exception
// {
//   List<T> jsonList = (List<T>)OM.readValue(jsonArrya, clazz);
//   return jsonList;
// }
   
   public static <T> List<T> toListByJsonArray(String jsonArrya, Class<List> class1)
     throws Exception
   {
     List<T> jsonList = (List<T>)OM.readValue(jsonArrya, class1);
     return jsonList;
   }
 }