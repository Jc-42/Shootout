import java.awt.*;

public class Bullet{
    public double x, y;
    public double sizeX, sizeY;
    private double[] hitBox;
    public int xDirection;
    public int yDirection;
    public double speed;
    public int bounces;


    public Bullet(double x, double y, int xDirection){
        this.x = x;
        this.y = y;
        sizeX = 10;
        sizeY = 5;
        hitBox = new double[]{x, y, sizeX, sizeY};
        this.xDirection = xDirection;
        yDirection = 0;
        speed = 20;
        bounces = 0;
    }

    public void paint(Graphics g, int num){
        if(num == 1){
           g.setColor(new Color(0, 0, 255));   
        }
        else if(num == 2){  
          g.setColor(new Color(255, 0, 0));
        }
        g.fillRect((int)x, (int)y, (int)sizeX, (int)sizeY);
    }

    public void update(Shootout game, int num){
        hitBox[0] += speed * Math.signum(xDirection);
        hitBox[1] += speed * Math.signum(yDirection);
        for(Wall w : game.walls){
            if(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3])){
                hitBox[0] -= speed * Math.signum(xDirection);
                //Interpolation
                while(w.intersects(hitBox[0], hitBox[1], hitBox[2], hitBox[3]) == false) hitBox[0] += Math.signum(xDirection);
                hitBox[0] -= Math.signum(xDirection);
                x += Math.signum(xDirection) * (speed);
                y += Math.signum(yDirection) * (speed);
                
                if(bounces < 1){
                    if(num == 1){
                        if(game.playerTwo.y >= game.playerOne.y){
                            yDirection = 1;
                        }
                        else{
                            yDirection = -1;
                        }
                    }
                    else if(num == 2){
                        if(game.playerOne.y >= game.playerTwo.y){
                            yDirection = 1;
                        }
                        else{
                            yDirection = -1;
                        }
                    }
                }
                
                
                xDirection *= -1;
                speed *= .6;
                bounces++;
            }
        }
        

        x = hitBox[0];
        y = hitBox[1];
    }

    public boolean intersects(double otherX, double otherY, double otherSizeX, double otherSizeY) {
        int tw = (int)sizeX;
        int th = (int)sizeY;
        int rw = (int)otherSizeX;
        int rh = (int)otherSizeY;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int tx = (int)x;
        int ty = (int)y;
        int rx = (int)otherX;
        int ry = (int)otherY;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }

}
