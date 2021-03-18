import java.awt.geom.*;
import java.awt.*;

public class Water {
    private Color c;
    private int x, y, length, sw;

    public Water(int level) {
        if (level == 1) {
            c = new Color(62, (int) (Math.random() * 50 + 150), 230);
        } else {
            c = new Color(245, (int) (Math.random() * 150 + 50), 34);
        }
        x = (int) (Math.random() * 400);
        y = (int) (Math.random() * 1000 - 200);
        length = (int) (Math.random() * 150 + 50);
        sw = (int) (Math.random() * 10 + 8);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(sw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(c);
        g2.drawLine(x, y, x, y + length);
    }

    public void update() {
        y += (int) (sw / 4);

        if (y > 810) {
            y = -200;
            x = (int) (Math.random() * 400);
        }
    }

    public void updateLVL(int lvl) {
        if (lvl == 1) {
            c = new Color(62, (int) (Math.random() * 50 + 150), 230);
        } else {
            c = new Color(245, (int) (Math.random() * 150 + 50), 34);
        }
    }
}