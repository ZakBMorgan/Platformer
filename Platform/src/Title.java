import java.awt.*;

public class Title {
    private int x, y, width, height;

    public Title(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);  // Set the desired color for the title
        g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
        g.drawString("E L E M E N T A L", x, y);
        y++;
    }

    // Add any other methods as needed
}
