import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    Image backgroundImage;
    Image scaledBackground;

    Image gameOverBackground;
    Image levelCompleteBackground;

    Player player;
    Level level;
    int currentLevel = 1;
    int unlockedLevel = 1;

    long levelStartTime;
    double completionTime;

    int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    int cameraX = 0;

    int menuBackgroundX = 0;

    boolean showTutorialBubble = true;
    boolean tutorialDismissed = false;

    GameState gameState = GameState.MENU;

    Rectangle playButton = new Rectangle(600, 450, 250, 80);
    Rectangle exitButton = new Rectangle(1300, 670, 125, 40);
    Rectangle backButton = new Rectangle(20, 30, 260, 80);

    Rectangle level1Button = new Rectangle(400, 350, 150, 150);
    Rectangle level2Button = new Rectangle(650, 350, 150, 150);
    Rectangle level3Button = new Rectangle(900, 350, 150, 150);

    Rectangle menuButton = new Rectangle(20, 20, 220, 70);

    Rectangle retryButton = new Rectangle(400, 500, 300, 80);
    Rectangle nextLevelButton = new Rectangle(760, 500, 300, 80);
    Rectangle menuScreenButton = new Rectangle(580, 620, 300, 80);
    Rectangle retryMenuScreenButton = new Rectangle(760, 500, 300, 80);

    public GamePanel() {
        backgroundImage = new ImageIcon("src/levelBackground.jpg").getImage();
        scaledBackground = backgroundImage.getScaledInstance(screenWidth + 400, screenHeight, Image.SCALE_FAST);

        gameOverBackground = new ImageIcon("src/gameover.jpg").getImage();
        levelCompleteBackground = new ImageIcon("src/complete.jpg").getImage();

        player = new Player();
        level = new Level(currentLevel);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new InputHandler(player, this));

        Timer timer = new Timer(25, e -> update());
        timer.start();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                int mx = e.getX();
                int my = e.getY();

                // PLAYING
                if (gameState == GameState.PLAYING) {

                    if (menuButton.contains(mx, my)) {

                        gameState = GameState.MENU;

                        resetPlayer();

                        cameraX = 0;

                        return;
                    }
                }

                // GAME OVER
                if (gameState == GameState.GAME_OVER) {

                    // retry button
                    if (retryButton.contains(mx, my)) {

                        startLevel(currentLevel);
                    }

                    // main menu button
                    if (retryMenuScreenButton.contains(mx, my)) {

                        gameState = GameState.MENU;

                        resetPlayer();

                        cameraX = 0;

                        return;
                    }

                    return;
                }

                // LEVEL COMPLETE
                if (gameState == GameState.LEVEL_COMPLETE) {

                    // retry
                    if (retryButton.contains(mx, my)) {
                        startLevel(currentLevel);
                    }

                    // next level
                    if (nextLevelButton.contains(mx, my)) {
                        currentLevel++;
                        if (currentLevel > 3) {
                            gameState = GameState.LEVEL_SELECT;
                            currentLevel = 1;
                            cameraX = 0;
                            return;
                        }
                        startLevel(currentLevel);
                    }

                    // main menu button
                    if (menuScreenButton.contains(mx, my)) {
                        gameState = GameState.MENU;
                        resetPlayer();
                        cameraX = 0;
                        return;
                    }

                    return;
                }

                // MENU
                if (gameState == GameState.MENU) {

                    if (playButton.contains(mx, my)) {
                        gameState = GameState.LEVEL_SELECT;
                    }

                    if (exitButton.contains(mx, my)) {
                        System.exit(0);
                    }
                }

                // LEVEL SELECT
                else if (gameState == GameState.LEVEL_SELECT) {

                    if (gameState == GameState.LEVEL_SELECT) {

                        if (backButton.contains(mx, my)) {
                            gameState = GameState.MENU;
                        }
                    }

                    if (level1Button.contains(mx, my)) {
                        startLevel(1);
                    }

                    if (unlockedLevel >= 2 &&
                            level2Button.contains(mx, my)) {

                        startLevel(2);
                    }

                    if (unlockedLevel >= 3 &&
                            level3Button.contains(mx, my)) {

                        startLevel(3);
                    }
                }
            }
        });
    }

    public void update() {

        // moving menu backgrounds
        if (gameState == GameState.MENU ||
                gameState == GameState.LEVEL_SELECT) {

            menuBackgroundX--;

            if (menuBackgroundX <= -getWidth() + 10) {
                menuBackgroundX = 0;
            }
        }

        if (gameState != GameState.PLAYING) {
            repaint();
            return;
        }

        int prevY = player.y;

        // HORIZONTAL MOVEMENT
        player.x += player.velocityX;
        for (Platform p : level.platforms) {

            boolean yOverlap =
                    player.y < p.y + p.height &&
                            player.y + 50 > p.y;

            if (!yOverlap) continue;

            // moving right
            if (player.velocityX > 0 &&
                    player.x + 50 > p.x &&
                    player.x < p.x) {

                player.x = p.x - 50;
            }

            // moving left
            if (player.velocityX < 0 &&
                    player.x < p.x + p.width &&
                    player.x + 50 > p.x + p.width) {

                player.x = p.x + p.width;
            }
        }

        // VERTICAL MOVEMENT
        prevY = player.y;

        player.velocityY += player.gravity;
        player.y += player.velocityY;

        cameraX = player.x - getWidth()/2;

        if (cameraX < 0)
            cameraX = 0;

        int levelWidth = 0;
        if (currentLevel == 1)
           levelWidth = level.goal.x + 70;
        else if (currentLevel == 2)
            levelWidth = level.goal.x + 50;
        else if (currentLevel == 3)
            levelWidth = level.goal.x + 50;

        if (cameraX > levelWidth - getWidth()) {
            cameraX = levelWidth - getWidth();
        }

        player.onSurface = false;

        // Level borders
        if (player.x < 0) {
            player.x = 0;
        }

        levelWidth = level.goal.x + 400;

        if (player.x + 50 > levelWidth) {
            player.x = levelWidth - 50;
        }

        // Platform collision
        for (Platform p : level.platforms) {

            boolean xOverlap = player.x < p.x + p.width && player.x + 50 > p.x;

            if (!xOverlap) continue;

            // Falling downward
            if (player.velocityY > 0) {

                if (prevY + 50 <= p.y &&
                        player.y + 50 >= p.y) {

                    player.y = p.y - 50;
                    player.velocityY = 0;

                    if (player.gravity > 0) {
                        player.onSurface = true;
                    }
                }
            }

            // Moving upward
            else if (player.velocityY < 0) {

                if (prevY >= p.y + p.height &&
                        player.y <= p.y + p.height) {

                    player.y = p.y + p.height;
                    player.velocityY = 0;

                    if (player.gravity < 0) {
                        player.onSurface = true;
                    }
                }
            }
        }

        // Kill blocks
        for (Platform k : level.killBlocks) {

            if (player.x < k.x + k.width &&
                    player.x + 50 > k.x &&
                    player.y < k.y + k.height &&
                    player.y + 50 > k.y) {

                gameState = GameState.GAME_OVER;

                return;
            }
        }

        // Goal collision
        if (player.x < level.goal.x + level.goal.width &&
                player.x + 50 > level.goal.x &&
                player.y < level.goal.y + level.goal.height &&
                player.y + 50 > level.goal.y) {

            completionTime =
                    (System.currentTimeMillis() - levelStartTime) / 1000.0;

            if (currentLevel == unlockedLevel && unlockedLevel < 3) {
                unlockedLevel++;
            }

            gameState = GameState.LEVEL_COMPLETE;

            return;
        }

        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // pusling screen on gameover and levelcomplete
        double pulse = Math.sin(System.currentTimeMillis() * 0.005);
        int glowSize = (int)(pulse * 20);

        // background
        g.drawImage(
                scaledBackground,
                0,
                0,
                null
        );

        if (gameState == GameState.MENU) {

            g.drawImage(
                    scaledBackground,
                    menuBackgroundX,
                    0,
                    getWidth(),
                    getHeight(),
                    null
            );

            g.drawImage(
                    scaledBackground,
                    menuBackgroundX + getWidth() - 10,
                    0,
                    getWidth() + 2,
                    getHeight(),
                    null
            );

            // play button
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 64));
            drawOutlinedText(g,"GRAVITY FLIP PLATFORMER", 280, 250, Color.WHITE, Color.BLACK);
            g.setColor(new Color(70, 130, 255));
            g.fillRoundRect(playButton.x, playButton.y, playButton.width, playButton.height,25,25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            drawOutlinedText(g, "PLAY", 675, 505, Color.WHITE, Color.BLACK);

            // exit button
            g.setColor(new Color(220, 70, 70));
            g.fillRoundRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height,5,5);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 22));
            drawOutlinedText(g,"EXIT",1340,697, Color.WHITE, Color.BLACK);

            // controls instructions
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            drawOutlinedText(g, "CONTROLS", 50, 550, Color.WHITE, Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 28));
            drawOutlinedText(g, "A / D  -  Move", 50, 610, Color.WHITE, Color.BLACK);
            drawOutlinedText(g, "SPACE  -  Jump", 50, 660, Color.WHITE, Color.BLACK);
            drawOutlinedText(g, "E  -  Flip Gravity", 50, 710, Color.WHITE, Color.BLACK);

            return;
        }

        if (gameState == GameState.LEVEL_SELECT) {

            // moving background
            g.drawImage(
                    scaledBackground,
                    (int)menuBackgroundX,
                    0,
                    getWidth(),
                    getHeight(),
                    null
            );

            g.drawImage(
                    scaledBackground,
                    (int)menuBackgroundX + getWidth() - 10,
                    0,
                    getWidth() + 10,
                    getHeight(),
                    null
            );


            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            drawOutlinedText(g, "SELECT LEVEL", 550, 220, Color.WHITE, Color.BLACK);

            // =========================
            // Levels box coloring
            // =========================
            // LEVEL 1
            g.setColor(new Color(70, 130, 255));
            g.fillRect(level1Button.x, level1Button.y, level1Button.width, level1Button.height);

            // LEVEL 2
            if (unlockedLevel >= 2)
                g.setColor(new Color(70, 130, 255));
            else
                g.setColor(Color.DARK_GRAY);
            g.fillRect(level2Button.x, level2Button.y, level2Button.width, level2Button.height);

            // LEVEL 3
            if (unlockedLevel >= 3)
                g.setColor(new Color(70, 130, 255));
            else
                g.setColor(Color.DARK_GRAY);
            g.fillRect(level3Button.x, level3Button.y, level3Button.width, level3Button.height);

            // Levels text
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            drawOutlinedText(g, "1", 460, 445, Color.WHITE, Color.BLACK);
            drawOutlinedText(g, "2", 710, 445, Color.WHITE, Color.BLACK);
            drawOutlinedText(g, "3", 960, 445, Color.WHITE, Color.BLACK);
            if (unlockedLevel < 2) {
                drawOutlinedText(g, "LOCKED", 625, 560, Color.WHITE, Color.BLACK);
            }
            if (unlockedLevel < 3) {
                drawOutlinedText(g, "LOCKED", 875, 560, Color.WHITE, Color.BLACK);
            }

            // main menu button in level select
            g.setColor(new Color(70, 120, 255));
            g.fillRoundRect(backButton.x, backButton.y, backButton.width, backButton.height, 25, 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            drawOutlinedText(g, "MAIN MENU", backButton.x + 40, backButton.y + 50, Color.WHITE, Color.BLACK);

            return;
        }

        // GAME OVER SCREEN
        if (gameState == GameState.GAME_OVER) {

            // game over screen background
            g.drawImage(
                    gameOverBackground,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    null
            );
            g.setColor(new Color(0,0,0,120));
            g.fillRect(0,0,getWidth(),getHeight());

            // pulse
            g.setColor(new Color(255, 0, 0, 80));
            g.fillOval(465 - glowSize/2, 160 - glowSize/2, 520 + glowSize, 220 + glowSize);

            // Try again text
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 80));
            drawOutlinedText(g, "TRY AGAIN!", 500, 300, Color.RED, Color.BLACK);

            // retry button
            g.setColor(new Color(70,130,255));
            g.fillRoundRect(
                    retryButton.x,
                    retryButton.y,
                    retryButton.width,
                    retryButton.height,
                    25,
                    25
            );
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            drawOutlinedText(g, "RETRY", 490, 550, Color.WHITE, Color.BLACK);

            // main menu button
            g.setColor(new Color(70,130,255));
            g.fillRoundRect(
                    retryMenuScreenButton.x,
                    retryMenuScreenButton.y,
                    retryMenuScreenButton.width,
                    retryMenuScreenButton.height,
                    25,
                    25
            );
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            drawOutlinedText(g, "MAIN MENU", 820, 550, Color.WHITE, Color.BLACK);

            return;
        }

        // LEVEL COMPLETE SCREEN
        if (gameState == GameState.LEVEL_COMPLETE) {

            // level complete screen background
            g.drawImage(
                    levelCompleteBackground,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    null
            );
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0,0,getWidth(),getHeight());

            // pulse
            g.setColor(new Color(0, 255, 120, 80));
            g.fillOval(380 - glowSize/2, 110 - glowSize/2, 720 + glowSize, 220 + glowSize);

            // level complete text
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 72));
            drawOutlinedText(g, "LEVEL COMPLETE!", 400, 240, Color.GREEN, Color.BLACK);

            // time displayed
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            drawOutlinedText(g, "Time: " + String.format("%.2f", completionTime) + " seconds", 560, 350, Color.WHITE, Color.BLACK);

            // retry button
            g.setColor(new Color(70,130,255));
            g.fillRoundRect(
                    retryButton.x,
                    retryButton.y,
                    retryButton.width,
                    retryButton.height,
                    25,
                    25
            );

            // next level button
            g.fillRoundRect(
                    nextLevelButton.x,
                    nextLevelButton.y,
                    nextLevelButton.width,
                    nextLevelButton.height,
                    25,
                    25
            );
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            drawOutlinedText(g, "RETRY", 490, 550, Color.WHITE, Color.BLACK);
            drawOutlinedText(g, "NEXT LEVEL", 820, 550, Color.WHITE, Color.BLACK);

            // main menu button
            g.setColor(new Color(70,130,255));
            g.fillRoundRect(
                    menuScreenButton.x,
                    menuScreenButton.y,
                    menuScreenButton.width,
                    menuScreenButton.height,
                    25,
                    25
            );
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            drawOutlinedText(g, "MAIN MENU", 645, 670, Color.WHITE, Color.BLACK);

            return;
        }

        // ----------------------------
        // LEVEL GENERATION
        // ----------------------------
        // platform drawing
        for (Platform p : level.platforms) {

            int drawX = p.x - cameraX;

            // shadow
            g.setColor(new Color(50, 30, 15));

            g.fillRoundRect(
                    drawX + 4,
                    p.y + 4,
                    p.width,
                    p.height,
                    14,
                    14
            );

            // dirt body
            g.setColor(new Color(101, 67, 33));

            g.fillRoundRect(
                    drawX,
                    p.y,
                    p.width,
                    p.height,
                    14,
                    14
            );

            // grass top
            g.setColor(new Color(60, 180, 75));

            g.fillRoundRect(
                    drawX,
                    p.y,
                    p.width,
                    6,
                    14,
                    14
            );

            // grass highlight
            g.setColor(new Color(120, 255, 140));

            g.drawLine(
                    drawX + 5,
                    p.y + 2,
                    drawX + p.width - 5,
                    p.y + 2
            );
        }

        // SPIKES
        for (Platform k : level.killBlocks) {

            int drawX = k.x - cameraX;

            g.setColor(new Color(220,0,0));

            // UP SPIKES
            if (k.spikeDirection.equals("up")) {

                for (int x = 0; x < k.width; x += 20) {

                    int[] xs = {
                            drawX + x,
                            drawX + x + 10,
                            drawX + x + 20
                    };

                    int[] ys = {
                            k.y + k.height,
                            k.y,
                            k.y + k.height
                    };

                    g.fillPolygon(xs, ys, 3);
                }
            }

            // DOWN SPIKES
            else if (k.spikeDirection.equals("down")) {

                for (int x = 0; x < k.width; x += 20) {

                    int[] xs = {
                            drawX + x,
                            drawX + x + 10,
                            drawX + x + 20
                    };

                    int[] ys = {
                            k.y,
                            k.y + k.height,
                            k.y
                    };

                    g.fillPolygon(xs, ys, 3);
                }
            }

            // LEFT SPIKES
            else if (k.spikeDirection.equals("left")) {

                for (int y = 0; y < k.height; y += 20) {

                    int[] xs = {
                            drawX + k.width,
                            drawX,
                            drawX + k.width
                    };

                    int[] ys = {
                            k.y + y,
                            k.y + y + 10,
                            k.y + y + 20
                    };

                    g.fillPolygon(xs, ys, 3);
                }
            }

            // RIGHT SPIKES
            else if (k.spikeDirection.equals("right")) {

                for (int y = 0; y < k.height; y += 20) {

                    int[] xs = {
                            drawX,
                            drawX + k.width,
                            drawX
                    };

                    int[] ys = {
                            k.y + y,
                            k.y + y + 10,
                            k.y + y + 20
                    };

                    g.fillPolygon(xs, ys, 3);
                }
            }
        }

        // -----------------------
        // GOAL DRAWING
        // -----------------------
        // goal coordinates
        int gx = level.goal.x - cameraX;
        int gy = level.goal.y;

        // glow
        g.setColor(new Color(0, 255, 120, 80));
        g.fillOval(gx - 15, gy - 15, 50, 120);

        // outer portal
        g.setColor(new Color(0, 255, 120));
        g.fillRoundRect(gx, gy, level.goal.width, level.goal.height, 20, 20);

        // inner glow
        g.setColor(new Color(180, 255, 220));
        g.fillRoundRect(gx + 5, gy + 5,
                level.goal.width - 10,
                level.goal.height - 10,
                20, 20);

        // center shine
        g.setColor(Color.WHITE);
        g.fillOval(gx + 6, gy + 10,
                level.goal.width - 12,
                level.goal.height - 20);

        // PLAYER DRAWING
        g.setColor(Color.WHITE);
        player.draw(g, cameraX);

        // chat bubble
        if (currentLevel == 1 && showTutorialBubble) {

            int bubbleX = player.x - cameraX + 70;
            int bubbleY = player.y - 90;

            // bubble background
            g.setColor(new Color(255, 255, 255, 230));
            g.fillRoundRect(bubbleX, bubbleY, 320, 70, 25, 25);

            // bubble tail
            int[] tx = {
                    player.x - cameraX + 55,
                    player.x - cameraX + 75,
                    player.x - cameraX + 90
            };

            int[] ty = {
                    player.y - 10,
                    player.y - 30,
                    player.y - 5
            };

            g.fillPolygon(tx, ty, 3);

            // text
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Press E to flip gravity", bubbleX + 20, bubbleY + 42);
        }

        // MAIN MENU BUTTON in-game
        g.setColor(new Color(70, 130, 255));
        g.fillRoundRect(20, 20, 220, 70, 20, 20);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        FontMetrics fm = g.getFontMetrics();
        String text = "MAIN MENU";
        int textX = 20 + (220 - fm.stringWidth(text)) / 2;
        int textY = 20 + ((70 - fm.getHeight()) / 2) + fm.getAscent();
        drawOutlinedText(g, text, textX, textY, Color.WHITE, Color.BLACK);

        // timer in-game
        double currentTime = (System.currentTimeMillis() - levelStartTime) / 1000.0;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        drawOutlinedText(g, String.format("Timer: %.2f", currentTime), 260, 65, Color.WHITE, Color.BLACK);
    }

    // TEXT OUTLINES
    public void drawOutlinedText(Graphics g,
                                 String text,
                                 int x,
                                 int y,
                                 Color fillColor,
                                 Color outlineColor) {

        g.setColor(outlineColor);

        g.drawString(text, x - 2, y);
        g.drawString(text, x + 2, y);
        g.drawString(text, x, y - 2);
        g.drawString(text, x, y + 2);

        g.setColor(fillColor);

        g.drawString(text, x, y);
    }

    public void resetPlayer() {
        player.x = 100;
        player.y = 100;
        player.velocityY = 0;
        player.velocityX = 0;
        player.gravity = 0.8;
    }

    public void startLevel(int levelNumber) {

        currentLevel = levelNumber;
        level = new Level(currentLevel);

        resetPlayer();

        cameraX = 0;

        levelStartTime = System.currentTimeMillis();

        gameState = GameState.PLAYING;

        if (currentLevel == 1) {
            showTutorialBubble = true;
            tutorialDismissed = false;
        }
    }
}