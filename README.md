# 🐍 9x9 贪吃蛇小游戏

一个基于 Java Swing 实现的经典贪吃蛇游戏，采用 9x9 紧凑网格设计，支持速度调节、快捷键操作和完整的游戏说明。

![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![Swing](https://img.shields.io/badge/Swing-GUI-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

## ✨ 游戏特性

- **9x9 网格**：相比传统贪吃蛇，9x9 网格更具挑战性，共81格
- **多种控制方式**：支持方向键和 WASD 两种控制方式
- **速度调节**：5档速度可选，从很慢到很快
- **快捷键支持**：1键设置、H键帮助、R键重置
- **游戏说明**：内置完整的操作指南和游戏技巧
- **中文支持**：完美显示中文，无乱码问题
- **视觉反馈**：蛇身渐变效果，清晰的游戏状态提示

## 🎮 游戏预览

### 主界面
┌──────────────────────────────────────────────────────────┐
│ 按 1 设置 | 按 H 帮助 速度: [▼ 慢] [⚙设置] [❓帮助] [🔄重置] │
├──────────────────────────────────────────────────────────┤
│ │
│ 9x9 游戏区域 │
│ 🟢 蛇身 │
│ 🔴 食物 │
│ │
│ 长度: 3 / 81 │
└──────────────────────────────────────────────────────────┘

text

### 功能对话框
- **设置面板**：调整游戏速度，查看快捷键
- **帮助面板**：完整的游戏操作指南和技巧

## 📥 安装与运行

### 前提条件
- Java JDK 11 或更高版本
- Git（可选，用于克隆仓库）

### 方法一：命令行运行

```bash
# 克隆仓库
git clone https://github.com/yourusername/9x9-snake-game.git

# 进入项目目录
cd 9x9-snake-game

# 编译
javac -d out src/SnakeGame.java

# 运行
java -cp out org.example.SnakeGame
方法二：IDE运行（推荐）
使用 IntelliJ IDEA 或 Eclipse 打开项目

找到 src/org/example/SnakeGame.java

右键 → Run 'SnakeGame.main()'

方法三：JAR包运行
bash
# 如果已打包成JAR
java -jar 9x9-snake-game.jar
🎯 游戏规则
基本规则
🟢 蛇会自动朝当前方向前进

🔴 吃到红色食物，蛇身长度 +1

❌ 撞到墙壁或自己的身体，游戏结束

🏆 蛇身占满全部 81 个格子，获得胜利！

控制方式
按键	功能	说明
↑ / W	向上移动	蛇头朝上
↓ / S	向下移动	蛇头朝下
← / A	向左移动	蛇头朝左
→ / D	向右移动	蛇头朝右
1	打开设置	调整游戏速度
H	帮助说明	查看操作指南
R	重置游戏	重新开始
速度档位
档位	速度	适用场景
很慢	1000ms/步	新手入门
慢	600ms/步	默认推荐
适中	400ms/步	标准体验
快	250ms/步	挑战模式
很快	150ms/步	专家模式
🛠️ 技术架构
技术栈
语言：Java 11+

GUI框架：Java Swing + AWT

集合框架：Java Collections (LinkedList)

多线程：javax.swing.Timer

项目结构
text
9x9-snake-game/
├── src/
│   └── org/
│       └── example/
│           └── SnakeGame.java    # 主程序（含所有功能）
├── README.md                      # 项目说明
├── PROMPT.md                      # AI提示词记录
├── .gitignore                     # Git忽略文件
└── LICENSE                        # MIT许可证
📖 代码亮点
1. 完整的游戏逻辑
java
// 蛇的移动、碰撞检测、食物生成
private void moveSnake() {
    // 计算新头部位置
    // 检测碰撞（墙壁/自身）
    // 更新蛇身
    // 生成新食物
}
2. 流畅的UI交互
java
// 使用Timer实现游戏循环
timer = new Timer(currentSpeed, this);
timer.start();
3. 中文乱码修复
java
// 设置全局字体为微软雅黑
UIManager.put("Button.font", new Font("Microsoft YaHei", ...));
4. 快捷键系统
java
// 支持数字键1、H键、R键等快捷键
if (key == KeyEvent.VK_1) showSettingsDialog();
if (key == KeyEvent.VK_H) showHelpDialog();
if (key == KeyEvent.VK_R) initGame();
🎨 界面设计
颜色方案
背景：黑色 (#000000)

蛇头：亮绿色 (#00FF64)

蛇身：深绿色 (#00B432)

食物：红色 (#FF0000)

网格：深灰色 (#404040)

控制栏：深灰色 (#282828)

字体方案
主字体：微软雅黑 (Microsoft YaHei)

备选字体：宋体 (SimSun)

游戏标题：粗体 24pt

状态信息：粗体 24pt

常规文本：常规 14pt

🔧 自定义配置
修改网格大小
java
// 在 SnakeGame.java 中修改
private static final int GRID_SIZE = 9;  // 改为其他数值
修改单元格大小
java
private static final int CELL_SIZE = 55; // 调整像素值
添加新的速度档位
java
// 在 speedOptions 数组中添加
String[] speedOptions = {"自定义 (500ms)", ...};
// 在 getSpeedValue 方法中添加对应的case
📝 版本历史
v1.0.0 (2026-06-25)
✅ 初始版本发布

✅ 9x9网格贪吃蛇

✅ 速度调节功能

✅ 快捷键支持

✅ 完整操作说明

✅ 中文显示支持

🙏 致谢
Java Swing 官方文档

经典贪吃蛇游戏设计

所有贡献者和用户

🎉 享受游戏！如果喜欢这个项目，请给个 Star ⭐ 支持一下！
