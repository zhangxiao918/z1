/**
 * 
 */
package com.skymobi.android.transport;

import java.util.TimerTask;

import org.apache.mina.core.service.IoServiceStatistics;

/**
 * @author hp
 *
 */
public class MinaStatisticsUpdater extends TimerTask {

	private IoServiceStatistics[] statisticses;
	
	public void setStatisticses(IoServiceStatistics[] statisticses) {
		this.statisticses = statisticses;
	}
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		long now = System.currentTimeMillis();
		for ( IoServiceStatistics statistics : statisticses) {
			statistics.updateThroughput(now);
		}
	}

}
