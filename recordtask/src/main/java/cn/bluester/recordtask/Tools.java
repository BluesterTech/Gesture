package cn.bluester.recordtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tools {
    /**
     * 去除字符串前后所有的空格
     *
     * @param strValue 需要去除前后空格的字符串
     * @return 去除前后空格后的字符串
     */
    public static String trimSpaces(String strValue) {
        while (strValue.startsWith(" ")) {
            strValue = strValue.substring(1, strValue.length()).trim();
        }
        while (strValue.endsWith(" ")) {
            strValue = strValue.substring(0, strValue.length() - 1).trim();
        }
        return strValue;
    }

    /**
     * dip转px
     *
     * @param context 上下文
     * @param dp      需要转换的dip值
     * @return 转换后的px值
     */
    public static int dip2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param px      需要转换的培训值
     * @return 转换后的dip值
     */
    public static int px2dip(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * bitmap缩放
     *
     * @param b 原bitmap
     * @param x 缩放后的宽
     * @param y 缩放后的高
     * @return
     */
    public static Bitmap bitmapChange(Bitmap b, float x, float y) {
        int w = b.getWidth();
        int h = b.getHeight();
        float sx = (float) x / w;// 要强制转换，不转换我会死掉。
        float sy = (float) y / h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w, h, matrix, true);
        return resizeBmp;
    }

    /**
     * 获取sdk版本
     *
     * @return 版本号
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            Log.e("main", e.toString());
        }
        return version;
    }

    /**
     * 通过inputstream解析网页返回的数据
     * @param in
     * @return
     */
    public static String readStream(InputStream in) {
        InputStreamReader isr;
        String result = "";

        try {
            //每一行的数据
            String line;
            isr = new InputStreamReader(in, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Typeface getTypeface(Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"iconfont.ttf");
        return typeface;
    }

}
