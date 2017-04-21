package com.fuleme.business.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.internal.protocol.NlsRequest;
import com.alibaba.idst.nls.internal.protocol.NlsRequestProto;
import com.fuleme.business.App;

/**
 * 阿里语音合成
 * Created by Administrator on 2017/3/20.
 */

public class TtsUtil {
    public static final String TAG = "TtsUtil";
    static String appkey = "nls-service";//请设置申请到的Appkey
    static String authorize = "LTAImzsbZf0No4FH";
    static String secretKey = "X4gEPac0bak2DmbK1lHpu1PJXZV7SY";

    public static NlsClient mNlsClient;

    public static NlsRequest mNlsRequest = initNlsRequest();

    static int iMinBufSize = AudioTrack.getMinBufferSize(8000,
            AudioFormat.CHANNEL_CONFIGURATION_STEREO,
            AudioFormat.ENCODING_PCM_16BIT);
    public static AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
            AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT,
            iMinBufSize, AudioTrack.MODE_STREAM); //使用audioTrack播放返回的pcm数据


    private static NlsRequest initNlsRequest() {
        NlsRequestProto proto = new NlsRequestProto(App.getInstance());
        proto.setApp_user_id(App.phone); //设置用户名
        return new NlsRequest(proto);

    }

    public static void play(String user_input) {
        mNlsRequest.setApp_key(appkey);    //appkey请从 "快速开始" 帮助页面的appkey列表中获取
        mNlsRequest.initTts();               //初始化tts请求
        NlsClient.openLog(true);
        mNlsClient = NlsClient.newInstance(App.getInstance(), mRecognizeListener, null, mNlsRequest);                          //实例化NlsClient
        if (!user_input.equals("")) {
            mNlsRequest.authorize(authorize, secretKey);   //请替换为用户申请到的数加认证key和密钥
            mNlsRequest.setTtsEncodeType("pcm"); //返回语音数据格式，支持pcm,wav.alaw
            mNlsRequest.setTtsVolume(100);   //音量大小默认50，阈值0-100
            mNlsRequest.setTtsSpeechRate(0);//语速，阈值-500~500
            mNlsClient.PostTtsRequest(user_input); //用户输入文本
            audioTrack.play();
//            audioTrack.release();
        }
    }


    private static NlsListener mRecognizeListener = new NlsListener() {
        @Override
        public void onTtsResult(int status, byte[] ttsResult) {
            switch (status) {
                case NlsClient.ErrorCode.TTS_BEGIN:
                    audioTrack.play();
                    LogUtil.e(TAG, "tts begin");
                    audioTrack.write(ttsResult, 0, ttsResult.length);
                    break;
                case NlsClient.ErrorCode.TTS_TRANSFERRING:
                    LogUtil.e(TAG, "tts transferring" + ttsResult.length);
                    audioTrack.write(ttsResult, 0, ttsResult.length);
                    break;
                case NlsClient.ErrorCode.TTS_OVER:
                    audioTrack.stop();
                    LogUtil.e(TAG, "tts over");
                    break;
                case NlsClient.ErrorCode.CONNECT_ERROR:
                    ToastUtil.showMessage("CONNECT ERROR");
                    break;
            }
        }
    };


}
