package com;

import java.io.*;
import java.net.*;

public class TcpServer {
    private final int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP Echo Server started on 127.0.0.1:" + port);

            while (Main.isRunning()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    // Запускаем обработчик клиента в новом потоке
                    new Thread(new TcpClientHandler(clientSocket)).start();
                } catch (IOException e) {
                    if (!Main.isRunning()) {
                        break;
                    }
                    System.err.println("TCP: Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("TCP Server error: " + e.getMessage());
        }
        System.out.println("TCP Server stopped");
    }
}