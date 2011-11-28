package com.tdtsh.twitterbot.util;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.log.JdkLogChute;

public class Renderer {
	static Logger logger = Logger.getAnonymousLogger(); 
	private static boolean initialized = false;
	private static void initializeVelocity() throws Exception {
	    Velocity.setProperty(
			Velocity.RUNTIME_LOG_LOGSYSTEM, new JdkLogChute());
	    Velocity.setProperty(
	            Velocity.INPUT_ENCODING, "UTF-8");              
	    Velocity.setProperty(
	            Velocity.OUTPUT_ENCODING, "UTF-8");              
		Velocity.init();
		initialized = true;
	}

	private static DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(); 
	static {
		dateTimeFormat.setTimeZone(TimeZone.getTimeZone("JST"));
	}
	
	public static void render(String filename, Context context, Writer writer) 
	throws IOException {
		try {
			synchronized (logger) { 
				if (!initialized)  
					initializeVelocity();
			}
			context.put("_datetimeFormat", dateTimeFormat);
			Template template = Velocity.getTemplate(filename); 
			template.merge(context, writer);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
}
