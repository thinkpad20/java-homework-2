package ballBounce;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.util.Random;

public class Ball {
   static Random R = new Random();
   static int ballCount;
   public static enum Size { SMALL, MEDIUM, LARGE };
   private double x;
   private double y;
   private double dx;
   private double dy;
   private int radius;
   private Color color;
   private Size size;
   private int collisionTimer;
   public int num;

   public Ball(Color color, Size size) {
      this();
      setColor(color);
      setSize(size);
      if (color == Color.RED) {
         BallComponent.incRed();
      } else {
         BallComponent.incBlue();
      }
   }

   public Ball() {
      this.num = ballCount++;
      x = R.nextDouble() * BounceFrame.DEFAULT_WIDTH;
      y = R.nextDouble() * BounceFrame.DEFAULT_HEIGHT;
      dx = R.nextDouble();
      dy = R.nextDouble();
      if (R.nextInt() % 3 == 0) {
         color = Color.RED;
         BallComponent.incRed();
      } else {
         color = Color.BLUE;
         BallComponent.incBlue();
      }
      int sizeChoice = R.nextInt() % 7;
      if (sizeChoice == 0) { setSize(Size.LARGE); } 
      else if (sizeChoice < 3) { setSize(Size.MEDIUM); } 
      else { setSize(Size.SMALL); }
   }

   public void move(BallComponent comp) {
      x += dx;
      y += dy;
      Rectangle2D bounds = comp.getBounds();
      // escape hatch in upper left corner
      if (x - getRadius() <= bounds.getMinX()) {
         if (y < bounds.getMinY() + BounceFrame.ESCAPE_HATCH_Y) {
            comp.remove(this);
            return;
         }
         x = bounds.getMinX() + getRadius();
         dx = -dx;
      }
      if (x + getRadius() >= bounds.getMaxX()) {
         x = bounds.getMaxX() - getRadius();
         dx = -dx;
      }
      if (y - getRadius() <= bounds.getMinY()) {
         if (x < bounds.getMinX() + BounceFrame.ESCAPE_HATCH_X) {
            comp.remove(this);
            return;
         }
         y = bounds.getMinY() + getRadius();
         dy = -dy;
      }
      if (y + getRadius() >= bounds.getMaxY()) {
         y = bounds.getMaxY() - getRadius();
         dy = -dy;
      }
      incrementCollisionTimer();
   }

   public Ellipse2D getShape() {
      return new Ellipse2D.Double(x, y, radius*2, radius*2);
   }

   public void reverseX() { dx *= -1; }
   public void reverseY() { dy *= -1; }
   public int getRadius() { return radius; }
   public void setRadius(int radius) { this.radius = radius; }
   public Size getSize() { return size; }
   public void setSize(Size size) { 
      this.size = size;
      if (size == Size.LARGE)
         radius = 20;
      else if (size == Size.MEDIUM)
         radius = 10;
      else
         radius = 5;
   }
   public int getDiameter() { return 2 * radius; }
   public Color getColor() { return color; }
   public void setColor(Color color) { this.color = color; }
   public double getX() { return getShape().getCenterX(); }
   public double getY() { return getShape().getCenterY(); }
   public double getDX() { return dx; }
   public double getDY() { return dy; }
   public void setDX(double dx) { this.dx = dx; }
   public void setDY(double dy) { this.dy = dy; }
   public double getMass() {
      if (color == Color.RED)
         return radius * radius * radius * 8 / 3;
      else
         return radius * radius * radius * 4 / 3;
   }
   public void resetCollisionTimer() { collisionTimer = 0; }
   public void incrementCollisionTimer() { collisionTimer++; }
   public int getCollisionTimer() { return collisionTimer; }

}