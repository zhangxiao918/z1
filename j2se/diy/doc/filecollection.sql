
DROP DATABASE IF EXISTS filecollection;
CREATE DATABASE IF NOT EXISTS filecollection default charset utf8 COLLATE utf8_general_ci;
USE filecollection;

drop table if exists `tbl_web_site`;
create table `tbl_web_site`(
	`d_id` int(4) not null auto_increment,
	`d_parent_id` int(4) default 0,
	`d_web_url` varchar(256) not null,
	`d_web_name` varchar(30) not null,
	`d_createtime` timestamp default current_timestamp,
	primary key(d_id)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8;

#文章表
drop table if exists `tbl_article`;
create table `tbl_article`(
	`d_id` int(4) not null auto_increment,
	`d_web_id` int(4) not null,
	`d_acticle_url` varchar(256) not null,
	`d_title` varchar(100),
	`d_text` varchar(300),
	`d_createtime` timestamp default current_timestamp,
	FOREIGN KEY (`d_web_id`) REFERENCES tbl_web_site(`d_id`) ON DELETE CASCADE ON UPDATE CASCADE,
	primary key(`d_id`)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8;

#图片列表
drop table if exists `tbl_image`;
create table `tbl_image`(
	`d_id` int(4) not null auto_increment,
	`d_title` varchar(128),
	`d_name` varchar(128),
	`d_imgurl` varchar(256),
	`d_httpurl` varchar(256),
	`d_orderid` int(4) not null,
	`d_time` varchar(16),
	`d_intro` varchar(1024),
	`d_commentsuburl` varchar(1024),
	`d_commentshowurl` varchar(1024),
	`d_link` varchar(256),
	`d_createtime` timestamp default current_timestamp,
	primary key(`d_id`)
	
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8;

#文件表[图片]
drop table if exists `tbl_pic_file`;
create table `tbl_pic_file`(
	`d_id` int(4) not null auto_increment,
	`d_article_id` int(4) not null,
	`d_image_id` int(4) not null,
	`d_file_url` varchar(256) not null,
	`d_file_title` varchar(200) not null,
	`d_file_name` varchar(100),
	`d_createtime` timestamp default current_timestamp,
	FOREIGN KEY (`d_article_id`) REFERENCES tbl_article(`d_id`) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`d_image_id`) REFERENCES tbl_image(`d_id`) ON DELETE CASCADE ON UPDATE CASCADE,
	primary key(`d_id`)
)
ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=UTF8;