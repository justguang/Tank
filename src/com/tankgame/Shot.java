package com.tankgame;

/**
 * @author justguang
 * @version 1.0
 * @date 2021/9/25
 * @description 发射子弹
 */
@SuppressWarnings("all")
public class Shot implements Runnable {

    private int x;//子弹x坐标
    private int y;//子弹y坐标
    private int direction;//子弹方向
    private int speed = 3;//子弹移速【默认3】

    private boolean isLive = true;//子弹是否存活【默认true存活】

    public Shot(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /**
     * 子弹的运动
     */
    @Override
    public void run() {
        while (true) {
            //子弹休眠
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //根据方向改变xy坐标【上下左右 => 0 1 2 3】
            switch (direction) {
                //上
                case 0:
                    y -= speed;
                    break;
                //下
                case 1:
                    y += speed;
                    break;
                //左
                case 2:
                    x -= speed;
                    break;
                //右
                case 3:
                    x += speed;
                    break;
            }

            //设置子弹销毁条件
            if (!(x >= 1 && x <= 999 && y >= 1 && y <= 749 && isLive)) {
                isLive = false;
                break;
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}
