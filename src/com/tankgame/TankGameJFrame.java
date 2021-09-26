package com.tankgame;

import javax.swing.*;

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
        //创建面板对象
        this.mp = new MyPanel();

        //启动线程绘制面板
        new Thread(mp).start();

        //将面板添加到窗体内
        this.add(mp);
        //设置窗体宽高
        this.setSize(1030, 800);
        //设置键盘监听事件
        this.addKeyListener(mp);
        //设置窗体关闭事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗体可见
        this.setVisible(true);
    }
}
