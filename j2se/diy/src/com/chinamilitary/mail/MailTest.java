package com.chinamilitary.mail;

public class MailTest {
	public static void main(String[] args){   
        //这个类主要是设置邮件   
     MailSenderInfo mailInfo = new MailSenderInfo();    
     mailInfo.setMailServerHost("smtp.163.com");    
     mailInfo.setMailServerPort("25");    
     mailInfo.setValidate(true);    
     mailInfo.setUserName("bluestome@163.com");    
     mailInfo.setPassword("ZHANGXIAO917");//您的邮箱密码    
     mailInfo.setFromAddress("bluestome@163.com");    
     mailInfo.setToAddress("xiao_zhang@email.sky-mobi.com");    
     mailInfo.setSubject("数据库异常，请启动数据库!!");    
     mailInfo.setContent("海外天气预报数据库连接出现异常，请检查数据库是否启动等相关！！！ 从速!!!");    
        //这个类主要来发送邮件   
     SimpleMailSender sms = new SimpleMailSender();   
         sms.sendTextMail(mailInfo);//发送文体格式    
         sms.sendHtmlMail(mailInfo);//发送html格式   
   }  

}
