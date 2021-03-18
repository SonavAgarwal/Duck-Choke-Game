import java.awt.Color;
import java.awt.Graphics;

import java.awt.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;
 
public class Ship{
    private int x, y, width, height;

    private boolean living;

    private int cooldown, maxCool;
    
    private BufferedImage skin;
     
    public Ship(int x, int y){
         
        this.x = x;
        this.y = y;
        this.width = 80;
        this.height = 80;

        try {
            skin = ImageIO.read(new File("./Images/Raft.png"));
        } catch (IOException e) {
            System.out.println("Couldn't find image");
        }

        cooldown = 0;

        maxCool = 15;
 
    }
     
 
    public void draw(Graphics g){
        g.drawImage(skin, x, y, null);
    }

    public void shoot(Projectile[] p) {
        if (cooldown < 1) {
            cooldown = maxCool;
            for (int i = 0; i < p.length; i++) {
                if (p[i].getOccupied()) {
                    ;
                } else {
                    p[i].fire(x, y);
                    SP.playSound("bang.wav");
                    break;
                }
            }
        }
        
    }

    public void update(Duck[] ds, Screen sc) {
        cooldown --;

        for (Duck d : ds) {
            if (d != null) {
                int tX = d.getX();
                int tY = d.getY();
                if(x + 80 >= tX && x <= tX + 30 && y + 80 >= tY && y <= tY + 50 && d.getAlive()){
                    sc.restart();
                }
            }
        }
    }

    public void moveX(int xm) {
        x += xm;
        if (x > 320) {
            x = 320;
        }
        if (x < 0) {
            x = 0;
        }
    }
    
    public void updateLVL(int lvl) {
        if (lvl == 1) {
            maxCool = 15;
        } else {
            maxCool = 10;
        }
    }
     
}