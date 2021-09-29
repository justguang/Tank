package com.tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author justguang
 * @version 1.1
 * @date 2021/9/25
 * @description 窗口
 */
@SuppressWarnings("all")
public class TankGameJFrame extends JFrame {

    //游戏战斗面板
    public static MyPanel gamePanel = null;

    //战斗面板线程
    public static Thread gamePanelThread = null;

    public static void main(String[] args) {
        new TankGameJFrame("坦克大战 v1.2");
    }

    public TankGameJFrame(String name) {
        super(name);

        //开启一个守护线程，用来播放背景音乐，同时开启另一个线程监视音乐的无限播放
        Thread bgmThread = new Thread(new BgmThread());
        bgmThread.setDaemon(true);
        bgmThread.start();


        //设置窗体宽高
        this.setSize(1300, 900);
        //设置窗口大小不可改变
        this.setResizable(false);

        //设置窗体出现的位置，左上角XY坐标
        //this.setLocation(100, 100);
        this.setLocationRelativeTo(null);//窗口置于屏幕中央

        //设置窗体关闭事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //取消layout布局管理
        this.setLayout(null);


        //添加按钮
        addButton(this);

        //创建面板对象
        gamePanel = new MyPanel();

        gamePanel.setBounds(0, 0, 1200, 900);

        //将面板添加到窗体内
        this.add(gamePanel);

        //设置键盘监听事件
        this.addKeyListener(gamePanel);

        //获取焦点
        this.setFocusable(true);

        //设置窗体可见
        this.setVisible(true);

        //添加窗体关闭事件，执行战斗数据保存
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //退出游戏
                exitGame();
                System.exit(0);
            }
        });

    }

    /**
     * 添加按钮
     *
     * @param jFrame 要添加按钮的窗体
     */
    void addButton(JFrame jFrame) {

        /**
         * 继续游戏按钮
         */
        JButton continueGame = new JButton("继续游戏");
        continueGame.setFont(new Font("宋体", Font.BOLD, 15));
        continueGame.setBackground(Color.orange);
        continueGame.setBounds(5, 820, 100, 30);

        /**
         * 新游戏按钮
         */
        JButton newGame = new JButton("新 游 戏");
        newGame.setFont(new Font("宋体", Font.BOLD, 15));
        newGame.setBackground(Color.green);
        newGame.setBounds(130, 820, 100, 30);


        continueGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("继续游戏");
                startGame(jFrame, false);

                continueGame.setVisible(false);
                newGame.setVisible(false);

                continueGame.setEnabled(false);
                newGame.setEnabled(false);


            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("新游戏");
                startGame(jFrame, true);

                continueGame.setVisible(false);
                newGame.setVisible(false);

                continueGame.setEnabled(false);
                newGame.setEnabled(false);



            }
        });


        //将按钮添加到窗体内
        jFrame.add(continueGame);
        jFrame.add(newGame);
    }


    /**
     * 开始游戏
     *
     * @param jFrame    窗体
     * @param isNewGame true=>新游戏，false=>继续之前的游戏战斗
     */
    void startGame(JFrame jFrame, boolean isNewGame) {

        //开始游戏
        gamePanel.startGame(isNewGame);

        //启动线程绘制面板
        gamePanelThread = new Thread(gamePanel);
        gamePanelThread.start();


    }


    /**
     * 退出游戏
     */
    void exitGame() {

        //保存战斗数据
        BattleInfo.saveBattleInfo();

    }
}


