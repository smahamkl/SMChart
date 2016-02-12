/*    */ package org.smchart.util;
/*    */ 
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class DownloadQuotes
/*    */ {
/*    */   static final int size = 1024;
/*    */   private String m_url;
/*    */   private String m_destFilePath;
/* 19 */   private boolean m_isProxy = false;
/* 20 */   private String m_proxyHost = "";
/* 21 */   private String m_proxyPort = "";
/*    */ 
/*    */   public DownloadQuotes(String url, String destFile) throws Exception {
/* 24 */     this.m_url = url;
/* 25 */     this.m_destFilePath = destFile;
/* 26 */     downloadFile();
/*    */   }
/*    */ 
/*    */   public DownloadQuotes(String url, String destFile, String proxyHost, String proxyPort) throws Exception {
/* 30 */     this.m_url = url;
/* 31 */     this.m_destFilePath = destFile;
/* 32 */     this.m_proxyHost = proxyHost;
/* 33 */     this.m_proxyPort = proxyPort;
/* 34 */     this.m_isProxy = true;
/* 35 */     downloadFile();
/*    */   }
/*    */   private void downloadFile() throws Exception {
/* 38 */     OutputStream os = null;
/* 39 */     URLConnection URLConn = null;
/* 40 */     InputStream is = null;
/*    */ 
/* 44 */     int ByteWritten = 0;
/*    */     try {
/* 46 */       File f = new File(this.m_destFilePath);
/* 47 */       if (!f.exists())
/*    */       {
/* 49 */         if (this.m_isProxy) {
/* 50 */           System.getProperties().put("proxySet", "true");
/* 51 */           System.getProperties().put("proxyPort", this.m_proxyPort);
/* 52 */           System.getProperties().put("proxyHost", this.m_proxyHost);
/*    */         }
/* 54 */         URL fileUrl = new URL(this.m_url);
/* 55 */         os = new BufferedOutputStream(new FileOutputStream(this.m_destFilePath));
/* 56 */         URLConn = fileUrl.openConnection();
/* 57 */         is = URLConn.getInputStream();
/* 58 */         byte[] buf = new byte[1024];
/*    */         int ByteRead;
/* 59 */         while ((ByteRead = is.read(buf)) != -1) {
/* 60 */           os.write(buf, 0, ByteRead);
/* 61 */           ByteWritten += ByteRead;
/*    */         }
/* 63 */         os.flush();
/*    */       }
/*    */     } catch (Exception e) {
/* 66 */       throw new Exception(e.getMessage(), e);
/*    */     } finally {
/* 68 */       if (is != null) {
/* 69 */         is.close();
/*    */       }
/* 71 */       if (os != null)
/* 72 */         os.close();
/*    */     }
/*    */   }
/*    */ }