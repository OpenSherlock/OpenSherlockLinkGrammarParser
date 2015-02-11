package net.sf.jlinkgrammar;

import net.sf.jlinkgrammar.api.IParserConstants;

/**
 * TODO add javadoc
 *
 */
public class Sublinkage {
	/* Number of links in array */
    public int num_links=0; 
    /* Array of links */
    public Link link[]; 
    /* PP info for each link */
    PPInfo pp_info[]; 
    /* Name of violation, if any */
    public String violation = null; 
    PPData pp_data;

    Sublinkage() {
    }

    Sublinkage(ParseInfo pi) {
        int i;
        link = new Link[IParserConstants.MAX_LINKS];
        pp_info = null;
        violation = null;
        for (i = 0; i < IParserConstants.MAX_LINKS; i++)
            link[i] = null;
        num_links = pi.N_links;
        if (!(pi.N_links < IParserConstants.MAX_LINKS)) {
            throw new RuntimeException("Too many links");
        }
    }

}
