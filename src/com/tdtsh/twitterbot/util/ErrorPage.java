package com.tdtsh.twitterbot.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ErrorPage {

    public static void create(HttpServletResponse res, String message,
            String redirectURL) throws IOException {
        res.setContentType("text/html");
        res.setCharacterEncoding("utf-8");        
        PrintWriter pw = res.getWriter();
        pw.println("<html><body>");    
        pw.println("<h2>"+ message + "</h2>");
        pw.println("<a href=\"" + redirectURL +"\">Go back </a>");
        pw.println("</body></html>");    
    }
    
}
