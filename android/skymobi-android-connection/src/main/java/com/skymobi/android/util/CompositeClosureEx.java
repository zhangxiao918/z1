/**
 * 
 */
package com.skymobi.android.util;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 *
 */
public class CompositeClosureEx implements ClosureEx {

    private static final Logger logger = LoggerFactory.getLogger(CompositeClosureEx.class);
	private ClosureEx[] closures;
	
	/**
	 * @return the closures
	 */
	public List<ClosureEx> getClosures() {
		return Arrays.asList( closures );
	}

	/**
	 * @param closures the closures to set
	 */
	public void setClosures(List<ClosureEx> closures) {
		this.closures = closures.toArray(new ClosureEx[0]);
	}

	/* (non-Javadoc)
	 * @see com.skymobi.util.ClosureEx#execute(java.lang.Object[])
	 */
	public void execute(final Object... args) {
		for ( ClosureEx closure : this.closures ) {
			try {
				closure.execute(args);
			}
			catch (Exception e) {
				logger.error("execute:", e);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.skymobi.util.ClosureEx#setCanceled(boolean)
	 */
	public void setCanceled(boolean canceled) {
		for ( ClosureEx closure : this.closures ) {
			try {
				closure.setCanceled(canceled);
			}
			catch (Exception e) {
				logger.error("setCanceled:", e);
			}
		}
	}

}
