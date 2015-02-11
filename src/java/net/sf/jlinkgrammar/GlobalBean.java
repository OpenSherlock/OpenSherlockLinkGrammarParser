package net.sf.jlinkgrammar;

//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import net.sf.jlinkgrammar.api.IParserConstants;

import org.topicquests.common.ResultPojo;
import org.topicquests.common.api.IResult;

/**
 * <p>This is a hacked construct.  This bean holds several constructs and  variables
 * needed across the entire link grammar program.  This should be a parent and all
 * link grammar objects should derive from it.  That way we better encapsulate the 
 * variables.</p>
 * @author unknown (original)
 * @author park (updates)
 */
public class GlobalBean {
	

    public static int batch_errors = 0;
    public static boolean input_pending = false;
    public static int input_char;
    public static ParseOptions opts;

    public static String lperrmsg;
    /** keeping statistics */
    public static int STAT_N_disjuncts; 
    public static int STAT_calls_to_equality_test;


    /** Prints s then prints the last |t|-|s| characters of t.
           if s is longer than t, it truncates s.
        */
    public static void left_append_string(StringBuffer string, String s, String t) {
        
        int i, j, k;
        j = t.length();
        k = s.length();
        for (i = 0; i < j; i++) {
            if (i < k) {
                string.append(s.charAt(i));
            } else {
                string.append(t.charAt(i));
            }
        }
    }

    public static int strip_off_label(String string) {
    	StringBuffer buf = new StringBuffer(string);
    	return strip_off_label(buf);
    }
    public static int strip_off_label(StringBuffer input_string) {
        int c;

        c = input_string.charAt(0);
        switch (c) {
            case IParserConstants.UNGRAMMATICAL :
            case IParserConstants.PARSE_WITH_DISJUNCT_COST_GT_0 :
                input_string.setCharAt(0, ' ');
                return c;
            default :
                return IParserConstants.NO_LABEL;
        }
    }

    public static boolean special_command(StringBuffer input_string, Dictionary dict) {

        if (input_string.charAt(0) == '\n')
            return true;
        if (input_string.charAt(0) == IParserConstants.COMMENT_CHAR)
            return true;
        if (input_string.charAt(0) == '!') {
            opts.issue_special_command(input_string.substring(1), dict);
            return true;
        }
        return false;
    }

    public static void batch_process_some_linkages(int label, Sentence sent, ParseOptions opts) {
        Linkage linkage;
        System.out.println("BATCHSOME");
        if (there_was_an_error(label, sent, opts) != 0) {
            if (sent.sentence_num_linkages_found() > 0) {
                linkage = new Linkage(0, sent, opts);
                linkage.process_linkage(opts);
            }
            opts.out.println("+++++ error " + batch_errors);
        }
    }

    public static int there_was_an_error(int label, Sentence sent, ParseOptions opts) {

        if (sent.sentence_num_valid_linkages() > 0) {
            if (label == IParserConstants.UNGRAMMATICAL) {
                opts.out.println("error: parsed ungrammatical sentence");
                batch_errors++;
                return IParserConstants.UNGRAMMATICAL;
            }
            if ((sent.sentence_disjunct_cost(0) == 0) && (label == IParserConstants.PARSE_WITH_DISJUNCT_COST_GT_0)) {
                opts.out.println("error: cost=0");
                batch_errors++;
                return IParserConstants.PARSE_WITH_DISJUNCT_COST_GT_0;
            }
        } else {
            if (label != IParserConstants.UNGRAMMATICAL) {
                opts.out.println("error: failed");
                batch_errors++;
                return IParserConstants.UNGRAMMATICAL;
            }
        }
        return 0;
    }

    /**
     * TopicQuests extension to analyze <code>sent</code>
     * adapted from <code>process_some_linkages</code>
     * @param sent
     * @param opts
     * @return
     * /
    public IResult analyeSentence(Sentence sent, ParseOptions opts) {
    	IResult result = new ResultPojo();
        int c, num_to_query;
        Linkage linkage;

        //if (opts.verbosity > 0)
        //    sent.print_parse_statistics(opts);
        if (!opts.parse_options_get_display_bad()) {
            num_to_query = Math.min(sent.sentence_num_valid_linkages(), IParserConstants.DISPLAY_MAX);
        } else {
            num_to_query = Math.min(sent.sentence_num_linkages_post_processed(), IParserConstants.DISPLAY_MAX);
        }
    	System.out.println("analyzeSentence "+num_to_query);
    	//num_to_query is the same as num_linkages at the end of parsing a sentence
    	
    	IResult r;
    	//int  num_displayed = 0;
        for (int i = 0; i < num_to_query; ++i) {

            if ((sent.sentence_num_violations(i) > 0) && (!opts.parse_options_get_display_bad())) {
                continue;
            }

            //create a linkage for the given (i) linkage and this sentence
            linkage = new Linkage(i, sent, opts);  

 /*           if (opts.verbosity > 0) {
                if (sent.sentence_num_valid_linkages() == 1 && (!opts.parse_options_get_display_bad())) {
                    opts.out.print("  Unique linkage, ");
                } else if ((opts.parse_options_get_display_bad()) && (sent.sentence_num_violations(i) > 0)) {
                    opts.out.print("  ResultLinkage " + (i + 1) + " (bad), ");
                } else {
                    opts.out.print("  ResultLinkage " + (i + 1) + ", ");
                }

                if (!linkage.linkage_is_canonical()) {
                    opts.out.print("non-canonical, ");
                }
                if (linkage.linkage_is_improper()) {
                    opts.out.print("improper fat linkage, ");
                }
                if (linkage.linkage_has_inconsistent_domains()) {
                    opts.out.print("inconsistent domains, ");
                }
                opts.out.println(
                    "cost vector = (UNUSED="
                        + linkage.linkage_unused_word_cost()
                        + " DIS="
                        + linkage.linkage_disjunct_cost()
                        + " AND="
                        + linkage.linkage_and_cost()
                        + " LEN="
                        + linkage.linkage_link_cost()
                        + ")");
            } * /

            //linkage.process_linkage(opts);
            
            //process the linkage
            r = linkage.processLinkage(opts);
            //TODO
            // What to do with r???

 /*           if (++num_displayed < num_to_query) {
                if (opts.verbosity > 0) {
                    opts.out.println("Press RETURN for the next linkage.");
                }
                c = fget_input_char(System.in, opts);
                if (c != '\n' && c != '\r') {
                    input_char = c;
                    input_pending = true;
                    break;
                }
            } * /
        }    	
        return result;
    }
    */
    
    public static void process_some_linkages(Sentence sent, ParseOptions opts) throws IOException {
        int i, c, num_displayed, num_to_query;
        Linkage linkage;

        if (opts.verbosity > 0)
            sent.print_parse_statistics(opts);
        if (!opts.parse_options_get_display_bad()) {
            num_to_query = Math.min(sent.sentence_num_valid_linkages(), IParserConstants.DISPLAY_MAX);
        } else {
            num_to_query = Math.min(sent.sentence_num_linkages_post_processed(), IParserConstants.DISPLAY_MAX);
        }
    	System.out.println("PROCESSSOME "+num_to_query);
    	//num_to_query is the same as num_linkages at the end of parsing a sentence

        for (i = 0, num_displayed = 0; i < num_to_query; ++i) {

            if ((sent.sentence_num_violations(i) > 0) && (!opts.parse_options_get_display_bad())) {
                continue;
            }

            linkage = new Linkage(i, sent, opts);  

            if (opts.verbosity > 0) {
                if (sent.sentence_num_valid_linkages() == 1 && (!opts.parse_options_get_display_bad())) {
                    opts.out.print("  Unique linkage, ");
                } else if ((opts.parse_options_get_display_bad()) && (sent.sentence_num_violations(i) > 0)) {
                    opts.out.print("  ResultLinkage " + (i + 1) + " (bad), ");
                } else {
                    opts.out.print("  ResultLinkage " + (i + 1) + ", ");
                }

                if (!linkage.linkage_is_canonical()) {
                    opts.out.print("non-canonical, ");
                }
                if (linkage.linkage_is_improper()) {
                    opts.out.print("improper fat linkage, ");
                }
                if (linkage.linkage_has_inconsistent_domains()) {
                    opts.out.print("inconsistent domains, ");
                }
                opts.out.println(
                    "cost vector = (UNUSED="
                        + linkage.linkage_unused_word_cost()
                        + " DIS="
                        + linkage.linkage_disjunct_cost()
                        + " AND="
                        + linkage.linkage_and_cost()
                        + " LEN="
                        + linkage.linkage_link_cost()
                        + ")");
            }

            linkage.process_linkage(opts);

            if (++num_displayed < num_to_query) {
                if (opts.verbosity > 0) {
                    opts.out.println("Press RETURN for the next linkage.");
                }
                c = fget_input_char(System.in, opts);
                if (c != '\n' && c != '\r') {
                    input_char = c;
                    input_pending = true;
                    break;
                }
            }
        }
    }

    public static int fget_input_char(InputStream in, ParseOptions opts) throws IOException {
        if (!opts.parse_options_get_batch_mode() && (opts.verbosity > 0))
            opts.out.print("linkparser> ");
        opts.out.flush();
        return in.read();
    }

    public static boolean fget_input_string(StringBuffer input_string, InputStream in, PrintStream out, ParseOptions opts)
        throws IOException {
        int c;
        input_string.setLength(0);
        if (input_pending) {
            input_pending = false;
            c = input_char;
        } else {
            if (!opts.parse_options_get_batch_mode() && opts.verbosity > 0)
                out.println("linkparser> ");
            out.flush();
            c = in.read();
        }
        while (c != '\n') {
            if (c < 0) {
                return false;
            }
            input_string.append((char)c);
            c = in.read();
        }
        return true;
    }

}
