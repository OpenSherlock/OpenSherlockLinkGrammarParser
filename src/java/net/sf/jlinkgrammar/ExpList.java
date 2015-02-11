package net.sf.jlinkgrammar;

/**
 * TODO add javadoc
 *
 */
public class ExpList {
	public ExpList next;
	public  Exp e;

    static ExpList  copy_ExpList(ExpList l) {
        ExpList nl;
        if (l == null) return null;
        nl = new ExpList();
        nl.next = copy_ExpList(l.next);
        nl.e = Exp.copy_Exp(l.e);
        return nl;
    }

}
