package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * 9x9 贪吃蛇小游戏
 * 使用 Java Swing 实现，网格为 9x9，支持键盘方向键控制。
 * 新增：数字键1打开设置，速度调节，重置功能，游戏操作说明
 */
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // 游戏常量
    private static final int GRID_SIZE = 9;          // 9x9 网格
    private static final int CELL_SIZE = 55;         // 每格像素
    private static final int PANEL_SIZE = GRID_SIZE * CELL_SIZE;
    private static final int DEFAULT_SPEED = 600;    // 默认速度（毫秒）

    // 游戏状态
    private LinkedList<Point> snake;
    private Point food;
    private Direction currentDirection;
    private boolean running;
    private boolean gameOver;
    private boolean gameWon;
    private Timer timer;
    private Random random;
    private int currentSpeed;  // 当前速度

    // UI组件
    private JPanel controlPanel;
    private JButton settingsButton;
    private JLabel speedLabel;
    private JComboBox<String> speedComboBox;
    private JPanel gamePanel;

    // 方向枚举
    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public SnakeGame() {
        // 设置全局字体，解决中文乱码
        setUIFont();

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        random = new Random();
        currentSpeed = DEFAULT_SPEED;
        initGame();
        timer = new Timer(currentSpeed, this);
        timer.start();

        // 添加控制面板
        setupControlPanel();
    }

    /**
     * 设置全局字体，解决中文乱码问题
     */
    private void setUIFont() {
        // 设置默认字体为支持中文的字体
        Font defaultFont = new Font("Microsoft YaHei", Font.PLAIN, 12);
        if (defaultFont.getFamily().equals("Microsoft YaHei")) {
            UIManager.put("Button.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("Label.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("ComboBox.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("TextField.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("TextArea.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("List.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("Table.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("Tree.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("Menu.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("MenuItem.font", new Font("Microsoft YaHei", Font.PLAIN, 14));
            UIManager.put("ToolTip.font", new Font("Microsoft YaHei", Font.PLAIN, 12));
        } else {
            // 备选字体
            try {
                Font font = new Font("SimSun", Font.PLAIN, 14);
                UIManager.put("Button.font", font);
                UIManager.put("Label.font", font);
                UIManager.put("ComboBox.font", font);
            } catch (Exception e) {
                // 如果字体加载失败，使用系统默认
            }
        }
    }

    /**
     * 设置控制面板（包含设置按钮和帮助按钮）
     */
    private void setupControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setBackground(new Color(40, 40, 40));
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        // 快捷键提示
        JLabel shortcutLabel = new JLabel("按 1 设置  |  按 H 帮助");
        shortcutLabel.setForeground(new Color(200, 200, 100));
        shortcutLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        controlPanel.add(shortcutLabel);

        // 分隔线（不可见，用于间距）
        controlPanel.add(new JLabel("  "));

        // 速度标签
        speedLabel = new JLabel("速度:");
        speedLabel.setForeground(Color.WHITE);
        speedLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        controlPanel.add(speedLabel);

        // 速度下拉选择框
        String[] speedOptions = {"很慢 (1000ms)", "慢 (600ms)", "适中 (400ms)", "快 (250ms)", "很快 (150ms)"};
        speedComboBox = new JComboBox<>(speedOptions);
        speedComboBox.setSelectedIndex(1); // 默认选择"慢"
        speedComboBox.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        speedComboBox.addActionListener(e -> changeSpeed());
        controlPanel.add(speedComboBox);

        // 设置按钮
        settingsButton = new JButton("⚙ 设置 (1)");
        settingsButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        settingsButton.setBackground(new Color(70, 130, 180));
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setFocusPainted(false);
        settingsButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        settingsButton.addActionListener(e -> showSettingsDialog());
        controlPanel.add(settingsButton);

        // 帮助按钮
        JButton helpButton = new JButton("❓ 帮助 (H)");
        helpButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        helpButton.setBackground(new Color(60, 180, 80));
        helpButton.setForeground(Color.WHITE);
        helpButton.setFocusPainted(false);
        helpButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        helpButton.addActionListener(e -> showHelpDialog());
        controlPanel.add(helpButton);

        // 重置按钮
        JButton resetButton = new JButton("🔄 重置 (R)");
        resetButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        resetButton.setBackground(new Color(220, 80, 80));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        resetButton.addActionListener(e -> {
            initGame();
            repaint();
        });
        controlPanel.add(resetButton);

        // 添加控制面板到北部
        add(controlPanel, BorderLayout.NORTH);

        // 游戏面板
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setFocusable(false);
        add(gamePanel, BorderLayout.CENTER);
    }

    /**
     * 绘制游戏（从paintComponent提取出来）
     */
    private void drawGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // 绘制网格线（浅灰色）
        g2d.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= GRID_SIZE; i++) {
            g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, PANEL_SIZE);
            g2d.drawLine(0, i * CELL_SIZE, PANEL_SIZE, i * CELL_SIZE);
        }

        // 绘制食物（红色圆形）
        if (food != null) {
            g2d.setColor(Color.RED);
            int x = food.x * CELL_SIZE + CELL_SIZE / 2;
            int y = food.y * CELL_SIZE + CELL_SIZE / 2;
            int radius = CELL_SIZE / 2 - 4;
            g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }

        // 绘制蛇（绿色渐变）
        if (snake != null && !snake.isEmpty()) {
            for (int i = 0; i < snake.size(); i++) {
                Point p = snake.get(i);
                int x = p.x * CELL_SIZE;
                int y = p.y * CELL_SIZE;
                // 头部亮绿色，身体深绿色
                if (i == 0) {
                    g2d.setColor(new Color(0, 255, 100));
                } else {
                    g2d.setColor(new Color(0, 180, 50));
                }
                g2d.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                // 边框
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            }
        }

        // 显示游戏结束或胜利信息
        if (gameOver) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
            String msg = "游戏结束! 按 R 重新开始";
            int msgWidth = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (PANEL_SIZE - msgWidth) / 2, PANEL_SIZE / 2);
        } else if (gameWon) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Microsoft YaHei", Font.BOLD, 28));
            String msg = "🎉 你赢了! 🎉";
            int msgWidth = g2d.getFontMetrics().stringWidth(msg);
            g2d.drawString(msg, (PANEL_SIZE - msgWidth) / 2, PANEL_SIZE / 2);
        }

        // 显示当前得分/长度
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        String score = "长度: " + snake.size() + " / " + (GRID_SIZE * GRID_SIZE);
        g2d.drawString(score, 10, 20);
    }

    /**
     * 显示游戏操作说明
     */
    private void showHelpDialog() {
        // 暂停游戏
        boolean wasRunning = running;
        if (wasRunning) {
            running = false;
        }

        JDialog helpDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "游戏操作说明", true);
        helpDialog.setLayout(new BorderLayout());
        helpDialog.setSize(500, 500);
        helpDialog.setLocationRelativeTo(this);
        helpDialog.setResizable(false);

        // 内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(50, 50, 50));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 标题
        JLabel titleLabel = new JLabel("🐍 贪吃蛇游戏操作指南", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 255, 100));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // 说明文本区域
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setBackground(new Color(60, 60, 60));
        helpText.setForeground(Color.WHITE);
        helpText.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 构建说明内容
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════\n");
        sb.append("           🎮 游戏目标\n");
        sb.append("═══════════════════════════════════════\n\n");
        sb.append("  控制蛇在 9x9 的网格中移动，\n");
        sb.append("  吃到红色食物来增加蛇的长度。\n");
        sb.append("  目标是让蛇占满全部 81 个格子！\n\n");

        sb.append("═══════════════════════════════════════\n");
        sb.append("           ⌨️ 键盘控制\n");
        sb.append("═══════════════════════════════════════\n\n");
        sb.append("  ↑ 或 W    向上移动\n");
        sb.append("  ↓ 或 S    向下移动\n");
        sb.append("  ← 或 A    向左移动\n");
        sb.append("  → 或 D    向右移动\n");
        sb.append("  1         打开设置面板\n");
        sb.append("  H         显示本帮助\n");
        sb.append("  R         重新开始游戏\n\n");

        sb.append("═══════════════════════════════════════\n");
        sb.append("           🎯 游戏规则\n");
        sb.append("═══════════════════════════════════════\n\n");
        sb.append("  ✅ 吃到食物 → 蛇身长度 +1\n");
        sb.append("  ❌ 撞到墙壁 → 游戏结束\n");
        sb.append("  ❌ 撞到自己 → 游戏结束\n");
        sb.append("  🏆 占满81格 → 获得胜利！\n\n");

        sb.append("═══════════════════════════════════════\n");
        sb.append("           ⚙️ 游戏设置\n");
        sb.append("═══════════════════════════════════════\n\n");
        sb.append("  速度调节：\n");
        sb.append("  很慢 (1000ms) → 适合新手\n");
        sb.append("  慢   (600ms)  → 默认速度\n");
        sb.append("  适中 (400ms)  → 标准体验\n");
        sb.append("  快   (250ms)  → 挑战模式\n");
        sb.append("  很快 (150ms)  → 专家模式\n\n");

        sb.append("═══════════════════════════════════════\n");
        sb.append("           💡 游戏技巧\n");
        sb.append("═══════════════════════════════════════\n\n");
        sb.append("  • 提前规划蛇的移动路线\n");
        sb.append("  • 避免蛇身过长时困住自己\n");
        sb.append("  • 利用 9x9 网格的空间优势\n");
        sb.append("  • 可以根据节奏调整游戏速度\n");
        sb.append("  • 保持冷静，不要慌！\n\n");

        sb.append("═══════════════════════════════════════\n");
        sb.append("           祝你玩得开心！🎉\n");
        sb.append("═══════════════════════════════════════\n");

        helpText.setText(sb.toString());
        contentPanel.add(helpText, BorderLayout.CENTER);

        // 底部按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(50, 50, 50));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton closeButton = new JButton("我知道了");
        closeButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeButton.addActionListener(e -> {
            helpDialog.dispose();
            // 恢复游戏
            if (wasRunning && !gameOver && !gameWon) {
                running = true;
            }
        });
        buttonPanel.add(closeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        helpDialog.add(contentPanel);

        // 关闭对话框时恢复游戏
        helpDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (wasRunning && !gameOver && !gameWon) {
                    running = true;
                }
            }
        });

        helpDialog.setVisible(true);
    }

    /**
     * 显示设置对话框
     */
    private void showSettingsDialog() {
        // 暂停游戏
        boolean wasRunning = running;
        if (wasRunning) {
            running = false;
        }

        JDialog settingsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "游戏设置", true);
        settingsDialog.setLayout(new GridBagLayout());
        settingsDialog.setSize(400, 300);
        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.setResizable(false);

        // 设置对话框字体
        setDialogFont(settingsDialog);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("⚙ 游戏设置", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
        settingsDialog.add(titleLabel, gbc);

        // 速度设置
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel speedLabel = new JLabel("游戏速度:");
        speedLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        settingsDialog.add(speedLabel, gbc);

        gbc.gridx = 1;
        String[] speedOptions = {"很慢 (1000ms)", "慢 (600ms)", "适中 (400ms)", "快 (250ms)", "很快 (150ms)"};
        JComboBox<String> speedCombo = new JComboBox<>(speedOptions);
        // 设置当前选中项
        int currentIndex = getSpeedIndex(currentSpeed);
        speedCombo.setSelectedIndex(currentIndex);
        speedCombo.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        settingsDialog.add(speedCombo, gbc);

        // 当前速度显示
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel currentSpeedLabel = new JLabel("当前速度: " + currentSpeed + "ms/步", SwingConstants.CENTER);
        currentSpeedLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        currentSpeedLabel.setForeground(new Color(70, 130, 180));
        settingsDialog.add(currentSpeedLabel, gbc);

        // 网格大小信息
        gbc.gridy = 3;
        JLabel gridInfoLabel = new JLabel("网格大小: " + GRID_SIZE + "x" + GRID_SIZE + " (共" + (GRID_SIZE * GRID_SIZE) + "格)", SwingConstants.CENTER);
        gridInfoLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        gridInfoLabel.setForeground(new Color(200, 200, 200));
        settingsDialog.add(gridInfoLabel, gbc);

        // 快捷键提示
        gbc.gridy = 4;
        JLabel shortcutInfoLabel = new JLabel("快捷键: 1=设置  H=帮助  R=重置  ↑↓←→=方向", SwingConstants.CENTER);
        shortcutInfoLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        shortcutInfoLabel.setForeground(new Color(200, 200, 100));
        settingsDialog.add(shortcutInfoLabel, gbc);

        // 按钮面板
        gbc.gridy = 5;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton confirmButton = new JButton("确认");
        confirmButton.setBackground(new Color(70, 130, 180));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        confirmButton.addActionListener(e -> {
            int selectedIndex = speedCombo.getSelectedIndex();
            int newSpeed = getSpeedValue(selectedIndex);
            changeGameSpeed(newSpeed);
            settingsDialog.dispose();
            // 恢复游戏
            if (wasRunning && !gameOver && !gameWon) {
                running = true;
            }
        });
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.setBackground(new Color(160, 160, 160));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        cancelButton.addActionListener(e -> {
            settingsDialog.dispose();
            // 恢复游戏
            if (wasRunning && !gameOver && !gameWon) {
                running = true;
            }
        });
        buttonPanel.add(cancelButton);

        settingsDialog.add(buttonPanel, gbc);

        // 关闭对话框时恢复游戏
        settingsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (wasRunning && !gameOver && !gameWon) {
                    running = true;
                }
            }
        });

        settingsDialog.setVisible(true);
    }

    /**
     * 设置对话框组件的字体
     */
    private void setDialogFont(Container container) {
        Font font = new Font("Microsoft YaHei", Font.PLAIN, 14);
        for (Component comp : container.getComponents()) {
            comp.setFont(font);
            if (comp instanceof Container) {
                setDialogFont((Container) comp);
            }
        }
    }

    /**
     * 获取速度对应的索引
     */
    private int getSpeedIndex(int speed) {
        if (speed >= 900) return 0;
        if (speed >= 500) return 1;
        if (speed >= 300) return 2;
        if (speed >= 200) return 3;
        return 4;
    }

    /**
     * 获取索引对应的速度值
     */
    private int getSpeedValue(int index) {
        switch (index) {
            case 0: return 1000;
            case 1: return 600;
            case 2: return 400;
            case 3: return 250;
            case 4: return 150;
            default: return 600;
        }
    }

    /**
     * 改变游戏速度
     */
    private void changeGameSpeed(int newSpeed) {
        if (newSpeed != currentSpeed) {
            currentSpeed = newSpeed;
            timer.setDelay(currentSpeed);
            // 更新下拉框的选中项
            int index = getSpeedIndex(currentSpeed);
            speedComboBox.setSelectedIndex(index);
        }
    }

    /**
     * 从下拉框改变速度
     */
    private void changeSpeed() {
        int index = speedComboBox.getSelectedIndex();
        int newSpeed = getSpeedValue(index);
        changeGameSpeed(newSpeed);
    }

    /**
     * 初始化或重置游戏
     */
    private void initGame() {
        snake = new LinkedList<>();
        // 初始蛇：长度为3，水平放置于网格中央附近
        int center = GRID_SIZE / 2;
        snake.add(new Point(center, center));
        snake.add(new Point(center - 1, center));
        snake.add(new Point(center - 2, center));
        currentDirection = Direction.RIGHT;

        running = true;
        gameOver = false;
        gameWon = false;
        spawnFood();
    }

    /**
     * 在空白位置生成食物
     */
    private void spawnFood() {
        int maxAttempts = 200;
        while (maxAttempts-- > 0) {
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);
            Point candidate = new Point(x, y);
            if (!snake.contains(candidate)) {
                food = candidate;
                return;
            }
        }
        // 如果没有空白位置，说明蛇已占满所有格子 → 胜利
        if (snake.size() == GRID_SIZE * GRID_SIZE) {
            gameWon = true;
            running = false;
        } else {
            spawnFood();
        }
    }

    /**
     * 游戏主逻辑：移动蛇
     */
    private void moveSnake() {
        if (!running) return;

        Point head = snake.getFirst();
        Point newHead = new Point(head);

        // 根据当前方向计算新头部
        switch (currentDirection) {
            case UP:    newHead.y--; break;
            case DOWN:  newHead.y++; break;
            case LEFT:  newHead.x--; break;
            case RIGHT: newHead.x++; break;
        }

        // 检查是否吃到食物
        boolean ateFood = newHead.equals(food);

        // 执行移动（先加头）
        snake.addFirst(newHead);
        if (!ateFood) {
            snake.removeLast();
        }

        // 碰撞检测（墙壁或自身）
        if (newHead.x < 0 || newHead.x >= GRID_SIZE || newHead.y < 0 || newHead.y >= GRID_SIZE) {
            gameOver = true;
            running = false;
            return;
        }

        // 自身碰撞
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i).equals(newHead)) {
                gameOver = true;
                running = false;
                return;
            }
        }

        // 如果吃到食物，生成新食物
        if (ateFood) {
            spawnFood();
            if (snake.size() == GRID_SIZE * GRID_SIZE) {
                gameWon = true;
                running = false;
            }
        }
    }

    /**
     * Timer 回调：每帧更新
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();
        }
        repaint();
    }

    // --- 键盘事件 ---
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 数字键1 打开设置
        if (key == KeyEvent.VK_1) {
            showSettingsDialog();
            return;
        }

        // H键 打开帮助
        if (key == KeyEvent.VK_H) {
            showHelpDialog();
            return;
        }

        // 按 R 重新开始（任何状态）
        if (key == KeyEvent.VK_R) {
            initGame();
            repaint();
            return;
        }

        // 只有游戏运行中才处理方向
        if (!running) return;

        // 防止反向移动（支持方向键和WASD）
        switch (key) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (currentDirection != Direction.DOWN)
                    currentDirection = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (currentDirection != Direction.UP)
                    currentDirection = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (currentDirection != Direction.RIGHT)
                    currentDirection = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (currentDirection != Direction.LEFT)
                    currentDirection = Direction.RIGHT;
                break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        // 设置系统属性，确保中文显示正常
        System.setProperty("file.encoding", "UTF-8");

        JFrame frame = new JFrame("9x9 贪吃蛇");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}