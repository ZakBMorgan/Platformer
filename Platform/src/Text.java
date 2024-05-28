import java.awt.*;

public class Text {
    private int x, y, size, vy;
    private String text;

    public Text(int x, int y, int size, String text, int vy) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.text = text;
        this.vy = vy;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);  // Set the desired color for the title
        g.setFont(new Font("TimesRoman", Font.PLAIN, size));
        g.drawString(text, x, y);
        y+=vy;
    }

    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }
}
