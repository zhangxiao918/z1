/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.endpoint;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.transport.AbstractEndpoint;
import com.skymobi.android.transport.MutableEndpoint;
import com.skymobi.android.transport.protocol.esb.signal.HeartbeatToEsbReq;
import com.skymobi.android.transport.protocol.esb.signal.HeartbeatToEsbResp;
import com.skymobi.android.transport.protocol.esb.signal.RegisterToEsbReq;
import com.skymobi.android.transport.protocol.esb.signal.RegisterToEsbResp;

/**
 * @author hp
 *
 */
public class ESBModuleEndpoint extends AbstractEndpoint 
	implements MutableEndpoint {

    private static final Logger logger = 
    	LoggerFactory.getLogger(ESBModuleEndpoint.class);
    
    private ScheduledExecutorService exec = 
        Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

			public Thread newThread(Runnable r) {
				return new Thread(r, "ESBModule-Sender-thread");
			}} );
    
	private	int	moduleId;
	
	private	int	instanceId;
	
	//	esbConnectionState enum
	private static final int CONN_STATE_OFFLINE = 0;
	private static final int CONN_STATE_REGISTERING = 1;
	private static final int CONN_STATE_ONLINE = 2;
	
	//	esbHeartbeatState enum
	private static final int HB_STATE_IDLE = 0;
	private static final int HB_STATE_SENDED = 1;
	private static final int HB_STATE_RECVD = 2;
	
	private	int		esbConnectionState = CONN_STATE_OFFLINE;
	private	int		esbHeartbeatState = HB_STATE_IDLE;
	
	private	long	checkESBDelay = 5 * 1000;
	private	long	esbRespTimeout = 10 * 1000;
	private	long	esbHeartbeatInterval = 120 * 1000;
    private	long	fetchSignalTimeout = 1000;
    private	long	lastRegisterToEsbTime; //鏈�悗涓�娉ㄥ唽鐨勮姹傛椂闂�
	private	long	lastRegisterToEsbRespTime; //鏈�悗涓�娉ㄥ唽鐨勫搷搴旀椂闂�
	private	int		reRegisterCount = 0;
	private	long	lastHeartbeatToEsbTime = 0;
	private	long	esbHeartbeatTimeout = 10 * 1000;
	private	int		reHeartbeatCount = 0;
	
	private void doSendPending() {
		exec.submit(new Runnable() {

			public void run() {
				sendPending();
			}} );
    }
    
    private	boolean isESBConnectionValid() {
    	return	( (null != session) && (CONN_STATE_ONLINE == esbConnectionState) );
    }
    
    private	void sendPending() {
    	if ( isESBConnectionValid() ) {
			try {
				Object bean = pendings.poll(fetchSignalTimeout, TimeUnit.MILLISECONDS);
	    		if ( null != bean ) {
	    			session.write(bean);
	    		}
			} catch (InterruptedException e) {
	    		logger.error("sendPending", e);
			}
	    	finally {
	    		doSendPending();
	    	}
    	}
    	else {
    		//	esb not connected or not registered 
    		exec.schedule(new Runnable(){

				public void run() {
					sendPending();
				}}, fetchSignalTimeout, TimeUnit.MILLISECONDS);
    	}
    }
    
    private	boolean	isHeartbeatToEsbTimeout() {
    	long duration = System.currentTimeMillis() - lastHeartbeatToEsbTime;
    	return	( duration >= esbHeartbeatTimeout );
    }

    private	boolean	isHeartbeatToEsbExceedInterval() {
    	long duration = System.currentTimeMillis() - lastHeartbeatToEsbTime;
    	return	( duration >= esbHeartbeatInterval );
    }
    
    private	void	sendHeartbeatToEsb() {
    	lastHeartbeatToEsbTime = System.currentTimeMillis();
		HeartbeatToEsbReq	req = new HeartbeatToEsbReq();
		
		req.setDstESBAddr(0xE000);
		req.setSeqNum(0);
 
		session.write(req);
    }
    
    private	void	doProcessHeartbeat() {
    	if ( HB_STATE_IDLE == this.esbHeartbeatState ) {
    		if ( isHeartbeatToEsbExceedInterval() ) {
	    		esbHeartbeatState = HB_STATE_SENDED;
	    		sendHeartbeatToEsb();
    		}
    	}
    	else if ( HB_STATE_SENDED == this.esbHeartbeatState ) {
    		if ( isHeartbeatToEsbTimeout() ) {
    			if ( reHeartbeatCount < 1 ) {
	    			reHeartbeatCount++;
	        		sendHeartbeatToEsb();
    			}
    			else {
    				//	heartbeat timeout for twice, disconnect session					
    				this.session.close(true);
    			}
    		}
    	}
    	else if ( HB_STATE_RECVD == this.esbHeartbeatState ) {
    		reHeartbeatCount = 0;
    		esbHeartbeatState = HB_STATE_IDLE;
    	}
    	else {
    		throw new RuntimeException("Internal Error: Unknown ESB Heartbeat State [" 
    				+ esbHeartbeatState + "]");
    	}
    }
    
    private	boolean	isRegisterToEsbTimeout() {
    	long temp = lastRegisterToEsbRespTime > lastRegisterToEsbTime ? lastRegisterToEsbRespTime : System.currentTimeMillis();
    	long duration =  temp - lastRegisterToEsbTime;
    	return	( duration >= esbRespTimeout );
    }
    
    private	void	sendRegisterToEsb() {
    	lastRegisterToEsbTime = System.currentTimeMillis();
		RegisterToEsbReq	req = new RegisterToEsbReq();
		
		req.setDstESBAddr(0xE000);
		req.setSeqNum(0);
		
		//	鐢变簬鐜扮綉鏈変簺妯″潡涓嶆敮鎸佸彂閫佸績璺筹紝涓轰簡鍖哄垎锛屽鏋滄ā鍧楁敮鎸佸績璺筹紝
		//	鍦ㄥ彂閫佺涓�釜娉ㄥ唽娑堟伅鏃讹紝璇峰皢ESB娑堟伅澶翠腑鐨凢LAG璁剧疆涓�銆�
		req.setFlags(1);
 
		req.setModuleId(this.moduleId);
		req.setInstanceId(this.instanceId);
		
		session.write(req);
    }
    
    private	void	doCheckESB() {
    	if ( CONN_STATE_OFFLINE == esbConnectionState ) {
    		sendRegisterToEsb();
    		esbConnectionState = CONN_STATE_REGISTERING;
    	}
    	else if ( CONN_STATE_REGISTERING == esbConnectionState ) {
    		//	check registering timeout
    		if ( isRegisterToEsbTimeout() && CONN_STATE_REGISTERING == esbConnectionState) {
    			reRegisterCount++;
    			//	register to esb timeout, try re-register
        		sendRegisterToEsb();
    		}
    	}
    	else if ( CONN_STATE_ONLINE == esbConnectionState ) {
    		//	check weather to send heartbeat
    		doProcessHeartbeat();
    	}
    	else {
    		throw new RuntimeException("Internal Error: Unknown ESB Connection State [" 
    				+ esbConnectionState + "]");
    	}
    }
    
	public void start() {
    	exec.scheduleWithFixedDelay(new Runnable(){

			public void run() {
				doCheckESB();
			}}, 
			0, checkESBDelay, TimeUnit.MILLISECONDS);
    	
		doSendPending();
	}
	
	public void stop() {
		super.stop();
		
        this.exec.shutdownNow();
	}
	
	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	/* (non-Javadoc)
	 * @see com.skymobi.transport.AbstractEndpoint#messageReceived(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageReceived(IoSession session, Object msg) {
		if ( msg instanceof RegisterToEsbResp ) {
			RegisterToEsbResp resp = (RegisterToEsbResp)msg;
			if ( 0 == resp.getResult() ) {
				esbConnectionState = CONN_STATE_ONLINE;
				lastRegisterToEsbRespTime = System.currentTimeMillis();
				//	succees
				if ( logger.isDebugEnabled() ) {
					logger.debug("register to esb with moduleId:" 
							+ this.moduleId + ", instanceId:" + this.instanceId 
							+ " succeed.");
				}
			}
			else {
				logger.warn("register to esb with moduleId:" 
							+ this.moduleId + ", instanceId:" + this.instanceId 
							+ " FAILED, result:" + resp.getResult());
				esbConnectionState = CONN_STATE_OFFLINE;
				session.close(true);
			}
		}
		else if ( msg instanceof HeartbeatToEsbResp ) {
			esbHeartbeatState = HB_STATE_RECVD;
			if ( logger.isDebugEnabled() ) {
				logger.debug("recvd heartbeat to esb:" + msg);
			}
		}
		else {
			super.messageReceived(session, msg);
		}
	}

	/**
	 * @return the moduleId
	 */
	public int getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the instanceId
	 */
	public int getInstanceId() {
		return instanceId;
	}

	/**
	 * @return the fetchSignalTimeout
	 */
	public long getFetchSignalTimeout() {
		return fetchSignalTimeout;
	}

	/**
	 * @param fetchSignalTimeout the fetchSignalTimeout to set
	 */
	public void setFetchSignalTimeout(long fetchSignalTimeout) {
		this.fetchSignalTimeout = fetchSignalTimeout;
	}

	/**
	 * @return the checkESBDelay
	 */
	public long getCheckESBDelay() {
		return checkESBDelay;
	}

	/**
	 * @param checkESBDelay the checkESBDelay to set
	 */
	public void setCheckESBDelay(long checkESBDelay) {
		this.checkESBDelay = checkESBDelay;
	}

	/**
	 * @return the esbHeartbeatInterval
	 */
	public long getEsbHeartbeatInterval() {
		return esbHeartbeatInterval;
	}

	/**
	 * @param esbHeartbeatInterval the esbHeartbeatInterval to set
	 */
	public void setEsbHeartbeatInterval(long esbHeartbeatInterval) {
		this.esbHeartbeatInterval = esbHeartbeatInterval;
	}

	/**
	 * @return the esbRespTimeout
	 */
	public long getEsbRespTimeout() {
		return esbRespTimeout;
	}

	/**
	 * @param esbRespTimeout the esbRespTimeout to set
	 */
	public void setEsbRespTimeout(long esbRespTimeout) {
		this.esbRespTimeout = esbRespTimeout;
	}

	/**
	 * @return the reRegisterCount
	 */
	public int getReRegisterCount() {
		return reRegisterCount;
	}

	/**
	 * @return the esbConnectionState
	 */
	public int getEsbConnectionState() {
		return esbConnectionState;
	}

	/**
	 * @return the esbHeartbeatState
	 */
	public int getEsbHeartbeatState() {
		return esbHeartbeatState;
	}

    /**
	 * @return the esbHeartbeatTimeout
	 */
	public long getEsbHeartbeatTimeout() {
		return esbHeartbeatTimeout;
	}

	/**
	 * @param esbHeartbeatTimeout the esbHeartbeatTimeout to set
	 */
	public void setEsbHeartbeatTimeout(long esbHeartbeatTimeout) {
		this.esbHeartbeatTimeout = esbHeartbeatTimeout;
	}

}
