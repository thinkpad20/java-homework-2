package ballBounce;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class BounceFrame extends JFrame {
   private boolean isRunning = false;
   private ArrayList<Thread> threads;
   private JSlider ballSizeSlider;
   private BallComponent comp;
   public static final int DEFAULT_WIDTH = 800;
   public static final int DEFAULT_HEIGHT = 600;
   public static final int ESCAPE_HATCH_X = DEFAULT_WIDTH / 10;
   public static final int ESCAPE_HATCH_Y = DEFAULT_HEIGHT / 10;
   public static final int DELAY = 3;
   public static final int NUM_BALLS_PER_THREAD = 8;

   public BounceFrame() {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setTitle("BounceThread");
      comp = new BallComponent(this);
      threads = new ArrayList<Thread>();
      threads.add(new Thread(new BallThread(comp, this, 0)));
      add(comp, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      addButton(buttonPanel, "Add Ball", new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            addBall();
         }
      });

      addButton(buttonPanel, "Pause/Resume", new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            comp.pause();
         }
      });

      addButton(buttonPanel, "Quit", new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.exit(0);
         }
      });

      add(buttonPanel, BorderLayout.SOUTH);
   }

   public void addButton(Container c, String title, ActionListener listener) {
      JButton button = new JButton(title);
      c.add(button);
      button.addActionListener(listener);
   }

   public void addSlider(Container c, ChangeListener listener) {
      JSlider slider = new JSlider(1, 50, 10);
      slider.addChangeListener(listener);
      c.add(slider);
   }

   public synchronized void addThread(int start) {
      Thread t = new Thread(new BallThread(comp, this, start));
      t.start();
      threads.add(t);
   }

   public synchronized void addBall() {
      comp.add(new Ball());
      if (!isRunning) {
         threads.get(0).start();
         isRunning = true;
      }
   }

}