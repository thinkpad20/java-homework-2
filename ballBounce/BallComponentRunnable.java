package ballBounce;

public class BallComponentRunnable implements Runnable {
	private BallComponent ballc;
	public static final int DELAY = 5;
	private BounceFrame parent;

	public BallComponentRunnable(BallComponent ballc, BounceFrame parent) {
		this.ballc = ballc;
		this.parent = parent;
	}

	public void run() {
		try {
			while (true) {
				ballc.move();
				ballc.repaint();
				ballc.detectCollisions();
				Thread.sleep(DELAY);
			}
		} catch (InterruptedException e) {}
	}

}