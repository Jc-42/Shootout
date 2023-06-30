import java.awt.*;
import java.io.File;
import java.io.IOException;

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
    private int lastWallJumpDirection; //Used to stop the player from wall jumping on the same wall over and over
    private Image left, right, slideLeft, slideRight, dead;
    private Image activeSprite;
    
    public boolean onGround = false;
    public boolean onLWall = false;
    public boolean onRWall = false;

       
    private double lastFrameTime;
    private double frameRate;
    private double frameTime;
    private long currentTime;

    private Clip shot;
    private double speedMultiplier;

 

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
        lastWallJumpDirection = 0;
        this.left = left;
        this.right = right;
        this.slideLeft = slideLeft;
        this.slideRight = slideRight;
        this.dead = dead;
        currentTime = System.nanoTime();
        speedMultiplier = .8 * 100;
        frameRate = .01;

       

        // file = new File(".//data//usGunshot.wav");
        // audioStream = AudioSystem.getAudioInputStream(file);
        // shot = AudioSystem.getClip();
        // shot.open(audioStream);    
        // shot.setMicrosecondPosition(0);
        // shot.setMicrosecondPosition(0);
        // shot.setMicrosecondPosition(0);
        
    }

    

    public void setFile(String soundFileName){
        
        try{
            File file = new File(soundFileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            shot = AudioSystem.getClip();
            shot.open(sound);
        }
        catch(Exception e){

        }
    }

    public void play(){
        shot.setFramePosition(0);
        shot.start();
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
        currentTime = System.nanoTime();
        frameTime = (currentTime - lastFrameTime) / 1000000000.0;//000000000.0 / (currentTime - lastFrameTime);
        frameRate = .01;
        lastFrameTime = currentTime;
        if(frameTime == 0) frameTime = .01;
        else if(frameTime > 100) frameTime = .01;
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

        //Reset lastWallJumpDirection if the player hits the ground
        if(onGround) lastWallJumpDirection = 0;


        //If not moving or trying to move in the opposite direction facing, slow the player down quickly
        if((keyLeft && keyRight) || (!keyLeft && !keyRight) || (keyLeft && !keyRight && velocity[0] > 0) || (!keyLeft && keyRight && velocity[0] < 0)) velocity[0] *= 0.7 * speedMultiplier * frameRate;
        //Otherwise move the player
        else if(keyLeft && !keyRight){
            //directionFacing = -1;

            //Player moves faster if they are on the ground
            if(onGround){
                velocity[0] -= (0.6 * speedMultiplier * frameRate);
            }
            else{
                velocity[0] -= (0.2 * speedMultiplier * frameRate); 
            }
        }
        else if(!keyLeft && keyRight){
            //directionFacing = 1;
            if(onGround){
                velocity[0] += (0.6 * speedMultiplier * frameRate);
            }
            else{
                velocity[0] += (0.2 * speedMultiplier * frameRate); 
            }
        }

        //Set min speed
        if(Math.abs(velocity[0]) < .1) velocity[0] = 0;

        //Set max speed
        if(Math.abs(velocity[0]) >= 8) velocity[0] = 8 * Math.signum(velocity[0]);
        

        //Vertical max speed
        if(velocity[1] >= 13 * speedMultiplier * frameRate) velocity[1] = 13 * speedMultiplier * frameRate;
        else if(velocity[1] <= -13) velocity[1] = -13 * speedMultiplier * frameRate;

        //Jump
        if(keyUp){
            if(onGround) velocity[1] =(-13 * speedMultiplier * frameRate);
            else if(onLWall && !onRWall && keyRight && lastWallJumpDirection != 1) {
                velocity[1] = (-13 * speedMultiplier * frameRate); 
                lastWallJumpDirection = 1;
            }
            else if(!onLWall && onRWall && keyLeft && lastWallJumpDirection != -1){
                velocity[1] =(-13 * speedMultiplier * frameRate); 
                lastWallJumpDirection = -1;
            }
        }
        

        //Gravity
        if((onLWall || onRWall) && velocity[1] >= 0){
            //Set new Verticle max speed if on a wall
            if(Math.abs(velocity[1]) >= 2.6 * speedMultiplier * frameRate) velocity[1] = 2.6 * speedMultiplier * frameRate * Math.signum(velocity[1]);
            velocity[1] += (0.1 * speedMultiplier * frameRate);
        }
        else{
            //Added the if else so you can set a different fall gravity than jump gravity  
            if(velocity[1] <= 0) velocity[1] += (0.4 * speedMultiplier * frameRate);
            else if(velocity[1] > 0) velocity[1] += (0.4 * speedMultiplier * frameRate);   
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
                
                setFile(".//data//usGunshot.wav");
                play();
                shoot(game);
                onCooldown = true;
                shootCoolTimer = shootCoolDown;
            }
        }
        else{
            System.out.println(shootCoolTimer);
            if(frameTime != 0 && frameTime < 1) shootCoolTimer -= 1 * frameTime;
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
              game.p1Bullets.add(new Bullet(x - 6, y + 28, 1));
            }
            else if(!onLWall && onRWall && !onGround){
              game.p1Bullets.add(new Bullet(x + 6, y + 28, -1));
            }
            else{
                game.p1Bullets.add(new Bullet(x - 6 * Math.signum(directionFacing), y + 28, directionFacing));
            }

        }
        else if(num == 2){
            if(onLWall && !onRWall && !onGround){
                game.p2Bullets.add(new Bullet(x - 6, y + 28, 1));
            }
            else if(!onLWall && onRWall && !onGround){
                game.p2Bullets.add(new Bullet(x + 6, y + 28, -1));
            }
            else{
                game.p2Bullets.add(new Bullet(x - 6 * Math.signum(directionFacing), y + 28, directionFacing));
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
