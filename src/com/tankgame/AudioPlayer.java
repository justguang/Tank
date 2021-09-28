package com.tankgame;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author justguang
 * @version 1.0
 * @date 2021/9/27
 * @description 播放音频
 * PS：引入jlayer包，下载网址【https://mvnrepository.com/artifact/javazoom/jlayer】
 */
public class AudioPlayer implements Runnable {

    private String filename;
    private Player player;
    BufferedInputStream buffer;
    private InputStream inputStream;

    public AudioPlayer(String filename) {
        this.filename = filename;
        System.out.println("播放bgm：" + filename);
    }

    public AudioPlayer(InputStream inputStream) {
        this.inputStream = inputStream;
        System.out.println("使用InputStream 读取bgm文件：" + inputStream);
    }


    @Override
    public void run() {
        try {
            if (inputStream != null) {
                buffer = new BufferedInputStream(inputStream);
            } else if (filename != null) {

                buffer = new BufferedInputStream(new FileInputStream(filename));
            }

            if (buffer != null) {
                player = new Player(buffer);
                player.play();
            }else{
                System.out.println("没有要播放bgm的文件");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (buffer != null) {
                try {
                    if (inputStream != null) inputStream.close();
                    buffer.close();
                    buffer = null;
                    inputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (BgmThread.bgmIsPlaying.compareAndSet(true, false)) {
                System.out.println("一首bgm已结束~");
            }
        }
    }
}
