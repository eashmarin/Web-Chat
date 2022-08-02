package app;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class FTLManager {
    private static final FTLManager instance = new FTLManager();

    private Configuration config;

    private final HashMap<String, Object> root;

    public static FTLManager getInstance() {
        return instance;
    }

    private void initFTLConfig() {
        config = new Configuration(Configuration.VERSION_2_3_31);
        try {
            config.setDirectoryForTemplateLoading(new File(getClass().getResource("/templates").getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setWrapUncheckedExceptions(true);
        config.setFallbackOnNullLoopVariable(false);
    }

    public void putParameter(String key, Object value) {
        root.put(key, value);
    }

    public void executeTemplate(String templateName, Writer out) {
        try {
            Template tmp = config.getTemplate(templateName);

            tmp.process(root, out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        root.clear();
    }

    private FTLManager() {
        root = new HashMap<>();

        initFTLConfig();
    }
}
