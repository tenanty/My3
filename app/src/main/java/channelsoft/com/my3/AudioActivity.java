package channelsoft.com.my3;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by yuanshun on 2014/12/18.
 */
public class AudioActivity extends Activity {

    private static String ACTIVITY_TAG = "AudioActivity";

    private Button button_start;
    private Button button_stop;
    private MediaRecorder recorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//让界面横屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉界面标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.audio);
        init();
    }

    private void init() {
        button_start = (Button) this.findViewById(R.id.start_button);
        button_stop = (Button) this.findViewById(R.id.stop_button);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_start) {
                    Log.d(AudioActivity.ACTIVITY_TAG, "开始刻录");
                    initializeAudio();
                }
                if (v == button_stop) {
                    Log.d(AudioActivity.ACTIVITY_TAG, "停止刻录");
                    recorder.stop();// 停止刻录
                    // recorder.reset(); // 重新启动MediaRecorder.
                    recorder.release(); // 刻录完成一定要释放资源
                    // recorder = null;
                }
            }
        });

    }

    class AudioListerner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == button_start) {
                initializeAudio();
            }
            if (v == button_stop) {
                recorder.stop();// 停止刻录
                // recorder.reset(); // 重新启动MediaRecorder.
                recorder.release(); // 刻录完成一定要释放资源
                // recorder = null;
            }
        }

    }

    private void initializeAudio() {
        recorder = new MediaRecorder();// new出MediaRecorder对象
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        // 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置MediaRecorder录制音频的编码为amr.
        recorder.setOutputFile("/sdcard/peipei.amr");
        // 设置录制好的音频文件保存路径
        try {
            recorder.prepare();// 准备录制
            recorder.start();// 开始录制
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
