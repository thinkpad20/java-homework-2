package ballBounce;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class BounceFrame extends JFrame implements ChangeListener {
   private boolean isRunning = false;
   private Thread t;
   BallComponentRunnable bcr;
   private JSlider ballSizeSlider;
   private BallComponent comp;
   public static final int DEFAULT_WIDTH = 800;
   public static final int DEFAULT_HEIGHT = 600;
   static final int ESCAPE_HATCH_X = DEFAULT_WIDTH / 10;
   static final int ESCAPE_HATCH_Y = DEFAULT_HEIGHT / 10;
   public static final int DELAY = 3;

   public BounceFrame() {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
      setTitle("BounceThread");
      comp = new BallComponent();
      bcr = new BallComponentRunnable(comp, this);
      t = new Thread(bcr);
      add(comp, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      addButton(buttonPanel, "Start", new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            addBall();
         }
      });

      addButton(buttonPanel, "Close", new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            System.exit(0);
         }
      });

      ballSizeSlider = new JSlider(1, 50, 10);
      ballSizeSlider.addChangeListener(this);
      buttonPanel.add(ballSizeSlider);

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

   public void addBall() {
      Ball b = new Ball();
      comp.add(b);
      if (!isRunning) {
         t.start();
         isRunning = true;
      }
   }

   public void stateChanged(ChangeEvent e) {
      comp.setRadius(ballSizeSlider.getValue());
   }
}