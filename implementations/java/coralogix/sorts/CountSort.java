package implementations.java.coralogix.sorts;

import java.util.Arrays;

public class CountSort {
    public static void countSort(int[] arr){
        int len = arr.length;
        int maxVal = 0;
        for (int j : arr) {
            maxVal = Math.max(j, maxVal);
        }
         int[] counts = new int[maxVal + 1];
        for (int j : arr) {
            counts[j]++;
        }

        for (int i = 1; i <= maxVal; i++)
            counts[i] += counts[i - 1];

        int[] sorted = new int[len];

        for(int j= len -1; j>=0; j--){
            sorted[counts[arr[j]] -1] = arr[j];
            counts[arr[j]] --;
        }
        Arrays.stream(sorted).forEach(System.out::println);
    }

    public static void main(String[] args) {
        int[] arr = {5,4,0,5,6,6};
        CountSort.countSort(arr);
    }
}
