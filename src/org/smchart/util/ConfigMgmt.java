/*    */ package org.smchart.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public abstract class ConfigMgmt
/*    */ {
/* 18 */   private static Properties prop = null;
/*    */   static final String PROP_FILE = "smchart.properties";
/*    */ 
/*    */   private static void loadProperties(String filePath)
/*    */   {
/* 22 */     FileInputStream in = null;
/*    */     try {
/* 24 */       File f = new File(filePath + "smchart.properties");
/* 25 */       if (prop == null) {
/* 26 */         prop = new Properties();
/* 27 */         if (f.exists())
/*    */         {
/* 29 */           in = new FileInputStream(filePath + "smchart.properties");
/*    */         }
/* 31 */         if (in != null) {
/* 32 */           prop.load(in);
/*    */         }
/* 34 */         if (prop.size() == 0) {
/* 35 */           setProperty("webproxy", "False", filePath);
/*    */         }
/*    */ 
/* 38 */         saveProperties(filePath);
/*    */       }
/*    */     }
/*    */     catch (Exception ex) {
/* 42 */       ex.printStackTrace();
/*    */     } finally {
/*    */       try {
/* 45 */         if (in != null)
/* 46 */           in.close();
/*    */       }
/*    */       catch (Exception ex) {
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public static String getProperty(String key, String filePath) {
/* 54 */     String val = null;
/* 55 */     loadProperties(filePath);
/* 56 */     if ((prop != null) && 
/* 57 */       (prop.containsKey(key))) {
/* 58 */       val = prop.getProperty(key);
/*    */     }
/*    */ 
/* 61 */     return val;
/*    */   }
/*    */ 
/*    */   public static void setProperty(String key, String val, String filePath) {
/* 65 */     loadProperties(filePath);
/* 66 */     if (prop != null) {
/* 67 */       if (prop.containsKey(key))
/* 68 */         prop.setProperty(key, val);
/*    */       else {
/* 70 */         prop.put(key, val);
/*    */       }
/*    */     }
/* 73 */     saveProperties(filePath);
/*    */   }
/*    */ 
/*    */   private static void saveProperties(String filePath) {
/* 77 */     FileOutputStream out = null;
/*    */     try {
/* 79 */       out = new FileOutputStream(filePath + "smchart.properties");
/* 80 */       prop.store(out, "---No Comment---");
/*    */     } catch (Exception ex) {
/* 82 */       ex.printStackTrace();
/*    */     } finally {
/*    */       try {
/* 85 */         if (out != null)
/* 86 */           out.close();
/*    */       }
/*    */       catch (Exception ex)
/*    */       {
/*    */       }
/*    */     }
/*    */   }
/*    */ }