# Tank
自己编写的一个Java小游戏，《塔克大战》，仅供学习参考


### [Tank_v1.0](https://github.com/justguang/Tank/releases/tag/Tank_v1.0) 

玩法：键盘操作
移动：上下左右 => WSAD
攻击：J

 键盘操作要求输入法是英文的键盘 

实现功能：
1. 英文键盘下WSAD移动，J攻击(发射子弹)
2. 坦克的子弹发射
3. 敌方坦克随机方向移动，并随机发射子弹
4. 子弹打中坦克销毁
5. 被子弹打中的坦克销毁、播放爆炸效果
6. 坦克移动边界的限定



技术支持：
1. 使用了 javax.swing 包下的JFrame，创建游戏窗体，绘制战斗场景、坦克、子弹、爆炸效果。
2. 使用 java.awt.event 包下的相关事件，监听窗体的关闭，监听键盘按键。
3. 使用 线程编程 实现 子弹移动，敌方坦克的自主移动和发送子弹，战场不断刷新(面板重绘)。
4. 使用 Vector线程安全的集合，管理敌方坦克、子弹、爆炸特效等对象的管理。
5. 使用 继承关系，实现Tank(坦克基类)、MyTank(玩家坦克)、EnemyTank(敌方坦克)的对象之间的关系。
5. 使用泛型方法，实现子弹打中坦克，并做击中处理。

<br/>

《坦克大战 v1.0》展示图：

<img src="https://img2020.cnblogs.com/blog/2518177/202109/2518177-20210928122910177-173999971.png" alt="展示图1" style="zoom:50%;" />

<br/>
<br/>

<img src="https://img2020.cnblogs.com/blog/2518177/202109/2518177-20210928123015704-625622252.png" alt="展示图2" style="zoom:50%;" />



<br/>
<br/>
<br/>





### [Tank_v1.1](https://github.com/justguang/Tank/releases/tag/Tank_v1.1) 
实现功能：
1. 新增几种背景音乐随机循环播放。
2. 新增战场上的坦克之间不重叠。
3. 修改子弹、坦克的运动速度。

技术支持：
1. 使用 java.util.concurrent.atomic包下的 CAS功能与守护线程配合 实现循环监听bgm，
并依赖外部包 [jlayer-1.0.1.jar](https://mvnrepository.com/artifact/javazoom/jlayer)
与Math下的随机功能，实现随机播放bgm。
2. 使用 java.io包下的 I/O流技术 读写文件，实现战斗数据保存和加载。
3. 使用线程安全数据+泛型+逻辑判断，纯坐标计算实现坦克至之间不重叠。

<br/>

《坦克大战 v1.1》展示图：

<img src="https://img2020.cnblogs.com/blog/2518177/202109/2518177-20210928123718123-1824114823.png" alt="展示图1" style="zoom:50%;" />

<br/>
<br/>

<img src="https://img2020.cnblogs.com/blog/2518177/202109/2518177-20210928123736018-647094825.png" alt="展示图2" style="zoom:50%;" />


<br/>
<br/>
<br/>
<br/>


### [Tank_v1.2](https://github.com/justguang/Tank/releases/tag/Tank_v1.2) 
对比Tank_v1.1区别：
1. 实现继续游戏、新游戏功能
2. 添加游戏标题

<br/>

《坦克大战 v1.2》展示图：

<img src="https://img2020.cnblogs.com/blog/2518177/202109/2518177-20210929122838695-111929703.png" alt="展示图1" style="zoom:50%;" />

<br/>


<br/>
<br/>
<br/>
<br/>
**********************************************************************