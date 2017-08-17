/*-
 * Authors      : harry
 * Modified     : stephan
 *
 * Created Date : August 30, 2016
 *
 */

package cn.bluester.recordtask.util;

import android.util.Log;

import cn.bluester.recordtask.BuildConfig;


/**
 * This util offers flexible logging for development of Android application. We
 * will need log information when the application is in working process. But it
 * is not recommended to provide log information when the application is
 * released. Thus the wrapper of Log will help. The principal is to offer a
 * switch to turn on and off and any time to control if the log information is
 * visible.
 */
public class LoggingUtils {

    public static void debug (String tag, String msg) {

        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.d(tag, msg);
        }
    }

    public static void error (String tag, String msg) {

        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.e(tag, msg);
        }
    }

    public static void warning (String tag, String msg) {

        if (BuildConfig.DEBUG) {
            msg = msg == null ? "" : msg;
            Log.w(tag, msg);
        }
    }
}
