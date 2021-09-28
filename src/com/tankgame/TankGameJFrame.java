package com.tankgame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author justguang
 * @version 1.0
 * @date 2021/9/25
 * @description 窗口
 */
@SuppressWarnings("all")
public class TankGameJFrame extends JFrame {
    //定义MyPanel
    MyPanel mp = null;

    public static void main(String[] args) {
        new TankGameJFrame();
    }

    public TankGameJFrame() {
        //开启一个守护线程，用来播放背景音乐，同时开启另一个线程监视音乐的无限播放
        Thread bgmThread = new Thread(new BgmThread());
        bgmThread.setDaemon(true);
        bgmThread.start();

        //创建面板对象
        this.mp = new MyPanel("1");

        //启动线程绘制面板
        new Thread(mp).start();
        //设置窗体出现的位置，左上角XY坐标
        this.setLocation(100, 100);
        //将面板添加到窗体内
        this.add(mp);
        //设置窗体宽高
        this.setSize(1300, 900);
        //设置窗口大小不可改变
        this.setResizable(false);
        //设置键盘监听事件
        this.addKeyListener(mp);
        //设置窗体关闭事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗体可见
        this.setVisible(true);

        //增加窗口监听事件
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //监听窗口关闭事件，当游戏被关闭，保存战斗信息
                BattleInfo.saveBattleInfo();
                System.exit(0);

            }
        });
    }
}
