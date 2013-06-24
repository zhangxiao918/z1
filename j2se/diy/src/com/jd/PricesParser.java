/**
 * 
 */

package com.jd;

import com.chinamilitary.util.HttpClientUtils;
import com.utils.StringUtils;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.util.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author bluestome.zhang
 */
public class PricesParser {

    private static final String URL = "http://item.jd.com/847937.html";
    private static Map<String, String> pricesMap = new HashMap<String, String>(4);
    static final String WIN_PATH = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    static final String WIN_ID = "Windows";

    public static void parser(String url) {
        Parser parser = null;
        try {
            parser = new Parser();
            parser.setURL(url);
            parser.setEncoding("GB2312");

            NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
            NodeList list = parser.extractAllNodesThatMatch(fileter)
                    .extractAllNodesThatMatch(
                            new HasAttributeFilter("id", "summary-price"));

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    CompositeTag tag = (CompositeTag) list.elementAt(i);
                    System.out.println(tag.toPlainTextString());
                }
            }
            list = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != parser) {
                parser = null;
            }
        }
    }

    public String getPrices() {
        String value = "0";
        String body = HttpClientUtils
                .getResponseBody("http://p.3.cn/prices/get?skuid=J_847937&type=1");
        Map<String, String> map = new HashMap<String, String>(4);
        if (!StringUtils.isNull(body)) {
            int s = body.lastIndexOf("{");
            int e = body.lastIndexOf("}");
            if (s != -1 && e != -1) {
                String str = body.substring(s + 1, e);
                if (!StringUtils.isNull(str)) {
                    str = str.replace("\"", "");
                    String[] ts = str.split(",|:");
                    for (int i = 0; i < ts.length; i = i + 2) {
                        map.put(ts[i], ts[i + 1]);
                    }

                    if (map.size() > 0) {
                        Iterator<String> it = map.keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            if (key.equalsIgnoreCase("p")) {
                                value = map.get(key);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return value;

    }

    public void executorPoll() {
        final long pollTime = 2 * 60 * 1000L;
        final ScheduledExecutorService pool = Executors
                .newSingleThreadScheduledExecutor();
        pool.scheduleAtFixedRate(new Runnable() {
            public void run() {
                String prices = getPrices();
                System.err.println("价格:" + prices);
                if (!prices.equalsIgnoreCase("0") && (null == pricesMap.get(prices))) {
                    pricesMap.clear();
                    pricesMap.put(prices, prices);
                    new Thread(new Runnable() {
                        public void run() {
                            // 播放提示音
                            MediaPlayCase.play();
                            System.out.println("有新价格变动!");
                            showDocument(URL);
                        }
                    }).start();
                }
            }

        }, 100L, pollTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 判断是否为windows平台
     * 
     * @return
     */
    boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");
        if (os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;
    }

    /**
     * 打开指定的浏览器
     * 
     * @param url
     */
    public void showDocument(String url) {
        if (url == null)
            return;
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try {
            if (windows) {
                cmd = WIN_PATH + " " + url;
                Process p = Runtime.getRuntime().exec(cmd);
                p.getErrorStream();
            } else {
                System.err.println("非Windows系统");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        PricesParser prices = new PricesParser();
        prices.executorPoll();
    }

}

/**
 * 播放音频文件
 * 
 * @author Bluestome.Zhang
 */
class MediaPlayCase {
    private static Logger logger = LoggerFactory.getLogger(MediaPlayCase.class);

    static void play() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // From file
                    AudioInputStream stream = AudioSystem
                            .getAudioInputStream(new File(
                                    "D:\\notify.wav"));
                    AudioFormat format = stream.getFormat();
                    if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                        format = new AudioFormat(
                                AudioFormat.Encoding.PCM_SIGNED, format
                                        .getSampleRate(), format
                                        .getSampleSizeInBits() * 2, format
                                        .getChannels(),
                                format.getFrameSize() * 2, format
                                        .getFrameRate(), true); // big endian
                        stream = AudioSystem
                                .getAudioInputStream(format, stream);
                    }

                    // Create the clip
                    DataLine.Info info = new DataLine.Info(Clip.class, stream
                            .getFormat(),
                            ((int) stream.getFrameLength() * format
                                    .getFrameSize()));
                    Clip clip = (Clip) AudioSystem.getLine(info);

                    // 打开文件流
                    clip.open(stream);

                    // Start playing
                    clip.start();

                    // Wait for playing
                    Thread.sleep(500L);

                    // Stop playing
                    clip.stop();

                    // Reset clip
                    clip = null;
                } catch (MalformedURLException e) {
                    logger.error(e.getMessage());
                } catch (IOException e) {
                    logger.error(e.getMessage());
                } catch (LineUnavailableException e) {
                    logger.error(e.getMessage());
                } catch (UnsupportedAudioFileException e) {
                    logger.error(e.getMessage());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }).start();
    }
}
