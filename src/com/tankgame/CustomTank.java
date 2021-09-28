package com.tankgame;

import java.util.Vector;

/**
 * @author justguang
 * @version 1.0
 * @date 2021/9/25
 * @description 自定义的坦克
 */
@SuppressWarnings("all")
public class CustomTank extends Tank {

    //发射的子弹
    Vector<Shot> shots = new Vector<>();


    public CustomTank(int x, int y) {
        super(x, y);
    }


    public CustomTank(int x, int y, int direction) {
        super(x, y, direction);
    }

    /**
     * 发射子弹
     */
    public void shot() {
        //如果玩家已销毁，不能发射子弹
        if(!isLive())return;

        //判断是否可以发射子弹，子弹数量超过指定数量不能发射
        if (shots.size() > 5) return;

        Shot shot = null;

        //创建shot对象
        //根据坦克位置和方向创建
        switch (getDirection()) {
            //方向 上
            case 0:
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            //下
            case 1:
                shot = new Shot(getX() + 20, getY() + 60, 1);
                break;
            //左
            case 2:
                shot = new Shot(getX(), getY() + 20, 2);
                break;
            //右
            case 3:
                shot = new Shot(getX() + 60, getY() + 20, 3);
                break;
        }

        //将创建好的子弹对象添加到集合中
        if (shot != null)
            shots.add(shot);

        //启动shot线程
        new Thread(shot).start();
    }


}
