package com.tankgame;

import java.util.Random;
import java.util.Vector;

/**
 * @author justguang
 * @version 1.1
 * @date 2021/9/25
 * @description 敌人的坦克
 */
@SuppressWarnings("all")
public class EnemyTank extends Tank implements Runnable {

    /**
     * 存放子弹shot
     */
    Vector<Shot> shots = new Vector<>();

    /**
     * 随机数，用于概率发射子弹
     */
    Random random = new Random();

    public EnemyTank(int x, int y) {
        super(x, y);
    }

    public EnemyTank(int x, int y, int direction) {
        super(x, y, direction);
    }


    /**
     * 实现敌人坦克移动
     */
    @Override
    public void run() {
        while (true) {

            //如果坦克已销毁，跳出
            if (!isLive())
                break;//退出线程

            //根据当前方向移动
            switch (getDirection()) {
                //向上
                case 0:
                    for (int i = 0; i < 30; i++) {
                        //休眠一下
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //向上移动
                        moveUp();
                        //发射子弹
                        enemyTankShot();
                    }

                    break;

                //向下
                case 1:
                    for (int i = 0; i < 30; i++) {
                        //休眠一下
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //向下移动
                        moveDown();
                        //发射子弹
                        enemyTankShot();
                    }

                    break;

                //向左
                case 2:
                    for (int i = 0; i < 30; i++) {
                        //休眠一下
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //向左移动
                        moveLeft();
                        //发射子弹
                        enemyTankShot();
                    }

                    break;

                //向右
                case 3:
                    for (int i = 0; i < 30; i++) {
                        //休眠一下
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //向右移动
                        moveRight();
                        //发射子弹
                        enemyTankShot();
                    }

                    break;
            }

            //随机改变方向
            setDirection((int) (Math.random() * 4));

        }
    }

    /**
     * 发射子弹
     */
    public void enemyTankShot() {

        //如果已经发射子弹数量大于多少就不再发射子弹
        if (shots.size() > 3) return;

        //随机概率发射子弹，概率百分之一
        int i = random.nextInt(100);
        //获取的随机数不等于1 就不发射子弹
        if (i != 51) return;


        Shot s = null;
        //根据当前坦克方向，创建对应的子弹
        switch (getDirection()) {
            //向上
            case 0:
                s = new Shot(getX() + 20, getY(), 0);
                break;
            //向下
            case 1:
                s = new Shot(getX() + 20, getY() + 60, 1);
                break;
            //向左
            case 2:
                s = new Shot(getX(), getY() + 20, 2);
                break;
            //向右
            case 3:
                s = new Shot(getX() + 60, getY() + 20, 3);
                break;
        }

        //将创建好的子弹对象添加到集合中，并启动线程让子弹运动
        if (s != null) {
            shots.add(s);
            new Thread(s).start();
        }
    }
}
