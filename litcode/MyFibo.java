package litcode;

public class MyFibo {
    public int fibo(int n){
        int result =-1;
        if(n<=1){
            result = n;
        }
        else {
            int curr =1;
            int prev = 0;
            for(int i=2;i<=n;i++){
                result = curr +prev;
                prev= curr;
                curr = result;
            }
        }
        return result;
    }
    public static void main(String[] args) {
        MyFibo myFibo = new MyFibo();
        int n = 3;
        System.out.println("Fibonacci of " + n + " is: " + myFibo.fibo(n));
    }
}
