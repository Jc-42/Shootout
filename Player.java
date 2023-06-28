import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class Player{
    public double x, y;
    private double sizeX, sizeY;
    private int num;
    private double[] velocity;
    public boolean keyLeft, keyRight, keyUp, keyDown;
    public int directionFacing;
    private double[] hitBox;
    public float shootCoolDown = 1;
    public float shootCoolTimer;
    public boolean onCooldown = false;
    public int health;
    private int lastJumpDirection;
    private Image left, right, slideLeft, slideRight, dead;
    private Image activeSprite;
    
    public boolean onGround = false;
    public boolean onLWall = false;
    public boolean onRWall = false;

    private File file;
    private AudioInputStream audioStream;
    private Clip shot;

    public Player(double x, double y, double sizeX, double sizeY, int num, Image left, Image right, Image slideLeft, Image slideRight,Image dead) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.num = num;
        velocity = new double[2];
        directionFacing = 1;
        hitBox = new double[]{x, y, sizeX, sizeY};
        health = 3;
        lastJumpDirection = 0;
        this.left = left;
        this.right = right;
        this.slideLeft = slideLeft;
        this.slideRight = slideRight;
        this.dead = dead;
        

        file = new File("data\\usGunshot.wav");
        audioStream = AudioSystem.getAudioInputStream(file);
        shot = AudioSystem.getClip();
        shot.open(audioStream);        
        shot.setMicrosecondPosition(0);
    }

    public void paint(Graphics g, JFrame window){
        if(activeSprite == slideLeft){
            g.drawImage(activeSprite, (int)x - (int)(sizeX * 2.5) / 3 + 8, (int)y,(int)(sizeX * 2.5), (int)sizeY, window);
        }
        else if(activeSprite == slideRight){
            g.drawImage(activeSprite, (int)x - (int)(sizeX * 2.5) / 3 - 6, (int)y,(int)(sizeX * 2.5), (int)sizeY, window);
        }
        else if(num == 1){
            g.drawImage(activeSprite, (int)x - (int)(sizeX * 2) / 4, (int)y,(int)(sizeX * 2), (int)sizeY, window);
        }
        else if(num == 2){
            g.drawImage(activeSprite, (int)x - (int)(sizeX * 2) / 4, (int)y,(int)(sizeX * 2.2), (int)sizeY, window);
        }
       

        //Show Hitbox
        //g.drawRect((int)x, (int)y, (int)sizeX, (int)sizeY);
    }

    public void update(Shootout game){
        
        onLWall = false;
        onRWall = false;
        onGround = false;
        //Check if on ground
        hitBox[1]++;
        for(Wall w : game.walls){
            if(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3])) onGround = true;
        }
        hitBox[1]--;

        //Check if on a wall
        hitBox[0]--;
        for(Wall w : game.walls){
            if(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3])) onLWall = true;
        }
        hitBox[0]++;

        hitBox[0]++;
        for(Wall w : game.walls){
            if(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3])) onRWall = true;
        }
        hitBox[0]--;

        


        //If not moving or trying to move in the opposite direction facing, slow the player down quickly
        if((keyLeft && keyRight) || (!keyLeft && !keyRight) || (keyLeft && !keyRight && velocity[0] > 0) || (!keyLeft && keyRight && velocity[0] < 0)) velocity[0] *= 0.7;
        //Otherwise move the player
        else if(keyLeft && !keyRight){
            //directionFacing = -1;

            //Player moves faster if they are on the ground
            if(onGround){
                velocity[0] -= 0.6;
            }
            else{
                velocity[0] -= 0.2; 
            }
        }
        else if(!keyLeft && keyRight){
            //directionFacing = 1;
            if(onGround){
                velocity[0] += 0.6;
            }
            else{
                velocity[0] += 0.2; 
            }
        }

        //Set min speed
        if(Math.abs(velocity[0]) < .1) velocity[0] = 0;

        //Set max speed
        if(Math.abs(velocity[0]) >= 8) velocity[0] = 8 * Math.signum(velocity[0]);
        

        //Vertical max speed
        if(Math.abs(velocity[1]) >= 13) velocity[1] = 13 * Math.signum(velocity[1]);

        //Jump
        if(keyUp){
            if(onGround) velocity[1] = -13;
            else if(onLWall && !onRWall && keyRight && lastJumpDirection != 1) {
                velocity[1] = -13; 
                lastJumpDirection = 1;
            }
            else if(!onLWall && onRWall && keyLeft && lastJumpDirection != -1){
                 velocity[1] = -13; 
                 lastJumpDirection = -1;
            }
        } 
        

        //Gravity
        if((onLWall || onRWall) && velocity[1] >= 0){
            //Set new Verticle max speed if on a wall
            if(Math.abs(velocity[1]) >= 2.6) velocity[1] = 2.6 * Math.signum(velocity[1]);
            velocity[1] += .2;
        }
        else{
            velocity[1] += .5;
        }

        
        //Horizontal collision
        hitBox[0] += velocity[0];
        for(Wall w : game.walls){
            if(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3])){
                hitBox[0] -= velocity[0];
                //Interpolation
                while(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3]) == false) hitBox[0] += Math.signum(velocity[0]);
                hitBox[0] -= Math.signum(velocity[0]);
                velocity[0] = 0;
                x = hitBox[0];
            }
        }
        

        //Vertical collision
        hitBox[1] += velocity[1];
        for(Wall w : game.walls){
            if(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3])){
                hitBox[1] -= velocity[1];
                //Interpolation
                while(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3]) == false) hitBox[1] += Math.signum(velocity[1]);
                hitBox[1] -= Math.signum(velocity[1]);
                velocity[1] = 0;
                y = hitBox[1];
            }
        }
        

        x += velocity[0];
        y += velocity[1];
        hitBox[0] = x;
        hitBox[1] = y;
        


        if(!onCooldown){
            if(keyDown){
                
                shot.setMicrosecondPosition(0);
                shot.setMicrosecondPosition(0);
                shot.setMicrosecondPosition(0);
                shot.start();
                shoot(game);
                onCooldown = true;
                shootCoolTimer = shootCoolDown;
            }
        }
        else{
            shootCoolTimer -= .05; //frameRate;
            if(shootCoolTimer <= 0){
                onCooldown = false;
            }
        }

        if(num == 1){
            for(int i = 0; i < game.p2Bullets.size(); i++){
                if(game.p2Bullets.get(i).intersects(x, y, sizeX, sizeY)){
                    health--;
                    game.p2Bullets.remove(i);
                    i--;
                }
            }
        }
        else if(num == 2){
            for(int i = 0; i < game.p1Bullets.size(); i++){
                if(game.p1Bullets.get(i).intersects(x, y, sizeX, sizeY)){
                    health--;
                    game.p1Bullets.remove(i);
                    i--;
                }
            }  
        }
    }

    

    public void shoot(Shootout game){
        if(num == 1){
        }
        else if(num == 2){
        }

        if(num == 1){
            if(onLWall && !onRWall && !onGround){
              game.p1Bullets.add(new Bullet(x, y + 40, 1));
            }
            else if(!onLWall && onRWall && !onGround){
              game.p1Bullets.add(new Bullet(x, y + 40, -1));
            }
            
            else if(keyLeft && !keyRight){
                game.p1Bullets.add(new Bullet(x, y + 40, directionFacing));
            }
            else{
                game.p1Bullets.add(new Bullet(x, y + 40, directionFacing));
            }

        }
        else if(num == 2){
            if(onLWall && !onRWall && !onGround){
                game.p2Bullets.add(new Bullet(x, y + 40, 1));
            }
            else if(!onLWall && onRWall && !onGround){
                game.p2Bullets.add(new Bullet(x, y + 40, -1));
            }
            else if(keyLeft && !keyRight){
                game.p2Bullets.add(new Bullet(x, y + 40, directionFacing));
            }
            else{
                game.p2Bullets.add(new Bullet(x, y + 40, directionFacing));
            }
        }
    }

    public void setSprite(String sprite){
        if(sprite.toLowerCase().equals("left")){
            activeSprite = left;
        }
        else if(sprite.toLowerCase().equals("right")){
            activeSprite = right;
        }
        else if(sprite.toLowerCase().equals("slideleft")){
            activeSprite = slideLeft;
        }
        else if(sprite.toLowerCase().equals("slideright")){
            activeSprite = slideRight;
        }
        else if(sprite.toLowerCase().equals("dead")){
            activeSprite = dead;
        }
    }
}
