# OpenSherlockLinkGrammarParser
An adaptation of an existing Java LinkGrammarParser. The original is found [here](here "http://jlinkgrammar.sourceforge.net/").

This project adds an *environment* which provides an API for logging and for shutting down th parser.

Usage:

LinkGrammarEnvironment environment = //...get an instance of LinkGrammarEnvironment
LinkGrammarParser lgParser = environment.getParser();
IResult r = lgParser.parseSentence(String sentence);

Examples:

CO2 in the upper atmosphere is a cause of climate change



                +-------------Qd-------------+                              
                 +---------Js--------+        +------OF-----+                
                 |  +-------Ds-------+        +--SIs--+     +-------Jp------+
    +-----Wj-----+  |     +-----A----+        |  +-Ds-+     |     +----AN---+
    |            |  |     |          |        |  |    |     |     |         |
LEFT-WALL [CO2] in the upper.a atmosphere.n is.v a cause.n of climate.n change.n 


Some CO2 in the upper atmosphere is a cause of climate change
  +------------------Ss------------------+                              
  |         +---------Js--------+        |                              
  |         |  +-------Ds-------+        +--Ost--+     +-------Jp------+
  +----Mp---+  |     +-----A----+        |  +-Ds-+--Mp-+     +----AN---+
  |         |  |     |          |        |  |    |     |     |         |
some [CO2] in the upper.a atmosphere.n is.v a cause.n of climate.n change.n 


                  +-------Os-------+
  +---G--+---Ss---+        +---AN--+
  |      |        |        |       |
Lewy Dementia causes.v memory.n loss.n 
 1      2       3        4        5
[{values=[[0, 6, RW]], word=LEFT-WALL}, 
 {values=[[0, 2, Wd]], word=Lewy}, 
 {values=[[1, 2, G]], word=Dementia}, G: Lewy Dimentia are proper noun
 {values=[[2, 3, Ss]], word=causes},  Ss: connect subject to finite verb
 {values=[[3, 5, Os]], word=memory},  Os: connect transitive verb to object: links verb causes to noun loss, which is part of a noun phrase (s=singular)
 {values=[[4, 5, AN]], word=loss}]    An: connect noun modifier to noun: memory loss as noun phrase


                               +-------Cet-------+                       
  +---Dmc--+----Sp----+---TH---+       +----AN---+--Ss--+--Pv--+-MVp-+Js+
  |        |          |        |       |         |      |      |     |  |
many scientists.n believe.v that.c climate.n change.n is.v caused.v by CO2 
 1       2           3        4       5        6       6     7      8   9
[{values=[[0, 11, RW]], word=LEFT-WALL}, 
 {values=[[0, 2, Wd]], word=many}, 					Wd: attach subject to left wall
 {values=[[1, 2, Dmc]], word=scientists}, 	D: connect determiners with nouns
 {values=[[2, 3, Sp]], word=believe},   		Ss/p: connect subject to finite verb
 {values=[[3, 4, TH]], word=that}, 					TH connects words that take "that [clause]" complements with the word "that"
 	<note: the system will likely catch "believe that" as a pre-defined verb phrase
 {values=[[4, 6, Cet]], word=climate},  		Ce is used for verbs that take clausal complements, also known as embedded clauses
 {values=[[5, 6, AN]], word=change}, 				An: connect noun modifier to noun: climate change as a noun phrase
 {values=[[6, 7, Ss]], word=is}, 						Ss: connect subject to finite verb
 {values=[[7, 8, Pv]], word=caused},  			Pv: connects forms of "be" to passive participles
 {values=[[8, 9, MVp]], word=by}, 					MVp: connects verbs/adjectives (p=plural) to modifying phrases
 		<note: the system will catch "is caused by" as a pre-defined verb phrase and override this>
 {values=[[9, 10, Js]], word=CO2}] 					Js: connect preposition to its object