/*    */ package org.smchart.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutput;
/*    */ import java.io.ObjectOutputStream;
/*    */ 
/*    */ public class ObjectStateManager
/*    */ {
/*    */   public static void write(Object f, String filename)
/*    */     throws Exception
/*    */   {
/* 30 */     ObjectOutput out = new ObjectOutputStream(new FileOutputStream(filename));
/* 31 */     out.writeObject(f);
/* 32 */     out.close();
/*    */ 
/* 34 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 35 */     out = new ObjectOutputStream(bos);
/* 36 */     out.writeObject(f);
/* 37 */     out.close();
/*    */ 
/* 40 */     byte[] buf = bos.toByteArray();
/*    */   }
/*    */ 
/*    */   public static Object read(String filename) throws Exception {
/* 44 */     File file = new File(filename);
/* 45 */     byte[] bytes = getBytesFromFile(file);
/* 46 */     ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
/* 47 */     Object obj = in.readObject();
/* 48 */     in.close();
/* 49 */     return obj;
/*    */   }
/*    */ 
/*    */   public static byte[] getBytesFromFile(File file) throws IOException {
/* 53 */     InputStream is = new FileInputStream(file);
/*    */ 
/* 56 */     long length = file.length();
/*    */ 
/* 62 */     if (length > 2147483647L);
/* 67 */     byte[] bytes = new byte[(int)length];
/*    */ 
/* 70 */     int offset = 0;
/* 71 */     int numRead = 0;
/* 72 */     while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
/* 73 */       offset += numRead;
/*    */     }
/*    */ 
/* 77 */     if (offset < bytes.length) {
/* 78 */       throw new IOException("Could not completely read file " + file.getName());
/*    */     }
/*    */ 
/* 82 */     is.close();
/* 83 */     return bytes;
/*    */   }
/*    */ }