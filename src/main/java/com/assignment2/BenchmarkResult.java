package com.assignment2;

public class BenchmarkResult {
    double median;
    double mean ;
    double standardDeviation;
    int height ;

    public BenchmarkResult() {
    }

    public BenchmarkResult(double median, double mean, double standardDeviation) {
        this.median = median;
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public BenchmarkResult(double median, double mean, double standardDeviation, int height) {
        this.median = median;
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.height = height;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStdDev() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
