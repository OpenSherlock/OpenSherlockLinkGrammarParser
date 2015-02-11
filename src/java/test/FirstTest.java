/**
 * 
 */
package test;

import org.topicquests.common.api.IResult;
import org.topicquests.learner.lgparser.LinkGrammarEnvironment;
import org.topicquests.learner.lgparser.LinkGrammarParser;

/**
 * @author park
 *
 */
public class FirstTest {
	private LinkGrammarEnvironment environment;
	private LinkGrammarParser parser;
	private String testSentence = "Now is the time for all good men to come to the aid of their country.";
	private String test2 = "Many scientists believe that climate change is caused by CO2";
	private String test3 = "Lewy Dementia causes memory loss";
	private String test4 = "What are the causes of climate change";
	private String test5 = "Hello World";
	private String test6 = "Many scientists believe that CO2 is the cause of harm, mayhem, and general malaise";
	private String test7 = "Mary said that X is always true, but Sue says that X is never true";
	private String test8 = "Mary said that X is always true; Sue says that X is never true";
	private String test9 = "How many times can you spell a cat without so much as crossing your fingers, while you are taking a hike?";
	private String test10= "Jack Park has been a developer of artificial intelligence projects, most recently the DARPA CALO project at SRI International.";
	private String test11= "Using two biomedical corpora, "+
							"we implement and evaluate three approaches to addressing unknown words: automatic lexicon "+
							" expansion, the use of morphological clues, and disambiguation using a part-of-speech tagger.";
	private String test12= "the big snake the black cat chased bit Mary";
	private String test13= "A country can have a president";
	private String test14= "George Herbert Walker Bush was the president of the United States of America";
	private String bogus = "Sara said to drive more slowerly";
	private String testSentence2 = "Mike saw the girl with the telescope.";
	private String an1 = "Sue gave it to Bill";
	private String bigOne = "The physicochemical similarity of the target compounds with respect to standard drugs clozapine, ketanserin and risperidone was calculated by using software programs.";
	private String bb = "AG-221 specifically inhibits IDH2 in the mitochondria, which inhibits the formation of 2-hydroxyglutarate (2HG)";
	private String cc = "carbon dioxide in the upper atmosphere is a cause of climate change";
	private String dd = "CO2 is a greenhouse gas";
	private String ee = "CO2 is a cause of climate change";
	private String ff = "Upper atmospheric CO2 is a cause of climate change";
	private String gg = "Some CO2 in the upper atmosphere is a cause of climate change";
	private String hh = "CO2 located in the upper atmosphere is a cause of climate change";
	private String ii = "CO2 is a greenhouse gas and is a cause of climate change";
	private String jj = "CO2 is a greenhouse gas which is a cause of climate change";
	private String kk = "CO2 in the upper atmosphere is a greenhouse gas which is a cause of climate change";
	private String ll = "CO2, when located in the upper atmosphere, is a greenhouse gas which is a cause of climate change";
	private String mm = "CO2, located in the upper atmosphere, is a cause of climate change";

	/**
	 * 
	 */
	public FirstTest() {
		try {
			environment = new LinkGrammarEnvironment();
			parser = environment.getParser();
			runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void runTest() {
		IResult r = parser.testParseSentence(mm);
		//IResult r = parser.parseSentence(testSentence);
	}
/**
 *     +---------------------------------------------Xp---------------------------------------------+
    |             +------------------TO-----------------+                                        |
    |             |               +-------Jp-------+    |                                        |
    |             +---SIs---+     |    +----Dmc----+    |         +---Jp--+    +-----Js----+     |
    +------Qd-----+   +-D*u-+--Mp-+    |     +--A--+    +--I-+-MVp+  +-D*u+-Mp-+   +---Ds--+     |
    |             |   |     |     |    |     |     |    |    |    |  |    |    |   |       |     |
LEFT-WALL [now] is.v the time.n for.p all good.a men.n to come.v to the aid.n of their country.n . 

                               +-------Cet-------+                       
  +---Dmc--+----Sp----+---TH---+       +----AN---+--Ss--+--Pv--+-MVp-+Js+
  |        |          |        |       |         |      |      |     |  |
many scientists.n believe.v that.c climate.n change.n is.v caused.v by CO2 
                  +-------Os-------+
  +---G--+---Ss---+        +---AN--+
  |      |        |        |       |
Lewy Dementia causes.v memory.n loss.n 
                 +----SIpx---+     +-------Jp------+
    +---Wq--+Bswt+    +--Dmc-+--Mp-+     +----AN---+
    |       |    |    |      |     |     |         |
LEFT-WALL what are.v the causes.n of climate.n change.n 

                                          +-------------Mp-------------+     
                                          |       +-------J-------+    |     
                             +-----Ost----+       |  +-----DG-----+    |     
   +---G--+---G---+--G--+-Ss-+    +---Ds--+---Mp--+  |     +---G--+    +-Js-+
   |      |       |     |    |    |       |       |  |     |      |    |    |
George Herbert Walker Bush was.v the president.n of the United States of America 

    +-----------------------Xp-----------------------+
    |            +-------MVp------+                  |
    |            +----Os----+     +-----Js----+      |
    +---Wd--+-Ss-+    +--Ds-+     |   +---Ds--+      |
    |       |    |    |     |     |   |       |      |
LEFT-WALL Mike saw.v the girl.n with the telescope.n . 

 */
}
