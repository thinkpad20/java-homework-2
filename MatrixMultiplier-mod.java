import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MatrixMultiplier extends RecursiveAction {
   private double[][] a, b, c;
   private int rowStart, rowEnd, colStart, colEnd, n;
   
   public MatrixMultiplier(double[][] a, double[][] b) {
      this.a = a; this.b = b;
      this.c = new double[a.length][b[0].length];
      this.rowStart = 0; this.rowEnd = a.length;
      this.colStart = 0; this.colEnd = b[0].length;
      this.n = a[0].length;
   }

   public MatrixMultiplier(double[][] a, double[][] b, double[][] c,
                           int rowStart, int rowEnd, int colStart, 
                           int colEnd) {
      this.a = a; this.b = b; this.c = c;
      this.rowStart = rowStart; this.rowEnd = rowEnd;
      this.colStart = colStart; this.colEnd = colEnd;
      this.n = a[0].length;
   }

   protected void computeDirectly() {
      for(int i = rowStart; i < rowEnd; i++)
         for(int j = colStart; j < colEnd; j++)
            for(int k = 0; k < n; k++)
               c[i][j] += a[i][k] * b[k][j];
   }

   public double[][] getRes() { 
      return c; 
   }

   public void compute() {
      if (rowStart == rowEnd || colStart == colEnd) {
         computeDirectly();
         return;
      }

      int rowMid = (rowStart - rowEnd) / 2;
      int colMid = (colStart - colEnd) / 2;
    
      invokeAll(new MatrixMultiplier(a, b, c, 0, rowMid, 0, colMid),
                new MatrixMultiplier(a, b, c, 
                                     rowMid + 1, 
                                     rowEnd, 
                                     colMid + 1, 
                                     colEnd));
   }

   public String toString() {
      StringBuffer buf = new StringBuffer("a:\n");
      for (int i = 0; i < a.length; ++i) {
         for (int j = 0; j < a[0].length; ++j) {
            buf.append(String.format("%5.2f\t", a[i][j]));
         }
         buf.append("\n");
      }
      buf.append("b:\n");
      for (int i = 0; i < b.length; ++i) {
         for (int j = 0; j < b[0].length; ++j) {
            buf.append(String.format("%5.2f\t", b[i][j]));
         }
         buf.append("\n");
      }
      buf.append("c:\n");
      for (int i = 0; i < c.length; ++i) {
         for (int j = 0; j < c[0].length; ++j) {
            buf.append(String.format("%5.2f\t", c[i][j]));
         }
         buf.append("\n");
      }
      return buf.toString();
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
      double[][] a = new double[rows1][rows2];
      double[][] b = new double[rows2][cols2];
      double counter = 1.0;
      for (int i = 0; i < rows1; ++i) {
         for (int j = 0; j < rows2; ++j) {
            a[i][j] = counter++;
         }
      }
      for (int i = 0; i < rows2; ++i) {
         for (int j = 0; j < cols2; ++j) {
            b[i][j] = counter++;
         }
      }
      double[][] c = new double[a.length][b[0].length];
      ForkJoinPool fjp = new ForkJoinPool();
      long startTime = System.currentTimeMillis();
      MatrixMultiplier mm = new MatrixMultiplier(a,b);
      fjp.invoke(mm);
      long endTime = System.currentTimeMillis();
      if (rows1 < 6 && rows2 < 6 && cols2 < 6)
         System.out.print(mm);
      System.out.printf("Java: It took %f seconds to calculate.\n", 
                              (double)(endTime - startTime) / 1000);
   }
}