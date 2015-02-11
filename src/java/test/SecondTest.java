/**
 * 
 */
package test;

import org.topicquests.common.api.IResult;
import org.topicquests.learner.lgparser.LinkGrammarEnvironment;
import org.topicquests.learner.lgparser.LinkGrammarParser;

/**
 * @author Park
 *
 */
public class SecondTest {
	private LinkGrammarEnvironment environment;
	private LinkGrammarParser parser;
	private String testSentence = "Mike saw the girl with the telescope.";
	private String test11= "Using two biomedical corpora, "+
			"we implement and evaluate three approaches to addressing unknown words: automatic lexicon "+
			" expansion, the use of morphological clues, and disambiguation using a part-of-speech tagger.";
	private String test4 = "What are the causes of climate change";
	private String test6 = "Many scientists believe that CO2 is the cause of harm, mayhem, and general malaise";
	private String test7 = "Mary said that X is always true, but Sue says that X is never true";
	private String test8 = "Mary said that X is always true; Sue says that X is never true";
	private String test9 = "How many times can you spell a cat without so much as crossing your fingers, while you are taking a hike?";
	private String test3 = "Lewy Dementia causes memory loss";
	private String test2 = "Many scientists believe that climate change is caused by CO2";
	private String test2a = "Many scientists believe climate change is caused by CO2";
	private String test2b = "Many scientists believe that many climate deniers believe that CO2 does not cause climate change";
	private String test11a= "Using two biomedical corpora, "+
			"we implement and evaluate 3 approaches to addressing unknown words: automatic lexicon "+
			" expansion, the use of morphological clues, and disambiguation using a part-of-speech tagger.";
	private String an1 = "Sue gave it to Bill";
	private String an2 = "He wants them to spend the money";
	private String bigOne = "The physicochemical similarity of the target compounds with respect to standard drugs clozapine, ketanserin and risperidone was calculated by using software programs.";
	private String bb = "AG-221 specifically inhibits IDH2 in the mitochondria, which inhibits the formation of 2-hydroxyglutarate (2HG)";
	private String cc = "carbon dioxide in the upper atmosphere is a cause of climate change"; //fails on CO2 in the upper...
	private String dd = "CO2 is a greenhouse gas";
	private String ee = "CO2 is a cause of climate change";
	private String ff = "Upper atmospheric CO2 is a cause of climate change";
	private String gg = "Some CO2 in the upper atmosphere is a cause of climate change";
	private String ii = "CO2 is a greenhouse gas and is a cause of climate change";
	private String jj = "CO2 is a greenhouse gas which is a cause of climate change";
	private String ll = "CO2, when located in the upper atmosphere, is a greenhouse gas which is a cause of climate change";
	private String mm = "CO2, located in the upper atmosphere, is a cause of climate change";

	/**
	 * 
	 */
	public SecondTest() {
		try {
			environment = new LinkGrammarEnvironment();
			parser = environment.getParser();
			runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void runTest() {
		IResult r = parser.parseSentence(mm);
	}
/*	 Got partial results as reported at https://groups.google.com/forum/#!topic/link-grammar/0ky8xVtLvYI
ANALYZE 2
AAA 2
BBB 1
CCC 497
LEFT-WALL 0 8 Xp
Mike 0 1 Wd
saw 1 2 Ss
the 2 5 MVp
girl 2 4 Os
with 3 4 Ds
the 5 7 Js
telescope 6 7 Ds
. 8 9 RW

BBB 1
CCC 497
LEFT-WALL 0 8 Xp
Mike 0 1 Wd
saw 1 2 Ss
the 2 4 Os
girl 3 4 Ds
with 4 5 Mp
the 5 7 Js
telescope 6 7 Ds
. 8 9 RW
	*/
/** reported
 0 8 Xp 
0 1 Wd 
1 2 Ss 
2 5 MVp 
2 4 Os 
3 4 Ds 
5 7 Js 
6 7 Ds 
8 9 RW 


0 8 Xp 
0 1 Wd 
1 2 Ss 
2 5 MVp 
2 4 Os 
3 4 Ds 
5 7 Js 
6 7 Ds 
8 9 RW 


0 8 Xp 
0 1 Wd 
1 2 Ss 
2 4 Os 
3 4 Ds 
4 5 Mp 
5 7 Js 
6 7 Ds 
8 9 RW 


0 8 Xp 
0 1 Wd 
1 2 Ss 
2 4 Os 
3 4 Ds 
4 5 Mp 
5 7 Js 
6 7 Ds 
8 9 RW 

    +------------------------Xp------------------------+ 
    |              +----Os----+     +-----Js----+      | 
    +---Wd---+--Ss-+    +--Ds-+--Mp-+   +---Ds--+      | 
    |        |     |    |     |     |   |       |      | 
LEFT-WALL Mike.b saw.v the girl.n with the telescope.n . 
 */
}
