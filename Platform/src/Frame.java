import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Frame extends JFrame implements ActionListener, MouseListener, KeyListener {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();
    
    Music background_sfx = new Music("background_sfx.wav", false);
    Music jump_sfx = new Music("jump_sfx.wav", false);
    
    private int[] play = new int[]{width / 2, 0, 100, 100};
    private int[] plat = new int[]{width / 2, 2 * height / 3, 100, 10};

    Player player = new Player(play[0], play[1], play[2], play[3]);
    Platform platform = new Platform(plat[0], plat[1], plat[2], plat[3]);

    private Platform[] platforms = new Platform[1000];
    
    Timer t = new Timer(15, this);

    private boolean canJump;
    private boolean isFalling;

    // Declare off-screen buffer and its graphics context
    private Image offScreenImage;
    private Graphics offScreenGraphics;

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

        int offsetX = getWidth() / 2 - player.getX() - player.getWidth() / 2;
        int offsetY = getHeight() / 2 - player.getY() - player.getHeight() / 2;

        g2d.translate(offsetX, offsetY);
        
        player.paint(g2d);

        for (Platform platform : platforms) {
            platform.paint(g2d);
        }

        g2d.translate(-offsetX, -offsetY);

        // Draw the off-screen buffer to the screen
        g.drawImage(offScreenImage, 0, 0, this);
    }
    
    public Frame() {
    	background_sfx.play();
        setTitle("Platform");
        setLayout(new BorderLayout());
        setSize(600, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        
        int platformWidth = 100;
        for (int i = 0; i < platforms.length; i++) {
            //int randomY = (int) (Math.random() * 100) + 50 + i * 100;
        	int randomY = (int) (Math.random() * (height / 2 - 100)) + (height / 2 + 100);
            int randomX = i * 200;
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

    public void collision() {
        Rectangle playerRect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (Platform platform : platforms) {
            Rectangle platformRect = new Rectangle(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            //System.out.println(platform.getX() + ":" + platform.getY());
            if (playerRect.intersects(platformRect) && player.getVy() >= 0 && !isFalling) {
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
        platform.updatePosition();
        collision();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

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
            jump_sfx.play();
            canJump = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            isFalling = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
            player.setVx(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            isFalling = false;
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
