import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame implements ActionListener, MouseListener, KeyListener {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();

    private int[] play = new int[]{width / 2, 0, 100, 100};
    private int[] plat = new int[]{width / 2, 2 * height / 3, 100, 10};

    Player player = new Player(play[0], play[1], play[2], play[3]);
    Platform platform = new Platform(plat[0], plat[1], plat[2], plat[3]);

    private Platform[] platforms = new Platform[10];

    Timer t = new Timer(15, this);

    private boolean canJump;

    public Frame() {
        setTitle("Platform");
        setLayout(new BorderLayout());
        setSize(600, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        int platformWidth = 100;
        for (int i = 0; i < platforms.length; i++) {
            int randomX = (int) (Math.random() * (width - platformWidth)) + platformWidth / 2;
            int randomY = (int) (Math.random() * 100) + 50 + i * 100;
            platforms[i] = new Platform(randomX, randomY, platformWidth, 10);
        }

        t.start();
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());

        int offsetX = getWidth() / 2 - player.getX() - player.getWidth() / 2;
        int offsetY = getHeight() / 2 - player.getY() - player.getHeight() / 2;

        g2d.translate(offsetX, offsetY);

        player.paint(g2d);

        for (Platform platform : platforms) {
            platform.paint(g2d);
        }

        g2d.translate(-offsetX, -offsetY);
    }

    public void collision() {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (Platform platform : platforms) {
            Rectangle platformRect = new Rectangle(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            if (playerRect.intersects(platformRect) && player.getVy() >= 0) {
                player.stopFalling();
                player.setY(platform.getY() - player.getHeight());
                canJump = true;
                return;
            }
        }

        canJump = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.updatePosition();
        collision();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.setVx(-10);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.setVx(10);
        }
        if (e.getKeyCode() == KeyEvent.VK_W && canJump) {
            player.jump();
            canJump = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
            player.setVx(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Frame();
        });
    }
}
