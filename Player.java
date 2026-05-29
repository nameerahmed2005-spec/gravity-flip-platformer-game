import java.awt.*;

public class Player {

    // spawn point
    int x = 1200;
    int y = 300;

    double velocityY = 0;
    double gravity = 0.8;

    int velocityX = 0;
    int speed = 7;

    double jumpStrength = 15;
    boolean onSurface = false;

    public void draw(Graphics g, int cameraX) {
        Graphics2D g2 = (Graphics2D) g;

        // body
        g2.setColor(new Color(245, 245, 245));
        g2.fillRoundRect(x - cameraX, y, 50, 50, 12, 12);

        // outline
        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x - cameraX, y, 50, 50, 12, 12);

        // eyes
        g2.setColor(Color.BLACK);
        g2.fillOval(x - cameraX + 12, y + 15, 6, 6);
        g2.fillOval(x - cameraX + 32, y + 15, 6, 6);

        // gravity indicator
        if (gravity > 0) {

            g2.fillPolygon(
                    new int[]{x - cameraX + 25, x - cameraX + 18, x - cameraX + 32},
                    new int[]{y + 38, y + 28, y + 28},
                    3
            );
        }
        else {

            g2.fillPolygon(
                    new int[]{x - cameraX + 25, x - cameraX + 18, x - cameraX + 32},
                    new int[]{y + 12, y + 22, y + 22},
                    3
            );
        }
    }

    public void flipGravity() {
        if (onSurface) {
            gravity *= -1;
            velocityY = 0;
            onSurface = false; // prevents instant re-flip spam
        }
    }

    public void jump() {
        if (onSurface) {
            velocityY = -jumpStrength * Math.signum(gravity);
            onSurface = false;
        }
    }
}