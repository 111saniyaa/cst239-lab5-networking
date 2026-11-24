package edu.gcu.cst239.sharma.saniya.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import edu.gcu.cst239.sharma.saniya.service.ParserService;

public class ServerWorker implements Runnable {
    private final Socket clientSocket;
    private final ParserService parserService;

    public ServerWorker(
            Socket clientSocket,
            ParserService parserService) {

        this.clientSocket = clientSocket;
        this.parserService = parserService;
    }
    private void handleClient(Socket socket) {
        System.out.println("Client connected: " + socket.getRemoteSocketAddress());
        try (socket;
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String message;
            while ((message = input.readLine()) != null) {
                if (message.equals(".")) {
                    output.println("Goodbye");
                    break;
                }
                output.println(parserService.processMessage(message));
            }
        } catch (IOException ex) {
            System.err.println("Client error: " + ex.getMessage());
        }
        System.out.println("Client disconnected");
    }

    @Override
    public void run() {
        try (
                // This worker owns the connected client socket.
                Socket socket = clientSocket;

                // Read one text command at a time from this client.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                // Auto-flush sends each completed response immediately.
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            handleClient(socket);

        } catch (IOException exception) {
            // A client-session failure must not terminate the server.
            System.err.println(
                    "Client session failed: " + exception.getMessage());
        }
    }
    // handleClient moves from Server to ServerWorker
}
