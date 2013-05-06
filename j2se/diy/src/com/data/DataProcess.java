
package com.data;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DataProcess {

    String TAG = DataProcess.class.getSimpleName();

    Logger logger = LoggerFactory.getLogger(DataProcess.class);

    MemcacheClient client = MemcacheClient.getInstance();

    BlockingQueue<Integer> blockQueue = new LinkedBlockingQueue<Integer>(1000);

    BlockingQueue<Integer> backupQueue = new LinkedBlockingQueue<Integer>(1000);

    private AtomicInteger id = new AtomicInteger(23671);

    private static DataProcess instance = null;

    public static DataProcess getInstance() {
        if (null == instance)
            instance = new DataProcess();
        return instance;
    }

    private void patchImageFileSize() {
        ImageDao imageDao = DAOFactory.getInstance().getImageDao();
        ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();
        List<ImageBean> list = null;
        do {
            try {
                list = imageDao.findImageByFilesizeEqualX(0, 0, 200);
                for (ImageBean b : list) {
                    String key = genKey(b.getArticleId());
                    String aUrl = (String) client.get(key);
                    if (null == aUrl) {
                        System.err.println(genKey(b.getArticleId()) + "\t在缓存中没有记录");
                        Article a = articleDao.findById(b.getArticleId());
                        if (null == a) {
                            continue;
                        }
                        key = genKey(a.getId());
                        if (null == client.get(key)) {
                            if (client.add(key, a.getArticleUrl())) {
                                System.out.println("添加\t" + key + "\t成功!");
                                aUrl = a.getArticleUrl();
                            }
                        }
                    }
                    // 临时过滤掉china.com的图片大小，因为该网站图片比较多都是返回-1,为了不耽误其他图片的处理时间，先将china.com的数据搁置
                    if (null != b.getHttpUrl()
                            && b.getHttpUrl().toLowerCase().contains("china.com/")) {
                        System.out.println("\t不处理该站点[china.com]图片:" + b.getHttpUrl());
                        continue;
                    }
                    if (null == aUrl)
                        continue;
                    String len = HttpClientUtils.getHttpConentLength(aUrl, null, b.getHttpUrl());
                    if (null != len && !len.equals("0") && b.getStatus() != -3) {
                        int length = Integer.valueOf(len);
                        if (length > 0) {
                            b.setFileSize(Long.valueOf(length));
                            if (imageDao.update(b)) {
                                logger.info("S|" + b.getId() + "|" + b.getArticleId()
                                        + "|" + b.getHttpUrl() + "|" + length + "");
                            }
                        }
                    } else {
                        if (blockQueue.size() < 1000) {
                            blockQueue.add(b.getArticleId());
                        } else {
                            System.err.println(TAG + "\t*********向备用队列中添加数据[" + b.getArticleId()
                                    + "]*********");
                            backupQueue.add(b.getArticleId());
                        }
                        b.setFileSize(-1L);
                        // 经过处理发现图片大小获取不到
                        b.setStatus(-3);
                        imageDao.update(b);
                        logger.info("F|" + b.getId() + "|" + b.getArticleId() + "|"
                                + b.getHttpUrl() + "|" + b.getStatus() + "|" + b.getFileSize());
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } while (null != list && list.size() > 0);
    }

    private void initArticle2Cache() {
        WebSiteDao wesiteDao = DAOFactory.getInstance().getWebSiteDao();
        ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();
        List<WebsiteBean> list = null;
        try {
            list = wesiteDao.findAll();
            for (WebsiteBean b : list) {
                List<Article> al = articleDao.findByWebId(b.getId());
                for (Article a : al) {
                    if (!a.getText().equals("EOF")) {
                        String key = genKey(a.getId());
                        if (null == client.get(key)) {
                            if (client.add(key, a.getArticleUrl())) {
                                System.out.println("添加\t" + key + "\t成功!");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查文章站点是否可用
     */
    private void patchArticleAvailable() {
        WebSiteDao wesiteDao = DAOFactory.getInstance().getWebSiteDao();
        ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();
        List<WebsiteBean> list = null;
        try {
            list = wesiteDao.findAll();
            for (WebsiteBean b : list) {
                List<Article> al = articleDao.findByWebId(b.getId());
                for (Article a : al) {
                    if (!HttpClientUtils.urlValidation(a.getArticleUrl())) {
                        a.setText("EOF");
                        if (articleDao.update(a)) {
                            System.err.println("更新当前文章[" + a.getId() + "|" + a.getArticleUrl()
                                    + "]为不可用状态[EOF]");
                        }
                    } else {
                        System.out.println(a.getArticleUrl() + "\t可用");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String genKey(Object key) {
        return TAG + File.separator + "Article" + File.separator + key;
    }

    public void process() {
        final ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();
        new Thread(new Runnable() {
            public synchronized void run() {
                while (true) {
                    Integer aid = null;
                    try {

                        aid = blockQueue.poll(100L, TimeUnit.MILLISECONDS);
                        if (null != aid) {
                            // 从备用队列中获取数据
                            aid = backupQueue.poll(100L, TimeUnit.MILLISECONDS);
                        }
                        if (null != aid && aid > 0) {
                            String key = genKey(aid);
                            String aUrl = (String) client.get(key);
                            // TODO 检查网站是否可用，如果不可用，则更新为EOF状态
                            if (!HttpClientUtils.urlValidation(aUrl)) {
                                System.err.println("\t aid:" + aid + "|" + aUrl + "\t访问失败!");
                                try {
                                    Article a = articleDao.findById(aid);
                                    if (null == a)
                                        return;
                                    if (a.getText().equals("EOF")) {
                                        return;
                                    }
                                    a.setText("EOF");
                                    if (articleDao.update(a)) {
                                        System.out.println("更新当前文章[" + a.getId() + "|"
                                                + a.getArticleUrl() + "]为不可用状态[EOF]");
                                    }
                                } catch (Exception e) {
                                    System.err.println(TAG + "\t" + e.getMessage());
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        // patchImageFileSize();
    }

    private void processImageStatus() {

        final ImageDao dao = DAOFactory.getInstance().getImageDao();
        final ArticleDao aDao = DAOFactory.getInstance().getArticleDao();
        try {
            id.set(dao.getMin(-1));
        } catch (SQLException e1) {
        }
        System.out.println("初始ID值为:" + id.get());
        new Thread(new Runnable() {
            ImageBean bean = null;

            public synchronized void run() {
                while (true) {
                    try {
                        int sid = id.getAndIncrement();
                        bean = dao.findById(sid);
                        if (null != bean) {
                            if (bean.getStatus() != 1 && bean.getFileSize() == 0L) {
                                String len = HttpClientUtils.getHttpConentLength(bean.getHttpUrl(),
                                        bean.getHttpUrl(), bean.getHttpUrl());
                                if (null != len && Integer.valueOf(len) > 0) {
                                    System.out.println("文件[" + bean.getHttpUrl() + "]长度为：" + len);
                                    bean.setFileSize(Long.valueOf(len));
                                    bean.setStatus(1);
                                    dao.update(bean);
                                } else {
                                    // TODO SOCKET TIMEOUT EXCEPTION时的处理逻辑

                                    // TODO 删除Article
                                    if (aDao.delete(bean.getArticleId())) {
                                        System.err.println("删除文章:" + bean.getArticleId());
                                    }

                                    // TODO 删除Image
                                    if (dao.delete(bean.getId())) {
                                        System.err.println("删除图片:" + bean.getId());
                                    }
                                }
                                System.out.println(bean.getArticleId() + "" + bean.getTitle() + "|"
                                        + bean.getHttpUrl() + "|"
                                        + bean.getStatus());

                            } else if (bean.getStatus() != 1 && bean.getFileSize() > 200L) {
                                // TODO 更新文件大小
                                bean.setStatus(1);
                                if (dao.update(bean)) {
                                    System.out.println("更新记录[" + bean.getId() + "]为状态1");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (null != bean) {
                            bean = null;
                        }
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        DataProcess dp = new DataProcess();
        dp.processImageStatus();
    }

}
