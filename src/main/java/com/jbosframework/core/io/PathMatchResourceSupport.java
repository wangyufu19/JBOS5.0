package com.jbosframework.core.io;

import com.jbosframework.common.utils.AntPathMatcher;
import com.jbosframework.common.utils.PathMatcher;
import com.jbosframework.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PathMatchResourceSupport
 * @author youfu.wang
 * @since 2020.10.10
 */
@Slf4j
public class PathMatchResourceSupport {
    private PathMatcher pathMatcher=new AntPathMatcher();

    public Resource[] getResources(String locationPattern) throws IOException {
        if(locationPattern.startsWith("classpath")){
            String path=locationPattern.substring("classpath:".length());
            if(pathMatcher.isPattern(path)){
                return getClassPathResources(path);
            }
        }
        return null;
    }
    private Resource[] getClassPathResources(String location) throws IOException {
        List<Resource> resources=new ArrayList<Resource>();
        String path=location;
        if(location.startsWith("/")){
            path=location.substring(1);
        }
        String rootDirPath=path.substring(0,path.indexOf("/"));
        String subPattern=path.substring(path.indexOf("/"));
        Resource resource=new ClassPathResource(rootDirPath);
        if(resource.getFile().isDirectory()){
            String fullPattern=StringUtils.replace(resource.getFile().getAbsolutePath(),File.separator,"/")+subPattern;
            this.putClassPathResources(fullPattern,resource.getFile(),resources);
        }
        return resources.toArray(new FileResource[resources.size()]);
    }
    private void putClassPathResources(String fullPattern,File file, List<Resource> resources) throws IOException {
        if(file.isDirectory()){
            for(File dirFile:file.listFiles()){
                putClassPathResources(fullPattern,dirFile,resources);
            }
        }else{
            String path=StringUtils.replace(file.getAbsolutePath(),File.separator,"/");
            if(this.pathMatcher.match(fullPattern,path)){
                Resource resource=new FileResource(file.getPath());
                resources.add(resource);
            }
        }
    }

}
