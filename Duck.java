import java.awt.Graphics;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;

public class Duck {

    private int x, y, health, speed, xshift; 
    private double maxHealth;
    private boolean living = true;
    private BufferedImage skin;
    private boolean shifting = false;
    private boolean right = true;
    
    public Duck(int level) {
        this.x = (int) (Math.random() * 350 + 25);
        this.y = (int) (Math.random() * 3990 - 4000);

        health = 100 + (level - 1) * 50;
        speed = level;
        maxHealth = 100 + (level - 1) * 50;

        if (level == 2) {
            xshift = (int) (Math.random() * 20 - 10);
            shifting = true;
        } else {
            xshift = 0;
        }
        
        try {
            skin = ImageIO.read(new File("./Images/ducky" + level + ".png"));
        } catch (IOException e) {
            System.out.println("Couldn't find image");
        }
    }

    public void draw(Graphics g) {
        if (living) {
            g.drawImage(skin, cap(x + xshift, 375), y, null);
            g.setColor(Color.RED);
            g.fillRect(cap(x + xshift, 375), y, 30, 5);
            g.setColor(Color.GREEN);
            g.fillRect(cap(x + xshift, 375), y, (int) ((health / maxHealth) * 30), 5);
        }
        
    }

    public void update() {
        if (living)
            y += speed;
        if (shifting) {
            if (xshift < -30) {
                right = true;
            } else if (xshift > 30) {
                right = false;
            }
            if (right) {
                xshift ++;
            } else {
                xshift --;
            }
        }
    }

    public int cap(int n, int max) {
        if (n <= max) {
            return n;
        } else {
            return max;
        }
    }

    public boolean getAlive() {
        return living;
    }

    public void die(Screen sc) {
        living = false;
        SP.playSound("wyQuack 2.wav");
        sc.addScore();
    }

    public int getX() {
        return cap(x + xshift, 375);
    }

    public int getY() {
        return y;
    }

    public void takeDamage(Screen sc) {
        health -= 50;
        if (health <= 0) {
            die(sc);
        }
    }

    

}