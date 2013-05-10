/**
 * 
 */

package com;

/**
 * @author bluestome.zhang
 */
public class B {

    private static String createTables(String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE `").append(tableName).append("` (").append("\n");
        sb.append("\t`d_id` INT(4) NOT NULL,").append("\n");
        sb.append("\t`d_article_id` INT(4) NOT NULL,").append("\n");
        sb.append("\t`d_title` VARCHAR(128) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_name` VARCHAR(128) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_imgurl` VARCHAR(256) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_httpurl` VARCHAR(256) NOT NULL,").append("\n");
        sb.append("\t`d_orderid` INT(4) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_time` VARCHAR(16) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_intro` VARCHAR(1024) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_commentsuburl` VARCHAR(1024) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_commentshowurl` VARCHAR(1024) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_link` VARCHAR(256) NULL DEFAULT NULL,").append("\n");
        sb.append("\t`d_createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,").append("\n");
        sb.append("\t`d_status` INT(4) NULL DEFAULT '3',").append("\n");
        sb.append("\t`d_filesize` INT(4) NULL DEFAULT '0',").append("\n");
        sb.append("\tPRIMARY KEY (`d_id`),").append("\n");
        sb.append("\tINDEX `idx_").append(tableName).append("_article_id` (`d_article_id`)")
                .append("\n");
        sb.append(")").append("\n");
        sb.append("COLLATE='utf8_general_ci'").append("\n");
        sb.append("ENGINE=InnoDB;").append("\n");
        return sb.toString();
    }

    private static String dropTables(String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append("drop table ").append(tableName).append(";");
        return sb.toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        StringBuffer sql = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            String tableName = "tbl_image_" + i;
            sql.append(dropTables(tableName));
            sql.append("\r\n");
        }
        System.out.println(sql.toString());
        System.out.println(3250 % 8);
    }
}
