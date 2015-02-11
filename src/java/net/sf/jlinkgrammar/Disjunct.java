package net.sf.jlinkgrammar;

import net.sf.jlinkgrammar.api.IParserConstants;

/**
 * Notes from the paper: Parsing English with a ResultLink Grammar
 *<p>In disjunctive form, each word of the grammar has a set of disjuncts associated with it. Each
 * disjunct corresponds to one particular way of satisfying the requirements of a word. A disjunct
 * consists of two ordered lists of connector names: the left list and the right list. The left list contains
 * connectors that connect to the left of the current word (those connectors end in -), and the right list
 * contains connectors that connect to the right of the current word.</p>
 * <p>Left list elements are the connectors that must connect to the left, and right list elements are
 * connectors that must connect to the right. The number of connectors in either list may be zero.
 * The trailing +or -may be omitted from the connector names when using disjunctive form, since
 * the direction is implicit in the form of the disjunct.</p>
 * <p>To satisfy the linking requirements of a word, one of its disjuncts must be satisfied (and no links
 * may attach to any other disjunct). To satisfy a disjunct all of its connectors must be satisfied by
 * appropriate links. The words to which left elements are linked are to the left of the current word, and
 * are monotonically increasing in distance from the current word. The words to which right elements
 * are linked are to the right of the current word, and are monotonically increasing in distance from
 * the current word.</p>
 */
public class Disjunct {
	public Disjunct next;
	public int cost;
	public boolean marked;
	public String string;
	public Connector left, right;

	/**
	 * Live constructor
	 * @param d
	 */
    Disjunct(Disjunct d) {
        next = d.next;
        cost = d.cost;
        marked = d.marked;
        string = d.string;
        left = d.left;
        right = d.right;
    }

    /**
     * Bean constructor
     */
    Disjunct() {
    }

    /**
     *  <p>Destructively catenates the two disjunct lists d1 followed by d2.
     *  Doesn't change the contents of the disjuncts.
     *  Traverses the first list, but not the second</p>
     * @param d1
     * @param d2
     * @return
     */
   static Disjunct catenate_disjuncts(Disjunct d1, Disjunct d2) {
        Disjunct dis = d1;

        if (d1 == null)
            return d2;
        if (d2 == null)
            return d1;
        while (dis.next != null)
            dis = dis.next;
        dis.next = d2;
        return d1;
    }

    static int dup_table_size;
    static Disjunct dup_table[];

    /**
     * <p>Takes the list of disjuncts pointed to by d, eliminates all
     * duplicates, and returns a pointer to a new list.
     * It frees the disjuncts that are eliminated.</p>
     * @param opts
     * @param d
     * @return
     */
    static Disjunct eliminate_duplicate_disjuncts(ParseOptions opts, Disjunct d) {
        int i, h, count;
        Disjunct dn, dx;
        count = 0;
        dup_table_size = MyRandom.next_power_of_two_up(2 * count_disjuncts(d));
        dup_table = new Disjunct[dup_table_size];
        for (i = 0; i < dup_table_size; i++)
            dup_table[i] = null;
        while (d != null) {
            dn = d.next;
            h = d.old_hash_disjunct();

            for (dx = dup_table[h]; dx != null; dx = dx.next) {
                if (dx.disjuncts_equal(d))
                    break;
            }
            if (dx == null) {
                d.next = dup_table[h];
                dup_table[h] = d;
            } else {
                d.next = null; /* to prevent it from freeing the whole list */
                if (d.cost < dx.cost)
                    dx.cost = d.cost;
                count++;
            }
            d = dn;
        }
        // d is already null
        for (i = 0; i < dup_table_size; i++) {
            for (dn = dup_table[i]; dn != null; dn = dx) {
                dx = dn.next;
                dn.next = d;
                d = dn;
            }
        }
        if (opts.verbosity > 2 && count != 0)
            opts.out.println("killed " + count + " duplicates");
        return d;
    }

    /**
     * returns the number of disjuncts in the list pointed to by <code>d</code>
     * @param d
     * @return
     */
    static int count_disjuncts(Disjunct d) {
        int count = 0;
        for (; d != null; d = d.next) {
            count++;
        }
        return count;
    }

    /**
     * <p>returns <code>true</code> if the disjuncts are exactly the same</p>
     * @param d2
     * @return
     */
    boolean disjuncts_equal(Disjunct d2) {
        Connector e1 = left;
        Connector e2 = d2.left;
        while (e1 != null && e2 != null) {
            if (!e1.connectors_equal_prune(e2))
                break;
            e1 = e1.next;
            e2 = e2.next;
        }
        if (e1 != null || e2 != null)
            return false;
        e1 = right;
        e2 = d2.right;
        while (e1 != null && e2 != null) {
            if (!e1.connectors_equal_prune(e2))
                break;
            e1 = e1.next;
            e2 = e2.next;
        }
        if ((e1 != null) || (e2 != null))
            return false;
        return string.equals(d2.string);
    }

    /**
     * Here is the old version that doesn't check for domination, just equality.
     * @return
     */
    int old_hash_disjunct() {
        //This is a hash function for disjuncts
        Connector e;
        int i = 0;
        for (e = left; e != null; e = e.next) {
            i = string_hash(e.string, i);
        }
        for (e = right; e != null; e = e.next) {
            i = string_hash(e.string, i);
        }
        return string_hash(string, i);
    }

    /**
     * 
     * @param s
     * @param seed
     * @return
     */
    static int string_hash(String s, int seed) {
        for (int j = 0; j < s.length(); j++)
            seed = seed + (seed << 1) + MyRandom.randtable[(s.charAt(j) + seed) & (IParserConstants.RTSIZE - 1)];
        return (seed & (dup_table_size - 1));
    }

    /**
     * </p> Two disjuncts are said to be the same type if they're the same
     * ignoring the multi fields, the priority fields, and the subscripts
     * of the connectors (and the string field of the disjunct of course).
     * Disjuncts of the same type are located in the same label_table list.</p>
     * @param d2
     * @return  <code>true</code> if they are of the same type
     */
    boolean disjunct_types_equal(Disjunct d2) {
        Connector e1, e2;

        e1 = left;
        e2 = d2.left;
        while ((e1 != null) && (e2 != null)) {
            if (!e1.connector_types_equal(e2))
                break;
            e1 = e1.next;
            e2 = e2.next;
        }
        if ((e1 != null) || (e2 != null))
            return false;
        e1 = right;
        e2 = d2.right;
        while ((e1 != null) && (e2 != null)) {
            if (!e1.connector_types_equal(e2))
                break;
            e1 = e1.next;
            e2 = e2.next;
        }
        if ((e1 != null) || (e2 != null))
            return false;
        return true;
    }

    /**
     * Return true if the disjuncts are equal (ignoring priority fields)
     * and the string of the disjunct.
     * @param d2
     * @return
     */
    boolean disjuncts_equal_AND(Disjunct d2) {
        Connector e1, e2;
        GlobalBean.STAT_calls_to_equality_test++;
        e1 = left;
        e2 = d2.left;
        while (e1 != null && e2 != null) {
            if (!e1.connectors_equal_AND(e2))
                break;
            e1 = e1.next;
            e2 = e2.next;
        }
        if ((e1 != null) || (e2 != null))
            return false;
        e1 = right;
        e2 = d2.right;
        while (e1 != null && e2 != null) {
            if (!e1.connectors_equal_AND(e2))
                break;
            e1 = e1.next;
            e2 = e2.next;
        }
        if ((e1 != null) || (e2 != null))
            return false;
        return true;
    }

    /**
     * <p> Create a new disjunct that is the GCD of d1 and d2.
     * It assumes that the disjuncts are of the same type, so the GCD will not be empty.</p>
     * @param d2
     * @return
     */
    Disjunct intersect_disjuncts(Disjunct d2) {
        Disjunct d;
        Connector c1, c2, c;
        d = copy_disjunct(this);
        c = d.left;
        c1 = left;
        c2 = d2.left;
        while (c1 != null) {
            c.string = Sentence.intersect_strings(c1.string, c2.string);
            c.multi = c1.multi && c2.multi;
            c = c.next;
            c1 = c1.next;
            c2 = c2.next;
        }
        c = d.right;
        c1 = right;
        c2 = d2.right;
        while (c1 != null) {
            c.string = Sentence.intersect_strings(c1.string, c2.string);
            c.multi = c1.multi && c2.multi;
            c = c.next;
            c1 = c1.next;
            c2 = c2.next;
        }
        return d;
    }

    /**
     * Hash function for disjuncts
     * @return
     */
    int and_hash_disjunct() {
        int i;
        Connector e;
        i = 0;
        for (e = left; e != null; e = e.next) {
            i = e.and_connector_hash(i);
        }
        i = i + (i << 1) + MyRandom.randtable[i & (IParserConstants.RTSIZE - 1)];
        for (e = right; e != null; e = e.next) {
            i = e.and_connector_hash(i);
        }
        return (i & (IParserConstants.HT_SIZE - 1));
    }

    /**
     * <p>Return a new copy of the disjunct pointed to by d (except for the
     * next field which is set to null).  Strings, as usual, are not copied.</p>
     * @param d
     * @return
     */
    static Disjunct copy_disjunct(Disjunct d) {
        Disjunct d1;
        if (d == null)
            return null;
        d1 = new Disjunct(d);
        d1.next = null;
        d1.left = Connector.copy_connectors(d.left);
        d1.right = Connector.copy_connectors(d.right);
        return d1;
    }

}
