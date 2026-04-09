package com.assignment2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        int n = 100000;
        int timesToRun = 5;
        String fileName = "benchmark_results.csv";

        int[] fullyRandom = InputGenerator.fullyRandom(n);
        int[] nearlySorted1 = InputGenerator.nearlySorted(n, 1);
        int[] nearlySorted5 = InputGenerator.nearlySorted(n, 5);
        int[] nearlySorted10 = InputGenerator.nearlySorted(n, 10);

        int[][] distributions = {fullyRandom, nearlySorted1, nearlySorted5, nearlySorted10};
        String[] distributionNames = {"Fully Random", "Nearly Sorted 1%", "Nearly Sorted 5%", "Nearly Sorted 10%"};

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Distribution,Operation,BST Median(ms),BST Mean(ms),BST Std(ms),RBT Median(ms),RBT Mean(ms),RBT Std(ms),Speedup,BST Height,RBT Height");

            for (int d = 0; d < distributions.length; d++) {
                System.out.println("\n========================================");
                System.out.println("Distribution: " + distributionNames[d]);
                System.out.println("========================================");

                runAndPrint(distributionNames[d], distributions[d], timesToRun, writer);
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private static void runAndPrint(String distName, int[] input, int timesToRun, PrintWriter writer) {
        // Run Benchmarks
        BenchmarkResult bstInsert = Benchmark.benchmarkInsert("BST", input, timesToRun);
        BenchmarkResult rbInsert  = Benchmark.benchmarkInsert("RBTree", input, timesToRun);
        BenchmarkResult bstContain = Benchmark.benchmarkContain("BST", input, timesToRun);
        BenchmarkResult rbContain  = Benchmark.benchmarkContain("RBTree", input, timesToRun);
        BenchmarkResult bstDelete = Benchmark.benchmarkDelete("BST", input, timesToRun);
        BenchmarkResult rbDelete  = Benchmark.benchmarkDelete("RBTree", input, timesToRun);
        BenchmarkResult bstSort = Benchmark.benchmarkTreeSort("BST", input, timesToRun);
        BenchmarkResult rbSort  = Benchmark.benchmarkTreeSort("RBTree", input, timesToRun);
        BenchmarkResult mergeSort = Benchmark.benchmarkMergeSort(input, timesToRun);

        // --- Console Output ---
        System.out.printf("%n%-12s | %-12s %-12s %-12s | %-12s %-12s %-12s | %-10s%n",
                "Operation", "BST Med(ms)", "BST Mean(ms)", "BST Std(ms)", "RBT Med(ms)", "RBT Mean(ms)", "RBT Std(ms)", "Speedup");
        System.out.println("-".repeat(110));

        printRow("Insert", bstInsert, rbInsert);
        System.out.printf("   [BST Height: %d | RBTree Height: %d]%n", bstInsert.getHeight(), rbInsert.getHeight());
        printRow("Contains", bstContain, rbContain);
        printRow("Delete", bstDelete, rbDelete);
        printRow("Tree Sort", bstSort, rbSort);

        System.out.printf("%-12s | %-12s %-12s %-12s | %-12s %-12s %-12s | %-10s%n",
                "Merge Sort", toMs(mergeSort.getMedian()), toMs(mergeSort.getMean()), toMs(mergeSort.getStdDev()), "-", "-", "-", "-");

        // --- CSV Output ---
        writeCsvRow(writer, distName, "Insert", bstInsert, rbInsert);
        writeCsvRow(writer, distName, "Contains", bstContain, rbContain);
        writeCsvRow(writer, distName, "Delete", bstDelete, rbDelete);
        writeCsvRow(writer, distName, "Tree Sort", bstSort, rbSort);
        // Special case for Merge Sort in CSV
        writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                distName, "Merge Sort", toMs(mergeSort.getMedian()), toMs(mergeSort.getMean()), toMs(mergeSort.getStdDev()),
                "0", "0", "0", "0", "0", "0");
    }

    private static void printRow(String operation, BenchmarkResult bst, BenchmarkResult rb) {
        double speedup = bst.getMedian() / rb.getMedian();
        System.out.printf("%-12s | %-12s %-12s %-12s | %-12s %-12s %-12s | %-10s%n",
                operation, toMs(bst.getMedian()), toMs(bst.getMean()), toMs(bst.getStdDev()),
                toMs(rb.getMedian()), toMs(rb.getMean()), toMs(rb.getStdDev()), String.format("%.2fx", speedup));
    }

    private static void writeCsvRow(PrintWriter writer, String dist, String op, BenchmarkResult bst, BenchmarkResult rb) {
        double speedup = bst.getMedian() / rb.getMedian();
        writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%d,%d%n",
                dist, op, toMs(bst.getMedian()), toMs(bst.getMean()), toMs(bst.getStdDev()),
                toMs(rb.getMedian()), toMs(rb.getMean()), toMs(rb.getStdDev()),
                speedup, bst.getHeight(), rb.getHeight());
    }

    private static String toMs(double nanos) {
        return String.format("%.4f", nanos / 1000000.0);
    }
}