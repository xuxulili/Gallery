package com.data.gallery.gallery.data;

import android.util.Log;

import com.data.gallery.gallery.app;
import com.data.gallery.gallery.model.Picture;
import com.data.gallery.gallery.model.PictureDetails;
import com.data.gallery.gallery.utils.NetWorkUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/13.
 */
public class GetData {
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
    public static String readString(String url)throws Exception {
        InputStreamReader isr;
        String result = "";
        String line = "";
        try {
            URL url1 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();
            isr = new InputStreamReader(httpURLConnection.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            try {
                while ((line = br.readLine()) != null) {
                    result += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str = unicodeToUtf8(result);
        Log.e("getJson",str);
        return result;
    }

    public static List<PictureDetails> getPictureDetails(String url) {
        List<PictureDetails> pictureDetailsList = null;
        if (NetWorkUtil.isNetWorkConnected(app.getContext())) {
            try {
                Document document = Jsoup.connect(url).timeout(4000).get();
                pictureDetailsList = new ArrayList<>();
                Element element_text = document.getElementsByAttributeValue("class", "content").get(0);
                Elements element_details = document.getElementsByAttributeValue("class", "slider");
//                Log.e("详细信息",element_details.toString());
                Elements elements_details_img = element_details.get(0).select("li");
                if (!elements_details_img.isEmpty()) {
                    for (int i = 0; i <= elements_details_img.size(); i++) {
                        if (i == 0) {
                            PictureDetails pictureDetails = new PictureDetails();
                            pictureDetails.setImageDetail_text(element_text.text());
                            pictureDetailsList.add(pictureDetails);
                        } else {
                            Element element_details_img = elements_details_img.get(i - 1);
                            PictureDetails pictureDetails = new PictureDetails();
                            pictureDetails.setImageDetails(element_details_img.attr("data-source"));
                            pictureDetails.setImageDetail_text(element_details_img.attr("data-text"));
                            Log.e("截取到的详细信息", pictureDetails.toString());
                            pictureDetailsList.add(pictureDetails);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pictureDetailsList;
    }

    public static List<Picture> getPictures(int type, int page) {
        List<Picture> pictures = null;
        if (NetWorkUtil.isNetWorkConnected(app.getContext())) {
            try {
                pictures = new ArrayList<>();
                String str = "http://www.dili360.com/gallery/cate/" + type + "/" + page + ".htm";
                Log.e("请求网址", str);
                Document document = Jsoup.connect(str).timeout(5000).get();
                Elements elements_img = document.getElementsByAttributeValue("class", "img");
                Elements elements_details = document.getElementsByAttributeValue("class", "detail");
                if (!elements_details.isEmpty()) {
                    for (int i = 3; i < elements_details.size(); i++) {
                        Picture picture = new Picture();
                        Element element_about = elements_details.get(i);
                        Element elements_title = element_about.select("h3").get(0);
                        Element elements_time = element_about.select("span").get(0);
                        Element element_details = elements_img.get(i);
                        Element element_img_url = element_details.getElementsByTag("img").get(0);
                        Element element_url = element_details.getElementsByTag("a").get(0);
//                        picture.setImgUrl(elements_details_img.attr("src"));
                        picture.setTitle(elements_title.text());
                        picture.setDate(elements_time.text());
                        picture.setImgUrl(element_img_url.attr("src"));
                        picture.setUrl("http://www.dili360.com" + element_url.attr("href"));
//                        Log.e("picture",picture.toString());
                        pictures.add(picture);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return pictures;
    }
}
