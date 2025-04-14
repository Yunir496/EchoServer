package com;

import java.io.*;
import java.net.*;

public class TcpClientHandler implements Runnable {
    private final Socket clientSocket;

    public TcpClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        // Логируем подключение клиента
        String clientAddress = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
        System.out.println("TCP: Client connected: " + clientAddress);

        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Читаем данные от клиента и отправляем обратно
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                output.flush();
                System.out.println("TCP: Echoed " + bytesRead + " bytes to " + clientAddress);
            }
        } catch (IOException e) {
            System.err.println("TCP: Client handling error for " + clientAddress + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("TCP: Client disconnected: " + clientAddress);
            } catch (IOException e) {
                System.err.println("TCP: Error closing client socket: " + e.getMessage());
            }
        }
    }
}