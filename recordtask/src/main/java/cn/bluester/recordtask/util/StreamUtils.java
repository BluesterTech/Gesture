package cn.bluester.recordtask.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by JesseHu on 2016/9/8.
 */
public class StreamUtils {
    /**
     * 通过is解析网页返回的数据
     *
     * @param in
     * @return
     */
    public static String readStream(InputStream in) {
        InputStreamReader isr;
        String result = "";

        try {
            String line;
            isr = new InputStreamReader(in, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
