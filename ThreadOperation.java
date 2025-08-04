/*
Name: Ernesto Morales Carrasco
Email: emoralescarras@cnm.edu
Assignment: Matrix Addition Part 2
Purpose: Extends Thread to perform addition of two submatrices (from matrices A and B) 
in a specified quadrant, storing the result in a shared matrix C, enabling concurrent processing for efficiency.
*/

public class ThreadOperation extends Thread {
    private final int[][] A;
    private final int[][] B;
    private final int[][] C;
    private final int startRow;
    private final int startCol;
    private final int endRow;
    private final int endCol;
    private final String quadrant;

    /**
     * Constructs a ThreadOperation to add submatrices of A and B within a specified
     * quadrant,
     * storing the result in the shared matrix C.
     *
     * @param A        The first input matrix
     * @param B        The second input matrix
     * @param C        The result matrix to store the sum
     * @param startRow Starting row index of the quadrant
     * @param startCol Starting column index of the quadrant
     * @param endRow   Ending row index of the quadrant (exclusive)
     * @param endCol   Ending column index of the quadrant (exclusive)
     * @param quadrant String identifier for the quadrant (e.g., "00" for
     *                 upper-left)
     */
    public ThreadOperation(int[][] A, int[][] B, int[][] C, int startRow, int startCol, int endRow, int endCol,
            String quadrant) {
        this.A = A;
        this.B = B;
        this.C = C; // Shared result matrix
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.quadrant = quadrant;
    }

    /**
     * Executes the thread, performing element-wise addition of submatrices A and B
     * within the assigned quadrant and storing the result in C. Includes boundary
     * checks
     * to prevent out-of-bounds access.
     */
    @Override
    public void run() {
        // Perform addition for the assigned submatrix
        for (int i = startRow; i < endRow && i < A.length; i++) {
            for (int j = startCol; j < endCol && j < A[0].length; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
    }

    /**
     * Determines the index boundaries for a specified quadrant of a matrix.
     *
     * @param rows     Number of rows in the matrix
     * @param columns  Number of columns in the matrix
     * @param quadrant String identifier for the quadrant ("00" for upper-left, "01"
     *                 for upper-right,
     *                 "10" for lower-left, "11" for lower-right)
     * @return An array of four integers: [startRow, endRow, startCol, endCol]
     * @throws IllegalArgumentException if quadrant is invalid
     */
    public static int[] getQuadrantIndexes(int rows, int columns, String quadrant) {
        int midRow = (rows + 1) / 2; // Ceiling for odd rows
        int midCol = (columns + 1) / 2; // Ceiling for odd columns
        int[] indexes = new int[4]; // [startRow, endRow, startCol, endCol]

        switch (quadrant) {
            case "00": // Upper-left
                indexes[0] = 0;
                indexes[1] = midRow;
                indexes[2] = 0;
                indexes[3] = midCol;
                break;
            case "01": // Upper-right
                indexes[0] = 0;
                indexes[1] = midRow;
                indexes[2] = midCol;
                indexes[3] = columns;
                break;
            case "10": // Lower-left
                indexes[0] = midRow;
                indexes[1] = rows;
                indexes[2] = 0;
                indexes[3] = midCol;
                break;
            case "11": // Lower-right
                indexes[0] = midRow;
                indexes[1] = rows;
                indexes[2] = midCol;
                indexes[3] = columns;
                break;
            default:
                throw new IllegalArgumentException("Invalid quadrant identifier: " + quadrant);
        }
        return indexes;
    }
}