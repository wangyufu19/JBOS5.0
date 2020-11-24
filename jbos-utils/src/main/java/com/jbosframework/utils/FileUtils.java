package com.jbosframework.utils;

import java.io.*;

/**
 * FileUtils
 * @author youfu.wang
 * @date 2019-11-12
 */
public class FileUtils {
    /**
     * 移动文件
     * @param oldfile 源文件
     * @param newfile 新文件
     */
    public boolean move(String oldfile, String newfile,boolean delete) throws IOException {
        byte[] buffer = new byte[1024];
        File of = new File(oldfile);
        BufferedInputStream bis = null;
        bis=new BufferedInputStream(new FileInputStream(of));
        File nf = new File(newfile);
        if(!nf.getParentFile().exists()) nf.getParentFile().mkdirs();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(nf));
        int v;
        while((v = bis.read(buffer)) != -1) {
            bos.write(buffer, 0 , v);
        }
        bis.close();
        bos.close();
        System.gc();
        if(delete) of.delete();
        return true;
    }

    public InputStream getInputStream(File file){
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bis;
    }
    public InputStream getInputStream(String path){
        File file=new File(path);
        return getInputStream(file);
    }
    public String getInputStreamString(String path,String charset){
        String s1="";
        BufferedInputStream bis = null;
        try {
            File file=new File(path);
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            int i= bis.available();
            byte buffer[] = new byte[i];
            bis.read(buffer);
            bis.close();
            s1= new String(buffer, 0, buffer.length,charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s1;
    }
    public boolean exists(String path){
        File file=new File(path);
        return file.exists();
    }
    public boolean exists(File file){
        return file.exists();
    }
    public void write(String s,String path,String charset){
        File file=new File(path);
        this.write(s, file,charset);
    }
    public void write(String s,File file,String charset){
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes(charset));
            BufferedInputStream bis=new BufferedInputStream(bais);
            if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if(file.exists()) file.delete();
            BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
            int i;
            byte buffer[] = new byte[1024];
            while((i = bis.read(buffer)) != -1){
                bos.write(buffer, 0 , i);
            }
            bais.close();
            bis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
