package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * This is a standard implementation of the calculation.
 * 
 */
public final class MultiThreadedSumMatrix implements SumMatrix {

    /**
     * 
     * @param nthread
     *            no. of thread performing the sum.
     */
    private final int nThreads;

    public MultiThreadedSumMatrix(final int nThreads) {
        this.nThreads = nThreads;
    }

    private static class Worker extends Thread {
        private final double [][] matrix;
        private final int startPos;
        private final int colums;
        private double res;

        /**
         * Build a new Worker.
         * 
         * @param matrix
         *              the matrix to sum
         * @param startPos
         *              the initial position for this worker
         * @param colums
         *              the no. of colums to sum
         */
        Worker(final double [][] matrix, final int startPos, final int colums) {
            this.matrix = Arrays.copyOf(matrix, matrix.length);
            this.startPos = startPos;
            this.colums = colums;
        }

        @Override
        public void run() {
            for (int i = this.startPos; i < matrix.length && i < startPos + colums; i++) {
                for (final double elem : matrix[i]) {
                    this.res += elem;
                }
            }
        }

        /**
         * Returns the result of summing up the double within the matrix.
         * 
         * @return the sum of every element in the array
         */
        public double getResult() {
            return this.res;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length % this.nThreads + matrix.length / this.nThreads;
        /*
         * Build a list of worker
         */
        final List<Worker> workers = new ArrayList<>(this.nThreads);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        /*
         * Start them
         */
        for (final Worker w : workers) {
            w.start();
        }
        long sum = 0;
        for (final Worker w : workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }
}
