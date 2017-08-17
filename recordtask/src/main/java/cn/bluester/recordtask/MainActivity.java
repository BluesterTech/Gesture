package cn.bluester.recordtask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.bluester.recordtask.bean.Attachment;
import cn.bluester.recordtask.bean.TaskItem;
import cn.bluester.recordtask.util.CacheUtils;
import cn.bluester.recordtask.util.FucUtil;
import cn.bluester.recordtask.util.JsonParser;
import cn.bluester.recordtask.util.MyConstants;
import cn.bluester.recordtask.util.PermissionsChecker;
import com.augumenta.agapi.AugumentaManager;
import com.augumenta.agapi.CameraFrameProvider;
import com.augumenta.agapi.HandPose;
import com.augumenta.agapi.HandPoseEvent;
import com.augumenta.agapi.HandPoseListener;
import com.augumenta.agapi.Poses;
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final MainActivity TAG = MainActivity.this;
    // 屏幕常亮
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    Button nextBtn;
    private SurfaceView sv_record_camera_view;

    private MovieRecorder movieRecorder;
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;

    private Context mContext;

    private static List<TaskItem> taskItems;
    private ViewPager mViewPager;
    private List<View> subViews;//所有的节点view

    // 语音听写对象
    private SpeechRecognizer mAsr;
    // 本地语法文件
    private String mLocalGrammar = null;
    // 本地语法构建路径
    private String grmPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/test";
    private VoiceWakeuper mIvw;
    private String resPath;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            android.Manifest.permission.RECORD_AUDIO
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private String task;
    private String[] names = {"发动机", "机翼", "起落架"};
    private String[] tasks = {"{\n" +
            "    \"node\": [\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://2.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 42\n" +
            "            },\n" +
            "            \"keyid\": \"state_start2\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"41\",\n" +
            "            \"text\": \"第一步：拆开发动机\",\n" +
            "            \"type\": \"1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://3.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 43\n" +
            "            },\n" +
            "            \"keyid\": \"state_process3\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"42\",\n" +
            "            \"text\": \"第二步：拧下螺丝\",\n" +
            "            \"type\": \"2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://1.jpg\"\n" +
            "            },\n" +
            "            \"link\": {},\n" +
            "            \"keyid\": \"state_end4\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"43\",\n" +
            "            \"text\": \"第三步：更换配件\",\n" +
            "            \"type\": \"4\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"task\": {\n" +
            "        \"creator\": 9001,\n" +
            "        \"id\": 14,\n" +
            "        \"is_complete\": 0,\n" +
            "        \"name\": \"发动机\"\n" +
            "    }\n" +
            "}", "{\n" +
            "    \"node\": [\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://4.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 42\n" +
            "            },\n" +
            "            \"keyid\": \"state_start2\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"41\",\n" +
            "            \"text\": \"第一步：拆开机翼\",\n" +
            "            \"type\": \"1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://5.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 43\n" +
            "            },\n" +
            "            \"keyid\": \"state_process3\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"42\",\n" +
            "            \"text\": \"第二步：维修机翼\",\n" +
            "            \"type\": \"2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://6.jpg\"\n" +
            "            },\n" +
            "            \"link\": {},\n" +
            "            \"keyid\": \"state_end4\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"43\",\n" +
            "            \"text\": \"第三步：组装机翼\",\n" +
            "            \"type\": \"4\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"task\": {\n" +
            "        \"creator\": 9001,\n" +
            "        \"id\": 14,\n" +
            "        \"is_complete\": 0,\n" +
            "        \"name\": \"机翼\"\n" +
            "    }\n" +
            "}", "{\n" +
            "    \"node\": [\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://7.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 42\n" +
            "            },\n" +
            "            \"keyid\": \"state_start2\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"41\",\n" +
            "            \"text\": \"第一步：扭螺丝\",\n" +
            "            \"type\": \"1\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://8.jpg\"\n" +
            "            },\n" +
            "            \"link\": {\n" +
            "                \"from\": 3,\n" +
            "                \"to\": 1,\n" +
            "                \"id\": 43\n" +
            "            },\n" +
            "            \"keyid\": \"state_process3\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"42\",\n" +
            "            \"text\": \"第二步：卸轮胎\",\n" +
            "            \"type\": \"2\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"attachment\": {\n" +
            "                \"img\": \"assets://9.jpg\"\n" +
            "            },\n" +
            "            \"link\": {},\n" +
            "            \"keyid\": \"state_end4\",\n" +
            "            \"task_id\": \"14\",\n" +
            "            \"id\": \"43\",\n" +
            "            \"text\": \"第三步：装轮胎\",\n" +
            "            \"type\": \"4\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"task\": {\n" +
            "        \"creator\": 9001,\n" +
            "        \"id\": 14,\n" +
            "        \"is_complete\": 0,\n" +
            "        \"name\": \"起落架\"\n" +
            "    }\n" +
            "}"};
    //手势识别
    private AugumentaManager mAugumentaManager;
    private SurfaceView cameraPreview;

    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initPose() {
        cameraPreview =  (SurfaceView) findViewById(R.id.sv_camera);
        CameraFrameProvider cameraFrameProvider = new CameraFrameProvider();
        cameraFrameProvider.setCameraPreview(cameraPreview);
        cameraPreview.setVisibility(View.VISIBLE);
        cameraFrameProvider.setFastMode(true);
        try {
            mAugumentaManager = AugumentaManager.getInstance(this, cameraFrameProvider);
        } catch (IllegalStateException e) {
            // Something went wrong while authenticating license
            Toast.makeText(this, "License error: " + e.getMessage(), Toast.LENGTH_LONG);
            Log.e("aaa", "License error: " + e.getMessage());
        }
//        Toast.makeText(this, "开启", Toast.LENGTH_LONG).show();
    }


    private void setPoseListener() {
        //赞
        mAugumentaManager.registerListener(mPoseListener, new HandPose(Poses.P008));
        //手掌
        mAugumentaManager.registerListener(mPoseListener, Poses.P001);

        mAugumentaManager.registerListener(mPoseListener, Poses.P016);
        mAugumentaManager.registerListener(mPoseListener, Poses.P102);
        if (!mAugumentaManager.start()) {
            // Failed to start detection, probably failed to open camera
            Toast.makeText(this, "Failed to open camera!", Toast.LENGTH_LONG).show();
            // Close activity
            finish();
        }
    }



    //关闭手势识别
    private void closePose() {
        if (mAugumentaManager!=null){
            mAugumentaManager.unregisterAllListeners();
            mAugumentaManager.stop();
            mAugumentaManager = null;
            cameraPreview.setVisibility(View.GONE);
//            Toast.makeText(mContext,"关闭",Toast.LENGTH_SHORT).show();
        }

    }

    //手势监听
    private HandPoseListener mPoseListener = new HandPoseListener() {
        @Override
        public void onDetected(final HandPoseEvent handPoseEvent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int pose = handPoseEvent.handpose.pose();
                    //pose=10 true；pose=14 false
                    Toast.makeText(mContext, pose + "", Toast.LENGTH_SHORT).show();
//                    showNext(nextBtn);
                    // getCurrentView(pose + "");
                    getCurrentView("下一步");
                }
            });
        }

        @Override
        public void onLost(HandPoseEvent handPoseEvent) {

        }

        @Override
        public void onMotion(HandPoseEvent handPoseEvent) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        setContentView(R.layout.activity_main);


        // 保持屏幕常亮状态
        powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MyLock");
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);

        //初始化XUtils3
        x.Ext.init(this.getApplication());
        mContext = this;
        mPermissionsChecker = new PermissionsChecker(this);

        // 创建用户语音配置对象后才可以使用语音服务
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5800864d");
        //创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mAsr = SpeechRecognizer.createRecognizer(this, null);
        //创建唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(this, null);
        // 初始化语法、命令词
        mLocalGrammar = FucUtil.readFile(this, "call.bnf", "utf-8");
        //获取唤醒词路径，resPath为唤醒资源路径
        resPath = ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "ivw/5800864d.jet");

        initView();
        final Button saveVideoBtn = (Button) findViewById(R.id.btn_save_video);

        saveVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    if (!movieRecorder.isRecording) {
                        movieRecorder.startRecording(sv_record_camera_view, mCamera);
                        movieRecorder.isRecording = true;
                        saveVideoBtn.setText("保存视频");

                    } else {
                        movieRecorder.stopRecording();
                        movieRecorder.isRecording = false;
                        saveVideoBtn.setText("开始录制");
                        refreshViewByRecordingState();
                        String path = movieRecorder.getNewPath();
                        Uri uri = Uri.parse(path);
                        //调用系统自带的播放器
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Log.v("URI:::::::::", uri.toString());
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(TAG, "SD卡不存在或已卸载！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.vp_task);
        subViews = new ArrayList<>();
        String token = CacheUtils.getString(mContext, MyConstants.TOKEN);
        String taskListUrl = MyConstants.APIHOST + "api/task/?token=" + token;
//        new TaskListAsyncTask().execute(taskListUrl);


        ListView listView = (ListView) findViewById(R.id.lv_lsit);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_list, names));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subViews.clear();
                task = tasks[position];
                new MyAsyncTask().execute("");
            }
        });
        initPose();
    }

    //获取mac地址
    private String getMacAddress() {
        String macAddress = CacheUtils.getString(mContext, MyConstants.MACADRESS, null);
//        if (macAddress == null) {
//            WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = wifi.getConnectionInfo();
//            macAddress = info.getMacAddress();
//            if (null == macAddress) {
//                return "";
//            }
//            macAddress = macAddress.replace(":", "");
//            CacheUtils.putValue(mContext, MyConstants.MACADRESS, macAddress);
//        }
//        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//        if (macAddress==null){
//            macAddress = TelephonyMgr.getDeviceId();
//            CacheUtils.putValue(mContext, MyConstants.MACADRESS, macAddress);
//        }

        return macAddress;
    }

    public static String getLinuxCore_Ver() {
        Process process = null;
        String kernelVersion = "";
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);


        String result = "";
        String line;
        // get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (result != "") {
                String Keyword = "version ";
                int index = result.indexOf(Keyword);
                line = result.substring(index + Keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }
    /**
     * INNER-VER
     * 内部版本
     * return String
     */

    public static String getInner_Ver(){
        String ver = "" ;

        if(android.os.Build.DISPLAY .contains(android.os.Build.VERSION.INCREMENTAL)){
            ver = android.os.Build.DISPLAY;
        }else{
            ver = android.os.Build.VERSION.INCREMENTAL;
        }
        return ver;

    }

    private void initView() {
        movieRecorder = new MovieRecorder();
        // 判断保存的文件夹是否存在，不存在则创建新的文件夹
        movieRecorder.createSDCardDir(TAG);

        sv_record_camera_view = (SurfaceView) findViewById(R.id.sv_record_camera_view);
        surfaceHolder = sv_record_camera_view.getHolder();
        // 加入回调函数
        surfaceHolder.addCallback(this);
        // 手机低于Android3.0的需要配置
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 选择支持半透明模式
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            this.finish();
            Toast.makeText(this, "请允许应用调用摄像头权限", Toast.LENGTH_SHORT).show();
        }
        setCameraParams();

    }

    // 设置摄像头参数
    @SuppressLint("InlinedApi")
    public void setCameraParams() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            List<String> list = params.getSupportedFocusModes();
            if (list.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            params.set("orientation", "portrait");
            mCamera.setParameters(params);
        }
    }

    /* 刷新录制状态 */
    protected void refreshViewByRecordingState() {
        if (movieRecorder.isRecording) {
            movieRecorder.isRecording = true;
        } else {
            movieRecorder.isRecording = false;
        }

    }


    @Override
    protected void onResume() {
        if (mAugumentaManager != null)
            setPoseListener();
        wakeLock.acquire();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
//        initView();
//        Log.i("aaa", getInner_Ver());
//        Toast.makeText(this,getInner_Ver(),Toast.LENGTH_SHORT).show();
        String code = getLinuxCore_Ver()+getInner_Ver();
//        if (!"3.10.35eng.root.1470290713".equals(code)) {
//            finish();
//        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        closePose();
        if (movieRecorder != null) {
            movieRecorder.stopRecording();
        }
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAsr.stopListening();
        mAsr.cancel();
        mAsr.destroy();
        mIvw.stopListening();
        mIvw.cancel();
        mIvw.destroy();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        try {
            if (mCamera != null) {
                // 启动预览
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }

        } catch (Exception e) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder;
        if (mCamera != null) {
            setCameraParams();
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        sv_record_camera_view = null;
        surfaceHolder = null;
        movieRecorder = null;
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        if (hasFocus) {
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                if (!movieRecorder.isRecording) {
//                    movieRecorder.startRecording(sv_record_camera_view, mCamera);
//                    movieRecorder.isRecording = true;
//                } else {
//                    movieRecorder.stopRecording();
//                    movieRecorder.isRecording = false;
//                    refreshViewByRecordingState();
//                    // dialogIsUpload();
//                    String path = movieRecorder.getNewPath();
//                    finishWithResult(path);
//                }
//            } else {
//                Toast.makeText(TAG, "SD卡不存在或已卸载！", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void finishWithResult(String path) {
        Intent intent = new Intent();
        intent.putExtra("path", path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            getCurrentView("下一步");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }


        return false;
    }

    //获取当前界面
    private void getCurrentView(String text) {
        if (subViews.size() > 0) {
            int currentItem = mViewPager.getCurrentItem();
            View view = subViews.get(currentItem);
            Button preBtn = (Button) view.findViewById(R.id.btn_pre);
             nextBtn = (Button) view.findViewById(R.id.btn_next);

            if ("下一步".equals(text) || "yes".equals(text.toLowerCase()) || "完成".equals(text) || "10".equals(text)) {
//            if (nextBtn.getVisibility() == View.VISIBLE)
                showNext(nextBtn);

//            } else if ("上一步".equals(text) || "no".equals(text.toLowerCase()) || "7".equals(text)) {
////            if (preBtn.getVisibility() == View.VISIBLE)
//                showPre(preBtn);
            }
        }
    }

    private void showNext(Button nextBtn) {
        int tag = (int) nextBtn.getTag();
        if (tag != -1) {
            //获取下一个界面的角标
            int nextIndex = 0;
            for (int i = 0; i < taskItems.size(); i++) {
                if (taskItems.get(i).getId() == tag) {
                    nextIndex = i;
                }
            }
//                int nextIndex = mViewPager.indexOfChild(mViewPager.findViewWithTag(tag));
            mViewPager.setCurrentItem(nextIndex);
        } else {
            Toast.makeText(mContext, "完成", Toast.LENGTH_SHORT).show();
            mViewPager.setCurrentItem(0);
        }
    }

    private void showPre(Button preBtn) {
        int tag = (int) preBtn.getTag();
        //获取下一个界面的角标
        int preIndex = 0;
        for (int i = 0; i < taskItems.size(); i++) {
            if (taskItems.get(i).getId() == tag) {
                preIndex = i;
            }
        }
//                int preIndex = mViewPager.indexOfChild(mViewPager.findViewWithTag(tag));
        mViewPager.setCurrentItem(preIndex);
    }

    class MyAsyncTask extends AsyncTask<String, Void, List<TaskItem>> {
        @Override
        protected List<TaskItem> doInBackground(String... strings) {
            return getItemJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<TaskItem> taskItems) {
            super.onPostExecute(taskItems);
            for (int i = 0; i < taskItems.size(); i++) {
                View view = initSubview(i, taskItems.get(i));
                subViews.add(view);
            }
            MyAdapter myAdapter = new MyAdapter();
            mViewPager.setAdapter(myAdapter);
            for (int i = 0; i < taskItems.size(); i++) {
                int type = taskItems.get(i).getType();
                if (type == 1) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
            //唤醒+识别
            setWakeParameter();
        }
    }

    //解析流程图数据
    private List<TaskItem> getItemJsonData(String url) {
        taskItems = new ArrayList<>();
        try {
//            String jsonStr = StreamUtils.readStream(new URL(url).openStream());
//            String jsonStr = "{\n" +
//                    "    \"node\": [\n" +
//                    "        {\n" +
//                    "            \"attachment\": {\n" +
//                    "                \"img\": \"assets://2.jpg\"\n" +
//                    "            },\n" +
//                    "            \"link\": {\n" +
//                    "                \"from\": 3,\n" +
//                    "                \"to\": 1,\n" +
//                    "                \"id\": 42\n" +
//                    "            },\n" +
//                    "            \"keyid\": \"state_start2\",\n" +
//                    "            \"task_id\": \"14\",\n" +
//                    "            \"id\": \"41\",\n" +
//                    "            \"text\": \"拧开螺丝\",\n" +
//                    "            \"type\": \"1\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "            \"attachment\": {\n" +
//                    "                \"img\": \"assets://3.jpg\"\n" +
//                    "            },\n" +
//                    "            \"link\": {\n" +
//                    "                \"from\": 3,\n" +
//                    "                \"to\": 1,\n" +
//                    "                \"id\": 43\n" +
//                    "            },\n" +
//                    "            \"keyid\": \"state_process3\",\n" +
//                    "            \"task_id\": \"14\",\n" +
//                    "            \"id\": \"42\",\n" +
//                    "            \"text\": \"更换零件\",\n" +
//                    "            \"type\": \"2\"\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "            \"attachment\": {\n" +
//                    "                \"img\": \"assets://1.jpg\"\n" +
//                    "            },\n" +
//                    "            \"link\": {},\n" +
//                    "            \"keyid\": \"state_end4\",\n" +
//                    "            \"task_id\": \"14\",\n" +
//                    "            \"id\": \"43\",\n" +
//                    "            \"text\": \"拧上螺丝\",\n" +
//                    "            \"type\": \"4\"\n" +
//                    "        }\n" +
//                    "    ],\n" +
//                    "    \"task\": {\n" +
//                    "        \"creator\": 9001,\n" +
//                    "        \"id\": 14,\n" +
//                    "        \"is_complete\": 0,\n" +
//                    "        \"name\": \"维修\"\n" +
//                    "    }\n" +
//                    "}";
//            Intent intent = this.getIntent();
//            task = intent.getStringExtra("task");
            com.alibaba.fastjson.JSONObject rootJson = JSON.parseObject(task);
//            com.alibaba.fastjson.JSONObject taskJson = JSON.parseObject(rootJson.get("task").toString());
            String jsonArrayStr = rootJson.get("node").toString();

            taskItems = JSON.parseArray(jsonArrayStr, TaskItem.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskItems;
    }

    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return subViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(subViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = subViews.get(position);
            container.addView(view);
            return view;
        }
    }

    /**
     * 创建节点view
     *
     * @param index    节点在list中对应的index，即在viewpaper中的index
     * @param itemInfo 节点对应的信息
     * @return View
     */
    private View initSubview(int index, TaskItem itemInfo) {
        LayoutInflater inflater = this.getLayoutInflater();
        View subview = inflater.inflate(R.layout.single_item_task, null);
        TextView contentTv = (TextView) subview.findViewById(R.id.tv_content);
        final Button preBtn = (Button) subview.findViewById(R.id.btn_pre);
        final Button nextBtn = (Button) subview.findViewById(R.id.btn_next);
        ImageView imageView = (ImageView) subview.findViewById(R.id.iv_task_img);

        Attachment attachment = itemInfo.getAttachment();
        if (attachment != null) {
            String img = attachment.getImg();
            if (img != null)
                x.image().bind(imageView, img, options, new Callback.CacheCallback<Drawable>() {

                    @Override
                    public boolean onCache(Drawable result) {
                        //是否信任使用缓存，true使用，每次优先读取缓存；false不使用,每次都会重新获取
                        return true;
                    }

                    @Override
                    public void onSuccess(Drawable result) {

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
        }


        subview.setTag(itemInfo.getId());

        contentTv.setText(itemInfo.getText());
        int type = itemInfo.getType();
        if (type == 1 || type == 2) {
            //开始节点与过程节点
            preBtn.setVisibility(View.GONE);
            nextBtn.setTag(itemInfo.getLink().getId());

            nextBtn.setText("下一步");
        } else if (type == 3) {
            //判断节点
            preBtn.setTag(itemInfo.getLink().getNo().getId());
            nextBtn.setTag(itemInfo.getLink().getYes().getId());

            preBtn.setText("NO");
            nextBtn.setText("YES");
        } else if (type == 4) {
            //结束节点
            preBtn.setVisibility(View.GONE);
            nextBtn.setTag(-1);
            nextBtn.setText("完成");
        }

        preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPre(preBtn);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNext(nextBtn);

            }
        });

        return subview;
    }

    private ImageOptions options = new ImageOptions.Builder()
            //设置图片大小,设置图片大小时ImageView的大小必须定义为wrap_content
//            .setSize(DensityUtil.dip2px(160), DensityUtil.dip2px(90))
            //设置四个角的弧度
//            .setRadius(DensityUtil.dip2px(5))
            // 如果ImageView的大小不是定义为wrap_content, 不要crop.
            .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
            // 加载中或错误图片的ScaleType
            //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
            .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
            //加载中显示的图片
            .setLoadingDrawableId(R.mipmap.ic_launcher)
            //加载失败显示的图片
            .setFailureDrawableId(R.mipmap.ic_launcher)
            //设置使用缓存
            .setUseMemCache(true)
            //设置支持gif
            .setIgnoreGif(false)
            //设置显示圆形图片，设置圆形图片时ImageView的大小必须定义为wrap_content
            //.setCircular(true)
            .build();

    /**
     * 构建语法监听器。
     */
    private GrammarListener grammarListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String grammarId, SpeechError error) {
            if (error == null) {
                Log.i("aaa", "onBuildFinish: 语法构建成功");
            } else {
                Log.i("aaa", "onBuildFinish: 语法构建失败,错误码：" + error.getErrorCode());
            }
        }
    };

    //获取识别资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        return tempBuffer.toString();
    }


    //设置唤醒参数
    private void setWakeParameter() {
        mAsr.setParameter(SpeechConstant.PARAMS, null);
        mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        // 设置引擎类型
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        mAsr.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
        // 设置语法构建路径
        mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
        // 设置资源路径
        mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        int ret = mAsr.buildGrammar("bnf", mLocalGrammar, grammarListener);
        if (ret != ErrorCode.SUCCESS) {
            Log.i("aaa", "语法构建失败,错误码：" + ret);
        }

        // 非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 设置文本编码格式
            mIvw.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
            // 设置引擎类型
            mIvw.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mIvw.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
            //设置唤醒词加载路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, resPath);
            //唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:-100;1:-100");
            // 设置唤醒+识别模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "oneshot");
            // 设置返回结果格式
            mIvw.setParameter(SpeechConstant.RESULT_TYPE, "json");
            // 设置本地识别资源
            mIvw.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
            // 设置语法构建路径
            mIvw.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
            // 设置本地识别使用语法id
            mIvw.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");

            //设置唤醒一直保持，直到调用stopListening，传入0则完成一次唤醒后，会话立即结束（默认0）
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1");
            //4.开始唤醒
            mIvw.startListening(mWakeuperListener);
        }

    }

    //唤醒监听器
    private WakeuperListener mWakeuperListener = new WakeuperListener() {
        private String resultString;
        private String recoString;

        @Override
        public void onResult(WakeuperResult result) {
            try {
                String text = result.getResultString();
                org.json.JSONObject object;
                object = new org.json.JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString = buffer.toString();
            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
            }
            Log.i("aaa", "onResult: " + resultString);
        }

        @Override
        public void onError(SpeechError error) {
            Log.i("aaa", "onError: " + error.getPlainDescription(true));
            mIvw.startListening(mWakeuperListener);
        }

        @Override
        public void onBeginOfSpeech() {
            Log.i("aaa", "onBeginOfSpeech: 1");
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            Log.d("aaa", "eventType:" + eventType + "arg1:" + isLast + "arg2:" + arg2);
            // 识别结果
            if (SpeechEvent.EVENT_IVW_RESULT == eventType) {
                RecognizerResult reslut = ((RecognizerResult) obj.get(SpeechEvent.KEY_EVENT_IVW_RESULT));
                recoString = JsonParser.parseGrammarResult(reslut.getResultString());
                Log.i("aaa", "onEvent: " + recoString);
                getCurrentView(recoString);
                mIvw.startListening(mWakeuperListener);
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
            // TODO Auto-generated method stub
            Toast.makeText(mContext, volume + "", Toast.LENGTH_SHORT).show();
        }

    };


}
