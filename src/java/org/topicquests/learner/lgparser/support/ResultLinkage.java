/*************************************************************************/
/*                                                                       */
/* Use of the link grammar parsing system is subject to the terms of the */
/* license set forth in the LICENSE file included with this software.    */
/* This license allows free redistribution and use in source and binary  */
/* forms, with or without modification, subject to certain conditions.   */
/*                                                                       */
/*************************************************************************/

//package org.linkgrammar;
package org.topicquests.learner.lgparser.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A <code>ResultLinkage</code> represents one of possibly many parses
 * returned by the ResultLink Grammar parser. Each <code>ResultLinkage</code>
 * is defined by a list of <code>ResultLink</code>s between the tokens
 * in a sentence.  A <code>ResultLinkage</code> also has some metadata
 *  associated with it, e.g. for various cost measures. 
 *
 * @author Borislav Iordanov
 */
public class ResultLinkage implements Iterable<ResultLink>
{
	private List<ResultLink> links = new ArrayList<ResultLink>();
	private String [] disjuncts;
	private String [] words;
	private String constituentString;
	private int linkedWordCount;
	private int andCost;
	private int disjunctCost;
	private int linkCost;
	private int numViolations;
	
	public List<ResultLink> getLinks()
	{
		return links;
	}
	
	public Iterator<ResultLink> iterator()
	{
		return links.iterator();
	}
	
	public String disjunctAt(int i)
	{
		return disjuncts[i];
	}
 
	public String[] getDisjuncts()
	{
		return disjuncts;
	}

	public void setDisjuncts(String[] disjuncts)
	{
		this.disjuncts = disjuncts;
	}

	public String wordAt(int i)
	{
		return words[i];
	}
 
	public String[] getWords()
	{
		return words;
	}

	public void setWords(String[] words)
	{
		this.words = words;
	}

	public int getAndCost()
	{
		return andCost;
	}

	public void setAndCost(int andCost)
	{
		this.andCost = andCost;
	}

	public int getDisjunctCost()
	{
		return disjunctCost;
	}

	public void setDisjunctCost(int disjunctCost)
	{
		this.disjunctCost = disjunctCost;
	}

	public int getLinkCost()
	{
		return linkCost;
	}

	public void setLinkCost(int linkCost)
	{
		this.linkCost = linkCost;
	}

	public int getNumViolations()
	{
		return numViolations;
	}

	public void setNumViolations(int numViolations)
	{
		this.numViolations = numViolations;
	}

	public int getLinkedWordCount()
	{
		return linkedWordCount;
	}

	public void setLinkedWordCount(int linkedWordCount)
	{
		this.linkedWordCount = linkedWordCount;
	}

	public String getConstituentString()
	{
		return constituentString;
	}

	public void setConstituentString(String constituentString)
	{
		this.constituentString = constituentString;
	}	
}
