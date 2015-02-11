/**
 * 
 */
package org.topicquests.learner.lgparser;

import java.util.List;
import java.util.Map;

import net.sf.jlinkgrammar.Dictionary;
import net.sf.jlinkgrammar.GlobalBean;
import net.sf.jlinkgrammar.Sentence;
import net.sf.jlinkgrammar.ParseOptions;
import net.sf.jlinkgrammar.api.IParserConstants;

import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.learner.lgparser.support.ParseResult;

/**
 * @author park
 *
 */
public class LinkGrammarParser {
	private LinkGrammarEnvironment environment;
	private Dictionary dict;

	/**
	 * 
	 */
	public LinkGrammarParser(LinkGrammarEnvironment env) throws Exception {
		environment = env;
		String basePath = environment.getStringProperty("BasePath");
		String dictPath = environment.getStringProperty("DictionaryPath");
		String knowlPath = environment.getStringProperty("KnowledgePath");
		String conPath = environment.getStringProperty("ConstituentKnowledgePath");
		String affPath = environment.getStringProperty("AffixPath");
		ParseOptions opts = new ParseOptions();
        //TODO make these configurable
        opts.parse_options_set_max_sentence_length(200);
        opts.parse_options_set_linkage_limit(1000);
        opts.parse_options_set_short_length(10);
        opts.parse_options_set_disjunct_cost(2);
        opts.parse_options_set_min_null_count(0);
        opts.parse_options_set_max_null_count(0);
        opts.parse_options_reset_resources();
        dict = new Dictionary(opts, dictPath, knowlPath, 
                conPath, affPath, basePath,environment);
	}

	/**
	 * 
	 * @param sentence
	 * @return Map<String,Object>
	 */
	public IResult parseSentence(String sentence) {
		System.out.println("LinkGrammarParser.parseSentence- "+sentence);
		IResult result = new ResultPojo();
		//We make our own opts for thread safety
		ParseOptions opts = new ParseOptions();
		opts.parse_options_set_max_sentence_length(200);
        opts.parse_options_set_linkage_limit(1000);
        opts.parse_options_set_short_length(10);
        opts.parse_options_set_disjunct_cost(2);
        opts.parse_options_set_min_null_count(0);
        opts.parse_options_set_max_null_count(0);
        opts.parse_options_reset_resources();

		Sentence s =  new Sentence(environment, sentence, dict, opts);
		int numLinks = s.sentence_parse(opts);
		environment.logDebug("LinkGrammarParser.parseSentence-1 "+numLinks+" "+s);
		System.out.println("LinkGrammarParser.parseSentence-1 "+numLinks+" "+s);
		ResultBuilder rb = new ResultBuilder(environment);
		IResult r = rb.analyzeSentence(s,numLinks,opts);
		result.setResultObject(r.getResultObject());
		if (r.hasError())
			result.addErrorString(r.getErrorString());
		return result;
	}

	/**
	 * Simply prints out the parse results, for diagnostic use
	 * @param sentence
	 * @return
	 */
	public IResult testParseSentence(String sentence) {
		IResult result = new ResultPojo();
		//We make our own opts for thread safety
		ParseOptions opts = new ParseOptions();
		opts.parse_options_set_max_sentence_length(200);
        opts.parse_options_set_linkage_limit(1000);
        opts.parse_options_set_short_length(10);
        opts.parse_options_set_disjunct_cost(2);
        opts.parse_options_set_min_null_count(0);
        opts.parse_options_set_max_null_count(0);
        opts.parse_options_reset_resources();
        //create the sentence
		Sentence s =  new Sentence(environment, sentence, dict, opts);
		//do the parsing
		int num_linkages = s.sentence_parse(opts);
        System.out.println("PARSED "+num_linkages);
        if (num_linkages == 0 && !opts.parse_options_get_batch_mode()) {
            if (opts.verbosity > 0)
                opts.out.println("No complete linkages found.");
            if (opts.parse_options_get_allow_null()) {
                opts.parse_options_set_min_null_count(1);
                opts.parse_options_set_max_null_count(s.sentence_length());
                num_linkages = s.sentence_parse(opts);
            }
        }
        try {
    //    opts.print_total_time();
	        if (opts.parse_options_get_batch_mode()) {
	            s.getBean().batch_process_some_linkages(s.getLabel(), s, opts);
	        } else {
	            s.getBean().process_some_linkages(s, opts);
	        }
        } catch (Exception e) {
        	environment.logError(e.getMessage(), e);
        	e.printStackTrace();
        }

		//TODO
		//WHAT NEXT?
		return result;
	}
	
	public void shutDown() {
	}

}
