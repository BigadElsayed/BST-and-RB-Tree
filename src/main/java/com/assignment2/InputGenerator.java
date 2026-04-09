package com.assignment2;

import java.util.Random;

public class InputGenerator {
    public static int[] fullyRandom(int n) {
        Random random = new Random(42); //fixed seed -> each rerun with the same
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(n * 10);
        }
        return arr;
    }

    public static int[] nearlySorted(int n , int percentage) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        Random random = new Random(42);
        int swaps = ( percentage * n ) / 100 ;
        for (int i = 0; i < swaps; i++) {
            int x = random.nextInt(n);
            int y = random.nextInt(n);

            int temp = arr[x];
            arr[x] = arr[y];
            arr[y] = temp;
        }
        return arr;
    }
}
