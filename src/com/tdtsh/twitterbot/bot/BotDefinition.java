package com.tdtsh.twitterbot.bot;

import java.util.*;

import javax.jdo.Query;
import javax.jdo.*;
import javax.jdo.annotations.*;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.google.appengine.api.users.User;
import com.tdtsh.twitterbot.util.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable ="true")
public class BotDefinition {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent private User owner;
    @Persistent private String word;
    @Persistent private List<String> tweets;
    @Persistent private long sinceId;

    @Persistent private String requestToken;
    @Persistent private String requestTokenSecret;
    @Persistent private String accessToken;
    @Persistent private String accessTokenSecret;
    @Persistent private String twitterAccount;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public List<String> getTweets() {
        return tweets;
    }
    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }
    public long getSinceId() {
        return sinceId;
    }
    public void setSinceId(long sinceId) {
        this.sinceId = sinceId;
    }
    public RequestToken getRequestToken() {
        return new RequestToken(this.requestToken, this.requestTokenSecret);
    }
    public void setRequestToken(RequestToken token) {
        this.requestToken = token.getToken();
        this.requestTokenSecret = token.getTokenSecret();
    }
    public AccessToken getAccessToken() {
        return new AccessToken(accessToken, accessTokenSecret);
    }
    public void setAccessToken(AccessToken token) {
        this.accessToken = token.getToken();
        this.accessTokenSecret = token.getTokenSecret();
    }
    public boolean hasAccessToken() {
        return accessToken != null;
    }
    public String getTwitterAccount() {
        return twitterAccount;
    }
    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }
    
    // コンストラクタ    
    public BotDefinition(){
        this.tweets = new ArrayList<String>();
        this.sinceId = 0;
    }
    
    public BotDefinition(User owner, String word) {
        this();
        this.owner = owner;
        this.word = word;
    }

    // ３つの文字列のどれかを選択
    public String pickTweet() {
        int i = (int)(Math.random() * tweets.size());
        return tweets.get(i);
    }

    // ユーザ情報からボットの定義を取りだし
    public static List<BotDefinition> getBotDefinition(User user) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(BotDefinition.class);
        
            query.setFilter("owner == user");
            query.declareParameters(User.class.getName() + " user");
            List<BotDefinition> bots = (List<BotDefinition>) query.execute(user);    
            pm.detachCopyAll(bots);
            return bots;
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // ボットのIdでボットの定義を取り出し
    public static BotDefinition getBotDefinition(long botId) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            BotDefinition bot = pm.getObjectById(BotDefinition.class, new Long(botId));
            pm.detachCopy(bot);
            return bot;
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // ボットの定義を更新 
    public static BotDefinition update(String botId, User user, 
            String word, String tweet0, String tweet1, String tweet2) {
        BotDefinition bot = null;
        if (botId.trim().isEmpty()) {
            bot = new BotDefinition(user, word);
        } else {
            bot = getBotDefinition(Long.parseLong(botId));
            bot.word = word;
        }
        bot.tweets.clear();
        if (tweet0 != null && !tweet0.trim().isEmpty()) bot.tweets.add(tweet0);
        if (tweet1 != null && !tweet1.trim().isEmpty()) bot.tweets.add(tweet1);
        if (tweet2 != null && !tweet2.trim().isEmpty()) bot.tweets.add(tweet2);
        
        makePersistent(bot);
        return bot;
    }

    // ボット定義を削除
    public static void removeBotDefinition(BotDefinition bot) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            BotDefinition tmpBot = pm.getObjectById(BotDefinition.class, bot.getId());
            pm.deletePersistent(tmpBot);
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // ボット定義のリストを返却
    public static List<BotDefinition> getBots() { 
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(BotDefinition.class);
            List<BotDefinition> bots = (List<BotDefinition>) query.execute();
            pm.detachCopyAll(bots);
            return bots;
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    // ボット定義を永続化
    public static void makePersistent(BotDefinition bot) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            pm.makePersistent(bot);
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
}
