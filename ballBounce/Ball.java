package ballBounce;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Ball {
   Random randNum = new Random();
   int randHeight = randNum.nextInt(350);
   int randWidth = randNum.nextInt(450);
   private double x = randWidth;
   private double y = randHeight;
   private double dx = 1;
   private double dy = 1;
   private int nRadius = 10;

   public void move(Rectangle2D bounds) {
      x += dx;
      y += dy;
      if (x < bounds.getMinX()) {
         x = bounds.getMinX();
         dx = -dx;
      }
      if (x + getDiameter() >= bounds.getMaxX()) {
         x = bounds.getMaxX() - getDiameter();
         dx = -dx;
      }
      if (y < bounds.getMinY()) {
         y = bounds.getMinY();
         dy = -dy;
      }
      if (y + getDiameter() >= bounds.getMaxY()) {
         y = bounds.getMaxY() - getDiameter();
         dy = -dy;
      }
   }

   public Ellipse2D getShape() {
      return new Ellipse2D.Double(x, y, nRadius*2, nRadius*2);
   }

   public void reverseX() { dx *= -1; }
   public void reverseY() { dy *= -1; }
   public int getRadius() { return nRadius; }
   public void setRadius(int nRadius) { this.nRadius = nRadius; }
   public int getDiameter() { return 2 * nRadius; }
}