import javax.imageio.ImageIO;
import javax.swing.*;
import javax.sound.sampled.*;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Shootout extends JPanel {
    private static boolean gameState;
    //static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    public static void main(String[] args) throws InterruptedException, UnsupportedAudioFileException, IOException, LineUnavailableException {
		JFrame win = new JFrame();
		win.setResizable(true);
		win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //win.setUndecorated(true);
        win.setSize(500, 500);
        //device.setFullScreenWindow(win);
		win.setTitle("Shooter Game - Julian");
		Shootout game = new Shootout(win);
		win.add(game);
		game.setFocusable(true);
		win.setVisible(true);
		win.setFocusable(true);
        
	    // game loop
		gameState = true;

		while(gameState == true){
			
			Thread.sleep(10);
			game.repaint();
		}

		game.repaint();
	}

    private JFrame window;
    public Player playerOne;
    public Player playerTwo;
    public ArrayList<Wall> walls;
    public ArrayList<Bullet> p1Bullets;
    public ArrayList<Bullet> p2Bullets;
    private int level;
    private int screen;
    private Image p1Left, p1Right, p1SlideLeft, p1SlideRight, p1Dead;
    private Image p2Left, p2Right, p2SlideLeft, p2SlideRight, p2Dead;

    //sprites
    // Sprite mexicanSprite;
    // Sprite usSprite;

    // PImage shootoutImage; 
    // StopWatch sw = new StopWatch();

    // //sound stuff
    // SoundFile mxGunshot;
    // SoundFile mxReload;
    // SoundFile mxDeathSound;

    // SoundFile usGunshot;
    // SoundFile usReload; 
    // SoundFile usDeathSound;

    // SoundFile musicTwo;
    // SoundFile musicThree;

    //random int to select background music
    
    int randMusicInt;
    

    public boolean gameOver = false;

    public Shootout(JFrame win) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        window = win;
		setSize(window.getWidth(), window.getHeight());
		setBackground(new Color(200, 200, 200));
        //frameRate(60);
        screen = 1;

        try {
			p1Left = ImageIO.read(new File("data\\Sprites\\mexicanSpriteLeft.png"));
			p1Right = ImageIO.read(new File("data\\Sprites\\mexicanSpriteRight.png"));
			p1SlideLeft = ImageIO.read(new File("data\\Sprites\\mexicanSpriteSlideLeft.png"));
			p1SlideRight = ImageIO.read(new File("data\\Sprites\\mexicanSpriteSlideRight.png"));
			p1Dead = ImageIO.read(new File("data\\Sprites\\mexicanSpriteDead.png"));

			p2Left = ImageIO.read(new File("data\\Sprites\\americanSpriteLeft.png"));
			p2Right = ImageIO.read(new File("data\\Sprites\\americanSpriteRight.png"));
			p2SlideLeft = ImageIO.read(new File("data\\Sprites\\americanSpriteSlideLeft.png"));
			p2SlideRight = ImageIO.read(new File("data\\Sprites\\americanSpriteSlideRight.png"));
			p2Dead = ImageIO.read(new File("data\\Sprites\\americanSpriteDead.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

        start();
        
        //set up sprites
        // mexicanSprite = new Sprite(this, "finalMexicanSprite.png", 2, 3, 0);
        // S4P.registerSprite(mexicanSprite);

        // usSprite = new Sprite(this, "finalAmericanSprite.png", 3, 2, 0);
        // S4P.registerSprite(usSprite);

        
    // shootoutImage = loadImage("shootoutStartImage.png");

       
        
        //set up sounds
        
      
        // mxGunshot = new SoundFile(this, "mxGunshot.mp3");
        // mxReload = new SoundFile(this, "mxReload.wav");
        // mxDeathSound = new SoundFile(this, "mxDeathSound.wav"); 
        
        // usGunshot = new SoundFile(this, "usGunshot.mp3");
        // usReload = new SoundFile(this, "usReload.wav");
        // usDeathSound = new SoundFile(this, "usDeathSound.wav");
    
        
        // musicTwo = new SoundFile(this, "backgroundmusic2.wav");
        // musicThree = new SoundFile(this, "backgroundmusic3.wav");

        //ADD THIS MUSIC SELECTION TO "GAME RESTART" METHOD
        


        win.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}
		
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == 87) playerOne.keyUp = true;
				if(key == 65) {playerOne.keyLeft = true; playerOne.directionFacing = -1;}
				if(key == 83) playerOne.keyDown = true;
				if(key == 68) {playerOne.keyRight = true; playerOne.directionFacing = 1;}

                if(key == 38) playerTwo.keyUp = true;
                if(key == 37) {playerTwo.keyLeft = true; playerTwo.directionFacing = -1;}
                if(key == 40) playerTwo.keyDown = true;
                if(key == 39) {playerTwo.keyRight = true; playerTwo.directionFacing = 1;}

                if(key == 32 && gameOver)
                    try {
                        newGame();
                    } catch (UnsupportedAudioFileException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (LineUnavailableException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                else if(key == 32) screen = 2;
			}
		
			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == 87) playerOne.keyUp = false;
				if(key == 65) { playerOne.keyLeft = false;
                    if(playerOne.keyRight){
                        playerOne.directionFacing = 1;
                    }
                }
				if(key == 83) playerOne.keyDown = false;
				if(key == 68) {playerOne.keyRight = false;
                    if(playerOne.keyLeft){
                        playerOne.directionFacing = -1;
                    }
                }

                if(key == 38) playerTwo.keyUp = false;
                if(key == 37) { playerTwo.keyLeft = false; 
                    if(playerTwo.keyRight){ 
                        playerTwo.directionFacing = 1;
                    }
                }
                if(key == 40) playerTwo.keyDown = false;
                if(key == 39) {playerTwo.keyRight = false; 
                    if(playerTwo.keyLeft){
                        playerTwo.directionFacing = -1;
                    }
                }
			}
		});
    }

    public void paint(Graphics g){
        super.paint(g);



        if(screen == 1){
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 200));
            g.drawString("SHOOTOUT", window.getWidth() / 2 - 560, 200);
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.setColor(Color.BLACK);
            g.drawString("Vaquero moves with w/a/d and shoots with s", 60, window.getHeight() / 2);
            g.drawString("JUGADOR NUMERO UNO (vaquero mexicano hacia la izquierda de la pantalla):", 60, window.getHeight() / 2 + 20);
            g.drawString("Usa las teclas WASD del teclado para jugar.", 60, window.getHeight() / 2 + 40);
            g.drawString("1. Para saltar, presiona la tecla W.", 60, window.getHeight() / 2 + 60);
            g.drawString("2. Para ir a la izquierda, presiona la tecla A.", 60, window.getHeight() / 2 + 80);
            g.drawString("3. Para ir a la derecha, presiona la tecla D.", 60, window.getHeight() / 2 + 100);
            g.drawString("4. Para disparar, presiona la tecla S.", 60, window.getHeight() / 2 + 120);

            g.drawString("Cowboy moves with up/left/right arrow keys and shoots with the down arrow key", window.getWidth() - 590, window.getHeight() / 2);
            g.drawString("JUGADOR NUMERO DOS (vaquero americano hacia la derecha de la pantalla)", window.getWidth() - 590, window.getHeight() / 2 + 20);
            g.drawString("Usa las flechas del teclado para jugar.", window.getWidth() - 590, window.getHeight() / 2 + 40);
            g.drawString("1. Para saltar, presiona la tecla de la flecha hacia arriba.", window.getWidth() - 590, window.getHeight() / 2 + 60);
            g.drawString("2. Para ir a la izquierda y a la derecha, usa tu mente (es f\u00E1cil, carnal).", window.getWidth() - 590, window.getHeight() / 2 + 80);
            g.drawString("3. Para disparar, presiona la tecla de la flecha hacia abajo", window.getWidth() - 590, window.getHeight() / 2 + 100);

            g.drawString("Slide down walls by holding either the left or right key towards the direction of a wall as you are falling.", window.getWidth() / 2 - 290, window.getHeight() / 2 + 140);
            g.drawString("Climb walls by alternating your jumps from left to right between each wall while holding down your up arrow key", window.getWidth() / 2 - 290, window.getHeight() / 2 + 160);
            g.drawString("Adem\u00E1s, puedes deslizarse por las paredes presionando la tecla izquierda o derecha", window.getWidth() / 2 - 290, window.getHeight() / 2 + 180);
            g.drawString("PARA TODOS:", window.getWidth() / 2 - 290, window.getHeight() / 2 + 200);
            g.drawString("hacia la direcci\u00F3n de una pared.", window.getWidth() / 2 - 290, window.getHeight() / 2 + 220);
            g.drawString("Si quieres un desaf\u00EDo, puedes escalar paredes alternando tus saltos entre cada pared", window.getWidth() / 2 - 290, window.getHeight() / 2 + 240);
            g.drawString("mientras mantienes presionada la tecla hacia arriba.", window.getWidth() / 2 - 290, window.getHeight() / 2 + 260);
            g.drawString("Buena suerte!", window.getWidth() / 2 - 290, window.getHeight() / 2 + 280);
            g.setColor(Color.WHITE);
            g.drawString("Press space to begin", window.getWidth() / 2 - 110, window.getHeight() / 2 - 100);
            g.drawString("Presiona la barra espaciadora para iniciar", window.getWidth() / 2 - 110, window.getHeight() / 2 - 80);
            //image
            
            // shootoutImage.resize(400, 700);
            // image(shootoutImage, width / 2 - 400 / 2, height / 2 - 700 / 2 - 250);
            
        }
        else if(screen == 2){
            if(!gameOver){
                g.setColor( new Color(200,200,200));
                // mexicanSprite.setScale(2.0);
                // mexicanSprite.setXY(playerOne.x + playerOne.sizeX/2, playerOne.y + playerOne.sizeY/2);

                // usSprite.setScale(2.0);
                // usSprite.setXY(playerTwo.x + playerTwo.sizeX/2, playerTwo.y + playerTwo.sizeY/2);
            
            
                for(int i = 0; i < p1Bullets.size(); i++){
                    p1Bullets.get(i).update(this, 1);
                    p1Bullets.get(i).paint(g, 1);
                    g.setColor(new Color(0, 0, 255));
                    if(p1Bullets.get(i).bounces > 1){
                        p1Bullets.remove(i);
                    }  
                } 

                for(int i = 0; i < p2Bullets.size(); i++){
                    p2Bullets.get(i).update(this, 2);
                    p2Bullets.get(i).paint(g, 2);            
                    g.setColor(new Color(255, 0, 0));
                    if(p2Bullets.get(i).bounces > 1){
                        p2Bullets.remove(i);
                    }  
                }
            
            //negative = facing left
            if (playerOne.directionFacing < 0) {
                playerOne.setSprite("left");
            }
            //facing right
            else if(playerOne.directionFacing > 0) {
                playerOne.setSprite("right");
            }
            if(playerOne.onLWall && !playerOne.onGround){
                playerOne.setSprite("slideLeft");
            }
            else if(playerOne.onRWall && !playerOne.onGround){
                playerOne.setSprite("slideRight");
            }
            
            
            

            if(playerTwo.directionFacing < 0){
                playerTwo.setSprite("left");
            }
            else if(playerTwo.directionFacing > 0){
                playerTwo.setSprite("right");
            }
            if(playerTwo.onLWall && !playerTwo.onGround){
                playerTwo.setSprite("slideLeft");
            }
            else if(playerTwo.onRWall && !playerTwo.onGround){
                playerTwo.setSprite("slideRight");
            }

            
            playerTwo.update(this);
            playerTwo.paint(g, window);
            
            playerOne.update(this);
            playerOne.paint(g, window);
                
            
            for(Wall w : walls){
                w.paint(g);
            }  

            

            for(int i = 0; i < playerOne.health; i++){
                g.setColor(new Color(0, 0, 255));
                g.fillOval(i * 30 + 30, 30, 20, 20);  
            }

            for(int i = 0; i < playerTwo.health; i++){
                g.setColor(new Color(255, 0, 0));
                g.fillOval(window.getWidth() - (i * 30 + 50), 30, 20, 20);  
            }

            if(playerOne.health <= 0 || playerTwo.health <= 0){
                gameOver = true;
            }
        }
        else{
            
                for(int i = 0; i < p1Bullets.size(); i++){
                    p1Bullets.get(i).paint(g, 1);
                } 

                for(int i = 0; i < p2Bullets.size(); i++){
                    p2Bullets.get(i).paint(g, 2);
                }
                for(Wall w : walls){
                w.paint(g);
            } 

            playerTwo.paint(g, window);
            playerOne.paint(g, window);

            if(playerOne.health <= 0){
                g.setColor(new Color(145, 150, 158, 100));
                g.fillRect(0, 0, window.getWidth(), window.getHeight());
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 40));
                g.drawString("Player 2 Wins", window.getWidth() / 2 - 110, window.getHeight() / 2);
                g.drawString("Press Space To Restart", window.getWidth() / 2 - 200, window.getHeight() / 2 + 60);
                playerOne.setSprite("dead");
            }
            else if(playerTwo.health <= 0){
                g.setColor(new Color(145, 150, 158, 100));
                g.fillRect(0, 0, window.getWidth(), window.getHeight());
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 40));
                g.drawString("Player 1 Wins", window.getWidth() / 2 - 110, window.getHeight() / 2);
                g.drawString("Press Space To Restart", window.getWidth() / 2 - 200, window.getHeight() / 2 + 60);
                playerTwo.setSprite("dead");
            }

            
        }
        
    }
        
    }

    public void newGame() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        
        playerOne = null;
        playerTwo = null;
        walls = null;
        p1Bullets = null;
        p2Bullets = null;
        gameOver = false;

        start();

    
    }

    public void start() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        screen = 1;
        level = (int)(Math.random() * 3) + 1;

        walls = new ArrayList<Wall>();
        // walls.add(new Wall(0, window.getHeight() - 50, window.getWidth(), 50));
        // walls.add(new Wall(0, 0, 15, window.getHeight()));
        // walls.add(new Wall(window.getWidth() - 15, 0, 15, window.getHeight()));
        // walls.add(new Wall(0, 0, window.getWidth(), 15));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        // Calculate the scaling factor based on the reference resolution (1920x1080)
        double scaleX = screenWidth / 1920;
        double scaleY = screenHeight / 1080;

        // Calculate the center offset
        double offsetX = (screenWidth - 1920 * scaleX) / 2;
        double offsetY = (screenHeight - 1080 * scaleY) / 2;

        if(level == 1){
            playerOne = new Player(offsetX + 90 * scaleX, offsetY + 100 * scaleX, 45 * scaleX, 90 * scaleY, 1, p1Left, p1Right, p1SlideLeft, p1SlideRight, p1Dead);
            playerTwo = new Player(offsetX + 1830 * scaleX, offsetY + 100 * scaleX, 45 * scaleX, 90 * scaleY, 2, p2Left, p2Right, p2SlideLeft, p2SlideRight, p2Dead);
            

            // Scale and center the walls
            walls.add(new Wall(offsetX + 0 * scaleX, offsetY + 950 * scaleY, 1920 * scaleX, 250 * scaleY));
            walls.add(new Wall(offsetX + 0 * scaleX, offsetY + 800 * scaleY, 450 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1470 * scaleX, offsetY + 800 * scaleY, 450 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 960 * scaleX, offsetY + 350 * scaleY, 15 * scaleX, 450 * scaleY));
            walls.add(new Wall(offsetX + 0 * scaleX, offsetY + 500 * scaleY, 460 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 710 * scaleX, offsetY + 600 * scaleY, 250 * scaleX, 20 * scaleY));
            walls.add(new Wall(offsetX + 820 * scaleX, offsetY + 350 * scaleY, 310 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1470 * scaleX, offsetY + 500 * scaleY, 500 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 150 * scaleX, offsetY + 720 * scaleY, 15 * scaleX, 95 * scaleY));
            walls.add(new Wall(offsetX + 975 * scaleX, offsetY + 600 * scaleY, 250 * scaleX, 20 * scaleY));
            walls.add(new Wall(offsetX + 1750 * scaleX, offsetY + 725 * scaleY, 15 * scaleX, 90 * scaleY));
            walls.add(new Wall(offsetX + 250 * scaleX, offsetY + 500 * scaleY, 15 * scaleX, 90 * scaleY));
            walls.add(new Wall(offsetX + 100 * scaleX, offsetY + 400 * scaleY, 15 * scaleX, 115 * scaleY));
            walls.add(new Wall(offsetX + 1800 * scaleX, offsetY + 400 * scaleY, 15 * scaleX, 115 * scaleY));
            walls.add(new Wall(offsetX + 1650 * scaleX, offsetY + 500 * scaleY, 15 * scaleX, 95 * scaleY));
        }
        else if (level == 2){
            
            playerOne = new Player(offsetX + 90 * scaleX, offsetY + 50 * scaleX, 45 * scaleX, 90 * scaleY, 1, p1Left, p1Right, p1SlideLeft, p1SlideRight, p1Dead);
            playerTwo = new Player(offsetX + 1830 * scaleX, offsetY + 50 * scaleX, 45 * scaleX, 90 * scaleY, 2, p2Left, p2Right, p2SlideLeft, p2SlideRight, p2Dead);

            walls.add(new Wall(offsetX + 0 * scaleX, offsetY + 150 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 250 * scaleX, offsetY + 150 * scaleY, 15 * scaleX, 250 * scaleY));
            walls.add(new Wall(offsetX + 115 * scaleX, offsetY + 399 * scaleY, 150 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 115 * scaleX, offsetY + 294 * scaleY, 15 * scaleX, 120 * scaleY));
            walls.add(new Wall(offsetX + 115 * scaleX, offsetY + 510 * scaleY, 150 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 115 * scaleX, offsetY + 510 * scaleY, 15 * scaleX, 390 * scaleY));
            walls.add(new Wall(offsetX + 365 * scaleX, offsetY + 0 * scaleY, 15 * scaleX, 415 * scaleY));
            walls.add(new Wall(offsetX + 115 * scaleX, offsetY + 885 * scaleY, 265 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 115 * scaleX, offsetY + 700 * scaleY, 150 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 380 * scaleX, offsetY + 400 * scaleY, 235 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 360 * scaleX, offsetY + 510 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 360 * scaleX, offsetY + 510 * scaleY, 15 * scaleX, 275 * scaleY));
            walls.add(new Wall(offsetX + 1670 * scaleX, offsetY + 150 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1670 * scaleX, offsetY + 150 * scaleY, 15 * scaleX, 250 * scaleY));
            walls.add(new Wall(offsetX + 1670 * scaleX, offsetY + 399 * scaleY, 150 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1810 * scaleX, offsetY + 294 * scaleY, 15 * scaleX, 120 * scaleY));
            walls.add(new Wall(offsetX + 1670 * scaleX, offsetY + 510 * scaleY, 160 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1670 * scaleX, offsetY + 510 * scaleY, 15 * scaleX, 275 * scaleY));
            walls.add(new Wall(offsetX + 1670 * scaleX, offsetY + 890 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1550 * scaleX, offsetY + 0 * scaleY, 15 * scaleX, 410 * scaleY));
            walls.add(new Wall(offsetX + 1315 * scaleX, offsetY + 510 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1670 * scaleX, offsetY + 670 * scaleY, 150 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 510 * scaleX, offsetY + 890 * scaleY, 1060 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1300 * scaleX, offsetY + 510 * scaleY, 15 * scaleX, 275 * scaleY));
            walls.add(new Wall(offsetX + 1550 * scaleX, offsetY + 510 * scaleY, 15 * scaleX, 275 * scaleY));
            walls.add(new Wall(offsetX + 1300 * scaleX, offsetY + 650 * scaleY, 100 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 600 * scaleX, offsetY + 510 * scaleY, 15 * scaleX, 275 * scaleY));
            walls.add(new Wall(offsetX + 500 * scaleX, offsetY + 650 * scaleY, 100 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 720 * scaleX, offsetY + 510 * scaleY, 475 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 750 * scaleX, offsetY + 710 * scaleY, 400 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1295 * scaleX, offsetY + 395 * scaleY, 270 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 800 * scaleX, offsetY + 400 * scaleY, 280 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 925 * scaleX, offsetY + 345 * scaleY, 15 * scaleX, 70 * scaleY));
            walls.add(new Wall(offsetX + 550 * scaleX, offsetY + 200 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1150 * scaleX, offsetY + 200 * scaleY, 250 * scaleX, 15 * scaleY));

        }
        else if(level == 3){
            
            playerOne = new Player(offsetX + 90 * scaleX, offsetY + 100 * scaleX, 45 * scaleX, 90 * scaleY, 1, p1Left, p1Right, p1SlideLeft, p1SlideRight, p1Dead);
            playerTwo = new Player(offsetX + 1830 * scaleX, offsetY + 100 * scaleX, 45 * scaleX, 90 * scaleY, 2, p2Left, p2Right, p2SlideLeft, p2SlideRight, p2Dead);

            walls.add(new Wall(offsetX + 250 * scaleX, offsetY + 800 * scaleY, 1350 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 950 * scaleX, offsetY + 685 * scaleY, 15 * scaleX, 130 * scaleY));
            walls.add(new Wall(offsetX + 400 * scaleX, offsetY + 650 * scaleY, 350 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1150 * scaleX, offsetY + 650 * scaleY, 350 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 840 * scaleX, offsetY + 500 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 0 * scaleX, offsetY + 965 * scaleY, 160 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1760 * scaleX, offsetY + 965 * scaleY, 160 * scaleX, 15 * scaleY));
        }
        else if(level == 4){
            playerOne = new Player(offsetX + 90 * scaleX, offsetY + 100 * scaleX, 45 * scaleX, 90 * scaleY, 1, p1Left, p1Right, p1SlideLeft, p1SlideRight, p1Dead);
            playerTwo = new Player(offsetX + 1830 * scaleX, offsetY + 100 * scaleX, 45 * scaleX, 90 * scaleY, 2, p2Left, p2Right, p2SlideLeft, p2SlideRight, p2Dead);

            walls.add(new Wall(offsetX + 100 * scaleX, offsetY + 590 * scaleY, 770 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1025 * scaleX, offsetY + 590 * scaleY, 770 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 0 * scaleX, offsetY + 200 * scaleY, 150 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1770 * scaleX, offsetY + 200 * scaleY, 150 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 950 * scaleX, offsetY + 0 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 660 * scaleX, offsetY + 300 * scaleY, 600 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 500 * scaleX, offsetY + 150 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 300 * scaleX, offsetY + 300 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 825 * scaleX, offsetY + 500 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 450 * scaleX, offsetY + 450 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1250 * scaleX, offsetY + 450 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1400 * scaleX, offsetY + 150 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 1650 * scaleX, offsetY + 300 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 300 * scaleX, offsetY + 150 * scaleY, 100 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1500 * scaleX, offsetY + 150 * scaleY, 100 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 200 * scaleX, offsetY + 300 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 1750 * scaleX, offsetY + 300 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 0 * scaleX, offsetY + 700 * scaleY, 250 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1650 * scaleX, offsetY + 700 * scaleY, 350 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 350 * scaleX, offsetY + 700 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 1520 * scaleX, offsetY + 700 * scaleY, 15 * scaleX, 200 * scaleY));
            walls.add(new Wall(offsetX + 80 * scaleX, offsetY + 850 * scaleY, 200 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1610 * scaleX, offsetY + 850 * scaleY, 200 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 500 * scaleX, offsetY + 700 * scaleY, 900 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 450 * scaleX, offsetY + 900 * scaleY, 15 * scaleX, 100 * scaleY));
            walls.add(new Wall(offsetX + 450 * scaleX, offsetY + 900 * scaleY, 1000 * scaleX, 15 * scaleY));
            walls.add(new Wall(offsetX + 1450 * scaleX, offsetY + 900 * scaleY, 15 * scaleX, 100 * scaleY));
            walls.add(new Wall(offsetX + 950 * scaleX, offsetY + 800 * scaleY, 15 * scaleX, 200 * scaleY)); 
        }
        
        


        p1Bullets = new ArrayList<Bullet>();
        p2Bullets = new ArrayList<Bullet>();
    }
}
