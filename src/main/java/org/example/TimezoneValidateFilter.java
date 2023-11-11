package org.example;

import org.thymeleaf.context.Context;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.example.TimeServletConstants.*;

@WebFilter("/time")
public class TimezoneValidateFilter extends HttpFilter {
    private ServletTemplateUtils TEMPLATE_UTILS;
    @Override
    public void init() throws ServletException {
        TEMPLATE_UTILS = new ServletTemplateUtils(this.getServletContext());
        super.init();
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String parameter = req.getParameter(PARAMETER_NAME.getText());
        if (parameter == null) {
            chain.doFilter(req, res);
            return;
        }
        if (validate(parameter.replace(' ', '+'))) {
            chain.doFilter(req, res);
            return;
        }
        res.setContentType(DEFAULT_CONTENT_TYPE.getText());
        res.setStatus(400);
        TEMPLATE_UTILS.processHMTLTemplate("invalidTimezone",
                new Context(req.getLocale()),
                res.getWriter());
        chain.doFilter(req, res);
    }

    private boolean validate(String timezone) {
        return (timezone.matches("UTC[-+]\\d") || timezone.matches("UTC[-+]\\d{2}"))
                && (Integer.parseInt(timezone.substring(3)) <= 12);
    }
}

