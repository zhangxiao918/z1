/**
 * 
 */
package com.skymobi.android.transport;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 *
 */
public class TCPAcceptor {
    
	private static final Logger logger = LoggerFactory.getLogger(TCPAcceptor.class);
	
    private int  maxRetryCount = 20;
    private long retryTimeout = 30 * 1000;   // 30s
    
    private String                  acceptIp = "0.0.0.0";
    private int                     acceptPort = 7777;
    private NioSocketAcceptor      	acceptor = new NioSocketAcceptor();
    
    private	EndpointFactory			endpointFactory = null;
    private	ProtocolCodecFactory	codecFactory = null;
    private	String					acceptorMBeanName = null;
    
    private class IOHandler extends IoHandlerAdapter {
        @Override
        public void messageReceived(IoSession session, Object msg) throws Exception {
        	Endpoint endpoint = TransportUtils.getEndpointOfSession(session);
        	if ( null != endpoint ) {
        		endpoint.messageReceived(session, msg);
        	}
        	else {
        		logger.debug("missing endpoint, ignore incoming msg:", msg);
        	}
        }
        
		@Override
        public void sessionOpened(IoSession session) throws Exception {
            logger.debug("sessionOpened: " + session);
        }
        
        @Override
		public void sessionCreated(IoSession session) throws Exception {
            //	create endpoint
    		Endpoint endpoint = endpointFactory.createEndpoint(session);
    		if ( null != endpoint ) {
    			TransportUtils.attachEndpointToSession(session, endpoint);
    		}
		}

        @Override
        public void sessionClosed(final IoSession session) throws Exception {
            //	stop endpoint
        	Endpoint endpoint = TransportUtils.getEndpointOfSession(session);
        	if ( null != endpoint ) {
        		endpoint.stop();
        	}
        }
        
        @Override
        public void exceptionCaught(IoSession session, Throwable e) throws Exception {
            logger.debug( "TCPAcceptor:", e );
        }
    }
    
	public String getAcceptIp() {
		return acceptIp;
	}

	public void setAcceptIp(String acceptIp) {
		this.acceptIp = acceptIp;
	}

	public int getAcceptPort() {
		return acceptPort;
	}

	public void setAcceptPort(int acceptPort) {
		this.acceptPort = acceptPort;
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
	
    public void start() throws IOException {
        acceptor.setReuseAddress(true);

        acceptor.setHandler(new IOHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(codecFactory));

        int retryCount = 0;
        boolean binded = false;
        do {
            try {
                acceptor.bind( new InetSocketAddress(acceptIp, acceptPort) );
                binded = true;
            }
            catch ( BindException e ) {
                logger.debug("start failed : " + e + ", and retry...");
                retryCount++;
                if ( retryCount >= maxRetryCount ) {
                    throw e;
                }
                try {
                    Thread.sleep(retryTimeout);
                } catch (InterruptedException e1) {
                }
            }
            catch (IOException e) {
                throw e;
            }
        } while ( !binded );

        logger.debug("start succeed in " + acceptIp + ":" + acceptPort);
        
    }
    
    public void stop(){
        this.acceptor.dispose();
    }

	public int getMaxRetryCount() {
		return maxRetryCount;
	}

	public void setMaxRetryCount(int maxRetryCount) {
		this.maxRetryCount = maxRetryCount;
	}

	public long getRetryTimeout() {
		return retryTimeout;
	}

	public void setRetryTimeout(long retryTimeout) {
		this.retryTimeout = retryTimeout;
	}

	/**
	 * @return the acceptorMBeanName
	 */
	public String getAcceptorMBeanName() {
		return acceptorMBeanName;
	}

	/**
	 * @param acceptorMBeanName the acceptorMBeanName to set
	 */
	public void setAcceptorMBeanName(String acceptorMBeanName) {
		this.acceptorMBeanName = acceptorMBeanName;
	}

	public IoServiceStatistics getStatistics() {
		return	acceptor.getStatistics();
	}

}
