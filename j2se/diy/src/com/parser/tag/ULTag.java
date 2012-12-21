package com.parser.tag;

import org.htmlparser.tags.CompositeTag;

public class ULTag extends CompositeTag{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String[] mIds = new String[] { "UL" };

	public String[] getIds() {
		return (mIds);
	}

	public String[] getEnders() {
		return (mIds);
	}

}
