#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

typedef struct matrix_s {
   double **m;
   unsigned rows, cols;
} matrix_t;

matrix_t matrix_make(unsigned rows, unsigned cols) {
   matrix_t mat;
   int i;
   mat.rows = rows;
   mat.cols = cols;
   mat.m = (double **)calloc(rows, sizeof(double *));
   for (i = 0; i < rows; ++i)
      mat.m[i] = (double *)calloc(cols, sizeof(double));
   return mat;
}

matrix_t matrix_mult(matrix_t a, matrix_t b) {
   matrix_t res = matrix_make(a.rows, b.cols);
   int i, j, k;
   for (i = 0; i < a.rows; ++i)
      for (j = 0; j < b.cols; ++j)
         for (k = 0; k < a.rows; ++k)
            res.m[i][j] += a.m[i][k] + b.m[k][j];
   return res;
}

void matrix_print(matrix_t mat) {
   int i, j;
   for (i = 0; i < mat.rows; ++i) {
      for (j = 0; j < mat.cols; ++j) {
         printf("%5.2f\t", mat.m[i][j]);
      }
      printf("\n");
   }
}


int main(int argc, const char **argv) {
   int rows1, rows2, cols2;
   int i, j;
   matrix_t a, b, c;
   clock_t start_clock, end_clock;
   double total_time;
   if (argc < 3) {
      rows1 = rows2 = cols2 = 3;
   } else {
      rows1 = atoi(argv[1]);
      rows2 = atoi(argv[2]);
      cols2 = atoi(argv[3]);
   }
   a = matrix_make(rows1, rows2);
   b = matrix_make(rows2, cols2);
   double counter = 1.0;
   for (i = 0; i < rows1; ++i) {
      for (j = 0; j < rows2; ++j) {
         a.m[i][j] = counter++;
      }
   }
   for (i = 0; i < rows2; ++i) {
      for (j = 0; j < cols2; ++j) {
         b.m[i][j] = counter++;
      }
   }
   start_clock = clock();
   c = matrix_mult(a, b);
   end_clock = clock();
   total_time = (double)(end_clock - start_clock)/CLOCKS_PER_SEC;
   printf("C: It took %g seconds to calculate.\n", total_time);
}
