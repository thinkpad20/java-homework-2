package matrixMultiply;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MatrixMultiplier extends RecursiveTask<double[][]> {
   private double[][] a, b, c;
   private int rStart, rEnd, cStart, cEnd, n;
   public static int numRowsToComputeSingle = 8;

   public MatrixMultiplier(double[][] a, double[][] b) {
      if (a.length != b.length 
         || a[0].length != b[0].length)
         throw new RuntimeException("Matrices must be the same dimension.");
      if (a.length != a[0].length)
         throw new RuntimeException("Matrices must be square.");
         
      this.a = a; this.b = b;
      this.c = new double[a.length][a.length];
      this.rStart = 0; this.rEnd = a.length - 1;
      this.cStart = 0; this.cEnd = a[0].length - 1;
      this.n = a.length;
   }

   public MatrixMultiplier(double[][] a, double[][] b, double[][] c,
                           int rStart, int rEnd, int cStart, int cEnd) {
      this.a = a; this.b = b; this.c = c;
      this.rStart = rStart; this.rEnd = rEnd;
      this.cStart = cStart; this.cEnd = cEnd;
      this.n = a.length;
   }

   protected double[][] computeDirectly() {
      for(int i = rStart; i <= rEnd; i++)
         for(int j = cStart; j <= cEnd; j++)
            for(int k = 0; k < n; k++)
               c[i][j] += a[i][k] * b[k][j];
      return c;
   }

   public double[][] getRes() { 
      return c; 
   }

   protected double[][] compute() {
      if (rEnd - rStart <= numRowsToComputeSingle)
         return computeDirectly();

      // recursive call 1 will take the upper half of the left
      // matrix, and the left half of the right matrix.
      int rStart1 = rStart,
          rEnd1 = rStart + (rEnd - rStart) / 2,
          cStart1 = cStart,
          cEnd1 = cStart + (cEnd - cStart) / 2;
      // recursive call 2 will take the upper half of the left
      // and the right half of the right
      int rStart2 = rStart1,
          rEnd2 = rEnd1,
          cStart2 = cEnd1 + 1,
          cEnd2 = cEnd;
      // recursive call 3 will take the lower half of the left
      // and the left half of the right
      int rStart3 = rEnd1 + 1,
          rEnd3 = rEnd,
          cStart3 = cStart1,
          cEnd3 = cEnd1;
      // recursive call 4 will take the lower half of the left
      // and the right half of the right
      int rStart4 = rStart3,
          rEnd4 = rEnd3,
          cStart4 = cStart2,
          cEnd4 = cEnd2;

      // System.out.printf("(%d,%d,%d,%d) -> (%d,%d,%d,%d), (%d,%d,%d,%d), (%d,%d,%d,%d), (%d,%d,%d,%d)\n", 
      //                   rStart, rEnd, cStart, cEnd,
      //                   rStart1, rEnd1, cStart1, cEnd1,
      //                   rStart2, rEnd2, cStart2, cEnd2,
      //                   rStart3, rEnd3, cStart3, cEnd3,
      //                   rStart4, rEnd4, cStart4, cEnd4);

      MatrixMultiplier mm1 = new MatrixMultiplier(a, b, c, rStart1, rEnd1, cStart1, cEnd1);
      MatrixMultiplier mm2 = new MatrixMultiplier(a, b, c, rStart2, rEnd2, cStart2, cEnd2);
      MatrixMultiplier mm3 = new MatrixMultiplier(a, b, c, rStart3, rEnd3, cStart3, cEnd3);
      MatrixMultiplier mm4 = new MatrixMultiplier(a, b, c, rStart4, rEnd4, cStart4, cEnd4);
      mm1.fork(); mm2.fork(); mm3.fork(); mm4.fork();
      mm1.join(); mm2.join(); mm3.join(); mm4.join();
      return c;
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

   // public static void main(String[] args) throws Exception {
   //    int dim;
   //    if (args.length == 0) {
   //       dim = 4;
   //    } else {
   //       dim = Integer.parseInt(args[0]);
   //    }
   //    double[][] a = new double[dim][dim];
   //    double[][] b = new double[dim][dim];
   //    double counter = 1.0;
   //    for (int i = 0; i < dim; ++i) {
   //       for (int j = 0; j < dim; ++j) {
   //          a[i][j] = counter++;
   //       }
   //    }
   //    for (int i = 0; i < dim; ++i) {
   //       for (int j = 0; j < dim; ++j) {
   //          b[i][j] = counter++;
   //       }
   //    }
   //    double[][] c = new double[a.length][b[0].length];
   //    ForkJoinPool fjp = new ForkJoinPool();
   //    long rStartTime = System.currentTimeMillis();
   //    MatrixMultiplier mm = new MatrixMultiplier(a,b);
   //    fjp.invoke(mm);
   //    long rEndTime = System.currentTimeMillis();
   //    if (dim < 6)
   //       System.out.print(mm);
   //    System.out.printf("Java: It took %f seconds to calculate.\n", 
   //                            (double)(rEndTime - rStartTime) / 1000);
   // }

   public static void main(String[] args) {
      BufferedReader br = null;
      try {
         String line;
         ArrayList<String> m1 = new ArrayList<String>(), 
                           m2 = new ArrayList<String>(),
                           toAddTo = m1;
         br = new BufferedReader(new FileReader(args[0]));
         int dim = 0, lineCount = 0;
         double[][] a, b;
         while ((line = br.readLine()) != null) {
            if (line.equals("")) {dim = lineCount; toAddTo = m2;}
            else toAddTo.add(line);
            lineCount++;
         }
         System.out.printf("Detected a %d x %d matrix\n", dim, dim);
         a = new double[dim][dim];
         b = new double[dim][dim];

         // fill the matrices
         for (int i = 0; i < m1.size(); ++i) {
            StringBuffer num = new StringBuffer();
            String ln = m1.get(i);
            int col = 0;
            for (int j = 0; j < ln.length(); ++j) {
               char c = ln.charAt(j);
               if (c == ' ' || c == '\n' || j == ln.length() - 1) {
                  a[i][col++] = Double.parseDouble(num.toString());
                  num.delete(0, num.length());
               } else {
                  num.append(c);
               }
            }
         }
         for (int i = 0; i < m2.size(); ++i) {
            StringBuffer num = new StringBuffer();
            String ln = m2.get(i);
            int col = 0;
            for (int j = 0; j < ln.length(); ++j) {
               char c = ln.charAt(j);
               if (c == ' ' || c == '\n' || j == ln.length() - 1) {
                  b[i][col++] = Double.parseDouble(num.toString());
                  num.delete(0, num.length());
               } else {
                  num.append(c);
               }
            }
         }
         if (dim <= 5) {
            for (int i = 0; i < dim; ++i) {
               for (int j = 0; j < dim; ++j) {
                  System.out.printf("%f ", a[i][j]);
               }
               System.out.print("\n");
            }
            for (int i = 0; i < dim; ++i) {
               for (int j = 0; j < dim; ++j) {
                  System.out.printf("%f ", b[i][j]);
               }
               System.out.print("\n");
            }
         }
         System.out.println("Multiplying...");
         double[][] c = new double[a.length][b[0].length];
         ForkJoinPool fjp = new ForkJoinPool();
         long rStartTime = System.currentTimeMillis();
         MatrixMultiplier mm = new MatrixMultiplier(a,b);
         fjp.invoke(mm);
         long rEndTime = System.currentTimeMillis();
         System.out.printf("Java: It took %f seconds to calculate.\n", 
                                 (double)(rEndTime - rStartTime) / 1000);
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            if (br != null)br.close();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }
   }
}