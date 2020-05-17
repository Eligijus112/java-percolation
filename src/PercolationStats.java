import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double confInterval = 1.96;
    private final double[] fraction;
    private double numOfMean;
    private double numOfStddev;
    private final int t;

    public PercolationStats(final int n, final int trials) { // perform trials independent experiments on an n-by-n grid
        validate(n, trials);
        fraction = new double[trials];
        t = trials;
        int row, col;
        for (int i = 0; i < trials; i++) {
            final Percolation per = new Percolation(n);
            while (!per.percolates()) {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
                per.open(row, col);
            }
            fraction[i] = per.numberOfOpenSites() * 1.0 / (n * n);
        }
    }

    private void validate(final int n, final int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("IllegalArgument");
        }
    }

    public double mean() {
        numOfMean = StdStats.mean(fraction);
        return numOfMean;
    }

    public double stddev() {
        numOfStddev = StdStats.stddev(fraction);
        return numOfStddev;
    }

    public double confidenceLo() {
        return numOfMean - confInterval * Math.sqrt(numOfStddev) / Math.sqrt(t);
    }

    public double confidenceHi() {
        return numOfMean + confInterval * Math.sqrt(numOfStddev) / Math.sqrt(t);
    }

    public static void main(final String[] args) {
        final int n = StdIn.readInt();
        final int t = StdIn.readInt();
        final PercolationStats perStats = new PercolationStats(n, t);
        StdOut.printf("mean                    = %.8f\n", perStats.mean());
        StdOut.printf("stddev                  = %.8f\n", perStats.stddev());
        StdOut.printf("95%% confidence interval = [%.16f, %.16f]", perStats.confidenceLo(), perStats.confidenceHi());
    }
}
