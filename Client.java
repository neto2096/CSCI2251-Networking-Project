// Name: Ernesto Morales Carrasco
// Email: emoralescarras@cnm.edu
// Assignment: Networking Part 1
// Purpose: Implements the client-side GUI, file reading, and matrix transmission to the server

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    // GUI parts
    private JFrame frame;
    private JTextField fileField;
    private JButton submitButton;

    // Method to initialize and start the client
    public void start() {
        // Initialize GUI
        frame = new JFrame("Matrix Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(null);

        // Create and position text field for filename input
        fileField = new JTextField();
        fileField.setBounds(20, 20, 200, 30);
        frame.add(fileField);

        // Create and position submit button
        submitButton = new JButton("Submit");
        submitButton.setBounds(230, 20, 100, 30);
        frame.add(submitButton);

        // Add action listener to handle button click
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get filename from user
                String filename = fileField.getText();
                // Print filename to the console
                System.out.println("Entered filename: " + filename);

                try {
                    // Read matrices from specified file
                    int[][] matrix1 = null, matrix2 = null;
                    int rows = 0, cols = 0;

                    // Open and read file
                    File file = new File(filename);
                    Scanner scanner = new Scanner(file);
                    String[] dimensions = scanner.nextLine().split(" ");
                    rows = Integer.parseInt(dimensions[0]);
                    cols = Integer.parseInt(dimensions[1]);

                    // Initialize and read first matrix
                    matrix1 = new int[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            matrix1[i][j] = scanner.nextInt();
                        }
                    }

                    // Initialize and read second matrix
                    matrix2 = new int[rows][cols];
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            matrix2[i][j] = scanner.nextInt();
                        }
                    }
                    scanner.close();

                    // Send matrices to server
                    try (Socket socket = new Socket("localhost", 8000); // Connect to server
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                        out.writeObject(matrix1);
                        out.writeObject(matrix2);
                        out.flush(); 
                    } catch (IOException ex) {
                        System.err.println("Error connecting to server: " + ex.getMessage());
                    }

                } catch (FileNotFoundException ex) {
                    System.err.println("File not found: " + filename);
                } catch (Exception ex) {
                    System.err.println("Error reading file: " + ex.getMessage());
                }
            }
        });

        // Display GUI
        frame.setVisible(true);
    }
}