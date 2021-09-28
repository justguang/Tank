package com.tankgame;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author justguang
 * @version 1.1
 * @date 2021/9/27
 * @description 记录战斗数据
 */
@SuppressWarnings("all")
public class BattleInfo {

    /**
     * 玩家击败敌方坦克数量
     */
    private static int defeatEnemyTankNum = 0;

    //设置玩家击败的敌方坦克
    public static void setDefeatEnemyTankNum(int defeatEnemyTankNum) {
        BattleInfo.defeatEnemyTankNum = defeatEnemyTankNum;
    }

    //玩家击败敌方坦克数量+1
    public static void addDefeatEnemyTank() {
        BattleInfo.defeatEnemyTankNum++;
    }

    //获取玩家击败的坦克数量
    public static int getDefeatEnemyTankNum() {
        return BattleInfo.defeatEnemyTankNum;
    }


    /**
     * *********************I/O流用于文件读写***********************
     */
    private static BufferedWriter bw = null;

    //战斗信息文件路径
    private static String battleRecordFilePath;
    private static String fileName = "/record.properties";

    /**
     * 检查战斗记录文件，不存在就创建
     */
    public static void checkBattleRecordFileExists() {

        boolean isExists = false;

        //没有就创建对应文件到包同级别文件夹下
        battleRecordFilePath = System.getProperty("user.dir").replace("\\", "/");
        battleRecordFilePath = battleRecordFilePath + fileName;

        File file = new File(battleRecordFilePath);


        //判断战斗记录文件是否存在
        if (file != null && file.exists() && file.isFile()) {
            System.out.println("战斗记录文件存在，开始读取战斗记录……");
            isExists = true;
        }

        if (!isExists) {
            //战斗记录文件不存在，创建
            try {
                System.out.println("战斗记录文件不存在，正在创建……");
                if (file.createNewFile()) {
                    System.out.println("文件创建成功！");
                } else {
                    System.out.println("文件创建失败！！！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 加载战斗记录
     */
    public static void loadBattleRecord() {
        try {

            //加载properties文件
            Properties properties = new Properties();
            properties.load(new FileReader(battleRecordFilePath));

            System.out.println("加载上一局的战斗数据 => " + properties.toString());


            //一、读取击败敌方坦克数量
            String defeatEnemyTank = properties.getProperty("defeatEnemyTankNum");
            if (defeatEnemyTank != null && !defeatEnemyTank.trim().isEmpty()) {
                BattleInfo.defeatEnemyTankNum = Integer.valueOf(defeatEnemyTank);
            }


            //二、读取玩家坦克位置和方向
            String myTank = properties.getProperty("myTank");
            if (myTank != null && !myTank.equals("-1")) {
                //如果值不为-1，代表玩家还活着
                System.out.println("加载到玩家的坦克信息：" + myTank);
                //以下划线分割，获取坦克位置和方向【X_Y_direction】
                String[] strArr = myTank.split("_");
                //根据获取的位置和方向实例化玩家坦克
                MyPanel.myTank = new CustomTank(Integer.valueOf(strArr[0].trim()), Integer.valueOf(strArr[1].trim()), Integer.valueOf(strArr[2].trim()));
            }


            //三、读取敌方坦克位置和方向
            String enemyTanks = properties.getProperty("enemyTanks");
            if (enemyTanks != null && !enemyTanks.equals("-1")) {
                //如果值不为-1，代表还存有敌方的坦克
                System.out.println("加载到敌人的坦克信息：" + enemyTanks);

                //初始化面板时不再创建敌人的坦克
                MyPanel.enemyTankNum = 0;

                //去掉[]符号
                enemyTanks = enemyTanks.replace('[', ' ');
                enemyTanks = enemyTanks.replace(']', ' ');
                //以，分割，得到一个具体坦克位置和方向 => X_Y_direction
                String[] split = enemyTanks.split(",");

                //利用for遍历每个坦克位置和方向信息
                for (int i = 0; i < split.length; i++) {
                    String[] strArr = split[i].split("_");
                    //根据获取的敌人坦克的位置和方向，创建出坦克对象，并添加到集合中
                    EnemyTank enemyTank = new EnemyTank(Integer.valueOf(strArr[0].trim()), Integer.valueOf(strArr[1].trim()), Integer.valueOf(strArr[2].trim()));
                    new Thread(enemyTank).start();//开启线程，让敌人自己移动射击
                    MyPanel.enemyTanks.add(enemyTank);
                }
            }

        } catch (IOException e) {
            System.out.println("未找到以前的战斗记录");
            e.printStackTrace();
        }

    }


    /**
     * 保存战斗信息
     */
    public static void saveBattleInfo() {

        try {

            //加载properties文件
            Properties properties = new Properties();
            properties.load(new FileReader(battleRecordFilePath));

            //一、保存 玩家的击败敌数
            properties.setProperty("defeatEnemyTankNum", String.valueOf(BattleInfo.defeatEnemyTankNum));


            //二、保存玩家坦克的位置坐标和方向,如果已被销毁，值为-1
            if (MyPanel.myTank != null && MyPanel.myTank.isLive()) {
                //下划线分割，X_Y_direction
                String myTankInfo = MyPanel.myTank.getX() + "_" + MyPanel.myTank.getY() + "_" + MyPanel.myTank.getDirection();
                properties.setProperty("myTank", myTankInfo);
            } else {
                properties.setProperty("myTank", "-1");
            }


            //保存敌人坦克的坐标和方向、只保存没有被销毁的坦克
            List list = new ArrayList<>();
            for (int i = 0; i < MyPanel.enemyTanks.size(); i++) {
                EnemyTank enemyTank = MyPanel.enemyTanks.get(i);
                if (enemyTank != null && enemyTank.isLive()) {
                    //下划线分割，X_Y_direction
                    String enemyTankInfo = enemyTank.getX() + "_" + enemyTank.getY() + "_" + enemyTank.getDirection();
                    list.add(enemyTankInfo);
                }
            }


            //三、保存敌方坦克位置方向、如果没有敌方坦克，值为-1
            if (list.size() > 0) {
                properties.setProperty("enemyTanks", list.toString());
            } else {
                properties.setProperty("enemyTanks", "-1");
            }


            System.out.println("保存战斗数据 => " + properties.toString());


            //用FileWriter
            bw = new BufferedWriter(new FileWriter(battleRecordFilePath));

            //保存
            properties.store(bw, Instant.now().toString() + " Save");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    //如果bw不为空，关闭流
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
