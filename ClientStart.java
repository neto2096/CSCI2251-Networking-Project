// Name: Ernesto Morales Carrasco
// Email: emoralescarras@cnm.edu
// Assignment: Networking Assignment Part 1
// Purpose: Entry point to start the client application for the matrix client-server system

public class ClientStart {
    // Main method to initialize and start the client
    public static void main(String[] args) {
        // Create a new Client instance
        Client client = new Client();
        // Start the client (initializes GUI and networking)
        client.start();
    }
}