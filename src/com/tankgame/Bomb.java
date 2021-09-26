package com.tankgame;

/**
 * @author justguang
 * @version 1.0
 * @date 2021/9/25
 * @description 炸弹
 */
@SuppressWarnings("all")
public class Bomb {
    int x;//炸弹x坐标
    int y;//炸弹y坐标
    int life = 60;//炸弹生命周期
    boolean isLive = true;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 减少爆炸生命
     */
    public void lifeDown() {
        if (life > 0) {
            life--;
        } else {
            isLive = false;
        }
    }
}
