/**
 * 
 */
package org.topicquests.learner.lgparser;

import java.util.Map;

import org.nex.config.ConfigPullParser;
import org.topicquests.util.LoggingPlatform;

/**
 * @author park
 *
 */
public class LinkGrammarEnvironment {
	private LoggingPlatform log = LoggingPlatform.getLiveInstance();
	private Map<String,Object>properties;
	private LinkGrammarParser parser;

	/**
	 * 
	 */
	public LinkGrammarEnvironment() throws Exception {
		ConfigPullParser p = new ConfigPullParser("linkgrammar-props.xml");
		properties = p.getProperties();
		parser = new LinkGrammarParser(this);
	}

	public String getStringProperty(String key) {
		return (String)properties.get(key);
	}

	public LinkGrammarParser getParser() {
		return parser;
	}
	
	public void logDebug(String msg) {
		log.logDebug(msg);
	}
	
	public void logError(String msg, Exception e) {
		log.logError(msg,e);
	}

	public void shutDown() {
		parser.shutDown();
	}
}
