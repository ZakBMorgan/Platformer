import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class Frame extends JFrame implements ActionListener, MouseListener, KeyListener {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();

    Music background_sfx = new Music("background_sfx.wav", false);
    Music jump_sfx = new Music("jump_sfx.wav", false);

    private int[] play = new int[]{width / 2, 0, 100, 100};
    private int[] plat = new int[]{width / 2, 2 * height / 3, 100, 10};

    Player player = new Player(play[0], play[1], play[2], play[3], Color.black);
    Platform platform = new Platform(plat[0], plat[1], plat[2], plat[3], Color.green);
    Text title = new Text(width / 2 - 400, 0, 100, "E L E M E N T A L", 2);
    Text intro = new Text(width / 2 - 200, 0, 50, "use " + "\"W A S D\"" + " to move", 3);
    ArrayList<Heart> hearts = new ArrayList<>();

    private Color[] colors = new Color[]{Color.red, Color.blue, Color.green, Color.yellow};
    private Platform[] platforms = new Platform[1000];

    Timer t = new Timer(15, this);

    private boolean canJump;
    private boolean isFalling;
    private boolean hit;

    // Declare off-screen buffer and its graphics context
    private Image offScreenImage;
    private Graphics offScreenGraphics;

    private Map<Integer, Boolean> keyMap = new HashMap<>(); // can only store unique key
    														// in this HashMap, the key is an Integer

    @Override
    public void paint(Graphics g) {

        // Initialize off-screen buffer if it's null or if the size has changed
        if (offScreenImage == null || offScreenImage.getWidth(this) != getWidth() || offScreenImage.getHeight(this) != getHeight()) {
            offScreenImage = createImage(getWidth(), getHeight());
            offScreenGraphics = offScreenImage.getGraphics();
        }

        // Use off-screen graphics for drawing
        Graphics2D g2d = (Graphics2D) offScreenGraphics;
        g2d.clearRect(0, 0, getWidth(), getHeight());

        // Draw the title
        title.paint(g2d);
        if (title.getY() >= 1500) {
            intro.paint(g2d);
        }

        int offsetX = getWidth() / 2 - player.getX() - player.getWidth() / 2;
        int offsetY = getHeight() / 2 - player.getY() - player.getHeight() / 2;

        g2d.translate(offsetX, offsetY);

        player.paint(g2d);

        for (Platform platform : platforms) {
            platform.paint(g2d);
        }

        g2d.translate(-offsetX, -offsetY);

        for (int i = 0; i < hearts.size(); i++) {
            hearts.get(i).paint(g2d);
            if (hit) {
                hearts.remove(0);
                hit = false;
            }
        }

        // Draw the off-screen buffer to the screen
        g.drawImage(offScreenImage, 0, 0, this);
    }

    public Frame() {
        //background_sfx.play();
        setTitle("Platform");
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        int platformWidth = 100;
        for (int i = 0; i < platforms.length; i++) {
            int randomY = (int) (Math.random() * height / 2) + (height / 2);
            int randomX = i * 200;
            int n = (int) (Math.random() * colors.length);
            platforms[i] = new Platform(randomX, randomY, platformWidth, 10, colors[n]);
        }

        int size = 35;
        for (int i = 0; i < 3; i++) {
            hearts.add(new Heart(width - 55 - i * 50, 50, size, size, Color.red));
        }

        t.start();
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void collision() {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (Platform platform : platforms) {
            Rectangle platformRect = new Rectangle(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            if (playerRect.intersects(platformRect) && player.getVy() >= 0 && !isFalling) {
                player.stopFalling();
                player.setY(platform.getY() - player.getHeight());
                player.setColor(platform.getColor());
                canJump = true;
                return;
            }
        }

        canJump = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update player's velocity based on keyMap
        if (Boolean.TRUE.equals(keyMap.get(KeyEvent.VK_A))) {
            player.setVx(-10); // only the 'A' key is pressed
        } else if (Boolean.TRUE.equals(keyMap.get(KeyEvent.VK_D))) {
            player.setVx(10); // only the 'D' key is pressed
        } else {
            player.setVx(0); // no keys are pressed
        }

        if (Boolean.TRUE.equals(keyMap.get(KeyEvent.VK_W)) && canJump) {
            player.jump();
            jump_sfx.play();
            canJump = false;
        }

        player.updatePosition();
        platform.updatePosition();
        collision();
        repaint();
        if (player.getY() >= 1300) {
            hearts.remove(0);
            player.setY(0);
            if (hearts.size() == 0) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyMap.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyMap.put(e.getKeyCode(), false);
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
