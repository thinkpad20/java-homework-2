package ballBounce;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JComponent;

class BallComponent extends JComponent {
   private ArrayList<Ball> balls = new ArrayList<Ball>();

   public void add(Ball b) {
      balls.add(b);
   }

   public void paintComponent(Graphics g) {
   Graphics2D g2 = (Graphics2D) g;
      for (Ball b : balls) {
         g2.fill(b.getShape());
      }
   }

   public void detectCollisions() {
      for (int i=0; i<balls.size()-1; i++) {
         for (int j=i+1; j<balls.size(); j++) {
            if (isCollision(balls.get(i), balls.get(j))) {
               handleCollision(balls.get(i), balls.get(j));
            }
         }
      }
   }

   private boolean isCollision(Ball ball1, Ball ball2) {
      int nCenter1X = (int)ball1.getShape().getCenterX();
      int nCenter1Y = (int)ball1.getShape().getCenterY();
      int nCenter2X = (int)ball2.getShape().getCenterX();
      int nCenter2Y = (int)ball2.getShape().getCenterY();

      int nRadius1 = (int)ball1.getRadius();
      int nRadius2 = (int)ball2.getRadius();

      int nDistanceSquared = (nCenter2X-nCenter1X) * (nCenter2X-nCenter1X) +
      (nCenter2Y-nCenter1Y) * (nCenter2Y-nCenter1Y);

      return nDistanceSquared <= 4*nRadius1*nRadius2;
   }

   private void handleCollision(Ball ball1, Ball ball2) {
      if (ball1.getShape().getCenterX() > ball2.getShape().getCenterX()) {
         ball1.reverseX();
         ball2.reverseX();
      }

      if (ball1.getShape().getCenterY() > ball2.getShape().getCenterY()) {
         ball1.reverseY();
         ball2.reverseY();
      }
   }

   public void move() {
      for (Ball b : balls)
         b.move(getBounds());
   }

   public void setRadius(int newRadius) {
      for (Ball b : balls)
         b.setRadius(newRadius);
   }
}