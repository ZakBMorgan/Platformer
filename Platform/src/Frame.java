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
    Text title = new Text(width / 2 - 400, 0, 100, "E L E M E N T A L", 3);
    Text intro = new Text(width / 2 - 250, 0, 50, "use " + "\"W A S D\"" + " to move", 4);
    
    private ArrayList<Heart> hearts = new ArrayList<>(); // stores the 'lives' of the player

    private Color[] colors = new Color[]{Color.red, Color.blue, Color.green, Color.yellow};
    private Platform[] platforms = new Platform[1000];

    Timer t = new Timer(15, this);
    private int ellapseTime = 0, seconds = 0;

    //movement and overall gameplay functionality
    private boolean canJump;
    private boolean isFalling;
    private boolean hit;
    private boolean isBoss;
    
    private int lastPlatformX; // viable for player respawns

    // Declare off-screen buffer and its graphics context
    private Image offScreenImage;
    private Graphics offScreenGraphics;

    private Map<Integer, Boolean> keyMap = new HashMap<>(); // can only store unique key
    														// in this HashMap, the key is an Integer

    @Override
    public void paint(Graphics g) {
    	
    	ellapseTime += 15;
    	if(ellapseTime % 1000 < 15) {
    		seconds++;
    	}
    	
    	
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
        if (title.getY() >= 1200) {
            intro.paint(g2d);
            if(intro.getY() >= 1200) {
            	g2d.drawString("Time: "+seconds, width/2 - 100, 100);
            	g2d.drawString("Distance: " + player.getX(), width/2 - 500, 100);
            }
        }
        

        int offsetX = getWidth() / 2 - player.getX() - player.getWidth() / 2; // this is the center of the player object
        int offsetY = getHeight() / 2 - player.getY() - player.getHeight() / 2; // this is the center of the player object
        
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
    	isBoss = true;
        //background_sfx.play();
        setTitle("Platform");
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        int platformWidth = 100; 
        for (int i = 0; i < platforms.length; i++) {
        	int randomY = 1;
            while(randomY % 200 != 0) {
            	randomY = (int) (Math.random() * height - 100) + 100;
            }
            int randomX = i * 100;
            int n = (int) (Math.random() * colors.length);
            platforms[i] = new Platform(randomX, randomY, platformWidth, 10, colors[n]);
        }

        int size = 35; // size of the heart object
        int hp = 3; // number of hearts
        for (int i = 0; i < hp; i++) {
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
                lastPlatformX = platform.getX();
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
        } else if (Boolean.TRUE.equals(keyMap.get(KeyEvent.VK_S))) {
        	isFalling = true; // only the 'S' key is pressed
        } else {
            player.setVx(0); // no keys are pressed
            isFalling = false;
        }

        if (Boolean.TRUE.equals(keyMap.get(KeyEvent.VK_W)) && canJump) {
            player.jump();
            //jump_sfx.play();
            canJump = false;
        }

        player.updatePosition();
        platform.updatePosition();
        collision();
        repaint();
        if (player.getY() >= 1300) { // takes away 1 life if the player falls off the map
            hearts.remove(0);
            player.setY(-1000); // spawns the player above where they fell
            player.setX(lastPlatformX); // spawns the player above the last platform they touched
            if (hearts.size() == 0) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyMap.put(e.getKeyCode(), true); // adds a keyCode to the hashMap, and sets it to true
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyMap.put(e.getKeyCode(), false); // sets a keyCode to false when it isn't pressed
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
