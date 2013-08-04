package ballBounce;

public class BallComponentRunnable implements Runnable {
	private BallComponent ballc;
	public static final int STEPS = 1000;
	public static final int DELAY = 5;
	public static int nStepsRemaining = STEPS;
	private BounceFrame bfParent;

	public BallComponentRunnable(BallComponent ballc, BounceFrame bfParent) {
		this.ballc = ballc;
		this.bfParent = bfParent;
	}

	public void run() {
		try {
			for (int i = 1; i <= STEPS; i++) {
				ballc.move();
				ballc.repaint();
				ballc.detectCollisions();
				Thread.sleep(DELAY);
				nStepsRemaining--;
				bfParent.updateCount(nStepsRemaining);
			}
		} catch (InterruptedException e) {}
	}

	public int getStepsRemaining() {
		return nStepsRemaining;
	}

	public void resetStepsRemaining() {
		nStepsRemaining = STEPS;
	}

}