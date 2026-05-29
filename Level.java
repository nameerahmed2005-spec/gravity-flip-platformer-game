import java.util.ArrayList;

public class Level {

    ArrayList<Platform> platforms = new ArrayList<>();
    ArrayList<Platform> killBlocks = new ArrayList<>();
    Platform goal;

    public Level(int levelNumber) {

        // =========================
        // LEVEL 1
        // =========================
        if (levelNumber == 1) {

            // outer room
            platforms.add(new Platform(0, 0, 40, 730));
            platforms.add(new Platform(1760, 0, 40, 740));

            // staircase progression
            platforms.add(new Platform(0, 670, 420, 30));
            platforms.add(new Platform(520, 580, 220, 30));
            platforms.add(new Platform(840, 470, 220, 30));
            platforms.add(new Platform(1200, 450, 300, 30));

            // flipped staircase progression
            platforms.add(new Platform(0, 40, 420, 30));
            platforms.add(new Platform(520, 150, 220, 30));
            platforms.add(new Platform(840, 260, 220, 30));
            platforms.add(new Platform(1200, 280, 300, 30));

            // spike pits
            killBlocks.add(new Platform(0, 699, 1760, 40));

            // ceiling danger
            killBlocks.add(new Platform(0, 1, 1760, 40));

            // goal
            goal = new Platform(1720, 340, 20, 90);
        }

        // =========================
        // LEVEL 2
        // =========================
        if (levelNumber == 2) {

            // outer room
            platforms.add(new Platform(0, 0, 762, 40));
            platforms.add(new Platform(1040, 700, 240, 39));
            platforms.add(new Platform(1780, 700, 420, 39));
            platforms.add(new Platform(0, 0, 40, 730));
            platforms.add(new Platform(2000, 0, 40, 740));

            // left room
            platforms.add(new Platform(0, 550, 480, 30));

            // vertical chamber walls
            platforms.add(new Platform(700, 120, 40, 500));
            platforms.add(new Platform(1200, 120, 40, 500));

            // chamber interior
            platforms.add(new Platform(760, 520, 140, 30));
            platforms.add(new Platform(1100, 520, 40, 30));

            // right climb
            platforms.add(new Platform(1450, 200, 220, 30));
            platforms.add(new Platform(1750, 380, 220, 30));

            // floor spikes
            killBlocks.add(new Platform(40, 699, 1000, 40));
            killBlocks.add(new Platform(1280, 699, 500, 40));

            // ceiling spikes
            killBlocks.add(new Platform(766, 1, 2398, 40));

            // chamber spikes
            killBlocks.add(new Platform(900, 519, 190, 32));

            // tunnel spikes
            killBlocks.add(new Platform(680, 119, 20, 500, "left"));
            killBlocks.add(new Platform(1240, 300, 20, 120, "right"));

            // goal
            goal = new Platform(1950, 290, 20, 90);
        }

        // =========================
        // LEVEL 3
        // =========================
        if (levelNumber == 3) {

            // outer room
            platforms.add(new Platform(565, 0, 200, 40));
            platforms.add(new Platform(980, 700, 110, 39));
            platforms.add(new Platform(0, 0, 40, 739));
            platforms.add(new Platform(2490, 0, 40, 740));

            // opening section
            platforms.add(new Platform(0, 580, 400, 30));

            // vertical gravity maze
            platforms.add(new Platform(650, 120, 40, 500));
            platforms.add(new Platform(1050, 120, 40, 500));

            // middle spike cube
            platforms.add(new Platform(810, 280, 120, 120));

            // alternating gravity platforms
            platforms.add(new Platform(720, 520, 260, 30));
            platforms.add(new Platform(720, 180, 260, 30));

            platforms.add(new Platform(1120, 350, 185, 30));
            platforms.add(new Platform(1700, 520, 80, 30));
            platforms.add(new Platform(1520, 180, 260, 30));

            // final climb
            platforms.add(new Platform(2000, 450, 50, 30));
            platforms.add(new Platform(2200, 450, 50, 30));
            platforms.add(new Platform(2280, 350, 180, 30));

            // floor spikes
            killBlocks.add(new Platform(0, 699, 980, 40));
            killBlocks.add(new Platform(1090, 699, 1510, 40));

            // ceiling spikes
            killBlocks.add(new Platform(0, 1, 550, 40));
            killBlocks.add(new Platform(770, 1, 1830, 40));

            // spikes around cube
            killBlocks.add(new Platform(810, 260, 120, 20, "up"));
            killBlocks.add(new Platform(810, 400, 120, 20, "down"));
            killBlocks.add(new Platform(790, 280, 20, 120, "left"));
            killBlocks.add(new Platform(930, 280, 20, 120, "right"));

            // combined with platform spikes
            killBlocks.add(new Platform(1305, 349, 61, 32, "down"));
            killBlocks.add(new Platform(1305, 349, 61, 32, "down"));
            killBlocks.add(new Platform(2055, 450, 140, 30, "up"));

            // dangerous walls
            killBlocks.add(new Platform(630, 119, 20, 500, "left"));
            killBlocks.add(new Platform(1030, 300, 20, 120, "left"));

            // goal
            goal = new Platform(2440, 260, 20, 90);
            //platforms.add(new Platform(2280, 350, 180, 30));
        }
    }
}