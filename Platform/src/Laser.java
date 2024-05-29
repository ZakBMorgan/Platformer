import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Laser {
    private int x, y;
    private int vx, vy;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = 0;
    private int height = 0;
    private Color color;
    private boolean targetLockOn;
    private Timer blastTimer;
    private boolean isBlasting = false;
    private int r, g, b;
    private int n;

    public Laser() {
    	x = 0;
        y = (int) (Math.random() * 2000) - 1000;
        width = (int) (screenSize.getWidth() * 1.5);
        height = 200;
        vy = 10;
        vx = 10;
    	n = (int) (Math.random() * 3) + 1;
        blastTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blast();
            }
        });
    }

    public void paint(Graphics g2d, int a, int targetX, int targetY, boolean shoot) {
    	if(n == 1) {r = 255; g = 0; b = 0;}
    	if(n == 2) {r = 0; g = 255; b = 0;}
    	if(n == 3) {r = 0; g = 0; b = 255;}
    	if(n == 4) {r = 255; g = 255; b = 0;}
    	
        color = new Color(r, g, b, a);
        g2d.setColor(color);
        
        if (targetY != y || targetX != x) {
            if (targetY > y) {
                y += vy;
                if (y > targetY) {
                    y = targetY;
                }
            } else {
                y -= vy;
                if (y < targetY) {
                    y = targetY;
                }
            }
            if (targetX > x) {
                x += vx;
                if (x > targetX) {
                    x = targetX;
                }
            } else {
                x -= vx;
                if (x < targetX) {
                    x = targetX;
                }
            }
            targetLockOn = false;
        } else {
            targetLockOn = true;
        }
        
        if (targetLockOn && a == 255) {
            startBlasting();
        }

        if (shoot) {
            g2d.fillRect(x, y, width, height);
        }
    }

    private void startBlasting() {
        if (!isBlasting) {
            isBlasting = true;
            blastTimer.start();
        }
    }

    private void blast() {
        if (height > 0) {
            height -= 30;
            //y += 10 / 2; // y is not updating
            updateY();
            if (height < 0) {
                height = 0;
                blastTimer.stop();
                isBlasting = false;
            }
        } else {
            blastTimer.stop();
            isBlasting = false;
        }
    }
    
    public void updateY() {
    	y += vy;
    }
    
    public Color getColor() {
    	return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }
    
    public void setHeight(int height) {
    	this.height = height;
    }
    
    public void setX(int x) {
    	this.x = x;
    }
    
    public void setY(int y) {
    	this.y = y;
    }
}
