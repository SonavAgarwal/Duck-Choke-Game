import java.awt.*;

import javax.swing.JPanel;
 
import java.awt.event.*;
 
public class Screen extends JPanel implements KeyListener {
 
    private Projectile[] projectiles = new Projectile[5];
    private Ship ship;
    private Duck[] ducks;
    private int enemyCt;
    private int score;

    private String font = "Arial";

    private Water[] waves = new Water[60];

    private int level;

    private int lives = 3;

    private boolean left, right, shoot;

    private int scw, sch;

    private int enemyBase = 20;

    private Color statColor = new Color(100, 100, 100);
    private Color waterColor = new Color(52, 158, 235); //245, 90, 34

    private boolean lvl2trans = false;
    private boolean transdone = false;
    private boolean lvl2text = false;
    private double lvl2opacity = 0;

    private boolean lost = false;
    private boolean won = false;
 
    public Screen() {
        scw = 450;
        sch = 800;
         
        ship = new Ship(50,700);

        for (int i = 0; i < projectiles.length; i++) {
            projectiles[i] = new Projectile(i);
        }

        level = 1;

        ducks = new Duck[level * enemyBase + 10];
        enemyCt = level * enemyBase;
        for (int i = 0; i < ducks.length; i++) {
                ducks[i] = new Duck(level);
        }

        for (int i = 0; i < waves.length; i++) {
            waves[i] = new Water(level);
        }

        left = false;
        right = false;
        shoot = false;
 
        setFocusable(true);
        addKeyListener(this);
    }
 
    public Dimension getPreferredSize() {
        //Sets the size of the panel
            return new Dimension(scw, sch);
    }
     
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!lost && !won) {
            g.setColor(waterColor);
            g.fillRect(0, 0, scw, sch);

            for (int i = 0; i < waves.length; i++) {
                waves[i].draw(g);
            }

            g.setColor(statColor);
            g.fillRect(scw - 50, 0, 50, sch);
            

            for (Projectile p : projectiles) {
                p.draw(g);
            }

            for (Duck d : ducks) {
                d.draw(g);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font(font, Font.BOLD, 30));
            g.drawString("" + score, 407, 50);
            g.drawString("" + lives, 416, 100);
            g.drawString("" + level, 416, 220);

            g.setColor(Color.RED);
            g.fillOval(407, 80 + 40, 18, 18);
            g.fillOval(424, 80 + 40, 19, 18);
            g.fillPolygon(new int[]{407, 425, 443}, new int[]{92 + 40, 120 + 40, 92 + 40}, 3);

        } else if (lost) {
            g.setColor(Color.BLACK);
            g.setFont(new Font(font, Font.BOLD, 30)); 
            g.drawString("you lost", 90, 200);
            g.setFont(new Font(font, Font.PLAIN, 20)); 
            g.drawString("the ducks have beaten you", 90, 230);
            g.drawString("don't worry though", 90, 260);
            g.drawString("you can always try again", 90, 300);
            g.drawString("press 'r' to choke more ducks", 90, 330);
        } else if (won) {
            g.setColor(Color.BLACK);
            g.setFont(new Font(font, Font.BOLD, 30)); 
            g.drawString("amazing work", 90, 200);
            g.setFont(new Font(font, Font.PLAIN, 20)); 
            g.drawString("you've beaten the ducks", 90, 230);
            g.drawString("for now, that is", 90, 260);
            g.drawString("if you wish to play again", 90, 300);
            g.drawString("press 'r' to choke more ducks", 90, 330);
        }
        
        
        if (lvl2text) {
            g.setColor(new Color(0, 0, 0, (int) cap((int) lvl2opacity, 255)));

            g.setFont(new Font(font, Font.BOLD, 30)); 
            g.drawString("congratulations", 90, 200);
            g.setFont(new Font(font, Font.PLAIN, 20)); 
            g.drawString("but the ducks are strong", 90, 230);
            g.drawString("they've mutated", 90, 260);
            g.drawString("but you've had time to bake", 90, 300);
            g.drawString("lots and lots of bread", 90, 330);
        }
             
        ship.draw(g);
    }
 
    public void animateContents() {
        ship.update(ducks, this);
        for (Projectile  p : projectiles) {
            if (p != null) {
                p.update(ducks, this);
            }
            
        }

        for (int i = 0; i < waves.length; i++) {
            waves[i].update();
        }

        if (shoot)
            ship.shoot(projectiles);
        if (right)
            ship.moveX(3);
        if (left)
            ship.moveX(-3);

        enemyCt = level * enemyBase + 10;
        for (Duck d : ducks) {
            if (d != null) {
                d.update();
                if (d.getY() > sch) {
                    restart();
                    break;
                }
                if (!d.getAlive()) enemyCt--;
            }
        }
        if (enemyCt == 0) {
            if (level == 1) {
                updateLevel();
            } else {
                won();
            }
        }

    }

    public void won() {
        won = true;
        repaint();
    }

    public int cap(int n, int max) {
        if (n <= max) {
            return n;
        } else {
            return max;
        }
    }

    public void animate(){
        while(true){
            //wait for .01 second
            try {
                Thread.sleep(10);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            if (!lvl2trans || transdone) {
                animateContents();
            } else if (lvl2text) {
                if (lvl2opacity < 255) {
                    lvl2opacity  += 0.35;
                }
                if (lvl2opacity >= 255) {
                    lvl2text = false;
                    transdone = true;
                    lvl2trans = true;
                }
            }

            if (lost) break;
            if (won) break;

            repaint();
        }
 
    }

    public void gameOver() {
        lost = true;
        repaint();
    }

    public void restart() {
        lives--;
        score = 0;

        if (lives == 0) {
            gameOver();
            return;
        }

        waterColor = new Color(52, 158, 235);

        int oldLVL = level;

        level = 1;

        ship = new Ship(50,700);

        projectiles = new Projectile[5];

        for (int i = 0; i < projectiles.length; i++) {
            projectiles[i] = new Projectile(i);
        }

        for (int i = 0; i < projectiles.length; i++) {
            projectiles[i] = new Projectile(i);
        }

        ducks = new Duck[level * enemyBase + 10];
        enemyCt = level * enemyBase;
        for (int i = 0; i < ducks.length; i++) {
                ducks[i] = new Duck(level);
        }

        for (int i = 0; i < waves.length; i++) {
            waves[i] = new Water(level);
        }

        left = false;
        right = false;
        shoot = false;

        if (oldLVL == 2) {
            updateLevel();
        }
    }

    public void updateLevel() {

        if (!lvl2trans) {
            lvl2text = true;
            lvl2trans = true;
        }

        level++;
        if (level == 1) {
            waterColor = new Color(52, 158, 235);
        } else if (level == 2) {
            waterColor = new Color(245, 55, 34);
        }
        
        ship.updateLVL(level);

        projectiles = new Projectile[9];

        for (int i = 0; i < projectiles.length; i++) {
            projectiles[i] = new Projectile(i);
        }

        for (Projectile  p : projectiles) {
            p.updateLVL(level);
        }

        for (int i = 0; i < waves.length; i++) {
            waves[i].updateLVL(level);
        }
        
        // score = 0;
        // for (Duck d : ducks) {
        //     d.die();
        // }

        if (level == 3) {
            won();
            return;
        }

        ducks = new Duck[level * enemyBase + 10];
        enemyCt = level * enemyBase;
        for (int i = 0; i < ducks.length; i++) {
                ducks[i] = new Duck(level);
        }
    }

    public void addScore() {
        score++;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 32) {
            shoot = true;
        } else if (e.getKeyCode() == 39) {
            right = true;
        } else if (e.getKeyCode() == 37) {
            left = true;
        } else if (e.getKeyCode() == 80) {
            updateLevel();
        } else if (e.getKeyCode() == 82) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    lost = false;
                    won = false;
                    level = 1;
                    lives = 4;
                    score = 0;
                    restart();
                    animate();
                }
            });  
            t1.start();
            
            
        }
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 32) {
            shoot = false;
        } else if (e.getKeyCode() == 39) {
            right = false;
        } else if (e.getKeyCode() == 37) {
            left = false;
        }
    }
    public void keyTyped(KeyEvent e)  {
    }
    
}