package com.assignment2;

public class Main {
    public static void main(String[] args) {
        int n = 100000;
        int timesToRun = 5;

        int[] fullyRandom = InputGenerator.fullyRandom(n);
        int[] nearlySorted1 = InputGenerator.nearlySorted(n, 1);
        int[] nearlySorted5 = InputGenerator.nearlySorted(n, 5);
        int[] nearlySorted10 = InputGenerator.nearlySorted(n, 10);

        int[][] distributions = {fullyRandom, nearlySorted1, nearlySorted5, nearlySorted10};
        String[] distributionNames = {"Fully Random", "Nearly Sorted 1%", "Nearly Sorted 5%", "Nearly Sorted 10%"};

        for (int d = 0; d < distributions.length; d++) {
            System.out.println("\n========================================");
            System.out.println("Distribution: " + distributionNames[d]);
            System.out.println("========================================");
            runAndPrint(distributions[d], timesToRun);
        }
    }

    private static void runAndPrint(int[] input, int timesToRun) {
        // INSERT
        BenchmarkResult bstInsert = Benchmark.benchmarkInsert("BST", input, timesToRun);
        BenchmarkResult rbInsert  = Benchmark.benchmarkInsert("RBTree", input, timesToRun);

        // CONTAINS
        BenchmarkResult bstContain = Benchmark.benchmarkContain("BST", input, timesToRun);
        BenchmarkResult rbContain  = Benchmark.benchmarkContain("RBTree", input, timesToRun);

        // DELETE
        BenchmarkResult bstDelete = Benchmark.benchmarkDelete("BST", input, timesToRun);
        BenchmarkResult rbDelete  = Benchmark.benchmarkDelete("RBTree", input, timesToRun);

        // TREE SORT
        BenchmarkResult bstSort = Benchmark.benchmarkTreeSort("BST", input, timesToRun);
        BenchmarkResult rbSort  = Benchmark.benchmarkTreeSort("RBTree", input, timesToRun);

        // MERGE SORT
        BenchmarkResult mergeSort = Benchmark.benchmarkMergeSort(input, timesToRun);

        // PRINT TABLE HEADER
        System.out.printf("%n%-12s | %-12s %-12s %-12s | %-12s %-12s %-12s | %-10s%n",
                "Operation",
                "BST Med(ms)", "BST Mean(ms)", "BST Std(ms)",
                "RBT Med(ms)", "RBT Mean(ms)", "RBT Std(ms)",
                "Speedup");
        System.out.println("-".repeat(110));

        // INSERT ROW
        printRow("Insert",
                bstInsert, rbInsert);
        System.out.printf("   [BST Height: %d | RBTree Height: %d]%n",
                bstInsert.getHeight(), rbInsert.getHeight());

        // CONTAINS ROW
        printRow("Contains", bstContain, rbContain);

        // DELETE ROW
        printRow("Delete", bstDelete, rbDelete);

        // TREE SORT ROW
        printRow("Tree Sort", bstSort, rbSort);

        // MERGE SORT ROW
        System.out.printf("%-12s | %-12s %-12s %-12s | %-12s %-12s %-12s | %-10s%n",
                "Merge Sort",
                toMs(mergeSort.getMedian()),
                toMs(mergeSort.getMean()),
                toMs(mergeSort.getStdDev()),
                "-", "-", "-", "-");
    }

    private static void printRow(String operation,
                                 BenchmarkResult bst,
                                 BenchmarkResult rb) {
        double speedup = bst.getMedian() / rb.getMedian();
        System.out.printf("%-12s | %-12s %-12s %-12s | %-12s %-12s %-12s | %-10s%n",
                operation,
                toMs(bst.getMedian()),
                toMs(bst.getMean()),
                toMs(bst.getStdDev()),
                toMs(rb.getMedian()),
                toMs(rb.getMean()),
                toMs(rb.getStdDev()),
                String.format("%.2fx", speedup));
    }

    private static String toMs(double nanos) {
        return String.format("%.2f", nanos / 1000000.0);
    }
}
