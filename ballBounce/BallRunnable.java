package ballBounce;

import java.awt.Component;

class BallRunnable implements Runnable {
   private Ball ball;
   private Component component;
   public static final int DELAY = 5;

   public BallRunnable(Ball ball, Component component) {
      this.ball = ball;
      this.component = component;
   }

   public void run() {
      try {
         while (true) {
            ball.move(component.getBounds());
            component.repaint();
            Thread.sleep(DELAY);
         }
      } catch (InterruptedException e) {}
   }
}
