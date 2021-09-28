package com.tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * @author justguang
 * @version 1.0
 * @date 2021/9/25
 * @description 坦克大战的绘图区域
 */
@SuppressWarnings("all")
public class MyPanel extends JPanel implements KeyListener, Runnable {
    //定义我的坦克
    public static CustomTank myTank = null;

    //定义敌人坦克
    public static Vector<EnemyTank> enemyTanks = new Vector<>();

    //定义敌人数量【默认4架坦克】
    public static int enemyTankNum = 4;

    //存放炸弹集合
    Vector<Bomb> bombs = new Vector<>();

    //定义三张爆炸效果图片
    Image bombImg1 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/static/1.png"));
    Image bombImg2 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/static/2.png"));
    Image bombImg3 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/static/3.png"));

    {
        //加载战斗记录
        BattleInfo.checkBattleRecordFileExists();
        BattleInfo.loadBattleRecord();
    }

    public MyPanel(String key) {
        //key=> 1开始新游戏，
        if (key.equals("1")) {
            myTank = null;
            enemyTankNum = 4;
            enemyTanks.clear();
        }


        if (myTank == null) {
            //如果玩家坦克为空，创建玩家的坦克
            myTank = new CustomTank(500, 600);
        }
        //设置玩家的坦克移速
        myTank.setSpeed(6);

        if (enemyTanks.size() == 0) {
            //如果敌人坦克集合中没有对象，创建敌人的坦克
            for (int i = 0; i < enemyTankNum; i++) {
                //创建对象
                EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
                //设置方向
                enemyTank.setDirection(1);

                //启动线程让敌人坦克移动
                new Thread(enemyTank).start();

                //添加到集合中
                enemyTanks.add(enemyTank);
            }
        }
    }


    /**
     * 显示战斗信息
     *
     * @param g 画笔
     */
    public void showBattleInfo(Graphics g) {
        //设置画笔颜色
        g.setColor(Color.black);
        //字体
        g.setFont(new Font("宋体", Font.BOLD, 25));
        //显示文字
        g.drawString("你击毁的坦克", 1020, 30);
        //画出敌人坦克
        drawTank(1020, 60, g, 0, 1);
        //重新设置画笔颜色
        g.setColor(Color.black);
        //画出个数
        g.drawString(String.valueOf(BattleInfo.getDefeatEnemyTankNum()), 1080, 100);
    }

    /**
     * 显示其他信息
     *
     * @param g 画笔
     */
    public void showOtherInfo(Graphics g) {
        //设置画笔颜色
        g.setColor(Color.black);
        //字体
        g.setFont(new Font("宋体", Font.BOLD, 15));
        //显示文字
        g.drawString("移动：英文输入法下的 W S A D ", 10, 780);
        g.drawString("攻击：英文输入法下的 J ", 10, 800);

    }


    /**
     * @param g 画笔
     * @description 绘画操作
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //填充矩形，默认黑色
        g.fillRect(0, 0, 1000, 750);

        //显示战斗信息
        showBattleInfo(g);
        //显示其他信息
        showOtherInfo(g);

        //如果玩家的坦克存活、画出玩家自己的坦克
        if (myTank != null && myTank.isLive())
            drawTank(myTank.getX(), myTank.getY(), g, myTank.getDirection(), 0);

        //画出玩家的子弹
        for (int i = 0; i < myTank.shots.size(); i++) {
            g.setColor(Color.orange);
            Shot shot = myTank.shots.get(i);
            if (shot != null && shot.isLive()) {
                g.fill3DRect(shot.getX(), shot.getY(), 3, 3, false);
            } else {
                //移除已销毁的子弹
                myTank.shots.remove(shot);
            }
        }

        //如果bombs集合中右对象，就画出炸弹
        for (int i = 0; i < bombs.size(); i++) {
            //取出炸弹
            Bomb bomb = bombs.get(i);
            //移除已销毁的爆炸
            if (bomb == null && !bomb.isLive) {
                bombs.remove(bomb);
                continue;
            }

            //根据当前bomb对象的life值去画对应的图片
            if (bomb.life > 40) {
                g.drawImage(bombImg3, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 20) {
                g.drawImage(bombImg2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(bombImg1, bomb.x, bomb.y, 60, 60, this);
            }

            //减少炸弹生命值
            bomb.lifeDown();
            //如果bomb.life<=0 ，从集合中删除
            if (!bomb.isLive) {
                bombs.remove(bomb);
            }
        }

        //画出敌人的坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            //绘制存活的坦克
            if (enemyTank != null && enemyTank.isLive()) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirection(), 1);

                //画出敌人坦克所有子弹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    //取出子弹
                    Shot shot = enemyTank.shots.get(j);
                    //绘制未销毁的子弹
                    if (shot != null && shot.isLive()) {
                        g.fill3DRect(shot.getX(), shot.getY(), 3, 3, false);
                    } else {
                        //移除销毁的子弹
                        enemyTank.shots.remove(shot);
                    }
                }
            } else {
                //移除已销毁的敌人坦克
                enemyTanks.remove(enemyTank);
            }
        }
    }


    /**
     * @param x         坦克左上角X坐标
     * @param y         坦克左上角Y坐标
     * @param g         画笔
     * @param direction 坦克方向（上下左右 => 0、1、2、3）
     * @param type      坦克类型【0代表玩家自己，1代表敌人】
     */
    public void drawTank(int x, int y, Graphics g, int direction, int type) {
        //根据不同类型，给坦克设置不同颜色
        switch (type) {
            /**
             * 0 代表玩家自己
             */
            case 0:
                g.setColor(Color.orange);
                break;

            case 1:
                g.setColor(Color.red);
                break;
        }

        //根据不同方向，绘制坦克
        switch (direction) {
            /**
             * 0 代表方向向上
             */
            case 0:
                //画坦克左边轮子
                g.fill3DRect(x, y, 10, 60, false);
                //画坦克右边轮子
                g.fill3DRect(x + 30, y, 10, 60, false);
                //画坦克身体
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                //画坦克身体上的圆盖
                g.fillOval(x + 10, y + 20, 20, 20);
                //画坦克炮管
                g.drawLine(x + 20, y + 30, x + 20, y);
                break;
            /**
             * 1 =》 向下
             */
            case 1:
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 30, x + 20, y + 60);
                break;
            /**
             * 2 =》 向左
             */
            case 2:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);
                break;
            /**
             * 3 =》 向右
             */
            case 3:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;

            default:
                break;
        }
    }


    /**
     * 敌方坦克的攻击
     */
    public void enemyAttack() {
        //遍历所有坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            //取出坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            if (enemyTank != null && enemyTank.isLive()) {
                //遍历该坦克下所有子弹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    //取出子弹
                    Shot shot = enemyTank.shots.get(j);
                    if (shot != null && shot.isLive()) {
                        //调用击中坦克方法
                        hitTank(shot, myTank);
                    } else {
                        //移除已销毁的子弹
                        enemyTank.shots.remove(shot);
                    }
                }
            } else {
                //移除已销毁的坦克
                enemyTanks.remove(enemyTank);
            }
        }
    }

    /**
     * 玩家坦克的攻击
     */
    public void myTankAttack() {
        //判断玩家子弹击中敌人坦克
        for (int i = 0; i < myTank.shots.size(); i++) {
            Shot shot = myTank.shots.get(i);
            if (shot != null && shot.isLive()) {
                //变量所有敌人坦克
                for (int j = 0; j < enemyTanks.size(); j++) {
                    EnemyTank enemyTank = enemyTanks.get(j);
                    if (enemyTank != null && enemyTank.isLive()) {
                        hitTank(shot, enemyTank);
                    } else {
                        //移除已销毁的坦克
                        enemyTanks.remove(enemyTank);
                    }
                }
            } else {
                //移除已销毁的子弹
                myTank.shots.remove(shot);
            }
        }
    }


    /**
     * 子弹打中坦克
     *
     * @param s    子弹
     * @param tank 被打中的坦克
     * @param <T>  基本坦克类型
     */
    public <T extends Tank> void hitTank(Shot s, T tank) {
        //判断子弹，击中坦克
        switch (tank.getDirection()) {
            //方向 上
            case 0:
                //方向 下
            case 1:
                if (s.getX() >= tank.getX() && s.getX() <= tank.getX() + 40
                        && s.getY() >= tank.getY() && s.getY() <= tank.getY() + 60) {
                    //设置子弹销毁
                    s.setLive(false);
                    //设置坦克销毁
                    tank.setLive(false);
                    //将被击中的敌人坦克从集合中移除
                    if (tank instanceof EnemyTank) {
                        enemyTanks.remove(tank);
                        //玩家击败坦克数量+1
                        BattleInfo.addDefeatEnemyTank();
                    }

                    //创建一个Bomb对象，加入到bombs集合中
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;

            //方向 左
            case 2:
                //方向 右
            case 3:
                if (s.getX() >= tank.getX() && s.getX() <= tank.getX() + 60
                        && s.getY() >= tank.getY() && s.getY() <= tank.getY() + 40) {
                    s.setLive(false);
                    tank.setLive(false);
                    //将被击中的敌人坦克从集合中移除
                    if (tank instanceof EnemyTank) {
                        enemyTanks.remove(tank);
                        //玩家击败坦克数量+1
                        BattleInfo.addDefeatEnemyTank();
                    }

                    //创建一个Bomb对象，加入到bombs集合中
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 处理坦克移动【上下左右 => WSAD】
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                //修改方向
                myTank.setDirection(0);
                //坦克向上移动
                myTank.moveUp();
                break;
            case KeyEvent.VK_S:
                myTank.setDirection(1);
                myTank.moveDown();
                break;
            case KeyEvent.VK_A:
                myTank.setDirection(2);
                myTank.moveLeft();
                break;
            case KeyEvent.VK_D:
                myTank.setDirection(3);
                myTank.moveRight();
                break;
            default:
                break;
        }

        //监听键盘J事件 => 发射子弹
        if (e.getKeyCode() == KeyEvent.VK_J) {
            myTank.shot();
        }

        //事件触发，让面板重绘
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * 开启线程不断重绘面板
     */
    @Override
    public void run() {
        //每隔多少毫秒，重绘面板
        while (true) {

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //玩家坦克的攻击
            myTankAttack();

            //敌人坦克的攻击
            enemyAttack();
            this.repaint();
        }
    }


    /**
     * 判断移动的坦克是否将要与战场中的坦克重叠
     *
     * @param tank      移动中的坦克
     * @param direction 移动的方向
     * @param <T>       基本坦克类型
     * @return 如果将要重叠就返回true，反之返回false
     */
    public static <T extends Tank> boolean tankOverlap(T tank, int direction) {

        //判断移动的坦克的方向
        switch (direction) {
            //向上移动
            case 0:
                //计算移动的坦克移动后的Y、X坐标
                int upY = tank.getY() - 1;
                int upX = tank.getX() + 40;

                //遍历所有敌人坦克进行比较
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //取出敌人坦克，并比较活着的坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    if (enemyTank != null && enemyTank.isLive()) {
                        //跳出本次循环继续下次循环，不与自己比较
                        if (enemyTank.equals(tank)) continue;

                        //获取目标坦克的方向
                        int enemyTankDirection = enemyTank.getDirection();
                        if (enemyTankDirection == 0 || enemyTankDirection == 1) {
                            //目标坦克朝向是上下
                            if (upY >= enemyTank.getY() && upY <= (enemyTank.getY() + 60)
                                    && (tank.getX() >= enemyTank.getX() && tank.getX() <= (enemyTank.getX() + 40)
                                    || upX >= enemyTank.getX() && upX <= (enemyTank.getX() + 40)))
                                return true;
                        } else if (enemyTankDirection == 2 || enemyTankDirection == 3) {
                            //目标坦克朝向是左右
                            if (upY >= enemyTank.getY() && upY <= (enemyTank.getY() + 40)
                                    && (tank.getX() >= enemyTank.getX() && tank.getX() <= (enemyTank.getX() + 60))
                                    || upX >= enemyTank.getX() && upX <= (enemyTank.getX() + 60))
                                return true;
                        }

                    } else {
                        //移除已销毁的坦克
                        enemyTanks.remove(enemyTank);
                    }
                }

                if (tank instanceof EnemyTank) {
                    //如果该移动的坦克是敌人坦克，就要判断与玩家的坦克是否重叠
                    if (myTank.getDirection() == 0 || myTank.getDirection() == 1) {
                        //玩家坦克朝向是上下
                        if (upY >= myTank.getY() && upY <= (myTank.getY() + 60)
                                && (tank.getX() >= myTank.getX() && tank.getX() <= (myTank.getX() + 40)
                                || upX >= myTank.getX() && upX <= (myTank.getX() + 40)))
                            return true;
                    } else if (myTank.getDirection() == 2 || myTank.getDirection() == 3) {
                        //玩家坦克朝向是左右
                        if (upY >= myTank.getY() && upY <= (myTank.getY() + 40)
                                && (tank.getX() >= myTank.getX() && tank.getX() <= (myTank.getX() + 60)
                                || upX >= myTank.getX() && upX <= (myTank.getX() + 60)))
                            return true;
                    }
                }
                break;

            //向下移动
            case 1:

                //计算移动的坦克移动后的Y、x坐标
                int downY = tank.getY() + 60 + 1;
                int downX = tank.getX() + 40;

                //遍历所有敌人坦克进行比较
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //取出敌人坦克，并比较活着的坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    if (enemyTank != null && enemyTank.isLive()) {
                        //跳出本次循环继续下次循环，不与自己比较
                        if (enemyTank.equals(tank)) continue;

                        //获取目标坦克的方向
                        int enemyTankDirection = enemyTank.getDirection();
                        if (enemyTankDirection == 0 || enemyTankDirection == 1) {
                            //目标坦克朝向是上下
                            if (downY >= enemyTank.getY() && downY <= (enemyTank.getY() + 60)
                                    && (tank.getX() >= enemyTank.getX() && tank.getX() <= (enemyTank.getX() + 40)
                                    || downX >= enemyTank.getX() && downX <= (enemyTank.getX() + 40)))
                                return true;
                        } else if (enemyTankDirection == 2 || enemyTankDirection == 3) {
                            //目标坦克朝向是左右
                            if (downY >= enemyTank.getY() && downY <= (enemyTank.getY() + 40)
                                    && (tank.getX() >= enemyTank.getX() && tank.getX() <= (enemyTank.getX() + 60))
                                    || downX >= enemyTank.getX() && downX <= (enemyTank.getX() + 60))
                                return true;
                        }

                    } else {
                        //移除已销毁的坦克
                        enemyTanks.remove(enemyTank);
                    }
                }

                if (tank instanceof EnemyTank) {
                    //如果该移动的坦克是敌人坦克，就要判断与玩家的坦克是否重叠
                    if (myTank.getDirection() == 0 || myTank.getDirection() == 1) {
                        //玩家坦克朝向是上下
                        if (downY >= myTank.getY() && downY <= (myTank.getY() + 60)
                                && (tank.getX() >= myTank.getX() && tank.getX() <= (myTank.getX() + 40)
                                || downX >= myTank.getX() && downX <= (myTank.getX() + 40)))
                            return true;
                    } else if (myTank.getDirection() == 2 || myTank.getDirection() == 3) {
                        //玩家坦克朝向是左右
                        if (downY >= myTank.getY() && downY <= (myTank.getY() + 40)
                                && (tank.getX() >= myTank.getX() && tank.getX() <= (myTank.getX() + 60)
                                || downX >= myTank.getX() && downX <= (myTank.getX() + 60)))
                            return true;
                    }
                }
                break;

            //向左移动
            case 2:

                //计算移动的坦克移动后的Y、X坐标
                int leftY = tank.getY() + 40;
                int leftX = tank.getX() - 1;

                //遍历所有敌人坦克进行比较
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //取出敌人坦克，并比较活着的坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    if (enemyTank != null && enemyTank.isLive()) {
                        //跳出本次循环继续下次循环，不与自己比较
                        if (enemyTank.equals(tank)) continue;

                        //获取目标坦克的方向
                        int enemyTankDirection = enemyTank.getDirection();
                        if (enemyTankDirection == 0 || enemyTankDirection == 1) {
                            //目标坦克朝向是上下
                            if (leftX >= enemyTank.getX() && leftX <= (enemyTank.getX() + 40)
                                    && (tank.getY() >= enemyTank.getY() && tank.getY() <= (enemyTank.getY() + 60)
                                    || leftY >= enemyTank.getY() && leftY <= (enemyTank.getY() + 60)))
                                return true;
                        } else if (enemyTankDirection == 2 || enemyTankDirection == 3) {
                            //目标坦克朝向是左右
                            if (leftX >= enemyTank.getX() && leftX <= (enemyTank.getX() + 60)
                                    && (tank.getY() >= enemyTank.getY() && tank.getY() <= (enemyTank.getY() + 40)
                                    || leftY >= enemyTank.getY() && leftY <= (enemyTank.getY() + 40)))
                                return true;
                        }

                    } else {
                        //移除已销毁的坦克
                        enemyTanks.remove(enemyTank);
                    }
                }

                if (tank instanceof EnemyTank) {
                    //如果该移动的坦克是敌人坦克，就要判断与玩家的坦克是否重叠
                    if (myTank.getDirection() == 0 || myTank.getDirection() == 1) {
                        //玩家坦克朝向是上下
                        if (leftX >= myTank.getX() && leftX <= (myTank.getX() + 40)
                                && (tank.getY() >= myTank.getY() && tank.getY() <= (myTank.getY() + 60)
                                || leftY >= myTank.getY() && leftY <= (myTank.getY() + 60)))
                            return true;
                    } else if (myTank.getDirection() == 2 || myTank.getDirection() == 3) {
                        //玩家坦克朝向是左右
                        if (leftX >= myTank.getX() && leftX <= (myTank.getX() + 60)
                                && (tank.getY() >= myTank.getY() && tank.getY() <= (myTank.getY() + 40)
                                || leftY >= myTank.getY() && leftY <= (myTank.getY() + 40)))
                            return true;
                    }
                }
                break;

            //向右移动
            case 3:

                //计算移动的坦克移动后的Y、X坐标
                int rightY = tank.getY() + 40;
                int rightX = tank.getX() + 60 + 1;

                //遍历所有敌人坦克进行比较
                for (int i = 0; i < enemyTanks.size(); i++) {
                    //取出敌人坦克，并比较活着的坦克
                    EnemyTank enemyTank = enemyTanks.get(i);
                    if (enemyTank != null && enemyTank.isLive()) {
                        //跳出本次循环继续下次循环，不与自己比较
                        if (enemyTank.equals(tank)) continue;

                        //获取目标坦克的方向
                        int enemyTankDirection = enemyTank.getDirection();
                        if (enemyTankDirection == 0 || enemyTankDirection == 1) {
                            //目标坦克朝向是上下
                            if (rightX >= enemyTank.getX() && rightX <= (enemyTank.getX() + 40)
                                    && (tank.getY() >= enemyTank.getY() && tank.getY() <= (enemyTank.getY() + 60)
                                    || rightY >= enemyTank.getY() && rightY <= (enemyTank.getY() + 60)))
                                return true;
                        } else if (enemyTankDirection == 2 || enemyTankDirection == 3) {
                            //目标坦克朝向是左右
                            if (rightX >= enemyTank.getX() && rightX <= (enemyTank.getX() + 60)
                                    && (tank.getY() >= enemyTank.getY() && tank.getY() <= (enemyTank.getY() + 40)
                                    || rightY >= enemyTank.getY() && rightY <= (enemyTank.getY() + 40)))
                                return true;
                        }

                    } else {
                        //移除已销毁的坦克
                        enemyTanks.remove(enemyTank);
                    }
                }

                if (tank instanceof EnemyTank) {
                    //如果该移动的坦克是敌人坦克，就要判断与玩家的坦克是否重叠
                    if (myTank.getDirection() == 0 || myTank.getDirection() == 1) {
                        //玩家坦克朝向是上下
                        if (rightX >= myTank.getX() && rightX <= (myTank.getX() + 40)
                                && (tank.getY() >= myTank.getY() && tank.getY() <= (myTank.getY() + 60)
                                || rightY >= myTank.getY() && rightY <= (myTank.getY() + 60)))
                            return true;
                    } else if (myTank.getDirection() == 2 || myTank.getDirection() == 3) {
                        //玩家坦克朝向是左右
                        if (rightX >= myTank.getX() && rightX <= (myTank.getX() + 60)
                                && (tank.getY() >= myTank.getY() && tank.getY() <= (myTank.getY() + 40)
                                || rightY >= myTank.getY() && rightY <= (myTank.getY() + 40)))
                            return true;
                    }
                }
                break;
        }
        return false;
    }
}
