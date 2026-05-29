import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {

    Player player;
    GamePanel gamePanel;

    public InputHandler(Player player, GamePanel gamePanel) {
        this.player = player;
        this.gamePanel = gamePanel;
    }

    public void keyPressed(KeyEvent e)   {

        // tutorial bubble
        if (gamePanel.showTutorialBubble) {
            gamePanel.showTutorialBubble = false;
        }

        // move forward
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.velocityX = -player.speed;
        }
        // move backward
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.velocityX = player.speed;
        }
        // flip gravity
        if (e.getKeyCode() == KeyEvent.VK_E) {
            player.flipGravity();
        }
        // jump
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.jump();
        }
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A && player.velocityX < 0) player.velocityX = 0;
        if (e.getKeyCode() == KeyEvent.VK_D && player.velocityX > 0) player.velocityX = 0;
    }
}