package com.tdtsh.twitterbot.bot;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import com.google.appengine.api.users.*;
import com.tdtsh.twitterbot.util.ErrorPage;

/*

// to create app twitter following url
http://twitter.com/oauth_clients/new

// enter form
Application Name:nullpobot
Description:ぬるぽと言われるとガッと言うだけのbotです
Application Website: http://nullpotbot.appspot.com/
Organization:	
Website:
Application Type:○Client ●Browser
Callback URL:http://nullpotbot.appspot.com/authenticated
Use Twitter for login:	 □Yes, use Twitter for login 
Does your application intend to use Twitter for authentication?

// sample apps url
	https://twitter.com/oauth/authorize?oauth_token=1VGgWIrPoBU9QGnCinDBrzIIb3w1Dm5ceAlyxG9E
	http://bot-maker.appspot.com/authenticated?oauth_token=1VGgWIrPoBU9QGnCinDBrzIIb3w1Dm5ceAlyxG9E&oauth_verifier=Z5NnUUfkhjqBcDkngiG8ZH23lltAPN1IrfJ1WCADPQ
	http://nullpotbot.appspot.com/authenticated


// login dev twitter
https://dev.twitter.com/apps/

// re make
https://dev.twitter.com/apps/new

Name:nullpobot
Description:ぬるぽと言われるとガッと言うだけのbotです
Website: http://nullpotbot.appspot.com/
Callback URL:http://nullpotbot.appspot.com/authenticated

//make key!
Create My Access Token

//key get!
Consumer key	5FJpBX0cfPDWLwxmacow
Consumer secret	2GNOTrI4L8dW4B09MUt5XOog8lsiNqcsfDIrF2q0w

//API is This?
https://api.twitter.com/1/

//Twitter4J update
http://twitter4j.org/ja/

// to many errors
AccessTokenを型に解決できません
RequestTokenを型に解決できません
http://twitter4j.org/ja/versions.html#migration21x-22x



 */

public class BotEditorHandler extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        ConsumerKey.init(this);
    }
    static TwitterFactory factory = new TwitterFactory();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        User user = UserServiceFactory.getUserService().getCurrentUser();
        
        String botId          = req.getParameter("botId");
        String word           = req.getParameter("word");
        String tweet0         = req.getParameter("tweet0");
        String tweet1         = req.getParameter("tweet1");
        String tweet2         = req.getParameter("tweet2");
        
        if (word == null || word.isEmpty() ||
            tweet0 == null || tweet0.isEmpty()){
            ErrorPage.create(res, "情報が足りません", "/");
            return;
        }
        BotDefinition bot =
            BotDefinition.update(botId, user, word, tweet0, tweet1, tweet2); 
        if (! bot.hasAccessToken()) {
            // リクエストトークンを取得
            Twitter twitter = factory.getInstance();
            twitter.setOAuthConsumer(ConsumerKey.key, ConsumerKey.secret);
            try {
                RequestToken requestToken = twitter.getOAuthRequestToken();
                bot.setRequestToken(requestToken);
                BotDefinition.makePersistent(bot);
                long id = bot.getId();
                HttpSession session = req.getSession();
                session.setAttribute("requestingBotId", "" + id);
                res.sendRedirect(requestToken.getAuthorizationURL());
            } catch (TwitterException e) {
                throw new ServletException(e);
            }
        } else {  // ボットの内容を更新 アクセスキーはそのまま
            res.sendRedirect("/");
        }        
    }    
}
