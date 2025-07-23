package ps.demo.commonlibx.common;

import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

public class ThymeleafTool {

    public static String processText(String content, Map<String, Object> variables) {
        return process(content, variables, TemplateMode.TEXT);
    }
    public static String processHtml(String content, Map<String, Object> variables) {
        return process(content, variables, TemplateMode.HTML);
    }
    public static String processJavascript(String content, Map<String, Object> variables) {
        return process(content, variables, TemplateMode.JAVASCRIPT);
    }
    public static String processCss(String content, Map<String, Object> variables) {
        return process(content, variables, TemplateMode.CSS);
    }
    public static String processXml(String content, Map<String, Object> variables) {
        return process(content, variables, TemplateMode.XML);
    }

    public static String process(String content, Map<String, Object> variables, TemplateMode templateMode) {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(templateMode);
        templateEngine.setTemplateResolver(templateResolver);
        Context ct = new Context();
        ct.setVariable("env", System.getenv());
        if (!CollectionUtils.isEmpty(variables)) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                ct.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return templateEngine.process(content, ct);
    }



}
