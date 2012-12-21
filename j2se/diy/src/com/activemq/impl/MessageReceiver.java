package com.activemq.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.ObjectMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.support.JmsGatewaySupport;

import com.chinamilitary.bean.ArticleDoc;
import com.chinamilitary.dao.ArticleDocDao;
import com.chinamilitary.factory.DAOFactory;

public class MessageReceiver extends JmsGatewaySupport {
	
	static Log logger = LogFactory.getLog(MessageReceiver.class);
	ArticleDocDao articleDocDao = DAOFactory.getInstance().getArticleDocDao();	
	
	public int receiverMsg(){
		int result = -1;
		Message  msg = this.getJmsTemplate().receive();
		if(msg instanceof TextMessage){
			logger.debug(" >> TextMessage");
			//文本消息
			result = receiverTextMsg(msg);
		}
		
		if(msg instanceof ObjectMessage){
			logger.debug(" >> ObjectMessage");
			//对象消息
			result = receiverObjectMsg(msg);
		}
		return result;
	}
	
	public int receiverTextMsg(Message msg) {
		int result = 1;
		TextMessage textMsg = (TextMessage) msg;

		try {
			if(null == textMsg){
				return -1;
			}
			// 消息 header 中常有的 属性定义
			System.out.println("消息编码：" + textMsg.getJMSMessageID());
			System.out.println("目标对象：" + textMsg.getJMSDestination());
			System.out.println("消息模式：" + textMsg.getJMSDeliveryMode()); // 消息的模式
			// 分为持久模式和非持久模式,
			// 默认是
			// 非持久的模式（2）

			long sendTime = textMsg.getJMSTimestamp();
			Date date = new Date(sendTime);
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String temp = f.format(date);

			System.out.println("消息发送时间：" + temp);
			System.out.println("消息失效时间：" + textMsg.getJMSExpiration()); // 这里是一个
			// 整型值 0
			// 表示
			// 该消息永远不会过期
			System.out.println("消息优先级：" + textMsg.getJMSPriority()); // 优先级
			// 0~9,
			// 0
			// 表示最低
			System.out.println("关联编码：" + textMsg.getJMSCorrelationID());

			System.out.println("回复消息的地址：" + textMsg.getJMSReplyTo()); // 回复消息的地址(Destination类型),由发送者设定
			System.out.println("消息类型：" + textMsg.getJMSType()); // jms 不使用该字段，
			// 一般类型是由 用户自己定义
			System.out.println("是否签收过：" + textMsg.getJMSRedelivered()); // 如果是 真
			// ,表示客户端收到过该消息,但是并没有签收

			// 消息属性 (properties)
			System.out.println("用户编码："
					+ textMsg.getStringProperty("JMSXUserID"));
			System.out.println("应用程序编码："
					+ textMsg.getStringProperty("JMSXApp1ID"));
			System.out.println("已经尝试发送消息的次数："
					+ textMsg.getStringProperty("JMSXDeliveryCount"));

			// 消息体(body) 中传递的内容
			System.out.println("消息内容：" + textMsg.getText());

		}catch(JMSException exception){
			logger.error(" >> receiverObjectMsg.JMSException:[{}]",exception);
			result = -1;
		}catch(Exception e){
			logger.error(" >> receiverObjectMsg.Exception:[{}]",e);
			result = -1;
		}
		return result;
	}
	
	/**
	 * 接收对象消息
	 *
	 */
	public int receiverObjectMsg(Message msg){
		int result = 1;
		ObjectMessage objMsg = (ObjectMessage)msg;
		try{
			if(null == objMsg){
				return -1;
			}
			// 消息 header 中常有的 属性定义
//			logger.info("消息编码：" + objMsg.getJMSMessageID());
//			logger.info("目标对象：" + objMsg.getJMSDestination());
//			logger.info("消息类型：" + objMsg.getJMSType());
//			logger.info("消息模式：" + objMsg.getJMSDeliveryMode()); // 消息的模式
			
			if(objMsg.getObject() instanceof ArticleDoc){
				ArticleDoc doc = (ArticleDoc)objMsg.getObject();
				int id = articleDocDao.insert(doc);
				logger.info(" >> doc.url:"+doc.getUrl()+"|"+id);
			}

			//其他类型的对象
		}catch(JMSException exception){
			logger.error(" >> receiverObjectMsg.JMSException:[{}]",exception);
			result = -1;
		}catch(Exception e){
			logger.error(" >> receiverObjectMsg.Exception:[{}]",e);
			result = -1;
		}
		return result;
	}
}
