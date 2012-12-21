package com.activemq.impl;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.support.JmsGatewaySupport;

public class MessageSender extends JmsGatewaySupport {
	
	/**
	 * 发送文本消息
	 * @param msg
	 */
	public void sendTextMsg(final String msg) {
		this.getJmsTemplate().send(new MessageCreator() {
			// 这里创建了一个 message 对象,然后可以对该对象进行 各种属性的定义
			private Message message;

			public Message createMessage(Session session) throws JMSException {
				message = session.createTextMessage(msg);

				message.setStringProperty("JMSXUserID", "123456789"); // 消息所属的用户编码
				message.setStringProperty("JMSXApp1ID", "001002"); // 消息所属的应用程序编码

				return message;
			}
		});
	}
	
	/**
	 * 发送一个序列化对象
	 * @param serial 序列化对象
	 */
	public void sendObjectMsg(final Serializable serial){
		if(null != serial){
			this.getJmsTemplate().send(new MessageCreator() {
				// 这里创建了一个 message 对象,然后可以对该对象进行 各种属性的定义
				private Message message;
	
				public Message createMessage(Session session) throws JMSException {
					message = session.createObjectMessage(serial);
					message.setJMSType("JMS_SERIALIZABLE_OBJECT");
					message.setJMSTimestamp(System.currentTimeMillis());
					return message;
				}
			});
		}
	}
}
