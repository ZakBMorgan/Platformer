import java.awt.*;

public class Player {
    private int x, y, width, height;
    private int vy = 0;
    private int vx = 0; // Horizontal velocity
    private final int gravity = 1;
    private final int jumpStrength = -15;

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void paint(Graphics g) {
        g.fillRect(x, y, width, height);
    }

    public void updatePosition() {
        x += vx;
        y += vy;
        vy += gravity;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void jump() {
        vy = jumpStrength;
    }

    public void stopFalling() {
        vy = 0;
    }

    public void setY(int newY) {
        y = newY;
    }

    public void setX(int newX) {
        x = newX;
    }

    // Getters
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

    public int getVy() {
        return vy;
    }
}
