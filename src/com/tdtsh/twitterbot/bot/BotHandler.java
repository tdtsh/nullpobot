package com.tdtsh.twitterbot.bot;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class BotHandler extends HttpServlet {
    private static Logger logger = Logger.getLogger(BotHandler.class.getName());

    // ConsumerKey をweb.xmlから読み出してセット
    @Override
    public void init() throws ServletException {
        super.init();
        ConsumerKey.init(this);
    }
    //static TwitterFactory factory = new TwitterFactory();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String botId = req.getParameter("botId");
        
        // 対象となるボットを取得
        BotDefinition bot = BotDefinition.getBotDefinition(Long.parseLong(botId));

		//参考:http://groups.google.com/group/twitter4j/browse_thread/thread/d18c179ba0d85351
		//英語だけど読んでね！
		ConfigurationBuilder confbuilder  = new ConfigurationBuilder(); 
		//confbuilder.setOAuthConsumerKey(ConsumerKey.key).setOAuthConsumerSecret(ConsumerKey.secret); 

		confbuilder.setOAuthConsumerKey(ConsumerKey.key)
			.setOAuthConsumerSecret(ConsumerKey.secret)
			.setOAuthAccessToken(ConsumerKey.accessToken)
			.setOAuthAccessTokenSecret(ConsumerKey.accessTokenSecret);
		
		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();        // Twitterオブジェクトをコンシューマキー、アクセストークンを用いて初期r
        //Twitter twitter = factory.getInstance(ConsumerKey.key, ConsumerKey.secret, bot.getAccessToken());
       
		
        try {

            // 指定された文字列を含むtweetを検索
            Query q = new Query(bot.getWord());
            q.setSinceId(bot.getSinceId());

            long lastId = bot.getSinceId();
            QueryResult result = twitter.search(q);

			Date now = new Date();
			FromUser user = null;

            for (Tweet tw: result.getTweets()) {

                if (tw.getFromUser().equals(twitter.getScreenName())) 
                    continue;  // 自分のtweet には反応しない

				user = FromUser.getUser(tw.getFromUser());
				if (user != null)
				{
					if (user.getTweetTime().before(FromUser.makeDateDiff(1, now)))
					{
						logger.info("user exists but too new :"+user.getTweetTime());
						continue;  // 1日1回に制限
					}
					else
					{// ユーザが存在するが、最終ReTweetが昨日以前
						logger.info("exists user :"+tw.getFromUser());
					}

				}
				else
				{// まだ一度もガッしてないユーザ
					logger.info("new user "+tw.getFromUser());
					user = new FromUser(tw.getFromUser());
				}

				user.setTweetTime(now);

                // QTでメッセージを作成
                String tweet = bot.pickTweet() + " QT @" + 
                               tw.getFromUser() + " " + tw.getText();

                if (tweet.length() > 140)
                    tweet = tweet.substring(0, 140);

                logger.info(tweet);

                // つぶやく
                twitter.updateStatus(tweet);
                lastId = Math.max(tw.getId(), lastId);

				//fromUsers.add(tw.getFromUser());
				FromUser.makePersistent(user);

            }
            // 反応した最後のIDを保存
            bot.setSinceId(lastId);
            BotDefinition.makePersistent(bot);

        } catch (TwitterException e) {
            logger.log(Level.SEVERE, "failed to twitter API call", e);
            throw new ServletException(e);
        }

    }
}
