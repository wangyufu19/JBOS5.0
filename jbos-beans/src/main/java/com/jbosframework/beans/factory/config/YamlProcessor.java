package com.jbosframework.beans.factory.config;

import com.jbosframework.core.CollectionFactory;
import com.jbosframework.core.Nullable;
import com.jbosframework.core.io.Resource;
import com.jbosframework.utils.Assert;
import com.jbosframework.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public abstract class YamlProcessor {
    private final Log logger = LogFactory.getLog(this.getClass());
    private YamlProcessor.ResolutionMethod resolutionMethod;
    private Resource[] resources;
    private List<DocumentMatcher> documentMatchers;
    private boolean matchDefault;

    public YamlProcessor() {
        this.resolutionMethod = YamlProcessor.ResolutionMethod.OVERRIDE;
        this.resources = new Resource[0];
        this.documentMatchers = Collections.emptyList();
        this.matchDefault = true;
    }

    public void setDocumentMatchers(YamlProcessor.DocumentMatcher... matchers) {
        this.documentMatchers = Arrays.asList(matchers);
    }

    public void setMatchDefault(boolean matchDefault) {
        this.matchDefault = matchDefault;
    }

    public void setResolutionMethod(YamlProcessor.ResolutionMethod resolutionMethod) {
        Assert.notNull(resolutionMethod, "ResolutionMethod must not be null");
        this.resolutionMethod = resolutionMethod;
    }

    public void setResources(Resource... resources) {
        this.resources = resources;
    }

    protected void process(YamlProcessor.MatchCallback callback) {
        Yaml yaml = this.createYaml();
        Resource[] var3 = this.resources;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Resource resource = var3[var5];
            boolean found = this.process(callback, yaml, resource);
            if (this.resolutionMethod == YamlProcessor.ResolutionMethod.FIRST_FOUND && found) {
                return;
            }
        }

    }

    protected Yaml createYaml() {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        return new Yaml(options);
    }

    private boolean process(YamlProcessor.MatchCallback callback, Yaml yaml, Resource resource) {
        int count = 0;

        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Loading from YAML: " + resource);
            }

            Reader reader = new UnicodeReader(resource.getInputStream());
            Throwable var6 = null;

            try {
                Iterator var7 = yaml.loadAll(reader).iterator();

                while(var7.hasNext()) {
                    Object object = var7.next();
                    if (object != null && this.process(this.asMap(object), callback)) {
                        ++count;
                        if (this.resolutionMethod == YamlProcessor.ResolutionMethod.FIRST_FOUND) {
                            break;
                        }
                    }
                }

                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Loaded " + count + " document" + (count > 1 ? "s" : "") + " from YAML resource: " + resource);
                }
            } catch (Throwable var17) {
                var6 = var17;
                throw var17;
            } finally {
                if (reader != null) {
                    if (var6 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var16) {
                            var6.addSuppressed(var16);
                        }
                    } else {
                        reader.close();
                    }
                }

            }
        } catch (IOException var19) {
            this.handleProcessError(resource, var19);
        }

        return count > 0;
    }

    private void handleProcessError(Resource resource, IOException ex) {
        if (this.resolutionMethod != YamlProcessor.ResolutionMethod.FIRST_FOUND && this.resolutionMethod != YamlProcessor.ResolutionMethod.OVERRIDE_AND_IGNORE) {
            throw new IllegalStateException(ex);
        } else {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Could not load map from " + resource + ": " + ex.getMessage());
            }

        }
    }

    private Map<String, Object> asMap(Object object) {
        Map<String, Object> result = new LinkedHashMap();
        if (!(object instanceof Map)) {
            result.put("document", object);
            return result;
        } else {
            Map<Object, Object> map = (Map)object;
            map.forEach((key, value) -> {
                if (value instanceof Map) {
                    value = this.asMap(value);
                }

                if (key instanceof CharSequence) {
                    result.put(key.toString(), value);
                } else {
                    result.put("[" + key.toString() + "]", value);
                }

            });
            return result;
        }
    }

    private boolean process(Map<String, Object> map, YamlProcessor.MatchCallback callback) {
        Properties properties = CollectionFactory.createStringAdaptingProperties();
        properties.putAll(this.getFlattenedMap(map));
        if (this.documentMatchers.isEmpty()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Merging document (no matchers set): " + map);
            }

            callback.process(properties, map);
            return true;
        } else {
            YamlProcessor.MatchStatus result = YamlProcessor.MatchStatus.ABSTAIN;
            Iterator var5 = this.documentMatchers.iterator();

            YamlProcessor.MatchStatus match;
            do {
                if (!var5.hasNext()) {
                    if (result == YamlProcessor.MatchStatus.ABSTAIN && this.matchDefault) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("Matched document with default matcher: " + map);
                        }

                        callback.process(properties, map);
                        return true;
                    }

                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Unmatched document: " + map);
                    }

                    return false;
                }

                YamlProcessor.DocumentMatcher matcher = (YamlProcessor.DocumentMatcher)var5.next();
                match = matcher.matches(properties);
                result = YamlProcessor.MatchStatus.getMostSpecific(match, result);
            } while(match != YamlProcessor.MatchStatus.FOUND);

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Matched document with document matcher: " + properties);
            }

            callback.process(properties, map);
            return true;
        }
    }

    protected final Map<String, Object> getFlattenedMap(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap();
        this.buildFlattenedMap(result, source, (String)null);
        return result;
    }

    private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, @Nullable String path) {
        source.forEach((key, value) -> {
            if (StringUtils.hasText(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + '.' + key;
                }
            }

            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                Map<String, Object> map = (Map)value;
                this.buildFlattenedMap(result, map, key);
            } else if (value instanceof Collection) {
                Collection<Object> collection = (Collection)value;
                if (collection.isEmpty()) {
                    result.put(key, "");
                } else {
                    int count = 0;
                    Iterator var7 = collection.iterator();

                    while(var7.hasNext()) {
                        Object object = var7.next();
                        this.buildFlattenedMap(result, Collections.singletonMap("[" + count++ + "]", object), key);
                    }
                }
            } else {
                result.put(key, value != null ? value : "");
            }

        });
    }

    public static enum ResolutionMethod {
        OVERRIDE,
        OVERRIDE_AND_IGNORE,
        FIRST_FOUND;

        private ResolutionMethod() {
        }
    }

    public static enum MatchStatus {
        FOUND,
        NOT_FOUND,
        ABSTAIN;

        private MatchStatus() {
        }

        public static YamlProcessor.MatchStatus getMostSpecific(YamlProcessor.MatchStatus a, YamlProcessor.MatchStatus b) {
            return a.ordinal() < b.ordinal() ? a : b;
        }
    }

    public interface DocumentMatcher {
        YamlProcessor.MatchStatus matches(Properties var1);
    }

    public interface MatchCallback {
        void process(Properties var1, Map<String, Object> var2);
    }
}
