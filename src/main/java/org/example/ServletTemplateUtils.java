package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;

import static org.example.TimeServletConstants.*;

/**
 * ServletTemplateUtils is a class that used to parse templates with context
 */
public class ServletTemplateUtils {

    private static final TemplateEngine templateEngine = new TemplateEngine();
    private static WebApplicationTemplateResolver templateResolver;
    public ServletTemplateUtils(ServletContext servletContext){
        JavaxServletWebApplication jswa = JavaxServletWebApplication.buildApplication(servletContext);
        templateResolver = new WebApplicationTemplateResolver(jswa);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateEngine.setTemplateResolver(templateResolver);
    }

    public void processHMTLTemplate(String templateName, Context context, Writer writer) {
        templateEngine.process(templateName, context, writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
