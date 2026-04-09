package com.assignment2;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Benchmark {

    private static double[] computeStats(long[] times) {
        double[] stats = new double[3];
        Arrays.sort(times);
        int noOfTimes = times.length;

        // median
        if (noOfTimes % 2 != 0) {
            stats[0] = times[noOfTimes / 2];
        } else {
            stats[0] = (times[noOfTimes / 2 - 1] + times[noOfTimes / 2]) / 2.0;
        }

        // mean
        stats[1] = 0;
        for (long time : times) {
            stats[1] += time;
        }
        stats[1] = stats[1] / noOfTimes;

        // standard deviation
        double variance = 0;
        for (long time : times) {
            double diff = time - stats[1];
            variance += diff * diff;
        }
        variance = variance / noOfTimes;
        stats[2] = Math.sqrt(variance);


        return stats;
    }


    public static BenchmarkResult benchmarkInsert(String treeType, int[] input, int timesToRun) {
        long[] times = new long[timesToRun];
        int height = 0;

        for (int i = 0; i < timesToRun + 1; i++) {
            TreeInterface tree = null;

            if (treeType.equals("BST")) {
                tree = new BST();
            } else {
                tree = new RBTree();
            }

            long start = System.nanoTime();

            for (int value : input) {
                tree.insert(value);
            }

            long end = System.nanoTime();

            if (i != 0) {
                times[i - 1] = end - start;
                height = tree.height();
            }
        }
        double[] stats = computeStats(times);
        return new BenchmarkResult(stats[0], stats[1], stats[2], height);
    }

    public static BenchmarkResult benchmarkContain(String treeType, int[] input, int timesToRun) {
        TreeInterface tree = null;
        if (treeType.equals("BST")) {
            tree = new BST();
        } else {
            tree = new RBTree();
        }
        for (int value : input) {
            tree.insert(value);
        }

        int n = input.length;
        int[] lockups = new int[100000];

        Random random = new Random(42);

        for (int i = 0; i < 50000; i++) {
            int rand = random.nextInt(n);
            lockups[i] = input[rand];
        }

        for (int i = 0; i < 50000; i++) {
            lockups[50000 + i] = (10 * n) + 1 + i;
        }

        long[] times = new long[timesToRun];

        for (int i = 0; i < timesToRun + 1; i++) {
            long start = System.nanoTime();
            for (int value : lockups) {
                tree.contains(value);
            }
            long end = System.nanoTime();
            if (i != 0) {
                times[i - 1] = end - start;
            }
        }
        double[] stats = computeStats(times);
        return new BenchmarkResult(stats[0], stats[1], stats[2]);
    }

    public static BenchmarkResult benchmarkDelete(String treeType, int[] input, int timesToRun) {
        Random random = new Random(42);
        int noToBeDeleted = (int) (0.2 * input.length);
        int[] deletions = new int[noToBeDeleted];

        for (int i = 0; i < noToBeDeleted; i++) {
            int randIdx = random.nextInt(input.length);
            deletions[i] = input[randIdx];
        }

        long[] times = new long[timesToRun];

        for (int i = 0; i < timesToRun + 1; i++) {
            TreeInterface tree = null;
            if (treeType.equals("BST")) {
                tree = new BST();
            } else {
                tree = new RBTree();
            }
            for (int value : input) {
                tree.insert(value);
            }
            long start = System.nanoTime();
            for (int value : deletions) {
                tree.delete(value);
            }
            long end = System.nanoTime();
            if (i != 0) {
                times[i - 1] = end - start;
            }
        }
        double[] stats = computeStats(times);
        return new BenchmarkResult(stats[0], stats[1], stats[2]);
    }

    public static BenchmarkResult benchmarkTreeSort(String treeType, int[] input, int timesToRun) {

        long[] times = new long[timesToRun];
        for (int i = 0; i < timesToRun + 1; i++) {
            TreeInterface tree = null;
            if (treeType.equals("BST")) {
                tree = new BST();
            } else {
                tree = new RBTree();
            }
            long start = System.nanoTime();
            for (int value : input) {
                tree.insert(value);
            }
            List<Integer> sorted = tree.inOrder();
            long end = System.nanoTime();
            if (i != 0) {
                times[i - 1] = end - start;
            }
        }
        double[] stats = computeStats(times);
        return new BenchmarkResult(stats[0], stats[1], stats[2]);
    }

    public static BenchmarkResult benchmarkMergeSort(int[] input, int timesToRun) {
        long[] times = new long[timesToRun];

        for (int i = 0; i < timesToRun + 1; i++) {
            int[] copy = Arrays.copyOf(input, input.length);
            long start = System.nanoTime();
            MergeSort.doSort(copy);
            long end = System.nanoTime();
            if (i != 0) {
                times[i - 1] = end - start;
            }
        }
        double[] stats = computeStats(times);
        return new BenchmarkResult(stats[0], stats[1], stats[2]);
    }


}
