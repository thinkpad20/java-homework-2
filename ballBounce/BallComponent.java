package ballBounce;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.awt.Color;

import javax.swing.JComponent;

class BallComponent extends JComponent {
   private ArrayList<Ball> balls = new ArrayList<Ball>();

   public void add(Ball b) { balls.add(b); }

   public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      for (Ball b : balls) {
         g2.setPaint(b.getColor());
         g2.fill(b.getShape());
      }
   }

   public void detectCollisions() {
      for (int i=0; i<balls.size()-1; i++) {
         for (int j=i+1; j<balls.size(); j++) {
            Ball ball1 = balls.get(i), ball2 = balls.get(j);
            if (isCollision(ball1, ball2)) {
               if (ball1.getCollisionTimer() > 50 && ball2.getCollisionTimer() > 50) {
                  handleCollision(ball1, ball2);
                  ball1.resetCollisionTimer();
                  ball2.resetCollisionTimer();
               }
            }
         }
      }
   }

   private boolean isCollision(Ball ball1, Ball ball2) {
      double x1 = ball1.getX(), y1 = ball1.getY(),
             x2 = ball2.getX(), y2 = ball2.getY();

      int r1 = ball1.getRadius();
      int r2 = ball2.getRadius();

      double distanceSquared = (x2-x1) * (x2-x1) + (y2-y1) * (y2-y1);

      return distanceSquared <= 4*r1*r2;
   }

   private void bounce(Ball ball1, Ball ball2) {
      double xDist = ball1.getX()-ball2.getX(),
             yDist = ball1.getY()-ball2.getY(),
             theta = Math.atan2(yDist, xDist),
             speed1 = Math.sqrt(ball1.getDX() * ball1.getDX() 
                                 + ball1.getDY() * ball1.getDY()),
             speed2 = Math.sqrt(ball2.getDX() * ball2.getDX() 
                                 + ball2.getDY() * ball2.getDY()),
             dir1 = Math.atan2(ball1.getDY(), ball1.getDX()),
             dir2 = Math.atan2(ball2.getDY(), ball2.getDX()),
             newDX1 = speed1 * Math.cos(dir1 - theta),
             newDY1 = speed1 * Math.sin(dir1 - theta),
             newDX2 = speed2 * Math.cos(dir2 - theta),
             newDY2 = speed2 * Math.sin(dir2 - theta),
             finalDX1 = ((ball1.getMass() - ball2.getMass()) * newDX1
                  + (ball2.getMass() + ball2.getMass()) * newDX2)
                  / (ball1.getMass() + ball2.getMass()),
             finalDX2 = ((ball1.getMass() + ball1.getMass()) * newDX1
                  + (ball2.getMass() - ball1.getMass()) * newDX2)
                  / (ball1.getMass() + ball2.getMass()),
             finalDY1 = newDY1,
             finalDY2 = newDY2;
      ball1.setDX(Math.cos(theta) * finalDX1 + Math.cos(theta + Math.PI/2) * finalDY1);
      ball1.setDY(Math.sin(theta) * finalDX1 + Math.sin(theta + Math.PI/2) * finalDY1);
      ball2.setDX(Math.cos(theta) * finalDX2 + Math.cos(theta + Math.PI/2) * finalDY2);
      ball2.setDY(Math.sin(theta) * finalDX2 + Math.sin(theta + Math.PI/2) * finalDY2);
   }

   private void handleCollision(Ball ball1, Ball ball2) {
      if (ball1.getColor() == ball2.getColor()) {
         if (ball1.getSize() == ball2.getSize())
            bounce(ball1, ball2);
      // } else {
      //    System.out.println("ball " + ball1.num + " and " + ball2.num + " have collided!");
      //    add(new Ball(Color.RED, Ball.Size.SMALL));
      } else {
         if (ball1.getSize() != ball2.getSize()) {
            Ball smallerBall = ball1.getRadius() < ball2.getRadius() ? ball1 : ball2;
            balls.remove(smallerBall);
         }
         else if (ball1.getSize() == Ball.Size.SMALL) {
            add(new Ball(Color.RED, Ball.Size.SMALL));
         } else if (ball1.getSize() == Ball.Size.MEDIUM) {
            add(new Ball(Color.RED, Ball.Size.SMALL));
            add(new Ball(Color.RED, Ball.Size.MEDIUM));
         } else if (ball1.getSize() == Ball.Size.LARGE) {
            add(new Ball(Color.RED, Ball.Size.SMALL));
            add(new Ball(Color.RED, Ball.Size.SMALL));
            add(new Ball(Color.BLUE, Ball.Size.MEDIUM));
         }
      }
   }

   public void move() {
      for (Ball b : balls) {
         b.move(getBounds());
      }
   }

   public void setRadius(int newRadius) {
      for (Ball b : balls)
         b.setRadius(newRadius);
   }
}