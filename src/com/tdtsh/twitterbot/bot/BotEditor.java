package com.tdtsh.twitterbot.bot;

import java.util.List;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

import com.tdtsh.twitterbot.util.*;

@SuppressWarnings("serial")
public class BotEditor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String botId = req.getParameter("botId");
        String word ="";
        String tweet0 = "";
        String tweet1 = "";
        String tweet2 = "";        

        // ボット定義を取りだし
        if (botId != null && !botId.isEmpty()) {
            BotDefinition bot = 
                BotDefinition.getBotDefinition(Long.parseLong(botId));
            if (bot == null) {
                ErrorPage.create(res, "botId is not valid", "/");
                return;
            }

            // 削除要求の場合には削除
            if (req.getParameter("delete") != null) {
                BotDefinition.removeBotDefinition(bot);
                res.sendRedirect("/");
                return;
            }
            word           = bot.getWord();
            List<String> tweets = bot.getTweets();
            if (tweets.size() > 0) tweet0 = tweets.get(0);    
            if (tweets.size() > 1) tweet1 = tweets.get(1);    
            if (tweets.size() > 2) tweet2 = tweets.get(2);    
        } else {
            botId = "";
        }

        // コンテクストを設定して表示
        Context context = new VelocityContext();
        context.put("botId", botId);
        context.put("word", word);        
        context.put("tweet0", tweet0);        
        context.put("tweet1", tweet1);        
        context.put("tweet2", tweet2);        
   
        res.setContentType("text/html");
        res.setCharacterEncoding("utf-8");

        Renderer.render("WEB-INF/botEditor.vm", context, res.getWriter());
    }
}
