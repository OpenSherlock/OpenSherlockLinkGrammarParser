package net.sf.jlinkgrammar;

import net.sf.jlinkgrammar.api.IParserConstants;

/**
 * TODO add javadoc
 *
 */
public class AndList {
    AndList next;
    int conjunction;
    int num_elements;
    int element[]=new int[IParserConstants.MAX_SENTENCE];
    int num_outside_words;
    int outside_word[]=new int[IParserConstants.MAX_SENTENCE];
    int cost;

}
