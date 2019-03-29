package com.jbosframework.beans.support;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * AnnotationClassSupport
 * @author youfu.wang
 * @version 1.0
 */
public class AnnotationClassSupport {

    /**
     * 得到类库包
     * @param classPath
     * @param packageName
     * @return
     */
    public static List<String> getBasePackageClasses(URL classPath,String packageName){
        if(classPath==null) {
            return null;
        }
        packageName=packageName.replace(".","/");
        List<String> allClasses = new ArrayList<String>();
        String type = classPath.getProtocol();
        if(type.equals("file")){
            putClassNameByFile(classPath.getPath()+packageName,allClasses);
        }
        return allClasses;
    }
    private static void putClassNameByFile(String filePath, List<String> allClasses) {
        try {
            filePath= URLDecoder.decode(filePath,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                putClassNameByFile(childFile.getPath(), allClasses);
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    allClasses.add(childFilePath);
                }
            }
        }
    }
}
