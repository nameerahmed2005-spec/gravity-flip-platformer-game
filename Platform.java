import java.awt.*;

public class Platform {

    int x, y, width, height;
    String spikeDirection = "";

    public Platform(int x, int y, int w, int h) {

        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        // auto spike directions
        if (w > h) {

            if (y < 100)
                spikeDirection = "down";
            else
                spikeDirection = "up";
        }
        else {

            if (x < 1000)
                spikeDirection = "right";
            else
                spikeDirection = "left";
        }
    }

    public Platform(int x, int y, int w, int h, String dir) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.spikeDirection = dir;
    }

    public void draw(Graphics g, int cameraX) {
        g.fillRect(x - cameraX, y, width, height);
    }
}