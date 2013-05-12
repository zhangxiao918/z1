
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
import java.net.UnknownHostException;
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

    private AtomicInteger id = new AtomicInteger(0);
    private AtomicInteger insertCount = new AtomicInteger(1);
    private AtomicInteger deleteCount = new AtomicInteger(1);

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
                        if (sid <= 0) {
                            continue;
                        }
                        bean = dao.findById(sid);
                        if (null != bean) {
                            if (bean.getStatus() != 1 && bean.getFileSize() == 0L) {
                                try {
                                    String len = HttpClientUtils.getHttpConentLength(
                                            bean.getHttpUrl(),
                                            bean.getHttpUrl(), bean.getHttpUrl());
                                    if (null != len && Integer.valueOf(len) > 0) {
                                        bean.setFileSize(Long.valueOf(len));
                                        bean.setStatus(1);
                                        bean.setLink("FD");
                                        dao.update(bean);
                                    } else {
                                        // TODO SOCKET TIMEOUT EXCEPTION时的处理逻辑

                                        // TODO 删除Article
                                        if (aDao.delete(bean.getArticleId())) {
                                            System.err.println("删除文章:" +
                                                    bean.getArticleId());
                                        }
                                        // TODO 删除Image
                                        if (dao.delete(bean.getId())) {
                                            System.err.println("删除图片:" +
                                                    bean.getId());
                                        }
                                    }
                                    System.out.println("文件[" + bean.getHttpUrl() + "]\t长度为：" + len
                                            + ",状态:"
                                            + bean.getStatus());
                                } catch (Exception e) {
                                    if (e instanceof UnknownHostException) {
                                        // TODO 删除Article
                                        if (aDao.delete(bean.getArticleId())) {
                                            System.err.println("删除文章:" +
                                                    bean.getArticleId());
                                        }
                                        // TODO 删除Image
                                        if (dao.delete(bean.getId())) {
                                            System.err.println("删除图片:" +
                                                    bean.getId());
                                        }
                                    }
                                }
                            } else if (bean.getStatus() != 1 && bean.getFileSize() > 200L) {
                                // TODO 更新文件大小
                                bean.setStatus(1);
                                bean.setLink("FD");
                                if (dao.update(bean)) {
                                    System.out.println("更新记录[" + bean.getId() + "]为状态1");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != bean && e instanceof UnknownHostException) {
                            try {
                                // TODO 删除Article
                                if (aDao.delete(bean.getArticleId())) {
                                    System.err.println("删除文章:" +
                                            bean.getArticleId());
                                }
                                // TODO 删除Image
                                if (dao.delete(bean.getId())) {
                                    System.err.println("删除图片:" +
                                            bean.getId());
                                }
                            } catch (SQLException e1) {
                                System.err.println(e1);
                            }
                        }
                    } finally {
                        if (null != bean) {
                            bean = null;
                        }
                    }
                }
            }
        }).start();
    }

    private void processImageUrl() {

        final ImageDao dao = DAOFactory.getInstance().getImageDao();
        long s = System.currentTimeMillis();
        try {
            id.set(dao.getMin(-3));
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            System.out.println("获取最小值耗时:" + (System.currentTimeMillis() - s) + " ms");
        }
        System.out.println("初始ID值为:" + id.get());
        new Thread(new Runnable() {
            ImageBean bean = null;

            public synchronized void run() {
                while (true) {
                    int sid = id.getAndIncrement();
                    try {
                        bean = dao.findById(sid);
                        if (null != bean) {

                            // http://tuku.military.china.com
                            if (bean.getImgUrl().startsWith("http://tuku.military.china.com")) {
                                String url = bean.getImgUrl();
                                url = url.replace("http://tuku.military.china.com",
                                        "http://image.tuku.china.com/tuku.military.china.com");
                                bean.setImgUrl(url);
                            }
                            if (bean.getHttpUrl().startsWith("http://tuku.military.china.com")) {
                                String url = bean.getHttpUrl();
                                url = url.replace("http://tuku.military.china.com",
                                        "http://image.tuku.china.com/tuku.military.china.com");
                                bean.setHttpUrl(url);
                            }

                            // http://pic.news.china.com/
                            if (bean.getImgUrl().startsWith("http://pic.news.china.com/")) {
                                String url = bean.getImgUrl();
                                url = url.replace("http://pic.news.china.com",
                                        "http://image.tuku.china.com/pic.news.china.com");
                                bean.setImgUrl(url);
                            }
                            if (bean.getHttpUrl().startsWith("http://pic.news.china.com/")) {
                                String url = bean.getHttpUrl();
                                url = url.replace("http://pic.news.china.com",
                                        "http://image.tuku.china.com/pic.news.china.com");
                                bean.setHttpUrl(url);
                            }

                            // http://tuku.fun.china.com/
                            if (bean.getImgUrl().startsWith("http://tuku.fun.china.com/")) {
                                String url = bean.getImgUrl();
                                url = url.replace("http://tuku.fun.china.com",
                                        "http://image.tuku.china.com/tuku.fun.china.com");
                                bean.setImgUrl(url);
                            }
                            if (bean.getHttpUrl().startsWith("http://tuku.fun.china.com/")) {
                                String url = bean.getHttpUrl();
                                url = url.replace("http://tuku.fun.china.com",
                                        "http://image.tuku.china.com/tuku.fun.china.com");
                                bean.setHttpUrl(url);
                            }

                            // http://tuku.news.china.com/
                            if (bean.getImgUrl().startsWith("http://tuku.news.china.com/")) {
                                String url = bean.getImgUrl();
                                url = url.replace("http://tuku.news.china.com",
                                        "http://image.tuku.china.com/tuku.news.china.com");
                                bean.setImgUrl(url);
                            }
                            if (bean.getHttpUrl().startsWith("http://tuku.news.china.com/")) {
                                String url = bean.getHttpUrl();
                                url = url.replace("http://tuku.news.china.com",
                                        "http://image.tuku.china.com/tuku.news.china.com");
                                bean.setHttpUrl(url);
                            }

                            if (bean.getStatus() != 1 || bean.getFileSize() < 0L) {
                                String len = HttpClientUtils.getHttpConentLength(bean.getHttpUrl(),
                                        bean.getHttpUrl(), bean.getHttpUrl());
                                if (null != len && Integer.valueOf(len) > 0) {
                                    bean.setFileSize(Long.valueOf(len));
                                    bean.setStatus(1);
                                    bean.setLink("FD");
                                    dao.update(bean);
                                }
                                System.out.println("文件[" + bean.getHttpUrl() + "]\t缩略图:["
                                        + bean.getImgUrl() + "]\t长度为：" + len
                                        + ",状态:"
                                        + bean.getStatus());
                            } else if (bean.getStatus() != 1 && bean.getFileSize() > 200L) {
                                // TODO 更新文件大小
                                bean.setStatus(1);
                                bean.setLink("FD");
                                if (dao.update(bean)) {
                                    System.out.println("更新记录[" + bean.getId() + "]为状态1");
                                }
                            }
                        } else {
                            System.out.println("bean is null " + sid);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // if (sid > 0) {
                        // try {
                        // dao.delete(sid);
                        // System.out.println("删除记录成功");
                        // } catch (SQLException e1) {
                        // e1.printStackTrace();
                        // }
                        // }
                    } finally {
                        if (null != bean) {
                            bean = null;
                        }
                    }
                }
            }
        }).start();
    }

    private void processSeparateTable() {

        final ImageDao dao = DAOFactory.getInstance().getImageDao();
        final ArticleDao aDao = DAOFactory.getInstance().getArticleDao();
        id.set(1);
        System.out.println("初始ID值为:" + id.get());
        new Thread(new Runnable() {
            Article bean = null;

            public synchronized void run() {
                while (true) {
                    int sid = id.getAndIncrement();
                    try {
                        bean = aDao.findById(sid);
                        if (null != bean) {
                            List<ImageBean> list = dao.findImageByArticleId(sid);
                            if (null != list && list.size() > 0) {
                                int[] ids = dao.insertBatch(bean.getId(), list);
                                int c = 0;
                                for (int id : ids) {
                                    if (id > 0) {
                                        insertCount.getAndIncrement();
                                        c++;
                                    }
                                }
                                System.out.println("文章ID" + bean.getId() + ":图片数量:" + list.size()
                                        + ",添加了" + c + "条记录");
                                // TODO 删除图片表原始图片
                                if (c == list.size()) {
                                    if (dao.deleteBatch(list)) {
                                        final int size = list.size();
                                        new Thread(new Runnable() {
                                            public void run() {
                                                for (int i = 0; i < size; i++) {
                                                    deleteCount.getAndIncrement();
                                                }
                                                int d = deleteCount.get();
                                                if (d % 1000 == 0) {
                                                    System.out.println("删除了" + d + "条记录");
                                                }
                                            }
                                        }).start();
                                    }
                                }
                                c = insertCount.getAndIncrement();
                                if (c % 1000 == 0) {
                                    System.out.println("添加了" + c + "条记录");
                                }
                                // TODO
                                // for (ImageBean ib : list) {
                                // int iid = dao.insert(ib);
                                // if (iid > 0) {
                                // int c = insertCount.getAndIncrement();
                                // if (c % 1000 == 0) {
                                // System.out.println("添加了" + c + "条记录");
                                // }
                                // if (!dao.delete(ib.getId())) {
                                // System.err.println("删除图片【" + ib.getId() +
                                // "】失败");
                                // } else {
                                // int d = deleteCount.getAndIncrement();
                                // if (d % 1000 == 0) {
                                // System.out.println("删除了" + d + "条记录");
                                // }
                                // }
                                // }
                                // }
                            } else {
                                System.out.println(sid + "|" + bean.getTitle() + "|没有数据");
                            }
                        } else {
                            System.out.println("bean is null " + sid);
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
        // dp.processImageStatus();
        // dp.processImageUrl();
        dp.processSeparateTable();
    }
}
