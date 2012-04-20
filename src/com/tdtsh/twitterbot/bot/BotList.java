package com.tdtsh.twitterbot.bot;



import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.google.appengine.api.users.*;
import com.tdtsh.twitterbot.util.Renderer;

@SuppressWarnings("serial")
public class BotList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        User user = UserServiceFactory.getUserService().getCurrentUser();   
        
        List<BotDefinition> bots = BotDefinition.getBotDefinition(user);
        
        res.setContentType("text/html");
        res.setCharacterEncoding("utf-8");

        Context context = new VelocityContext();
        context.put("user", user);
        context.put("bots", bots);

        Renderer.render("WEB-INF/botList.vm", context, res.getWriter());
    }
}
