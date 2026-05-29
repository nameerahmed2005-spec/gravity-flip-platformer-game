import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        JFrame window = new JFrame("Gravity Flip Platformer");

        GamePanel panel = new GamePanel();

        window.add(panel);

        //window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        window.setSize(1600, 770);
        window.setLocationRelativeTo(null);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setResizable(true);
    }
}