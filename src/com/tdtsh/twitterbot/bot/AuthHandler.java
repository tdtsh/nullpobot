package com.tdtsh.twitterbot.bot;

import java.io.IOException;
import java.util.logging.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class AuthHandler extends HttpServlet {
    @Override
    public void init() throws ServletException {
        // コンシューマキーを読み込み
        super.init();
        ConsumerKey.init(this);
    }
    static TwitterFactory factory = new TwitterFactory();
    static Logger logger = Logger.getLogger(AuthHandler.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        // セッションから認証中のボットIDを取得
        HttpSession session = req.getSession();
        String idString = (String) session.getAttribute("requestingBotId");
        if (idString == null) {
            logger.warning("cannot get requestingBotId");
            throw new ServletException("cannot get requestingBotId");
        }
        
        BotDefinition bot = BotDefinition.getBotDefinition(Long.parseLong(idString));
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(ConsumerKey.key, ConsumerKey.secret);    
        try {
            // アクセストークンを取得、ボット定義に反映
            AccessToken accessToken = twitter.getOAuthAccessToken(bot.getRequestToken());
            bot.setAccessToken(accessToken);
            bot.setTwitterAccount(accessToken.getScreenName());
            BotDefinition.makePersistent(bot);            
        } catch (TwitterException e) {
            logger.log(Level.SEVERE, "failed to get AccessToken", e);
            throw new ServletException("failed to get AccessToken");
        }    
        resp.sendRedirect("/botList");
    }
}
