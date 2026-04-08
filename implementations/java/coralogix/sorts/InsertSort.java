package implementations.java.coralogix.sorts;

import java.util.Arrays;

public class InsertSort {
    public void insertSort(int[] arr, int n){
        for(int i=1; i<n;i++){
            int k = i;
            int tmp;
            while (k > 0) {
                if(arr[k] < arr[k-1]){
                    tmp = arr[k-1];
                    arr[k-1] = arr[k];
                    arr[k] = tmp;
                }
                k--;
            }
        }
        Arrays.stream(arr).forEach(System.out::println);
    }

    public static void main(String[] args) {
        InsertSort i = new InsertSort();
        int[] arr = { 5, 6, 1, 3 };
        int n = arr.length;
        i.insertSort(arr, n);
    }
}
