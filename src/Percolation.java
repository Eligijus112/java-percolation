import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // Creating private variables which we will change during the simulation
    private final int[][] id; // ids of square elements
    private final boolean[][] square; // 1 - open, 0  - blocked
    private final WeightedQuickUnionUF uf; // Shortening the algorithm name
    private int nOpen; // count of the number of open sites
    private final int nRowCol; // number of rows and columns

    private final int virtualTop; // The hidden layer that is connected to all the top rows
    private final int virtualBottom; // The hidden layer that is connected to all the bottom rows

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(final int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        nRowCol = n;
        nOpen = 0;

        uf = new WeightedQuickUnionUF(nRowCol * nRowCol + 2);
        id = new int[nRowCol + 1][nRowCol + 1];
        square = new boolean[nRowCol + 1][nRowCol + 1];
        for (int i = 1; i <= nRowCol; i++) {
            for (int j = 1; j <= nRowCol; j++) {
                id[i][j] = (i - 1) * nRowCol + j - 1;
                square[i][j] = false;
            }
        }
        virtualTop = nRowCol * nRowCol;
        virtualBottom = nRowCol * nRowCol + 1;
    }

    private void concat(final int row, final int col) {
        if (row != 1 && isOpen(row - 1, col)) {
            union(id[row][col], id[row - 1][col]);
        } else if (row == 1) {
            union(id[row][col], virtualTop);
        }
        if (row != nRowCol && isOpen(row + 1, col)) {
            union(id[row][col], id[row + 1][col]);
        } else if (row == nRowCol) {
            union(id[row][col], virtualBottom);
        }
        if (col != 1 && isOpen(row, col - 1)) {
            union(id[row][col], id[row][col - 1]);
        }
        if (col != nRowCol && isOpen(row, col + 1)) {
            union(id[row][col], id[row][col + 1]);
        }
    }

    private void union(final int p, final int q) {
        if (!(uf.find(p) == uf.find(q))) {
            uf.union(p, q);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(final int row, final int col) {
        checkRange(row, col);
        if (!square[row][col]) {
            square[row][col] = true;
            concat(row, col);
            nOpen++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(final int row, final int col) {
        checkRange(row, col);
        return square[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(final int row, final int col) {
        checkRange(row, col);
        return uf.find(id[row][col]) == uf.find(virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }

    // Checks the ranges of i and j
    private void checkRange(int i, int j) {
        if (i <= 0 || j <= 0 || i > nRowCol || j > nRowCol)
            throw new IllegalArgumentException();
    }
}
