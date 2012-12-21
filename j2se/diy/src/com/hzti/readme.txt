构造请求体时，服务端响应如下错误码:
16|error|500|此页的状态信息无效，可能已损坏。|
原因不明。等待查找。

区别，在提交的验证内容中，有如下差异：
1.__VIEWSTATE的值中，
	a.有空格没有转义为%20
	b.斜杠“/”没有转义为"%2F"

最终发现问题原因是__VIEWSTATE的值有问题。	


错误码:
15|error|500|未将对象引用设置到对象的实例。|

出现原因：在请求的Cookie中的【ASP.NET_SessionId】值过期，需要重新获取Cookie中的内容
connection.addRequestProperty("Cookie", "ASP.NET_SessionId=wizsvm45dfjb5bvr414ppy45; isLoginedWeb=T");

解决方法：
当前：手动抓包，从请求头中将Cookie中的值获取到即可。
长远：需要理解该值从哪可以自动获取，由程序完成该值的赋值。


如何获取Cookie中的ASP.NET_SessionId内容