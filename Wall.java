import java.awt.*;

public class Wall{
    private double x, y;
    private double sizeX, sizeY;
    private double[] hitBox;
    private Color color;

    public Wall(double x, double y, double sizeX, double sizeY){
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        hitBox = new double[]{x, y, x + sizeX, y + sizeY};
        this.color = Color.BLACK;
    }

    public void paint(Graphics g){
        g.setColor(color);
        g.fillRect((int)x, (int)y, (int)sizeX, (int)sizeY);
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
