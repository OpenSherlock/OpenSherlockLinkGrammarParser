package net.sf.jlinkgrammar;

import net.sf.jlinkgrammar.api.IParserConstants;

/**
 * TODO add javadoc
 *
 */
public class PPData {
    int N_domains;
    ListOfLinks word_links[] = new ListOfLinks[IParserConstants.MAX_SENTENCE];
    ListOfLinks links_to_ignore;
    Domain domain_array[] = new Domain[IParserConstants.MAX_LINKS]; /* the domains, sorted by size */
    int length; /* length of current sentence */

}
