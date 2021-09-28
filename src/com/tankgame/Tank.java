package com.tankgame;


/**
 * @author justguang
 * @version 1.0
 * @date 2021/9/25
 * @description 基础坦克
 */
@SuppressWarnings("all")
public class Tank {
    /**
     * 坦克的x坐标
     */
    private int x;
    /**
     * 坦克的y坐标
     */
    private int y;

    /**
     * 坦克的方向【上下左右 => 0、1、2、3，默认0】
     */
    private int direction;
    /**
     * 坦克移动的速度【默认2】
     */
    private int speed = 2;

    /**
     * 是否存活【默认true存活】
     */
    private boolean isLive = true;

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tank(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /**
     * ps：
     * 坦克移动要求
     * 1.坦克是存活状态
     * 2.移动范围在战场内，不能出界【边界框】
     * 3.不能与其他坦克重叠
     */

    /**
     * 向上移动
     */
    public void moveUp() {
        if (isLive && y > 0) {
            if (MyPanel.tankOverlap(this, 0)) return;//防止与其他坦克重叠
            y -= speed;
        }
    }

    /**
     * 向下移动
     */
    public void moveDown() {

        if (isLive && (y + 60) < 750) {
            if (MyPanel.tankOverlap(this, 1)) return;//防止与其他坦克重叠
            y += speed;
        }
    }

    /**
     * 向左移动
     */
    public void moveLeft() {
        if (isLive && x > 0) {
            if (MyPanel.tankOverlap(this, 2)) return;//防止与其他坦克重叠
            x -= speed;
        }
    }

    /**
     * 向右移动
     */
    public void moveRight() {
        if (isLive && (x + 60) < 1000) {
            if (MyPanel.tankOverlap(this, 3)) return;//防止与其他坦克重叠
            x += speed;
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
