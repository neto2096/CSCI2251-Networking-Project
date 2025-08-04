// Name: Ernesto Morales Carrasco
// Email: emoralescarras@cnm.edu
// Assignment: Networking Part 2
// Purpose: Implements the server to receive, process matrices 
// concurrently, and send results

import java.io.*;
import java.net.*;

public class Server {
    // Method to start the server
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8000)) { // Create server socket on port 8000
            // Indicate server is running
            System.out.println("Server started on port 8000...");
            // Run indefinitely to accept multiple clients
            while (true) {
                try (Socket clientSocket = serverSocket.accept(); // Accept client connection
                     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
                    // Log client connection
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // Receive and display first matrix
                    int[][] matrix1 = (int[][]) in.readObject();
                    System.out.println("Matrix 1:");
                    printMatrix(matrix1);

                    // Receive and display second matrix
                    int[][] matrix2 = (int[][]) in.readObject();
                    System.out.println("Matrix 2:");
                    printMatrix(matrix2);

                    // Read and process CONTINUE command
                    String command = (String) in.readObject();
                    if ("CONTINUE".equals(command)) {
                        // Compute matrix addition concurrently using ThreadOperation
                        int[][] resultMatrix = computeMatrixAddition(matrix1, matrix2);
                        System.out.println("Result Matrix:");
                        printMatrix(resultMatrix);

                        // Send result matrix to client
                        out.writeObject(resultMatrix);
                        out.flush();

                        // Read and process TERMINATE command
                        command = (String) in.readObject();
                        if ("TERMINATE".equals(command)) {
                            System.out.println("Client requested termination.");
                        } else {
                            System.err.println("Unexpected command: " + command);
                        }
                    } else {
                        System.err.println("Expected CONTINUE, received: " + command);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    // Handle client connection or data reading errors
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // Handle server startup errors
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // Helper method to compute matrix addition using ThreadOperation
    private int[][] computeMatrixAddition(int[][] matrix1, int[][] matrix2) {
        // Validate matrix dimensions
        if (matrix1.length == 0 || matrix2.length == 0 || 
            matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            throw new IllegalArgumentException("Matrices must be non-empty and compatible");
        }

        int rows = matrix1.length;
        int cols = matrix1[0].length;
        int[][] result = new int[rows][cols];

        // Create and start threads for each quadrant
        ThreadOperation[] threads = new ThreadOperation[4];
        String[] quadrants = {"00", "01", "10", "11"};
        for (int i = 0; i < 4; i++) {
            int[] indexes = ThreadOperation.getQuadrantIndexes(rows, cols, quadrants[i]);
            threads[i] = new ThreadOperation(matrix1, matrix2, result, 
                                            indexes[0], indexes[2], indexes[1], indexes[3], quadrants[i]);
            threads[i].start();
        }

        // Join threads to ensure completion
        for (ThreadOperation thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }

        return result;
    }

    // Helper method to print a matrix to the console
    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}