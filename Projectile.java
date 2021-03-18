
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.JPanel;
 
public class Projectile extends JPanel {
    private int x, y, width, height, index;
    private boolean drawn;
    private BufferedImage skin;
    private int drawCool = 0;
    private double rotation = 0;
 
    public Projectile(int index) {
        this.index = index;
        this.width = 30;
        this.height = 30; 
        x = -200;
        y = 0;
        drawCool = 1;
        drawn = true;
        try {
            skin = ImageIO.read(new File("./Images/bread.png"));
        } catch (IOException e) {
            System.out.println("Couldn't find image");
        }
    }
     
 
    public void draw(Graphics g){
        if (drawn) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform oldTransform=g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(rotation);
            g2d.drawImage(skin, -15, -15, 30, 30, this);
            g2d.setTransform(oldTransform);
        } else if (drawCool >= 1) 
            drawCool--;
        else 
            g.drawImage(skin, x, y, null);
    }
     
    public void update(Duck[] ds, Screen sc) {
        if (drawn) {
            y -= 8;
            rotation += 0.05;
        }
        if (y < -100) {
            die();
        }

        for (Duck d : ds) {
            if (d != null) {
                int tX = d.getX();
                int tY = d.getY();
                if(x+15 >= tX && x-15 <= tX + 30 && y+15 >= tY && y <= tY + 50 && d.getAlive() && drawn){
                    d.takeDamage(sc);
                    die();
                }
            }
        }
    }

    public void die() {
        x = 410;
        y = 760 - index * 40;
        drawn = false;
        drawCool = 5;
        rotation = 0;
    }

    public void fire(int x, int y) {

        rotation = (int) (Math.random() * 3);

        this.x = x + 40;
        this.y = y;
        drawn = true;
    }   

    public boolean getOccupied() {
        return drawn;
    }

    public void updateLVL(int lvl) {
        ;
    }
}