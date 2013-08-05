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
   }

   public Ball() {
      this.num = ballCount++;
      x = R.nextDouble() * BounceFrame.DEFAULT_WIDTH;
      y = R.nextDouble() * BounceFrame.DEFAULT_HEIGHT;
      dx = R.nextDouble();
      dy = R.nextDouble();
      this.color = R.nextInt() % 2 == 0 ? Color.RED : Color.BLUE;
      int sizeChoice = R.nextInt() % 7;
      if (sizeChoice == 0) { setSize(Size.LARGE); } 
      else if (sizeChoice < 3) { setSize(Size.MEDIUM); } 
      else { setSize(Size.SMALL); }
   }

   public void move(Rectangle2D bounds) {
      x += dx;
      y += dy;
      // escape hatch is when x - 
      if (x - getRadius() <= bounds.getMinX()) {
         x = bounds.getMinX() + getRadius();
         dx = -dx;
      }
      if (x + getRadius() >= bounds.getMaxX()) {
         x = bounds.getMaxX() - getRadius();
         dx = -dx;
      }
      if (y - getRadius() <= bounds.getMinY()) {
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
      return radius * radius * radius * 4 / 3;
   }
   public void resetCollisionTimer() { collisionTimer = 0; }
   public void incrementCollisionTimer() { collisionTimer++; }
   public int getCollisionTimer() { return collisionTimer; }

}