# 🧠 开发提示词记录 (Prompts)

本文档记录了开发 `9x9 贪吃蛇小游戏` 过程中使用的所有提示词和AI辅助指令，方便复现开发过程和后续迭代。

## 📋 提示词总览

| 阶段 | 提示词 | 日期 | 状态 |
|------|--------|------|------|
| 1 | 初始需求 | 2026-06-25 | ✅ 已完成 |
| 2 | 速度调整 | 2026-06-25 | ✅ 已完成 |
| 3 | 设置按钮 | 2026-06-25 | ✅ 已完成 |
| 4 | 快捷键扩展 | 2026-06-25 | ✅ 已完成 |
| 5 | 网格升级 | 2026-06-25 | ✅ 已完成 |
| 6 | 中文修复 | 2026-06-25 | ✅ 已完成 |
| 7 | 操作说明 | 2026-06-25 | ✅ 已完成 |

---

## 🎯 阶段一：初始需求

### 用户提示词
### AI响应
- 生成完整的 `SnakeGame.java` 源代码
- 提供 `README.md` 和 `PROMPT.md` 模板
- 实现基础功能：7x7网格、键盘控制、碰撞检测

### 核心代码
```java
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int GRID_SIZE = 7;
    private static final int GAME_SPEED = 300;
    // ... 基础游戏逻辑
}
⚡ 阶段二：速度调整
用户提示词
text
将速度放慢一半
AI响应
将 GAME_SPEED 从 300ms 改为 600ms

保持其他功能不变

提供速度自定义说明

代码变更
diff
- private static final int GAME_SPEED = 300;    // 毫秒
+ private static final int GAME_SPEED = 600;    // 毫秒（速度放慢一半）
⚙️ 阶段三：设置按钮
用户提示词
text
增加设置按钮
AI响应
添加控制面板（顶部工具栏）

实现速度下拉选择框（5档速度）

实现重置按钮

创建设置对话框

游戏暂停/恢复机制

新增功能
java
// 控制面板
private void setupControlPanel() {
    // 速度下拉框
    String[] speedOptions = {"很慢", "慢", "适中", "快", "很快"};
    speedComboBox = new JComboBox<>(speedOptions);
    
    // 设置按钮
    settingsButton = new JButton("⚙ 设置");
    settingsButton.addActionListener(e -> showSettingsDialog());
    
    // 重置按钮
    resetButton = new JButton("🔄 重置");
}
⌨️ 阶段四：快捷键扩展
用户提示词
text
将数字键1设置为设置快捷键并且扩充至9x9
AI响应
添加数字键1打开设置功能

将网格从 7x7 扩展到 9x9

调整单元格大小（60→55）

更新网格信息显示

代码变更
diff
// 网格升级
- private static final int GRID_SIZE = 7;
+ private static final int GRID_SIZE = 9;
- private static final int CELL_SIZE = 60;
+ private static final int CELL_SIZE = 55;

// 快捷键
+ if (key == KeyEvent.VK_1) {
+     showSettingsDialog();
+     return;
+ }
🌐 阶段五：中文修复
用户提示词
text
将中文乱码修复
AI响应
设置全局字体为微软雅黑

为所有UI组件指定中文字体

添加系统编码设置

实现字体递归设置方法

提供备选字体方案

核心解决方案
java
private void setUIFont() {
    Font defaultFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
    UIManager.put("Button.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
    UIManager.put("Label.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
    // ... 其他组件
}

// 主方法中添加
System.setProperty("file.encoding", "UTF-8");
📖 阶段六：操作说明
用户提示词
text
添加一份游戏操作说明
AI响应
添加帮助按钮（❓ 帮助 (H)）

创建完整的帮助对话框

包含：游戏目标、控制方式、游戏规则、速度设置、游戏技巧

支持H键快捷键

增加WASD控制支持

帮助内容结构
java
private void showHelpDialog() {
    // 游戏目标
    // 键盘控制（方向键 + WASD）
    // 游戏规则
    // 速度档位说明
    // 游戏技巧
}
增强控制
java
// 同时支持方向键和WASD
switch (key) {
    case KeyEvent.VK_UP:
    case KeyEvent.VK_W:
        // 向上移动
        break;
    // ...
}
🎯 最终功能清单
核心功能
9x9 网格游戏

键盘控制（方向键 + WASD）

碰撞检测（墙壁 + 自身）

食物生成

胜利判定（占满81格）

游戏重置

用户界面
顶部控制面板

速度调节下拉框

设置按钮（⚙）

帮助按钮（❓）

重置按钮（🔄）

游戏状态显示

交互优化
快捷键系统（1, H, R）

设置对话框

帮助对话框

游戏暂停/恢复

中文显示支持

性能优化
Timer游戏循环

高效渲染

内存管理

💡 开发技巧总结
1. 字体处理
java
// 跨平台中文字体处理
Font font = new Font("Microsoft YaHei", Font.PLAIN, 14);
// 备选方案
Font fallback = new Font("SimSun", Font.PLAIN, 14);
2. 游戏循环
java
// 使用Swing Timer而非Thread
Timer timer = new Timer(delay, this);
timer.start();
3. 键盘事件
java
// 支持多个按键
if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
    // 处理逻辑
}
4. 对话框管理
java
// 模态对话框 + 游戏暂停
JDialog dialog = new JDialog(frame, "Title", true);
// 关闭时恢复游戏
dialog.addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent e) {
        running = true;
    }
});
🚀 未来优化方向
待实现功能
最高分记录

游戏音效

障碍物模式

双人模式

移动端适配

技术改进
使用 MVC 模式重构

添加单元测试

构建自动化

打包为可执行JAR

📚 参考资源
官方文档
Java Swing Tutorial

Java AWT Documentation

相关项目
Classic Snake Game

Java Game Development

📝 版本信息
版本	日期	作者	更新内容
v1.0	2026-06-25	AI Assistant	初始版本创建
v1.1	2026-06-25	AI Assistant	添加设置功能
v1.2	2026-06-25	AI Assistant	升级9x9网格
v1.3	2026-06-25	AI Assistant	修复中文乱码
v1.4	2026-06-25	AI Assistant	添加操作说明
