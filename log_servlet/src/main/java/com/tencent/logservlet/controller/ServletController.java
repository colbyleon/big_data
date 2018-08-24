package com.tencent.logservlet.controller;



import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

@Controller
public class ServletController {

    private static Logger logger = Logger.getLogger("console.log");

    /**
     * http://localhost/LogDemo/servlet/LogServlet
     */
    @RequestMapping("/LogDemo/servlet/LogServlet")
    @ResponseBody
    public void ReceLog(HttpServletRequest request) throws UnsupportedEncodingException {
        String queryString = URLDecoder.decode(request.getQueryString(), "utf-8");
        StringBuilder sb = new StringBuilder();
        String[] attrs = queryString.split("&");
        for (String attr : attrs) {
            String[] kv = attr.split("=");
            sb.append("|").append(kv.length == 2 ? URLDecoder.decode(kv[1], "utf-8") : "");
        }
        String ip = request.getRemoteAddr();
        sb.append("|").append(ip);

        logger.info(sb.substring(1));
    }

    @ResponseBody
    @RequestMapping("/auok")
    public String test(){
        return "imok";
    }

}
