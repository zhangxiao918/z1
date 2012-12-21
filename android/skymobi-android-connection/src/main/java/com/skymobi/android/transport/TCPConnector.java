/**
 * 
 */
package com.skymobi.android.transport;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 *
 */
public class TCPConnector {
	private static final Logger logger = LoggerFactory.getLogger(TCPConnector.class);
    private ScheduledExecutorService exec = 
        Executors.newSingleThreadScheduledExecutor();
    
    private String					name = "connector";
    private String      			destIp = null;
    private int         			destPort = -1;
    private IoConnector				connector = null;
    private	EndpointFactory			endpointFactory = null;
    private	ProtocolCodecFactory	codecFactory = null;
    private	long					retryTimeout = 1000;
    
	private String getDesc() {
    	return	"[" + name + "]";
    }
    
    private class IOHandler extends IoHandlerAdapter {
        @Override
        public void messageReceived(IoSession session, Object msg) throws Exception {
        	Endpoint endpoint = TransportUtils.getEndpointOfSession(session);
        	if ( null != endpoint ) {
        		endpoint.messageReceived(session, msg);
        	}
        	else {
        		logger.debug(getDesc()+" missing endpoint, ignore incoming msg:", msg);
        	}
        }
        
		@Override
        public void sessionOpened(IoSession session) throws Exception {
			logger.debug( " sessionOpened:", session);
        }
        
        @Override
		public void sessionCreated(IoSession session) throws Exception {
        	
        	try {
	            //	create endpoint
	    		Endpoint endpoint = endpointFactory.createEndpoint(session);
	    		if ( null != endpoint ) {
	    			TransportUtils.attachEndpointToSession(session, endpoint);
	    		}
        	}
        	catch (Exception e) {
                logger.debug( " createEndpoint:", e );
                session.close(true);
        	}
		}

        @Override
        public void sessionClosed(final IoSession session) throws Exception {
            //	stop endpoint
        	Endpoint endpoint = TransportUtils.getEndpointOfSession(session);
        	if ( null != endpoint ) {
        		endpoint.stop();
        	}
        	exec.submit(new Runnable() {

				public void run() {
					onSessionClosed(session);
				}});
        	logger.debug(" > ip:"+destIp+"| port:"+destPort+" is close!");
        }
        
        @Override
        public void exceptionCaught(IoSession session, Throwable e) throws Exception {
            logger.debug( getDesc() + " TCPConnector:", e );
        }
    }
    
	private void doConnect() {
		if ( null == destIp || destIp.equals("") ) {
			logger.debug(getDesc()+" destIp is null, disable this connector.");
			return;
		}
		
        ConnectFuture connectFuture = 
        	connector.connect(new InetSocketAddress(destIp, destPort));
        
        connectFuture.addListener(new IoFutureListener<ConnectFuture>(){

            public void operationComplete(final ConnectFuture connectFuture) {
                exec.submit( new Runnable(){

                    public void run() {
                    	onConnectComplete(connectFuture);
                    }} );
            }
        });
	}
	
    private	void onConnectComplete(ConnectFuture connectFuture) {
    	if ( !connectFuture.isConnected() ) {
			logger.debug(getDesc() + " connect [" + this.destIp +":"+ this.destPort + "] failed, retry...");
    		exec.schedule(new Runnable(){

				public void run() {
		    		doConnect();
				}}, 
				retryTimeout, TimeUnit.MILLISECONDS);
    	}
    }
    
    private void onSessionClosed(IoSession session) {
		logger.debug(getDesc() + " session : " + session + "closed, retry connect...");
		exec.schedule(new Runnable(){

			public void run() {
	    		doConnect();
			}}, 
			retryTimeout, TimeUnit.MILLISECONDS);
    }
    
    public String getDestIp() {
		return destIp;
	}

	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}

	public int getDestPort() {
		return destPort;
	}

	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}

	public ProtocolCodecFactory getCodecFactory() {
		return codecFactory;
	}

	public void setCodecFactory(ProtocolCodecFactory codecFactory) {
		this.codecFactory = codecFactory;
	}

	public EndpointFactory getEndpointFactory() {
		return endpointFactory;
	}

	public void setEndpointFactory(EndpointFactory endpointFactory) {
		this.endpointFactory = endpointFactory;
	}
	
	public TCPConnector(String name,IoConnector connector) {
		this.name = name;
        this.connector = connector;
        this.connector.setHandler(new IOHandler() );
    }
	
	public TCPConnector(String name) {
		this.name = name;
        this.connector = new NioSocketConnector();
        this.connector.setHandler(new IOHandler() );
    }
	
    public void start() {
    	if(null != codecFactory){
	    	connector.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(codecFactory));
    	}
		doConnect();
    }

	public void stop()  {
		this.exec.shutdownNow();
		this.connector.dispose();
	}

	public long getRetryTimeout() {
		return retryTimeout;
	}

	public void setRetryTimeout(long retryTimeout) {
		this.retryTimeout = retryTimeout;
	}
	
	public boolean isConnected() {
		return	connector.isActive();
	}
	
	public IoServiceStatistics getStatistics() {
		return	connector.getStatistics();
	}

}
