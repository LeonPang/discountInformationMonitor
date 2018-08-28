package com.pangliang.discountInformationMonitor.Utils;

/**
 * Created by 庞亮 on 2017/6/14.
 */
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * url转码、解码
 *
 * @author lifq
 * @date 2015-3-17 下午04:09:35
 */
public class UrlUtil {
    private final static String ENCODE = "GBK";
    /**
     * URL 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @return void
     * @author lifq
     * @date 2015-3-17 下午04:09:16
     */
    public static void main(String[] args) {
        //https://s.2.taobao.com/list/list.htm?q=%BC%AA%CB%FB&search_type=item&app=shopsearch
        String str = "吉他";
        System.out.println(getURLEncoderString(str));
        System.out.println(getURLEncoderString(str).equals("%BC%AA%CB%FB"));
//        System.out.println(getURLDecoderString(str));

        System.out.println(new Date().getTime());
    }

}