all: ballBounce matrixMultiply
	@echo "Type 'make runballs' to run BallBouncer, or 'make runmatrix' to run MatrixMultiply"

ballBounce: ballBounce/Ball.java ballBounce/BallComponent.java ballBounce/BallThread.java\
				 ballBounce/BounceFrame.java ballBounce/BounceMain.java
	javac ballBounce/*.java

matrixMultiply: matrixMultiply/MatrixMultiplier.java
	javac matrixMultiply/*.java

runballs: ballBounce
	java ballBounce.BounceMain

runmatrix: matrixMultiply
	@chmod +x gen_matrices.py
	@./gen_matrices.py 500
	java matrixMultiply.MatrixMultiplier input_file
