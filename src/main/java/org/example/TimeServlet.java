package org.example;

import org.thymeleaf.context.Context;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import static org.example.TimeServletConstants.*;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Integer COOKIE_LIFE_TIME = 600;
    private ServletTemplateUtils TEMPLATE_UTILS;

    @Override
    public void init() throws ServletException {
        TEMPLATE_UTILS = new ServletTemplateUtils(this.getServletContext());
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parameter = req.getParameter(TimeServletConstants.PARAMETER_NAME.getText());
        resp.setContentType(DEFAULT_CONTENT_TYPE.getText());
        String UTCTimeZone;
        if (parameter == null) {
            UTCTimeZone = tryGetLastTimezoneFromCookie(req);
        } else {
            UTCTimeZone = parameter.replace(' ', '+');
            Cookie cookie = new Cookie(COOKIE_LAST_TIMEZONE_NAME.getText(), UTCTimeZone);
            cookie.setMaxAge(COOKIE_LIFE_TIME);
            resp.addCookie(cookie);
        }
        String dateTime = getNowWithUTC_TZ(UTCTimeZone);
        resp.setContentType(DEFAULT_CONTENT_TYPE.getText());
        TEMPLATE_UTILS.processHMTLTemplate("time",
                new Context(req.getLocale(), Map.of("time", dateTime, "utc", UTCTimeZone)),
                resp.getWriter());
    }
    //    returns lastTimezone from cookie or returns default UTC timezone
    private String tryGetLastTimezoneFromCookie(HttpServletRequest req) {
        try {
            Optional<Cookie> optionalCookie = Arrays.stream(req.getCookies())
                    .filter(cookie -> COOKIE_LAST_TIMEZONE_NAME.getText().equals(cookie.getName()))
                    .findFirst();
            //if cookie was return cookie value
            if (optionalCookie.isPresent()) {
                return optionalCookie.get().getValue();
            }
            //if (exception was thrown). This was first attempt to get Time
        } catch (Exception ignored) {
            //DO NOTHING
        }
        //default return
        return "UTC";
    }

    //returns formatted now with timeZone
    private String getNowWithUTC_TZ(String timeZoneSt) {
        if (timeZoneSt == null) {
            timeZoneSt = "GMT";
        }
        //GMT == UTC
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneSt.replace("UTC", "GMT"));
        // Format the date and time as a string
        return Instant.now()
                .atZone(timeZone.toZoneId())
                .format(formatter);
    }
}

