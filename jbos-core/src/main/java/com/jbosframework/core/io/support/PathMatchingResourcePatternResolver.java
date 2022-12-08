package com.jbosframework.core.io.support;


import com.jbosframework.core.Nullable;
import com.jbosframework.core.io.*;
import com.jbosframework.utils.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

public class PathMatchingResourcePatternResolver implements ResourcePatternResolver {

    private static final Log logger = LogFactory.getLog(PathMatchingResourcePatternResolver.class);
    @Nullable
    private static Method equinoxResolveMethod;
    private final ResourceLoader resourceLoader;
    private PathMatcher pathMatcher = new AntPathMatcher();

    public PathMatchingResourcePatternResolver() {
        this.resourceLoader = new DefaultResourceLoader();
    }

    public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
        Assert.notNull(resourceLoader, "ResourceLoader must not be null");
        this.resourceLoader = resourceLoader;
    }

    public PathMatchingResourcePatternResolver(@Nullable ClassLoader classLoader) {
        this.resourceLoader = new DefaultResourceLoader(classLoader);
    }

    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }
    @Nullable
    public ClassLoader getClassLoader() {
        return this.getResourceLoader().getClassLoader();
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        Assert.notNull(pathMatcher, "PathMatcher must not be null");
        this.pathMatcher = pathMatcher;
    }

    public PathMatcher getPathMatcher() {
        return this.pathMatcher;
    }

    public Resource getResource(String location) {
        return this.getResourceLoader().getResource(location);
    }

    public Resource[] getResources(String locationPattern) throws IOException {
        Assert.notNull(locationPattern, "Location pattern must not be null");
        if (locationPattern.startsWith("classpath*:")) {
            return this.getPathMatcher().isPattern(locationPattern.substring("classpath*:".length())) ? this.findPathMatchingResources(locationPattern) : this.findAllClassPathResources(locationPattern.substring("classpath*:".length()));
        } else {
            int prefixEnd = locationPattern.startsWith("war:") ? locationPattern.indexOf("*/") + 1 : locationPattern.indexOf(58) + 1;
            return this.getPathMatcher().isPattern(locationPattern.substring(prefixEnd)) ? this.findPathMatchingResources(locationPattern) : new Resource[]{this.getResourceLoader().getResource(locationPattern)};
        }
    }

    protected Resource[] findAllClassPathResources(String location) throws IOException {
        String path = location;
        if (location.startsWith("/")) {
            path = location.substring(1);
        }

        Set<Resource> result = this.doFindAllClassPathResources(path);
        if (logger.isTraceEnabled()) {
            logger.trace("Resolved classpath location [" + location + "] to resources " + result);
        }

        return (Resource[])result.toArray(new Resource[0]);
    }

    protected Set<Resource> doFindAllClassPathResources(String path) throws IOException {
        Set<Resource> result = new LinkedHashSet(16);
        ClassLoader cl = this.getClassLoader();
        Enumeration resourceUrls = cl != null ? cl.getResources(path) : ClassLoader.getSystemResources(path);

        while(resourceUrls.hasMoreElements()) {
            URL url = (URL)resourceUrls.nextElement();
            result.add(this.convertClassLoaderURL(url));
        }

        if ("".equals(path)) {
            this.addAllClassLoaderJarRoots(cl, result);
        }

        return result;
    }

    protected Resource convertClassLoaderURL(URL url) {
        return new UrlResource(url);
    }

    protected void addAllClassLoaderJarRoots(@Nullable ClassLoader classLoader, Set<Resource> result) {
        if (classLoader instanceof URLClassLoader) {
            try {
                URL[] var3 = ((URLClassLoader)classLoader).getURLs();
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    URL url = var3[var5];

                    try {
                        UrlResource jarResource = "jar".equals(url.getProtocol()) ? new UrlResource(url) : new UrlResource("jar:" + url + "!/");
                        if (jarResource.exists()) {
                            result.add(jarResource);
                        }
                    } catch (MalformedURLException var9) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Cannot search for matching files underneath [" + url + "] because it cannot be converted to a valid 'jar:' URL: " + var9.getMessage());
                        }
                    }
                }
            } catch (Exception var10) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Cannot introspect jar files since ClassLoader [" + classLoader + "] does not support 'getURLs()': " + var10);
                }
            }
        }

        if (classLoader == ClassLoader.getSystemClassLoader()) {
            this.addClassPathManifestEntries(result);
        }

        if (classLoader != null) {
            try {
                this.addAllClassLoaderJarRoots(classLoader.getParent(), result);
            } catch (Exception var8) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Cannot introspect jar files in parent ClassLoader since [" + classLoader + "] does not support 'getParent()': " + var8);
                }
            }
        }

    }

    protected void addClassPathManifestEntries(Set<Resource> result) {
        try {
            String javaClassPathProperty = System.getProperty("java.class.path");
            String[] var3 = StringUtils.delimitedListToStringArray(javaClassPathProperty, System.getProperty("path.separator"));
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String path = var3[var5];

                try {
                    String filePath = (new File(path)).getAbsolutePath();
                    int prefixIndex = filePath.indexOf(58);
                    if (prefixIndex == 1) {
                        filePath = StringUtils.capitalize(filePath);
                    }

                    UrlResource jarResource = new UrlResource("jar:file:" + filePath + "!/");
                    if (!result.contains(jarResource) && !this.hasDuplicate(filePath, result) && jarResource.exists()) {
                        result.add(jarResource);
                    }
                } catch (MalformedURLException var10) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Cannot search for matching files underneath [" + path + "] because it cannot be converted to a valid 'jar:' URL: " + var10.getMessage());
                    }
                }
            }
        } catch (Exception var11) {
            if (logger.isDebugEnabled()) {
                logger.debug("Failed to evaluate 'java.class.path' manifest entries: " + var11);
            }
        }

    }

    private boolean hasDuplicate(String filePath, Set<Resource> result) {
        if (result.isEmpty()) {
            return false;
        } else {
            String duplicatePath = filePath.startsWith("/") ? filePath.substring(1) : "/" + filePath;

            try {
                return result.contains(new UrlResource("jar:file:" + duplicatePath + "!/"));
            } catch (MalformedURLException var5) {
                return false;
            }
        }
    }

    protected Resource[] findPathMatchingResources(String locationPattern) throws IOException {
        String rootDirPath = this.determineRootDir(locationPattern);
        String subPattern = locationPattern.substring(rootDirPath.length());
        Resource[] rootDirResources = this.getResources(rootDirPath);
        Set<Resource> result = new LinkedHashSet(16);
        Resource[] var6 = rootDirResources;
        int var7 = rootDirResources.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Resource rootDirResource = this.resolveRootDirResource(var6[var8]);
            URL rootDirUrl = ((Resource)rootDirResource).getURL();
            if (equinoxResolveMethod != null && rootDirUrl.getProtocol().startsWith("bundle")) {
                URL resolvedUrl = (URL) ReflectionUtils.invokeMethod(equinoxResolveMethod, (Object)null, new Object[]{rootDirUrl});
                if (resolvedUrl != null) {
                    rootDirUrl = resolvedUrl;
                }

                rootDirResource = new UrlResource(rootDirUrl);
            }

            if (rootDirUrl.getProtocol().startsWith("vfs")) {
                result.addAll(PathMatchingResourcePatternResolver.VfsResourceMatchingDelegate.findMatchingResources(rootDirUrl, subPattern, this.getPathMatcher()));
            } else if (!ResourceUtils.isJarURL(rootDirUrl) && !this.isJarResource((Resource)rootDirResource)) {
                result.addAll(this.doFindPathMatchingFileResources((Resource)rootDirResource, subPattern));
            } else {
                result.addAll(this.doFindPathMatchingJarResources((Resource)rootDirResource, rootDirUrl, subPattern));
            }
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Resolved location pattern [" + locationPattern + "] to resources " + result);
        }

        return (Resource[])result.toArray(new Resource[0]);
    }

    protected String determineRootDir(String location) {
        int prefixEnd = location.indexOf(58) + 1;

        int rootDirEnd;
        for(rootDirEnd = location.length(); rootDirEnd > prefixEnd && this.getPathMatcher().isPattern(location.substring(prefixEnd, rootDirEnd)); rootDirEnd = location.lastIndexOf(47, rootDirEnd - 2) + 1) {
        }

        if (rootDirEnd == 0) {
            rootDirEnd = prefixEnd;
        }

        return location.substring(0, rootDirEnd);
    }

    protected Resource resolveRootDirResource(Resource original) throws IOException {
        return original;
    }

    protected boolean isJarResource(Resource resource) throws IOException {
        return false;
    }

    protected Set<Resource> doFindPathMatchingJarResources(Resource rootDirResource, URL rootDirURL, String subPattern) throws IOException {
        URLConnection con = rootDirURL.openConnection();
        JarFile jarFile;
        String jarFileUrl;
        String rootEntryPath;
        boolean closeJarFile;
        if (con instanceof JarURLConnection) {
            JarURLConnection jarCon = (JarURLConnection)con;
            ResourceUtils.useCachesIfNecessary(jarCon);
            jarFile = jarCon.getJarFile();
            jarFileUrl = jarCon.getJarFileURL().toExternalForm();
            JarEntry jarEntry = jarCon.getJarEntry();
            rootEntryPath = jarEntry != null ? jarEntry.getName() : "";
            closeJarFile = !jarCon.getUseCaches();
        } else {
            String urlFile = rootDirURL.getFile();

            try {
                int separatorIndex = urlFile.indexOf("*/");
                if (separatorIndex == -1) {
                    separatorIndex = urlFile.indexOf("!/");
                }

                if (separatorIndex != -1) {
                    jarFileUrl = urlFile.substring(0, separatorIndex);
                    rootEntryPath = urlFile.substring(separatorIndex + 2);
                    jarFile = this.getJarFile(jarFileUrl);
                } else {
                    jarFile = new JarFile(urlFile);
                    jarFileUrl = urlFile;
                    rootEntryPath = "";
                }

                closeJarFile = true;
            } catch (ZipException var17) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skipping invalid jar classpath entry [" + urlFile + "]");
                }

                return Collections.emptySet();
            }
        }

        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Looking for matching resources in jar file [" + jarFileUrl + "]");
            }

            if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
                rootEntryPath = rootEntryPath + "/";
            }

            Set<Resource> result = new LinkedHashSet(8);
            Enumeration entries = jarFile.entries();

            while(entries.hasMoreElements()) {
                JarEntry entry = (JarEntry)entries.nextElement();
                String entryPath = entry.getName();
                if (entryPath.startsWith(rootEntryPath)) {
                    String relativePath = entryPath.substring(rootEntryPath.length());
                    if (this.getPathMatcher().match(subPattern, relativePath)) {
                        result.add(rootDirResource.createRelative(relativePath));
                    }
                }
            }
            return result;
        } finally {
            if (closeJarFile) {
                jarFile.close();
            }

        }
    }

    protected JarFile getJarFile(String jarFileUrl) throws IOException {
        if (jarFileUrl.startsWith("file:")) {
            try {
                return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
            } catch (URISyntaxException var3) {
                return new JarFile(jarFileUrl.substring("file:".length()));
            }
        } else {
            return new JarFile(jarFileUrl);
        }
    }

    protected Set<Resource> doFindPathMatchingFileResources(Resource rootDirResource, String subPattern) throws IOException {
        File rootDir;
        try {
            rootDir = rootDirResource.getFile().getAbsoluteFile();
        } catch (FileNotFoundException var5) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot search for matching files underneath " + rootDirResource + " in the file system: " + var5.getMessage());
            }

            return Collections.emptySet();
        } catch (Exception var6) {
            if (logger.isInfoEnabled()) {
                logger.info("Failed to resolve " + rootDirResource + " in the file system: " + var6);
            }

            return Collections.emptySet();
        }

        return this.doFindMatchingFileSystemResources(rootDir, subPattern);
    }

    protected Set<Resource> doFindMatchingFileSystemResources(File rootDir, String subPattern) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Looking for matching resources in directory tree [" + rootDir.getPath() + "]");
        }

        Set<File> matchingFiles = this.retrieveMatchingFiles(rootDir, subPattern);
        Set<Resource> result = new LinkedHashSet(matchingFiles.size());
        Iterator var5 = matchingFiles.iterator();

        while(var5.hasNext()) {
            File file = (File)var5.next();
            result.add(new FileSystemResource(file));
        }

        return result;
    }

    protected Set<File> retrieveMatchingFiles(File rootDir, String pattern) throws IOException {
        if (!rootDir.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Skipping [" + rootDir.getAbsolutePath() + "] because it does not exist");
            }

            return Collections.emptySet();
        } else if (!rootDir.isDirectory()) {
            if (logger.isInfoEnabled()) {
                logger.info("Skipping [" + rootDir.getAbsolutePath() + "] because it does not denote a directory");
            }

            return Collections.emptySet();
        } else if (!rootDir.canRead()) {
            if (logger.isInfoEnabled()) {
                logger.info("Skipping search for matching files underneath directory [" + rootDir.getAbsolutePath() + "] because the application is not allowed to read the directory");
            }

            return Collections.emptySet();
        } else {
            String fullPattern = StringUtils.replace(rootDir.getAbsolutePath(), File.separator, "/");
            if (!pattern.startsWith("/")) {
                fullPattern = fullPattern + "/";
            }

            fullPattern = fullPattern + StringUtils.replace(pattern, File.separator, "/");
            Set<File> result = new LinkedHashSet(8);
            this.doRetrieveMatchingFiles(fullPattern, rootDir, result);
            return result;
        }
    }

    protected void doRetrieveMatchingFiles(String fullPattern, File dir, Set<File> result) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Searching directory [" + dir.getAbsolutePath() + "] for files matching pattern [" + fullPattern + "]");
        }

        File[] var4 = this.listDirectory(dir);
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            File content = var4[var6];
            String currPath = StringUtils.replace(content.getAbsolutePath(), File.separator, "/");
            if (content.isDirectory() && this.getPathMatcher().matchStart(fullPattern, currPath + "/")) {
                if (!content.canRead()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() + "] because the application is not allowed to read the directory");
                    }
                } else {
                    this.doRetrieveMatchingFiles(fullPattern, content, result);
                }
            }

            if (this.getPathMatcher().match(fullPattern, currPath)) {
                result.add(content);
            }
        }

    }

    protected File[] listDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            if (logger.isInfoEnabled()) {
                logger.info("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
            }

            return new File[0];
        } else {
            Arrays.sort(files, Comparator.comparing(File::getName));
            return files;
        }
    }

    static {
        try {
            Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator", PathMatchingResourcePatternResolver.class.getClassLoader());
            equinoxResolveMethod = fileLocatorClass.getMethod("resolve", URL.class);
            logger.trace("Found Equinox FileLocator for OSGi bundle URL resolution");
        } catch (Throwable var1) {
            equinoxResolveMethod = null;
        }

    }

    private static class PatternVirtualFileVisitor implements InvocationHandler {
        private final String subPattern;
        private final PathMatcher pathMatcher;
        private final String rootPath;
        private final Set<Resource> resources = new LinkedHashSet();

        public PatternVirtualFileVisitor(String rootPath, String subPattern, PathMatcher pathMatcher) {
            this.subPattern = subPattern;
            this.pathMatcher = pathMatcher;
            this.rootPath = !rootPath.isEmpty() && !rootPath.endsWith("/") ? rootPath + "/" : rootPath;
        }

        @Nullable
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (Object.class == method.getDeclaringClass()) {
                if (methodName.equals("equals")) {
                    return proxy == args[0];
                }

                if (methodName.equals("hashCode")) {
                    return System.identityHashCode(proxy);
                }
            } else {
                if ("getAttributes".equals(methodName)) {
                    return this.getAttributes();
                }

                if ("visit".equals(methodName)) {
                    this.visit(args[0]);
                    return null;
                }

                if ("toString".equals(methodName)) {
                    return this.toString();
                }
            }

            throw new IllegalStateException("Unexpected method invocation: " + method);
        }

        public void visit(Object vfsResource) {
            if (this.pathMatcher.match(this.subPattern, VfsPatternUtils.getPath(vfsResource).substring(this.rootPath.length()))) {
                this.resources.add(new VfsResource(vfsResource));
            }

        }

        @Nullable
        public Object getAttributes() {
            return VfsPatternUtils.getVisitorAttributes();
        }

        public Set<Resource> getResources() {
            return this.resources;
        }

        public int size() {
            return this.resources.size();
        }

        public String toString() {
            return "sub-pattern: " + this.subPattern + ", resources: " + this.resources;
        }
    }

    private static class VfsResourceMatchingDelegate {
        private VfsResourceMatchingDelegate() {
        }

        public static Set<Resource> findMatchingResources(URL rootDirURL, String locationPattern, PathMatcher pathMatcher) throws IOException {
            Object root = VfsPatternUtils.findRoot(rootDirURL);
            PathMatchingResourcePatternResolver.PatternVirtualFileVisitor visitor = new PathMatchingResourcePatternResolver.PatternVirtualFileVisitor(VfsPatternUtils.getPath(root), locationPattern, pathMatcher);
            VfsPatternUtils.visit(root, visitor);
            return visitor.getResources();
        }
    }
}
