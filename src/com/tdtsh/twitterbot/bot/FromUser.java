package com.tdtsh.twitterbot.bot;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.tdtsh.twitterbot.util.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable ="true")
public class FromUser {

    private static Logger logger = Logger.getLogger(FromUser.class.getName());

    @PrimaryKey
    @Persistent private String userName;

	//@Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
    @Persistent private Date tweetTime;

    // コンストラクタ    
    public FromUser(){
    }
    
    public FromUser(String userName) {
        this();
        this.userName = userName;
        this.tweetTime = new Date();
    }

    public FromUser(String userName, Date tweetTime) {
        this();
        this.userName = userName;
        this.tweetTime = tweetTime;
    }

    /**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the tweetTime
	 */
	public Date getTweetTime() {
		return tweetTime;
	}

	/**
	 * @param tweetTime the tweetTime to set
	 */
	public void setTweetTime(Date tweetTime) {
		this.tweetTime = tweetTime;
	}

	// GET
    public static FromUser getUser(String userName) {
		FromUser user = null;
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            user = pm.getObjectById(FromUser.class, new String(userName));
            pm.detachCopy(user);
            //return user;
		} 
		catch (Exception e)
		{
			logger.info("Exception :"+e.getMessage());
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
		return user;
    }

    // DELETE
    public static void removeFromUser(FromUser user) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            FromUser tmpBot = pm.getObjectById(FromUser.class, user.getUserName());
            pm.deletePersistent(tmpBot);
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // リストを返却
    public static List<FromUser> getUsers() { 
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(FromUser.class);
            List<FromUser> bots = (List<FromUser>) query.execute();
            pm.detachCopyAll(bots);
            return bots;
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

	// 永続化
    public static void makePersistent(FromUser user) {
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            pm.makePersistent(user);
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

	private static TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

    /**
     * def日前をDateで返す.
     * @param def 何日前
     * @param date java.util.Dateオブジェクト
     */
    public static Date makeDateDiff( int def, java.util.Date date ) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime( date );
        // 日時を YYYY/MM/DD -def(マイナスdef日) 00:00:00 にする
        cal.add( Calendar.DATE, def );
        cal.set( cal.get( Calendar.YEAR )
            , cal.get( Calendar.MONTH )
            , cal.get( Calendar.DATE ), 0, 0, 0 );
        date = cal.getTime();
		logger.info("date = "+date);
        return date;
    }

}
