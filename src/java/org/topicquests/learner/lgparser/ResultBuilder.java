/**
 * 
 */
package org.topicquests.learner.lgparser;

import java.util.*;
import java.io.*;

import net.sf.jlinkgrammar.Exp;
import net.sf.jlinkgrammar.Link;
import net.sf.jlinkgrammar.Linkage;
import net.sf.jlinkgrammar.ParseOptions;
import net.sf.jlinkgrammar.Sentence;
import net.sf.jlinkgrammar.Sublinkage;
import net.sf.jlinkgrammar.Word;
import net.sf.jlinkgrammar.XNode;

import org.json.simple.JSONObject;
import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;
import org.topicquests.learner.lgparser.support.ParseResult;
import org.topicquests.learner.lgparser.support.ResultLinkage;
import org.topicquests.learner.lgparser.api.IAnalysisConstants;

/**
 * @author park
 *
 */
public class ResultBuilder {
	private LinkGrammarEnvironment environment;

	/**
	 * 
	 */
	public ResultBuilder(LinkGrammarEnvironment env) {
		environment = env;
	}

	/**
	 * <p>We are interested in breaking up a sentence into its
	 * <em>subject,predicate,object</em> components. A simple sentence
	 * such as <code>A gas caused harm</code> will break down that way.</p>
	 * <p>A complex sentence such as <code>many scientists believe that
	 * CO2 is a cause of climate change</code> will break into a nested
	 * triple such as <em>{subject,predicate,{subject,predicate,object}}<em>, and
	 * an even more complex sentence such as <code>many scientists believe that
	 * climate deniers believe that CO2 does not cause climate change</code>
	 * will break into an even more complex structure: <em>{subject,predicate,
	 * {subject,predicate,{subject,predicate,object}}}</em></p>
	 * <p> It is the task here to identify the predicates, what is to the left and
	 * right of each.</p>
	 * <p>Along the way, this reveals, where possible, word types, e.g. nouns, verbs,
	 * etc, and it reveals the tags that the parser placed on each.</p>
	 * <p>For sentences that can be parsed at all, return a complex object.</p>
	 * <p>For others, return error messages and <code>null</code></p>
	 * @param sent the sentence
	 * @param numLinkages number of linkages found in parsed sentence
	 * @param opts parse options used in this parse
	 * @return Map<String,Object>
	 */
	public IResult analyzeSentence(Sentence sent, int numLinkages, ParseOptions opts) {
		System.out.println("ANALYZE "+numLinkages);
    	IResult result = new ResultPojo();
    	Map<String,Object>resultMap = new HashMap<String,Object>();
    	result.setResultObject(resultMap);
    	Linkage lg;
    	//get the words in this sentence
     	Word [] x = sent.getWords();
    	List<String> xl = new ArrayList<String>();
    	int len = x.length;
    	//put them into a List
    	for (int i=0;i<len;i++)
    		if (x[i] != null)
    			xl.add(x[i].string);
    	resultMap.put(IAnalysisConstants.WORD_KEY, xl);
    	ResultLinkage rlg;
    	List<Linkage>linkages = new ArrayList<Linkage>();
    	//create the linkages and add them to a List
    	for (int i=0;i< numLinkages;i++) {
    		lg = new Linkage(i,sent,opts);
    	//	lg.linkage_compute_union();
    	//	lg.linkage_set_current_sublinkage(i);
    		linkages.add(lg);
    	}
    	len = linkages.size();
    	Link link;
    	Sublinkage [] subs;
    	Sublinkage subl;
    	int len2,len3;
    	String errorString ="";
    	Link [] links;
    	System.out.println("AAA "+len);
    	List<JSONObject>theAnalysis = new ArrayList<JSONObject>();
//    	JSONObject m=null;
    	try { // diagnostics only
//    		Writer out = startFile();
	    	for (int i=0;i<len;i++) {
	    		lg = linkages.get(i);
	    		if (lg != null) {
		    		subs = lg.sublinkage;
	    			len2 = subs.length;
	    	    	System.out.println("BBB "+len2);
	    			for (int j=0;j<len2;j++) {
	    				subl = subs[j];
	    				errorString = subl.violation;
	    				if (errorString != null)
	    					result.addErrorString(errorString);
	    				links = subl.link;
	    				if (links != null) {
	    					len3 = links.length;
	    			    	System.out.println("CCC "+len3);
	    					for (int k=0;k<len3;k++) {
	    						link = links[k];
	    						if (link != null)
	    							System.out.println("DDD "+link +" "+x[k]);
	    						if (link != null && x[k] != null) {
	    							//TODO find the place where the word's type, e.g. <word>.n, etc
	    							// is stored and bring that up 
	    							System.out.println("EEE "+x[k].string+" "+x[k].x.string);
	    							doMap(x[k], k,link.l,link.r,link.name,theAnalysis);
//	    							out.write(x[k].string+" "+link.l+" "+link.r+" "+link.name+"\n");
	    						}
	    					}
//	    					out.write("\n");
	    				}
	    			}
	    		}
	    	}
//    		out.write(theAnalysis.toString()+"\n");
//    		out.flush();
//    		out.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	System.out.println(theAnalysis);
    	//TODO
    	resultMap.put(IAnalysisConstants.ANALYSIS, theAnalysis);
    	List<String>verbLocations = new ArrayList<String>();
    	findVerbs(theAnalysis,verbLocations);
    	resultMap.put(IAnalysisConstants.VERBS, verbLocations);
		environment.logDebug("ResultBuilder\n"+resultMap.toString());
    	return result;
	}
	//////////////////////////////////////////////////////////////////////
	//{verbs=[2, 7], 
	// word=[LEFT-WALL, Mike, saw, the, girl, with, the, telescope, ., RIGHT-WALL], 
	// analysis=[{values=[[0, 8, Xp], [0, 8, Xp]], word=LEFT-WALL}, 
	//{values=[[0, 1, Wd], [0, 1, Wd]], word=Mike mike.n}, 
	//{values=[[1, 2, Ss], [1, 2, Ss]], word=saw.v saw.n}, 
	//{values=[[2, 5, MVp], [2, 4, Os]], word=the}, 
	//{values=[[2, 4, Os], [3, 4, Ds]], word=girl.n}, 
	//{values=[[3, 4, Ds], [4, 5, Mp]], word=with}, 
	//{values=[[5, 7, Js], [5, 7, Js]], word=the}, 
	//{values=[[6, 7, Ds], [6, 7, Ds]], word=telescope.v telescope.n}, 
	//{values=[[8, 9, RW], [8, 9, RW]], word=.}]}
    ////////////////////////////////////////////////////////////////////////
	//{verbs=[3, 4, 7, 9, 11, 13, 15, 22, 28], 
	//		 word=[LEFT-WALL, using, two, biomedical, corpora, ,, we, implement, and, evaluate, three, approaches, to, addressing, unknown, words, :, automatic, lexicon, expansion, ,, the, use, of, morphological, clues, ,, and, disambiguation, using, a, part-of-speech, tagger, ., RIGHT-WALL], 
	//		 analysis=[
	//		 {values=[[0, 16, Xx], [0, 33, Xp]], word=LEFT-WALL}, 
	//		 {values=[[0, 6, Wd], [0, 16, Xx]], word=using.g}, 
	//		 {values=[[1, 6, COp], [0, 6, Wd]], word=two}, 
	//		 {values=[[1, 5, Xc], [1, 6, COp]], word=biomedical[?].v biomedical[?].n biomedical[?].a}, 
	//		 {values=[[1, 2, Op], [1, 5, Xc]], word=corpora[?].v corpora[?].n corpora[?].a}, 
	//		 {values=[[2, 4, Ma], [1, 2, Op]], word=,}, 
	//		 {values=[[2, 3, Ma], [2, 4, Ma]], word=we}, 
	//		 {values=[[6, 7, Sp], [6, 9, Sp], [2, 3, Ma]], word=implement.v implement.n}, 
	//		 {values=[[7, 12, MVp], [9, 12, MVp], [6, 7, Sp], [6, 9, Sp], [7, 11, Op], [9, 11, Op], [7, 11, Opn], [9, 11, Opn]], word=and}, 
	//		 {values=[[7, 11, Op], [9, 11, Op], [7, 11, Opn], [9, 11, Opn], [7, 12, MVp], [9, 12, MVp], [10, 11, Dmc]], word=evaluate.v}, 
	//		 {values=[[10, 11, Dmc], [7, 11, Op], [9, 11, Op], [7, 11, Opn], [9, 11, Opn], [11, 12, Mp]], word=three}, 
	//		 {values=[[12, 13, Mgp], [10, 11, Dmc], [11, 12, Mp], [12, 15, Jp]], word=approaches.v approaches.n}, 
	//		 {values=[[13, 15, Op], [12, 13, Mgp], [13, 15, AN], [12, 15, Jp]], word=to}, 
	//		 {values=[[14, 15, A], [13, 15, Op], [13, 15, AN]], word=addressing.v addressing.g}, 
	//		 {values=[[16, 19, Jp], [14, 15, A]], word=unknown.a}, 
	//		 {values=[[17, 19, A], [17, 19, AN], [16, 19, Jp], [16, 22, Jp], [16, 28, Jp], [16, 19, Wd], [16, 22, Wd], [16, 28, Wd]], word=words.v words.n}, 
	//		 {values=[[18, 19, AN], [17, 19, AN], [21, 22, D*u], [28, 29, Mg], [17, 19, A], [17, 28, A], [28, 32, Ma], [28, 32, Sp]], word=:}, 
	//		 {values=[[19, 22, MXs], [19, 22, MXsa], [19, 28, MXsa], [19, 28, MXs], [19, 22, MXsp], [19, 28, MXsp], [19, 22, MXs^], [19, 28, MXs^], [18, 19, AN], [22, 23, Mp], [29, 32, Os], [28, 29, Mg], [29, 31, O], [28, 32, Sp], [28, 32, Ma]], word=automatic.n automatic.a}, 
	//		 {values=[[20, 22, Xd], [20, 28, Xd], [33, 34, RW], [23, 25, Jp], [30, 32, Dsu], [19, 22, MXs], [29, 32, Os], [30, 31, Ds], [29, 31, O], [19, 32, Sp], [28, 29, Mg]], word=lexicon.n}, 
	//		 {values=[[21, 22, D*u], [28, 33, Xc], [21, 28, D*u], [24, 25, A], [31, 32, Ah], [20, 22, Xd], [30, 32, Dsu], [31, 32, Ma], [31, 32, AN], [30, 31, Ds], [33, 34, RW], [29, 31, O]], word=expansion.n}, 
	//		 {values=[[22, 33, Xc], [22, 23, Mp], [28, 29, MVs], [28, 29, Mg], [22, 26, Xc], [28, 33, Xc], [33, 34, RW], [21, 22, D*u], [31, 32, Ah], [28, 32, Ma], [22, 32, Sp], [31, 32, Ma], [30, 31, Ds], [31, 32, AN]], word=,}, 
	//		 {values=[[22, 23, Mp], [23, 25, Jp], [29, 32, Os], [22, 29, Mg], [28, 29, Mg], [22, 23, M^], [22, 28, M^], [22, 26, Xc], [33, 34, RW], [29, 31, O], [22, 32, Ma], [28, 32, Ma], [22, 23, Mj], [22, 32, Bsj]], word=the}, 
	//		 {values=[[23, 25, Jp], [23, 28, Jp], [24, 25, A], [30, 32, Dsu], [22, 23, Mp], [29, 32, Os], [28, 29, MVs], [22, 23, M^], [22, 28, M^], [30, 31, Ds], [29, 31, O], [22, 29, Mg], [28, 29, Mg], [23, 31, Cs], [22, 23, Mj]], word=use.v use.n}, 
	//		 {values=[[24, 25, A], [24, 28, A], [22, 33, Xc], [31, 32, Ah], [23, 25, Jp], [23, 28, Jp], [28, 29, Mg], [30, 32, Dsu], [29, 32, Os], [31, 32, Ma], [31, 32, AN], [30, 31, Ds], [22, 23, Mp], [28, 32, Ma], [28, 29, MVs], [29, 31, O], [22, 23, M^], [22, 28, M^], [23, 29, Jp], [23, 31, Cs]], word=of}, 
	//		 {values=[[25, 29, Mg], [28, 29, Mg], [33, 34, RW], [24, 25, A], [24, 28, A], [29, 32, Os], [31, 32, Ah], [30, 32, Dsu], [25, 32, Ma], [28, 32, Ma], [23, 25, Jp], [23, 28, Jp], [29, 31, O], [31, 32, Ma], [31, 32, AN], [30, 31, Ds], [28, 29, A], [31, 32, Sp]], word=morphological.a}, 
	//		 {values=[[29, 32, Os], [30, 32, Dsu], [33, 34, RW], [31, 32, Ah], [29, 31, O], [25, 29, Mg], [28, 29, Mg], [24, 25, A], [24, 28, A], [30, 31, Ds], [31, 32, Ma], [31, 32, AN], [19, 32, Sp], [31, 32, Sp], [23, 25, Jp], [23, 29, Jp]], word=clues.n}, 
	//		 {values=[[30, 32, Dsu], [31, 32, Ah], [33, 34, RW], [30, 31, Ds], [29, 31, O], [31, 32, Ma], [31, 32, AN], [31, 32, Sp], [24, 25, A], [28, 29, A]], word=,}, 
	//		 {values=[[31, 32, Ah], [33, 34, RW], [31, 32, Ma], [31, 32, AN], [30, 31, Ds]], word=and}, 
	//		 {values=[[33, 34, RW]], word=disambiguation[?].v disambiguation[?].n disambiguation[?].a}]}
	//////////////////////////////////////////////////////////////////////////////////////
	//{verbs=[3, 4, 7, 9, 11, 13, 15, 22, 28], 
	// word=[LEFT-WALL, using, two, biomedical, corpora, ,, we, implement, and, evaluate, 3, approaches, to, addressing, unknown, words, :, automatic, lexicon, expansion, ,, the, use, of, morphological, clues, ,, and, disambiguation, using, a, part-of-speech, tagger, ., RIGHT-WALL], 
	//		 analysis=[
	//		 {values=[[0, 16, Xx], [0, 33, Xp]], word=LEFT-WALL}, 
	//		 {values=[[0, 6, Wd], [0, 16, Xx]], word=using.g}, 
	//		 {values=[[1, 6, COp], [0, 6, Wd]], word=two}, 
	//		 {values=[[1, 5, Xc], [1, 6, COp]], word=biomedical[?].v biomedical[?].n biomedical[?].a}, 
	//		 {values=[[1, 2, Op], [1, 5, Xc]], word=corpora[?].v corpora[?].n corpora[?].a}, 
	//		 {values=[[2, 4, Ma], [1, 2, Op]], word=,}, {values=[[2, 3, Ma], [2, 4, Ma]], word=we}, 
	//		 {values=[[6, 7, Sp], [6, 9, Sp], [2, 3, Ma]], word=implement.v implement.n}, 
	//		 {values=[[7, 12, MVp], [9, 12, MVp], [6, 7, Sp], [6, 9, Sp], [7, 11, Op], [9, 11, Op], [7, 11, Opn], [9, 11, Opn]], word=and}, 
	//		 {values=[[7, 11, Op], [9, 11, Op], [7, 11, Opn], [9, 11, Opn], [7, 12, MVp], [9, 12, MVp], [10, 11, Dmcn]], word=evaluate.v}, 
	//		 {values=[[10, 11, Dmcn], [7, 11, Op], [9, 11, Op], [7, 11, Opn], [9, 11, Opn], [11, 12, Mp]], word=3}, 
	//		 {values=[[12, 13, Mgp], [10, 11, Dmcn], [11, 12, Mp], [12, 15, Jp]], word=approaches.v approaches.n}, 
	//		 {values=[[13, 15, Op], [12, 13, Mgp], [13, 15, AN], [12, 15, Jp]], word=to}, 
	//		 {values=[[14, 15, A], [13, 15, Op], [13, 15, AN]], word=addressing.v addressing.g}, 
	//		 {values=[[16, 19, Jp], [14, 15, A]], word=unknown.a}, 
	//		 {values=[[17, 19, A], [17, 19, AN], [16, 19, Jp], [16, 22, Jp], [16, 28, Jp], [16, 19, Wd], [16, 22, Wd], [16, 28, Wd]], word=words.v words.n}, 
	//		 {values=[[18, 19, AN], [17, 19, AN], [21, 22, D*u], [28, 29, Mg], [17, 19, A], [17, 28, A], [28, 32, Ma], [28, 32, Sp]], word=:}, 
	//		 {values=[[19, 22, MXs], [19, 22, MXsa], [19, 28, MXsa], [19, 28, MXs], [19, 22, MXsp], [19, 28, MXsp], [19, 22, MXs^], [19, 28, MXs^], [18, 19, AN], [22, 23, Mp], [29, 32, Os], [28, 29, Mg], [29, 31, O], [28, 32, Sp], [28, 32, Ma]], word=automatic.n automatic.a}, 
	//		 {values=[[20, 22, Xd], [20, 28, Xd], [33, 34, RW], [23, 25, Jp], [30, 32, Dsu], [19, 22, MXs], [29, 32, Os], [30, 31, Ds], [29, 31, O], [19, 32, Sp], [28, 29, Mg]], word=lexicon.n}, 
	//		 {values=[[21, 22, D*u], [28, 33, Xc], [21, 28, D*u], [24, 25, A], [31, 32, Ah], [20, 22, Xd], [30, 32, Dsu], [31, 32, Ma], [31, 32, AN], [30, 31, Ds], [33, 34, RW], [29, 31, O]], word=expansion.n}, 
	//		 {values=[[22, 33, Xc], [22, 23, Mp], [28, 29, MVs], [28, 29, Mg], [22, 26, Xc], [28, 33, Xc], [33, 34, RW], [21, 22, D*u], [31, 32, Ah], [28, 32, Ma], [22, 32, Sp], [31, 32, Ma], [30, 31, Ds], [31, 32, AN]], word=,}, 
	//		 {values=[[22, 23, Mp], [23, 25, Jp], [29, 32, Os], [22, 29, Mg], [28, 29, Mg], [22, 23, M^], [22, 28, M^], [22, 26, Xc], [33, 34, RW], [29, 31, O], [22, 32, Ma], [28, 32, Ma], [22, 23, Mj], [22, 32, Bsj]], word=the}, 
	//		 {values=[[23, 25, Jp], [23, 28, Jp], [24, 25, A], [30, 32, Dsu], [22, 23, Mp], [29, 32, Os], [28, 29, MVs], [22, 23, M^], [22, 28, M^], [30, 31, Ds], [29, 31, O], [22, 29, Mg], [28, 29, Mg], [23, 31, Cs], [22, 23, Mj]], word=use.v use.n}, 
	//		 {values=[[24, 25, A], [24, 28, A], [22, 33, Xc], [31, 32, Ah], [23, 25, Jp], [23, 28, Jp], [28, 29, Mg], [30, 32, Dsu], [29, 32, Os], [31, 32, Ma], [31, 32, AN], [30, 31, Ds], [22, 23, Mp], [28, 32, Ma], [28, 29, MVs], [29, 31, O], [22, 23, M^], [22, 28, M^], [23, 29, Jp], [23, 31, Cs]], word=of}, 
	//		 {values=[[25, 29, Mg], [28, 29, Mg], [33, 34, RW], [24, 25, A], [24, 28, A], [29, 32, Os], [31, 32, Ah], [30, 32, Dsu], [25, 32, Ma], [28, 32, Ma], [23, 25, Jp], [23, 28, Jp], [29, 31, O], [31, 32, Ma], [31, 32, AN], [30, 31, Ds], [28, 29, A], [31, 32, Sp]], word=morphological.a}, 
	//		 {values=[[29, 32, Os], [30, 32, Dsu], [33, 34, RW], [31, 32, Ah], [29, 31, O], [25, 29, Mg], [28, 29, Mg], [24, 25, A], [24, 28, A], [30, 31, Ds], [31, 32, Ma], [31, 32, AN], [19, 32, Sp], [31, 32, Sp], [23, 25, Jp], [23, 29, Jp]], word=clues.n}, 
	//		 {values=[[30, 32, Dsu], [31, 32, Ah], [33, 34, RW], [30, 31, Ds], [29, 31, O], [31, 32, Ma], [31, 32, AN], [31, 32, Sp], [24, 25, A], [28, 29, A]], word=,}, 
	//		 {values=[[31, 32, Ah], [33, 34, RW], [31, 32, Ma], [31, 32, AN], [30, 31, Ds]], word=and}, 
	//		 {values=[[33, 34, RW]], word=disambiguation[?].v disambiguation[?].n disambiguation[?].a}]}

	
	
	
	
	////////////////////////////////////////////////////////////////////////////
	//[{values=[[0, 8, Xp], [0, 8, Xp]], word=LEFT-WALL}, 
	// {values=[[0, 1, Wd], [0, 1, Wd]], word=Mike}, 
	// {values=[[1, 2, Ss], [1, 2, Ss]], word=saw}, 
	// {values=[[2, 5, MVp], [2, 4, Os]], word=the}, 
	// {values=[[2, 4, Os], [3, 4, Ds]], word=girl}, 
	// {values=[[3, 4, Ds], [4, 5, Mp]], word=with}, 
	// {values=[[5, 7, Js], [5, 7, Js]], word=the}, 
	// {values=[[6, 7, Ds], [6, 7, Ds]], word=telescope}, 
	// {values=[[8, 9, RW], [8, 9, RW]], word=.}]

	
	/**
	 * This is fed purely sequentially along the words, the first word being LEFT_WALL, etc
	 * @param theWord
	 * @param whereTheWordIs
	 * @param left
	 * @param right
	 * @param tag
	 * @param theAnalysis
	 * 
	 */
	void doMap( Word wd, int whereTheWordIs, int left, int right, String tag,
			List<JSONObject>theAnalysis) {
		String theWord = wd.string;
		XNode xn = wd.x;
		
		StringBuilder buf = new StringBuilder();
		if (xn != null) {
			buf.append(xn.string);
			int count = 0;
			while ((xn=xn.next) != null) {
				buf.append(" "+xn.string);
				if (++count > 4)
					break;
			}
			
		} else 
			buf.append(theWord);
		int len = theAnalysis.size();
		JSONObject result = null;
		if (whereTheWordIs >= len || len == 0) {
			result = new JSONObject();
			result.put(IAnalysisConstants.WORD_KEY,buf.toString());
			theAnalysis.add(result);
		} else {
			result = theAnalysis.get(whereTheWordIs);
		}
		List<List<String>>values = (List<List<String>>)result.get(IAnalysisConstants.VALUES);
		if (values == null) {
			values = new ArrayList<List<String>>();
			result.put(IAnalysisConstants.VALUES, values);
		}
		List<String>theValues = new ArrayList<String>();
		//Note the sequence: left, right, tag
		theValues.add(Integer.toString(left));
		theValues.add(Integer.toString(right));
		theValues.add(tag);
		if (!values.contains(theValues))
			values.add(theValues);

	}
	/////////////////////////////////////////////////
	//[{values=[[0, 16, RW]], word=LEFT-WALL}, 
	// {values=[[0, 2, Wd]], word=many}, 
	// {values=[[1, 2, Dmc]], word=scientists.n}, 
	// {values=[[2, 3, Sp]], word=believe.v believe.q}, 
	// {values=[[3, 4, TH]], word=that.r that.p that.d that.c}, 
	// {values=[[4, 7, Cet]], word=many}, 
	// {values=[[5, 7, Dmc]], word=climate.n}, 
	// {values=[[6, 7, AN]], word=deniers[!] deniers[!]}, 
	// {values=[[7, 8, Sp]], word=believe.v believe.q}, 
	// {values=[[8, 9, TH]], word=that.r that.p that.d that.c}, 
	// {values=[[9, 10, Cet]], word=CO2}, 
	// {values=[[10, 11, Ss]], word=does.v}, 
	// {values=[[11, 13, I*d]], word=not}, 
	// {values=[[11, 12, N]], word=cause.v cause.n}, 
	// {values=[[13, 15, Os]], word=climate.n}, 
	// {values=[[14, 15, AN]], word=change.v change.n}]
	/////
	
	/**
	 * Fill <code>goal</code> with locations of any verbs found in <code>them</code>
	 * @param them
	 * @param goal
	 */
	void findVerbs(List<JSONObject>them, List<String>goal) {
		int len = them.size();
		JSONObject m;
		String word;
		for (int i=0;i<len;i++) {
			m = them.get(i);
			word = (String)m.get(IAnalysisConstants.WORD_KEY);
			if (isVerb(word)) 
				goal.add(Integer.toString(i));
		}
	}
	
	/**
	 * Note: there are ambiguities in words which are spelled
	 * with a ".v" somewhere, e.g. some.very  -- which 
	 * would be really rare
	 * @param word
	 * @return
	 */
	boolean isVerb(String word) {
		return word.indexOf(".v") > -1;
	}
	/*
	Writer startFile() throws Exception {
		File f = new File("Test_"+System.currentTimeMillis()+".txt");
		OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
		return new PrintWriter(os);
	}*/
}
