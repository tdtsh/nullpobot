package com.tdtsh.twitterbot.bot;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions.Builder;

@SuppressWarnings("serial")
public class CronTaskHandler extends HttpServlet {
    private static Logger logger = 
        Logger.getLogger(CronTaskHandler.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        
        Queue queue = QueueFactory.getDefaultQueue();
        
        // ボット定義をとりだし，それぞれの処理をタスクキューで実行
        for (BotDefinition bot: BotDefinition.getBots()) {
            if (!bot.hasAccessToken())   // OAuthの認証に失敗しているものは処理しない
                continue;
            //queue.add(url("/botHandler").param("botId", ""+ bot.getId()));
            queue.add(Builder.withUrl("/botHandler").param("botId", ""+ bot.getId()));
            logger.info("submitted a task for botId " + bot.getId());
        }
    }
}
