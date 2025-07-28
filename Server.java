// Name: Ernesto Morales Carrasco
// Email: emoralescarras@cnm.edu
// Assignment: Networking Part 1
// Purpose: Implements the server to receive and display matrices from the client

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
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
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