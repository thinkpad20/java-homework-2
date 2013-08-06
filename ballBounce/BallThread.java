package ballBounce;

/*
Each thread will take some number of balls and do movement and collision
on them. Movement is simple: we just divide up the array of balls and 
give an equal number of balls to each thread.
*/

public class BallThread implements Runnable {
	private BallComponent comp;
	public static final int DELAY = 5;
	private BounceFrame parent;
	private int start;

	public BallThread(BallComponent comp, BounceFrame parent, int start) {
		System.out.printf("ballthread created with start = %d\n", start);
		this.parent = parent;
		this.comp = comp;
		this.start = start;
		System.out.printf("getend = %d\n", getEnd());
	}

	public int getEnd() {
		return Math.min(comp.getBalls().size(), 
							 start + BounceFrame.NUM_BALLS_PER_THREAD);
	}

	public void run() {
		try {
			while (true) {
				for (int i = start; i < getEnd(); ++i)
					BallComponent.balls.get(i).move(comp);
				comp.repaint();
				comp.detectCollisions();
				Thread.sleep(DELAY);
			}
		} catch (InterruptedException e) {}
	}

}