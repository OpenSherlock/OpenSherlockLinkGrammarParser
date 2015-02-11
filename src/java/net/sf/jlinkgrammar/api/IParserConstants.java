/**
 * 
 */
package net.sf.jlinkgrammar.api;

/**
 * @author park
 *
 */
public interface IParserConstants {


    /**
     * If this connector is used on the wall,
     * then suppress the display of the wall
     * bogus name to prevent ever suppressing
     */
    public final static String LEFT_WALL_SUPPRESS = "Wd";


    /**
     * Supress if this connector is used on the wall
     */
    final static String RIGHT_WALL_SUPPRESS = "RW";

    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String LEFT_WALL_WORD = "LEFT-WALL";
    
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String RIGHT_WALL_WORD = "RIGHT-WALL";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String POSTPROCESS_WORD = "POSTPROCESS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String ANDABLE_CONNECTORS_WORD = "ANDABLE-CONNECTORS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String UNLIMITED_CONNECTORS_WORD = "UNLIMITED-CONNECTORS";

    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String PROPER_WORD = "CAPITALIZED-WORDS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String PL_PROPER_WORD = "PL-CAPITALIZED-WORDS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String HYPHENATED_WORD = "HYPHENATED-WORDS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String NUMBER_WORD = "NUMBERS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String ING_WORD = "ING-WORDS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String S_WORD = "S-WORDS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String ED_WORD = "ED-WORDS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String LY_WORD = "LY-WORDS";
    
    /**
     * Defins the name of a special string in the dictionary.
     */
    public final static String UNKNOWN_WORD = "UNKNOWN-WORD";
    
    /**
     * This is a hack that allows one to discard disjuncts containing
     * connectors whose cost is greater than given a bound. This number plus
     * the cost of any connectors on a disjunct must remain negative, and
     * this number multiplied times the number of costly connectors on any
     * disjunct must fit into an integer.
     */
    public final static int NEGATIVECOST = -1000000;
    /**
     * no connector will have cost this high 
     */
    public final static int NOCUTOFF = 1000; 



    /**
     * file names (including paths)
     * should not be longer than this
     */
    public final static int MAX_PATH_NAME = 400;

    /**
     * Some size definitions.  Reduce these for small machines - Left over from C not needed in Java
     */
    /**
     * maximum number of chars in a word
     */
    public final static int MAX_WORD = 60;
    /**
     * maximum number of chars in a sentence
     */
    public final static int MAX_LINE = 1500;
     /**
      * maximum number of words in a sentence
      */
    public final static int MAX_SENTENCE = 250;

    /**
     * This is the maximum number of links allowed.
     *
     * It cannot be more than 254, because I use word MAX_SENTENCE+1 to
     * indicate that nothing can connect to this connector, and this
     * should fit in one byte (if the word field of a connector is an
     * (unsigned char) 
     */
    public final static int MAX_LINKS = (2 * MAX_SENTENCE - 3);
    /**
     * maximum number of chars in a token
     */
    public final static int MAX_TOKEN_LENGTH = 50;
    /**
     * Max disjunct cost to allow
     */
    public final static int MAX_DISJUNCT_COST = 10000;

    public final static int DOWN_priority = 2;
    public final static int UP_priority = 1;
    public final static int THIN_priority = 0;

    /** the labels >= 0 are used by fat links while -1 is used for normal connectors  */ 
    public final static int NORMAL_LABEL = (-1); 

    public final static int UNLIMITED_LEN = 255;
    public final static int SHORT_LEN = 6;
    public final static int NO_WORD = 255;

    /* Here are the types */
    public final static int OR_type = 0;
    public final static int AND_type = 1;
    public final static int CONNECTOR_TYPE = 2;

    /** These parameters tell power_pruning, to tell whether this is before or after
       generating and disjuncts.  GENTLE is before RUTHLESS is after. */
    public final static int GENTLE = 1;
    public final static int RUTHLESS = 0;

    public static final int PP_LEXER_MAX_LABELS = 512;
    /** CostModel sort by Violations, Disjunct cost, And cost, ResultLink cost */
    public static final int VDAL = 1; 

    public final static int HT_SIZE = (1 << 10);

    /** size of random table for computing the
       hash functions.  must be a power of 2 */
    public final static int RTSIZE = 256;

    public final static int NODICT = 1;
    public final static int DICTPARSE = 2;
    public final static int WORDFILE = 3;
    public final static int SEPARATE = 4;
    public final static int NOTINDICT = 5;
    public final static int BUILDEXPR = 6;
    public final static int INTERNALERROR = 7;

    public final static int LINKSET_SPARSENESS = 2;
    public final static int LINKSET_MAX_SETS = 512;
    public final static int LINKSET_DEFAULT_SEED = 37;

    public final static int PP_FIRST_PASS = 1;
    public final static int PP_SECOND_PASS = 2;

    public final static int MAXINPUT = 1024;
    public final static int DISPLAY_MAX = 1024;
    /** input lines beginning with this are ignored */
    public final static char COMMENT_CHAR = '%'; 

    public final static char UNGRAMMATICAL = '*';
    public final static char PARSE_WITH_DISJUNCT_COST_GT_0 = ':';
    public final static char NO_LABEL = ' ';

    /** the indication in a word field that this connector cannot
       be used -- is obsolete.
    */
    public final static int BAD_WORD = (MAX_SENTENCE + 1);

    public final static int PP_MAX_DOMAINS = 128;

    //final static int LINKSET_SPARSENESS=2;
    public final static int LINKSET_SEED_VALUE = 37;
    /** just needs to be approximate */
    public final static int PP_MAX_UNIQUE_LINK_NAMES = 1024; 

    public final static int LINE_LIMIT = 70;
    
    public final static int MAX_STRIP = 10;

    public final static int MAX_HEIGHT = 30;
    /** to hook the comma to the following "and" */
    public final static int COMMA_LABEL = (-2);
    /** to connect the "either" to the following "or" */
    public final static int EITHER_LABEL = (-3);
    /** to connect the "neither" to the following "nor"*/
    public final static int NEITHER_LABEL = (-4);
    /** to connect the "not" to the following "but"*/
    public final static int NOT_LABEL = (-5);
    /** to connect the "not" to the following "only"*/
    public final static int NOTONLY_LABEL = (-6);
    /** to connect the "both" to the following "and"*/
    public final static int BOTH_LABEL = (-7); 

    public final static int MAXCONSTITUENTS = 1024;
    public final static int MAXSUBL = 16;
    public final static char OPEN_BRACKET = '[';
    public final static char CLOSE_BRACKET = ']';

    public final static int CType_OPEN = 0;
    public final static int CType_CLOSE = 1;
    public final static int CType_WORD = 2;

    public final static int WType_NONE = 0;
    public final static int WType_STYPE = 1;
    public final static int WType_PTYPE = 2;
    public final static int WType_QTYPE = 3;
    public final static int WType_QDTYPE = 4;
}
