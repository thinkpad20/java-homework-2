public class Matrix {
   private int rows, cols;
   double[][] m;
   public Matrix(int rows, int cols) {
      this.cols = cols; this.rows = rows;
      this.m = new double[rows][cols];
   }
   public Matrix(double[][] m) {
      // if (m.length == 0 || m[0].length == 0)
      //    throw new Exception("Matrix must be 2-D");
      this.rows = m.length;
      this.cols = m[0].length;
      this.m = m;
   }
   public int getRows() { return rows; }
   public int getCols() { return cols; }
   public double[][] getM() { return m; }
   public String toString() {
      StringBuffer buf = new StringBuffer();
      for (int i = 0; i < rows; ++i) {
         for (int j = 0; j < cols; ++j) {
            buf.append(String.format("%5.2f\t", m[i][j]));
         }
         buf.append("\n");
      }
      return buf.toString();
   }

   public Matrix mult(Matrix other) throws Exception {//a[m][n], b[n][p]
      double[][] m2 = other.getM();
      if (cols != other.getRows())
         throw new Exception("First matrix must have same number of rows as other has columns");
      double newM[][] = new double[rows][m2[0].length];
      
      for(int i = 0; i < rows ; ++i)
         for(int j = 0; j < other.getCols(); ++j)
            for(int k = 0; k < cols; ++k)
               newM[i][j] += m[i][k] * m2[k][j];
            
      return new Matrix(newM);
   }

   public static void main(String[] args) throws Exception {
      int rows1, rows2, cols2;
      if (args.length < 3) {
         rows1 = rows2 = cols2 = 3;
      } else {
         rows1 = Integer.parseInt(args[0]);
         rows2 = Integer.parseInt(args[1]);
         cols2 = Integer.parseInt(args[2]);
      }
      double[][] m1 = new double[rows1][rows2];
      double[][] m2 = new double[rows2][cols2];
      double counter = 1.0;
      for (int i = 0; i < rows1; ++i) {
         for (int j = 0; j < rows2; ++j) {
            m1[i][j] = counter++;
         }
      }
      for (int i = 0; i < rows2; ++i) {
         for (int j = 0; j < cols2; ++j) {
            m2[i][j] = counter++;
         }
      }
      Matrix a = new Matrix(m1);
      Matrix b = new Matrix(m2);
      long startTime = System.currentTimeMillis();
      Matrix c = a.mult(b);
      long endTime = System.currentTimeMillis();
      System.out.printf("Java: It took %f seconds to calculate.\n", (double)(endTime - startTime) / 1000);
   }

}