package com.tankgame;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author justguang
 * @version 1.1
 * @date 2021/9/27
 * @description 用于监视背景音乐是否关闭，如果关闭就重新播放背景音乐，此为守护线程
 */
public class BgmThread implements Runnable {

    //背景音乐是否关闭【默认true关闭】
    public static AtomicBoolean bgmIsPlaying = new AtomicBoolean(false);

    /**
     * mp3资源名
     */
    public final static String[] MP3RESOURCES = {"Grace.mp3", "cikejiandao.mp3", "Opening.mp3", "slkxq.mp3"};


    //用来播放音频的对象
    AudioPlayer audioPlayer;

    @Override
    public void run() {
        while (true) {
            try {
                //睡眠，一秒检测一次
                Thread.sleep(1000);

                //利用CAS，防止线程冲突
                if (BgmThread.bgmIsPlaying.compareAndSet(false, true)) {
                    //随机下标实现随机播放音乐
                    int idx = (int) (Math.random() * 4);

                    //创建播放音频对象
                    audioPlayer = new AudioPlayer(getClass().getResourceAsStream("/static/" + MP3RESOURCES[idx]));
                    //创建线程进行播放
                    Thread audioThread = new Thread(audioPlayer);
                    //设置该线程为守护线程
                    audioThread.setDaemon(true);
                    audioThread.start();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
