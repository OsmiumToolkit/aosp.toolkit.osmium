package aosp.toolkit.perseus.base;

/*
 * @File:   GetMIUIThemeLinkJavaSample
 * @Author: 1552980358
 * @Time:   9:43 PM
 * @Date:   15 May 2019
 *
 */

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@SuppressWarnings("unused")
public class GetMIUIThemeLinkJavaSample extends Thread {

    // 版本后缀
    private static String[] VERSION = {
        "?capability=w%2Cb%2Cs%2Cm%2Ch5%2Cv%3A8%2Cvw&miuiUIVersion=V4\0",   //V4
        "?capability=w%2Cb%2Cs%2Cm%2Ch5%2Cv%3A8%2Cvw&miuiUIVersion=V5\0",   //V5
        "?capability=w%2Cb%2Cs%2Cm%2Ch5%2Cv%3A8%2Cvw&miuiUIVersion=V6\0",   //V6
        "?capability=w%2Cb%2Cs%2Cm%2Ch5%2Cv%3A8%2Cvw&miuiUIVersion=V7\0",   //V7
        "?capability=w%2Cb%2Cs%2Cm%2Ch5%2Cv%3A8%2Cvw&miuiUIVersion=V8\0",   //V8
        "?capability=w%2Cb%2Cs%2Cm%2Ch5%2Cv%3A8%2Cvw&miuiUIVersion=V9\0",   //V9
        "?capability=w%2Cb%2Cs%2Cm%2Ch5%2Cv%3A8%2Cvw&miuiUIVersion=V10\0"   //V10
    };

    private String url;
    private String version;
    private LinkFetchingListener linkFetchingListener;
    private String link;

    @SuppressWarnings("unused")
    public GetMIUIThemeLinkJavaSample(String url, int version) {
        this.url = url;
        this.version = VERSION[version];
    }

    @SuppressWarnings("unused")
    public GetMIUIThemeLinkJavaSample(String url, int version, LinkFetchingListener linkFetchingListener) {
        this.url = url;
        this.version = VERSION[version];
        this.linkFetchingListener = linkFetchingListener;
    }

    public GetMIUIThemeLinkJavaSample setVersion(String version) {
        this.version = version;
        return this;
    }

    @SuppressWarnings("unused")
    public GetMIUIThemeLinkJavaSample() {

    }


    public GetMIUIThemeLinkJavaSample setUrl(String url) {
        this.url = url;
        return this;
    }

    @SuppressWarnings("unused")
    public GetMIUIThemeLinkJavaSample setLinkFetchingListener(LinkFetchingListener linkFetchingListener) {
        this.linkFetchingListener = linkFetchingListener;
        return this;
    }



    interface LinkFetchingListener {
        void ProcessFail(Exception e);

        void ProcessSucceed(String link);
    }

    @Override
    public void run() {
        super.run();
        try {

            String u = url.contains("http://zhuti.xiaomi.com/detail/") ?
                           url.replace("http://zhuti.xiaomi.com/detail/",
                               "https://thm.market.xiaomi.com/thm/download/v2/") + version
                           :
                           url;
            if (!u.contains("https://thm.market.xiaomi.com/thm/download/v2/")) {
                throw new Exception("LinkFormatUnMatchException");
            }
            URL url = new URL(u);
            InputStream inputStream = url.openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // 读取
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            link = new JSONObject(stringBuilder.toString()).getJSONObject("apiData").getString("downloadUrl");

            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        } catch (Exception e) {
            linkFetchingListener.ProcessFail(e);
        }
        linkFetchingListener.ProcessSucceed(link);
    }

    @SuppressWarnings("unused")
    public String getLink() {
        return link;
    }

    @Override
    public synchronized void start() {
        super.start();
        while (isAlive()) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                //
            }
        }
    }
}
