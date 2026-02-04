package litcode;

import java.util.ArrayList;
import java.util.HashMap;

public class Fibo {
    ArrayList<Integer> memo = new ArrayList<>();
    public int fib(int n) {
        if (n <= 1) {
            return n;
        }
        // Initialize memoization list
        while (memo.size() <= n) {
            memo.add(-1);
        }
        // Check if already computed
        if (memo.get(n) != -1) {
            return memo.get(n);
        }
        // Compute and store in memo
        int result = fib(n - 1) + fib(n - 2);
        memo.set(n, result);
        return result;
    }

    HashMap<Integer, Integer> memo_map = new HashMap<>();

    public int fibNoRecursion(int n) {
        if (n <= 1) {
            return n;
        }
        if (memo_map.containsKey(n)) {
            return memo_map.get(n);
        }
        int result = 0;
        int prev1 = 0, prev2 = 1;
        for (int i = 2; i <= n; i++) {
            result = prev1 + prev2;
            prev1 = prev2;
            prev2 = result;
        }
        memo_map.put(n, result);
        return result;
    }
    public static void main(String[] args) {
        Fibo fibo = new Fibo();
        int n = 3; // Example input
        System.out.println("Fibonacci of " + n + " is: " + fibo.fib(n));
        System.out.println("Fibonacci of " + n + " is: " + fibo.fibNoRecursion(n));
    }
}
