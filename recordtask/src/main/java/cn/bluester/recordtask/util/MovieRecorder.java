package cn.bluester.recordtask.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class MovieRecorder {
	private MediaRecorder mediarecorder;
	public boolean isRecording; //是否录制中
	private String newPath; //文件路劲
	
	private Timer timer; //计时器，用于统计录制的时间
	int timeSize = 0; //录制的时间

	/**
	 * 开始录制
	 * @param surfaceView 录制预览
	 * @param camera 用于判断camera是否被占用，如果占用就将其释放
	 */
	@SuppressLint("InlinedApi")
	public void startRecording(SurfaceView surfaceView, Camera camera)
	{

//		if (camera != null)
//		{
//			camera.unlock();
//		}
		
		mediarecorder = new MediaRecorder();// 创建mediarecorder对象
		mediarecorder.setCamera(camera);// 设置录制视频源为Camera(相机)
//		mediarecorder.reset();

		mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//		mediarecorder.setOrientationHint(90);// 视频旋转90度
		//限制视频为480P，防止录制出来的视频过大
//		mediarecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
		// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
		mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		
		// 设置录制的视频编码h263 h264
		mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

		// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
//		mediarecorder.setVideoSize(width, height);
		 mediarecorder.setVideoSize(640, 480);
		// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
		mediarecorder.setVideoFrameRate(12);
//		mediarecorder.setVideoFrameRate(16);
		mediarecorder.setVideoEncodingBitRate(1024*512);
		
		mediarecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
		
		// 设置视频文件输出的路径
		lastFileName = newFileName();
		mediarecorder.setOutputFile(lastFileName);
		try
		{
			// 准备录制
			mediarecorder.prepare();
			// 开始录制
			mediarecorder.start();
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		isRecording = true;
		timeSize = 0;
		timer = new Timer();
		timer.schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				timeSize++;
			}
		}, 0, 1000);

	}


	private String lastFileName;

	//创建保存路劲
	public void createSDCardDir(Context context)
	{
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/VkeVloud/rVideos";
			File path1 = new File(path);
			if (!path1.exists())
			{
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();

			}
		} else
		{
			Toast.makeText(context, "SD卡不存在或已卸载!", Toast.LENGTH_SHORT).show();
			return;

		}

	}

	//停止录制
	@SuppressLint("SimpleDateFormat")
	public void stopRecording()
	{
		if (mediarecorder != null)
		{
			// 停止
			mediarecorder.stop();
			mediarecorder.reset();
			mediarecorder.release();
			mediarecorder = null;

			timer.cancel();
			if (null != lastFileName && !"".equals(lastFileName))
			{
				File f = new File(lastFileName);
				String name = "mov";
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timeStamp = format.format(new Date());
				name += "_" + timeStamp + "_" + timeSize + "s.mp4";
				newPath = f.getParentFile().getAbsolutePath() + "/" + name;
				//重命名
				f.renameTo(new File(newPath));
			}
		}
	}

	public String getNewPath()
	{
		return newPath;
	}

	public void setNewPath(String newPath)
	{
		this.newPath = newPath;
	}

	//获取视频路径
	public String newFileName()
	{
		try
		{
			String video_file_path = File.createTempFile("/VkeVloud/rVideos/mov_", ".mp4",
					Environment.getExternalStorageDirectory()).getAbsolutePath();
			return video_file_path;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void release()
	{
		if (mediarecorder != null)
		{
			// 停止
			mediarecorder.stop();
			mediarecorder.reset();
			mediarecorder.release();
			mediarecorder = null;
		}
	}
}
