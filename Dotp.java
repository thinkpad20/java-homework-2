import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
 
class Dotp extends RecursiveTask<Double>{
    private double a[], b[];
    private int i1,i2;
    private static final int task_threshold = 500;
 
    Dotp(double[] vec1, double[] vec2, int istrt, int iend){
        a = vec1;
        b = vec2;
        i1 = istrt;
        i2 = iend;
    }
     
    protected Double compute(){
        if (i2-i1 <= task_threshold){
            System.out.printf("calling dp with il:%d  i2:%d\n", i1,i2);
            double result = dp(a,b,i1,i2);
            return result;
        }
        else{
            int mid = i1 + (i2 - i1)/2;
            Dotp left = new Dotp(a,b,i1,mid);
            Dotp right = new Dotp(a,b,mid,i2);
            left.fork();
            right.fork();
            double r1 = left.join();
            double r2 = right.join();
            return r1+r2;
        }
    }
 
    private double dp(double[] a, double[] b, int i1, int i2){
        double val = 0.0;
        for (int i = i1; i < i2; ++i)
            val += a[i]*b[i];
        return val;
    }
     
    public static void main(String[] args){
        double[] a = new double[Integer.parseInt(args[0])];
        double[] b = new double[Integer.parseInt(args[0])];
        a[0] = 1.;
        b[0] = a[0];
        for (int i = 1; i < args.length; ++i){
            a[i] = 1.0;
            b[i] = 1 - b[i-1];
        }
        ForkJoinPool fjp = new ForkJoinPool();
        System.out.println(fjp.invoke(new Dotp(a,b,0,a.length)));
    }
}