Untitled如何把Https网站中的安全证书导入到java中的cacerts证书库中？在项目开发中,有时会遇到与SSL安全证书导入打交道的，如何把证书导入java中的cacerts证书库呢？
其实很简单，方法如下：
每一步：进入某个https://www.xxx.com开头的网站,把要导入的证书下载过来，
在该网页上右键 
>> 属性 >> 点击"证书" >>
再点击上面的"详细信息"切换栏 
>>
再点击右下角那个"复制到文件"的按钮
就会弹出一个证书导出的向导对话框，按提示一步一步完成就行了。
例如：保存为abc.cer,放在C盘下
 
第二步：如何把上面那步的(abc.cer)这个证书导入java中的cacerts证书库里?
方法如下
假设你的jdk安装在C:\jdk1.5这个目录，
开始 
>> 运行 >> 输入cmd 进入dos命令行 >>
再用cd进入到C:\jdk1.5\jre\lib\security这个目录下
敲入如下命令回车执行
       keytool -import -alias cacerts -keystore %java_home%\jre\lib\security\cacerts -file C:\abc.cer -trustcacerts  
 　　　 此时命令行会提示你输入cacerts证书库的密码，
你敲入changeit就行了，这是java中cacerts证书库的默认密码，
你自已也可以修改的。